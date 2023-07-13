package com.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.paint.database.dataclass.PaintData
import com.paint.viewmodel.DrawerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class DrawerPath @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint: Paint? = null
    private var pathList = ArrayList<PaintPath>()
    private var undoPathList = ArrayList<PaintPath>()
    private var mPath: Path? = null
    private var mX: Float? = null
    private var mY: Float? = null
    private var touchTolerance: Float = 2f


    private var selectedColor: Int = Color.RED
    private var indicatorPaint: Paint? = null
    private val indicatorRadius: Float = 40f //yarıçap
    private val indicatorMargin: Float = 30f

    private lateinit var viewModel: DrawerViewModel


    init {
        indicatorPaint = Paint()
        paint = Paint()
        mPath = Path()
        paint!!.color = Color.RED
        paint!!.strokeWidth = 10f
        paint!!.style = Paint.Style.STROKE
        paint!!.isAntiAlias = true
        indicatorPaint?.color = Color.RED
        initViewModel()

    }


    override fun onDraw(canvas: Canvas?) {

        for (path in pathList) {
            canvas?.drawPath(path.path,path.paint)
        }

       // cx değeri, sol kenardan başlayarak indicatorMargin değerini ekleyerek hesaplanır. Bu, çemberin sol kenarından itibaren indicatorMargin birim ilerlediğini temsil eder.
        // Ardından, indicatorRadius değerini ekleyerek, çemberin yarıçapı kadar sağa kayar. Bu şekilde, cx değeri çemberin yatay konumunu belirler.
            val cx = indicatorMargin + indicatorRadius
        //değeri ise yükseklik (height) üzerinden hesaplanır. height değeri, View bileşeninin yüksekliğini temsil eder. indicatorMargin değeri cy değerinden çıkarılarak,
        // çemberin alt kenarına göre bir yükseklik belirlenir. Ardından, indicatorRadius değeri çıkarılarak, çemberin yarıçapı kadar yukarı kayar. Bu şekilde, cy değeri
        // çemberin dikey konumunu belirler.
            val cy = height - indicatorMargin - indicatorRadius
            indicatorPaint?.color = selectedColor // Seçilen renge göre işaret rengini ayarla
            canvas?.drawCircle(cx, cy, indicatorRadius, indicatorPaint!!)
            // İşaretin çizimini canvas üzerine çiziyoruz
            // cx ve cy, işaretin merkez koordinatlarını belirtir
            // indicatorRadius, işaretin yarıçapını belirtir
            // indicatorPaint, işaretin özelliklerini ve rengini belirtir
    }

    fun setPaintColor(color: Int) {
        paint?.color = color
        selectedColor = color
        invalidate()
    }

    fun saveDrawer() {
        viewModel.deleteDraw()
        var i = 1
        var number=0

        viewModel.getDrawer().value?.forEach {
            number +=it.id
        }

       if (number>=i){
           i +=number+1

       }else{
           val paintDataList = ArrayList<PaintData>()
           for (path in pathList) {
               val newPath = PaintData(i, PaintPath(Path(path.path), Paint(path.paint)))
               paintDataList.add(newPath)
               i++
           }
           viewModel.insertDrawer(paintDataList)
       }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val xPos: Float = event?.x ?: 0f
        val yPos: Float = event?.y ?: 0f

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(xPos, yPos)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(xPos, yPos)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    private fun touchStart(xPos: Float, yPos: Float) {
        //Path nesnesi, çizim yollarını ve şekillerini tanımlamak için kullanılır.Bunu burdan kaldırınca da renk değiştirince çizim renkleri de değişiyor.
        mPath=Path()
        
        //paint nesnesinin kendisini PaintPath'e eklemek yerine, Paint(paint) ifadesiyle yeni bir Paint nesnesi oluşturarak bu nesneyi PaintPath'e eklemek,
        // her bir çizim yolunun ayrı bir Paint örneği ile oluşturulmasını sağlar. Böylece, her çizim yolunun bağımsız bir renk ayarına sahip olmasını sağlar.

        //Eğer val newPath = PaintPath(mPath!!, paint) şeklinde yazarsanız, aynı paint nesnesinin referansını PaintPath'e eklemiş olursunuz.
        // Dolayısıyla, her bir çizim yolunun paint örneği, aynı referansa sahip olur ve herhangi bir renk ayarı yapıldığında tüm çizim yollarının rengi değişir.
        // Bu nedenle, her çizim yolunun ayrı bir Paint örneği ile oluşturulması önemlidir.
        val newPath = PaintPath(mPath!!, Paint(paint))
        pathList.add(newPath)
        mPath?.moveTo(xPos, yPos)
        mX = xPos
        mY = yPos
    }

    //Çalışma olayı şu.Her seferinde onTouchevent calısıyor ordan xpos ypos degerleri geliyor.çizgi belli bir yere geldikten sonra tekrar çalışıyor.Bu değerler yeniden güncellenerek buraya
    //tekrar geliyor.böylelikle sürekli dewğerler değişiyor.mx my degerleri de en son kalan degerler tutuluyor.yani fonksiyon çalışıyor 1 kerz. 2.kez calısınca 1.kezde olan degerler tutuluyor.
    private fun touchMove(xPos: Float, yPos: Float) {
        val dx: Float = abs(xPos - mX!!)
        val dy: Float = abs(yPos - mY!!)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            mPath?.quadTo(mX!!, mY!!, (xPos + mX!!) / 2, (yPos + mY!!) / 2)
            mX = xPos
            mY = yPos
        }
    }

    private fun touchUp() {
        mPath?.lineTo(mX!!, mY!!)
    }

    fun setUndo() {
        val size = pathList.size
        if (size > 0) {
            undoPathList.add(PaintPath(pathList[size - 1].path, Paint(pathList[size - 1].paint)))
            pathList.removeAt(size - 1)
            invalidate()
        }
    }

    fun setRedo() {
        val size = undoPathList.size
        if (size > 0) {
            pathList.add(PaintPath(undoPathList[size - 1].path, Paint(undoPathList[size - 1].paint)))
            undoPathList.removeAt(size - 1)
            invalidate()
        }
    }

    fun setDelete() {
        pathList.clear()
        undoPathList.clear()
        viewModel.deleteDraw()
        selectedColor=Color.RED
        paint!!.color = Color.RED
        invalidate()
    }

    private fun initViewModel() {
        val activity = context as? AppCompatActivity
        viewModel = activity?.let {
            ViewModelProvider(it)[DrawerViewModel::class.java]
        } ?: throw IllegalStateException("Activity must be AppCompatActivity")
    }


}

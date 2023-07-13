package com.paint

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.paint.databinding.ActivityMainBinding
import com.paint.viewmodel.DrawerViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var canvas: Canvas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        canvas= Canvas()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.undo.setOnClickListener {
            binding.PaintArea.setUndo()
        }
        binding.redo.setOnClickListener {
            binding.PaintArea.setRedo()
        }
        binding.delete.setOnClickListener {
            binding.PaintArea.setDelete()
        }
        binding.blue.setOnClickListener {
            binding.PaintArea.setPaintColor(Color.BLUE)
        }
        binding.red.setOnClickListener {
            binding.PaintArea.setPaintColor(Color.RED)
        }
        binding.black.setOnClickListener {
            binding.PaintArea.setPaintColor(Color.BLACK)
        }
        binding.yellow.setOnClickListener {
            binding.PaintArea.setPaintColor(Color.YELLOW)
        }
        binding.green.setOnClickListener {
            binding.PaintArea.setPaintColor(Color.GREEN)
        }
        binding.saveButton.setOnClickListener {
            binding.PaintArea.saveDrawer()
        }

    }




}
package com.moonturns.picturemaker

import android.app.Dialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ibBrushSizeClickEvent()
        ibEraserClickEvent()
    }

    /* ibBrushSize at activity_main layout click event.

    * Open a brush size dialog.

    * */
    private fun ibBrushSizeClickEvent() {
        ibBrushSize.setOnClickListener {
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_brush_size)
            dialog.setTitle("Brush Size")
            dialog.ibSmallBrush.setOnClickListener {
                drawingView.setSizeForBrush(DrawingView.SMALL_BRUSH_SIZE)
                dialog.dismiss()
            }
            dialog.ibNormalBrush.setOnClickListener {
                drawingView.setSizeForBrush(DrawingView.NORMAL_BRUSH_SIZE)
                dialog.dismiss()
            }
            dialog.ibLargeBrush.setOnClickListener {
                drawingView.setSizeForBrush(DrawingView.LARGE_BRUSH_SIZE)
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    /* There are 8 color choices.
   *
   * Color button click events.
   *
   * */
    fun colorPalette(view: View) {
        when (view.id) {
            R.id.ibColor1 -> {
                drawingView.setBrushColor("#f9f7d9")
            }
            R.id.ibColor2 -> {
                drawingView.setBrushColor("#000000")
            }
            R.id.ibColor3 -> {
                drawingView.setBrushColor("#ffcc0000")
            }
            R.id.ibColor4 -> {
                drawingView.setBrushColor("#ff99cc00")
            }
            R.id.ibColor5 -> {
                drawingView.setBrushColor("#ff0099cc")
            }
            R.id.ibColor6 -> {
                drawingView.setBrushColor("#ff9234")
            }
            R.id.ibColor7 -> {
                drawingView.setBrushColor("#fddb3a")
            }
            R.id.ibColor8 -> {
                drawingView.setBrushColor("#ffc1f3")
            }
        }
    }

    // ibEraser at activity_main click event.
    // Clean drawing.
    private fun ibEraserClickEvent() {
        ibEraser.setOnClickListener {
            drawingView.setBrushColor("#ffffff")
        }
    }
}
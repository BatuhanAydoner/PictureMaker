package com.moonturns.picturemaker

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ibBrushSizeClickEvent()
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
}
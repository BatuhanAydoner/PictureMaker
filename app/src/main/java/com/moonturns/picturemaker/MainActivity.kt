package com.moonturns.picturemaker

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*

class MainActivity : AppCompatActivity() {

    private val STORAGE_PERMISSON_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ibBrushSizeClickEvent()
        ibEraserClickEvent()
        ibImageClickEvent()
        ibBackClickEvent()
    }

    // READ AND WRITE EXTERNAL PERMISSION
    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSON_CODE)
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSON_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSON_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery()
        }else {
            Toast.makeText(this, "Need permissions", Toast.LENGTH_SHORT).show()
        }
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

    // ibImage at activity_main click event.
    // To add image to the UI.
    private fun ibImageClickEvent() {
        ibImage.setOnClickListener {
            requestStoragePermission()
        }
    }

    // ibImage at activity_main click event.
    // Back to last draw.
    private fun ibBackClickEvent() {
        ibBack.setOnClickListener {
            drawingView.setDrawLast()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imgBackground.setImageURI(data.data)
            }
        }
    }

    // Open phone's gallery to choose an image.
    private fun openGallery() {
        var intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100)
    }
}
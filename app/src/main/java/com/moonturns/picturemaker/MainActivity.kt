package com.moonturns.picturemaker

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_brush_size.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val STORAGE_PERMISSON_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ibBrushSizeClickEvent()
        ibEraserClickEvent()
        ibImageClickEvent()
        ibBackClickEvent()
        ibSaveClickListener()
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
            if (isReadPermissionAllowed()) {
                openGallery()
            }else {
                requestStoragePermission()
            }
        }
    }

    // ibImage at activity_main click event.
    // Back to last draw.
    private fun ibBackClickEvent() {
        ibUndo.setOnClickListener {
            drawingView.setUndoDraw()
        }
    }

    // ibSave at activity_main click event.
    // Save the image.
    private fun ibSaveClickListener() {
        ibSave.setOnClickListener {
            if (isReadPermissionAllowed()) {
                var bitmap = getBitmapFromView(flDrawContainer)
                saveBitmap(bitmap)
            }else {
                requestStoragePermission()
            }
        }
    }

    // Open phone's gallery to choose an image.
    private fun openGallery() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100)
    }

    // READ AND WRITE EXTERNAL PERMISSION
    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSON_CODE)
        }else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSON_CODE)
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
            Toast.makeText(this, "You need the permission", Toast.LENGTH_SHORT).show()
        }
    }

    // If READ_EXTERNAL_STORAGE is allowed, return true or not, return false.
    private fun isReadPermissionAllowed(): Boolean {
        var result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data!!.data != null) {
                imgBackground.setImageURI(data.data)
            }
        }
    }

    /* Get a bitmap from draw view.

    * Create a new bitmap and get background. If there is a background,
    * draw it onto canvas, if there is no background, draw WHITE color onto canvas.
    *
    */
    private fun getBitmapFromView(view: View): Bitmap {
        var returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        var canvas = Canvas(returnedBitmap)
        var bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        }else {
            canvas.drawColor(Color.WHITE)
        }

        view.draw(canvas)
        return returnedBitmap

    }

    // Save image to device storage.
    private fun saveBitmap(bitmap: Bitmap) {
        var file = File(externalCacheDir!!.absoluteFile.toString() + File.separator + "PictureMaker_" + System.currentTimeMillis() / 1000 + ".png")
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                if (bitmap != null) {
                    try {
                        val bytes = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

                        var fos = FileOutputStream(file)
                        fos.write(bytes.toByteArray())
                        fos.close()
                    }catch (e: Exception) {
                        Log.e("myApp", e.toString())
                    }
                }
            }

            MediaScannerConnection.scanFile(this@MainActivity, arrayOf(file.toString()), null) { path, uri ->
                var sharedIntent = Intent()
                sharedIntent.action = Intent.ACTION_SEND
                sharedIntent.putExtra(Intent.EXTRA_STREAM, uri)
                sharedIntent.type = "image/png"
                startActivity(Intent.createChooser(sharedIntent, "Choose an app"))
            }
        }.start()
    }
}
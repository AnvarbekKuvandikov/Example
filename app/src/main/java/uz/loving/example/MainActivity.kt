package uz.loving.example

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.pdf.PrintedPdfDocument
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException


class MainActivity : AppCompatActivity() {


    val  REQUEST_PERMISSIONS = 111

    var bitmap: Bitmap? = null

    var booleanPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSIONS)

        }else {
            booleanPermission = true
        }

        share_btn.setOnClickListener {
            if (booleanPermission){
                createPdf()
            }else{
                Toast.makeText(this, "permission wrong: ", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                booleanPermission = true
            } else {
                Toast.makeText(this, "Please allow the permission", Toast.LENGTH_LONG).show();

            }
        }
    }

    private fun createPdf(){
        // Create a shiny new (but blank) PDF document in memory
        // We want it to optionally be printable, so add PrintAttributes
        // and use a PrintedPdfDocument. Simpler: new PdfDocument().
        // Create a shiny new (but blank) PDF document in memory
        // We want it to optionally be printable, so add PrintAttributes
        // and use a PrintedPdfDocument. Simpler: new PdfDocument().
        val printAttrs = PrintAttributes.Builder().setColorMode(PrintAttributes.COLOR_MODE_COLOR)
            .setMediaSize(PrintAttributes.MediaSize.NA_LETTER)
            .setResolution(PrintAttributes.Resolution("zooey", PRINT_SERVICE, 300, 300))
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build()
        val document: PdfDocument = PrintedPdfDocument(this, printAttrs)
        // crate a page description
        // crate a page description
        val pageInfo = PdfDocument.PageInfo.Builder(layout_pdf.width, layout_pdf.height, 1).create()
        // create a new page from the PageInfo
        // create a new page from the PageInfo
        val page = document.startPage(pageInfo)
        // repaint the user's text into the page
        // repaint the user's text into the page
        val content: ViewGroup = layout_pdf
        content.draw(page.canvas)
        // do final processing of the page
        // do final processing of the page
        document.finishPage(page)
        // Here you could add more pages in a longer doc app, but you'd have
        // to handle page-breaking yourself in e.g., write your own word processor...
        // Now write the PDF document to a file; it actually needs to be a file
        // since the Share mechanism can't accept a byte[]. though it can
        // accept a String/CharSequence. Meh.
        // Here you could add more pages in a longer doc app, but you'd have
        // to handle page-breaking yourself in e.g., write your own word processor...
        // Now write the PDF document to a file; it actually needs to be a file
        // since the Share mechanism can't accept a byte[]. though it can
        // accept a String/CharSequence. Meh.
        try {
            val f = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/PDFlayout.pdf")
            val fos = FileOutputStream(f)
            document.writeTo(fos)
            document.close()
            fos.close()
        } catch (e: IOException) {
            throw RuntimeException("Error generating file", e)
        }
    }


    private fun generatePdf(){
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val displayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val hight = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val  document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(width, hight, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas

        val paint = Paint()
        canvas.drawPaint(paint)

        if (bitmap !=null){
            bitmap =   Bitmap.createScaledBitmap(bitmap!!,width,hight,true)
            paint.color = Color.BLUE
            canvas.drawBitmap(bitmap!!,0F,0F,null)
            document.finishPage(page)
        }


        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Test/"

        val dir = File(path)
        if (!dir.exists())
            dir.mkdirs()

        val filePath = File(dir,"Testtt.pdf")
        try {
            document.writeTo(FileOutputStream(filePath))
            Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show()
        }
        // close the document
        document.close()
    }
}

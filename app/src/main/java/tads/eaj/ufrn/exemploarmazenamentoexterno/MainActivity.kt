package tads.eaj.ufrn.exemploarmazenamentoexterno

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Environment.MEDIA_MOUNTED_READ_ONLY
import android.os.Environment.MEDIA_MOUNTED
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import tads.eaj.ufrn.exemplopermissoes.PermissionUtil.Companion.alertAndFinish
import java.io.*
import tads.eaj.ufrn.exemplopermissoes.PermissionUtil
import java.lang.Thread.sleep
import java.net.URL


class MainActivity : AppCompatActivity() {

    private val filename = "SampleFile.txt"
    private val filepath = "MyFileStorage"
    var myExternalFile: File? = null
    var myData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveExternalStorage.setOnClickListener {
            if (checkStorage()) {
                myExternalFile = File(getExternalFilesDir(filepath), filename)
            }

            try {
                val fos = FileOutputStream(myExternalFile)
                fos.write(myInputText.text.toString().toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            myInputText.setText("")
            response.text = "SampleFile.txt adicionado a pasta privada do armazenamento externo..."
        }

        getExternalStorage.setOnClickListener {
            myData = ""

            if (checkStorage()) {
                myExternalFile = File(getExternalFilesDir(filepath), filename)
            }

            try {
                val fis = FileInputStream(myExternalFile)
                val datain = DataInputStream(fis)
                val br = BufferedReader(InputStreamReader(datain))
                var strLine = br.readLine()
                while (strLine != null) {
                    myData += strLine
                    strLine = br.readLine()
                }
                datain.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            myInputText.setText(myData)
            response.text = "SampleFile.txt lido da pasta privada do armazenamento externo..."
        }

        saveExternalStoragePublic.setOnClickListener {
            if (checkStorage()) {
                PermissionUtil.validate(
                    this@MainActivity,
                    255,
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                )
                myExternalFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename)
            }

            try {
                val fos = FileOutputStream(myExternalFile)
                fos.write(myInputText.text.toString().toByteArray())
                fos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            myInputText.setText("")
            response.text = "SampleFile.txt salvo na pasta publica do armazenamento externo..."
        }

        getExternalStoragePublic.setOnClickListener {
            Log.i("AULA20", "chamou salvar em pasta publica checkstorage = ${checkStorage()}")
            myData = ""

            if (checkStorage()) {
                PermissionUtil.validate(
                    this@MainActivity,
                    255,
                    READ_EXTERNAL_STORAGE,
                    WRITE_EXTERNAL_STORAGE
                )
                myExternalFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), filename)
                Log.i("AULA20", "Entrou ${myExternalFile?.path}")
            }

            try {
                val fis = FileInputStream(myExternalFile)
                val indata = DataInputStream(fis)
                val br = BufferedReader(InputStreamReader(indata))
                var strLine: String? = br.readLine()
                while (strLine != null) {
                    myData = myData + strLine
                    strLine = br.readLine()
                }
                indata.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            myInputText.setText(myData)
            response.text = "SampleFile.txt lido da pasta pública do armazenamento externo..."
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (result in grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                // Alguma permissão foi negada, agora é com você :-)
                alertAndFinish(this, R.string.app_name)
                return
            }
        }
    }

    private fun checkStorage(): Boolean {
        return !(!isExternalStorageAvailable() || isExternalStorageReadOnly())
    }

    private fun isExternalStorageReadOnly(): Boolean {
        return MEDIA_MOUNTED_READ_ONLY == Environment.getExternalStorageState()
    }

    private fun isExternalStorageAvailable(): Boolean {
        return MEDIA_MOUNTED == Environment.getExternalStorageState()
    }
}

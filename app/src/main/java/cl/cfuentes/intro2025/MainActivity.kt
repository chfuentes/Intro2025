package cl.cfuentes.intro2025

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private var flashEstaEncendido = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnLinterna : ImageButton = findViewById(R.id.btnLinterna)
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager.cameraIdList.first { id ->
                val caracteristicas = cameraManager.getCameraCharacteristics(id)
                caracteristicas.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            }
        }
        catch (e:Exception){
            Toast.makeText(this,"La camara de este dispositivo no tiene un flash para ser utilizado como linterna", Toast.LENGTH_SHORT)
            finish()
        }

        btnLinterna.setOnClickListener {
            cambiaEstadoFlash()
        }


    }

    private fun cambiaEstadoFlash() {
        try {
            flashEstaEncendido = !flashEstaEncendido
            cameraManager.setTorchMode(cameraId,flashEstaEncendido)
        }
        catch (e:CameraAccessException) {
            Toast.makeText(this,"Error al utilizar el flash de la camara",Toast.LENGTH_SHORT)
        }
    }
}
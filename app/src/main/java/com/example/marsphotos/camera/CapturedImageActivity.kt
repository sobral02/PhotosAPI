package com.example.marsphotos.camera

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.marsphotos.R
import com.google.marsphotos.databinding.CapturedImageBinding

class CapturedImageActivity : AppCompatActivity() {
    private lateinit var viewBinding: CapturedImageBinding // Substitua "CapturedImageBinding" pelo nome da classe gerada pelo ViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = CapturedImageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val capturedImageView = viewBinding.capturedImageView

        viewBinding.backButton.setOnClickListener {
            finish()
        }


        // Receba o Uri da imagem capturada dos extras da Intent
        val imageUri = intent.getStringExtra("imageUri")

        // Carregue a imagem no ImageView
        if (!imageUri.isNullOrEmpty()) {
            val uri = Uri.parse(imageUri)
            capturedImageView.setImageURI(uri)
        } else {
            // Se o Uri for nulo ou vazio, você pode tratar isso de acordo com a lógica do seu aplicativo
            Toast.makeText(this, "Imagem não encontrada", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}

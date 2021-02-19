package com.qbo.apppatitaskea3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.qbo.apppatitaskea3.databinding.ActivityInicioBinding
import com.qbo.apppatitaskea3.databinding.ActivityLoginBinding
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var colapeticiones : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val vista = binding.root
        setContentView(vista)
        colapeticiones = Volley.newRequestQueue(this)
        binding.btnregistrar.setOnClickListener {
            startActivity(Intent(this,
                    RegistroActivity::class.java))
            finish()
        }
        binding.btnlogin.setOnClickListener {
            binding.btnlogin.isEnabled = false
            if(validarUsuarioPassword()){
                autenticarUsarioApiRest(it,
                        binding.etusuario.text.toString(),
                        binding.etpassword.text.toString())
            }else{
                binding.btnlogin.isEnabled = true
                mostrarMensaje(it, getString(R.string.msgerrorlogin))
            }
        }

    }

    private fun autenticarUsarioApiRest(vista: View, usuario: String, password: String) {
        val urlapilogin = "http://www.kreapps.biz/patitas/login.php"
        val parametroJson = JSONObject()
        parametroJson.put("usuario", usuario)
        parametroJson.put("password", password)
        val request = JsonObjectRequest(
                Request.Method.POST,
                urlapilogin,
                parametroJson,
                { response ->
                    if(response.getBoolean("rpta")){
                        startActivity(Intent(this,
                                InicioActivity::class.java))
                        finish()
                    }else{
                        mostrarMensaje(vista, response.getString("mensaje"))
                    }
                    binding.btnlogin.isEnabled = true
                }, {
                    Log.e("APILOGIN", it.toString())
                    binding.btnlogin.isEnabled = true
                })
        colapeticiones.add(request)
    }

    fun validarUsuarioPassword(): Boolean{
        var respuesta = true
        if(binding.etusuario.text.toString().trim().isEmpty()){
            binding.etusuario.isFocusableInTouchMode = true
            binding.etusuario.requestFocus()
            respuesta = false
        }else if(binding.etpassword.text.toString().trim().isEmpty()){
            binding.etpassword.isFocusableInTouchMode = true
            binding.etpassword.requestFocus()
            respuesta = false
        }
        return respuesta
    }

    fun mostrarMensaje(vista: View, mensaje: String){
        Snackbar.make(vista, mensaje, Snackbar.LENGTH_LONG).show()
    }
}
package com.qbo.apppatitaskea3.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.qbo.apppatitaskea3.R
import com.qbo.apppatitaskea3.databinding.ActivityLoginBinding
import com.qbo.apppatitaskea3.viewmodel.PersonaViewModel
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    //Definiendo el ViewModel y las preferencias
    private lateinit var personaViewModel: PersonaViewModel
    private lateinit var preferencias: SharedPreferences
    private lateinit var binding : ActivityLoginBinding
    private lateinit var colapeticiones : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val vista = binding.root
        setContentView(vista)
        colapeticiones = Volley.newRequestQueue(this)
        //Referencia a las preferencias de la aplicación
        preferencias = getSharedPreferences("appPatitas", MODE_PRIVATE)
        //Referencia a la clase ViewModel
        personaViewModel = ViewModelProvider(this)
            .get(PersonaViewModel::class.java)
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

    fun setearValoresDeRecordar(vista: View){
        if(vista is CheckBox){
            val check: Boolean = vista.isChecked
            when(vista.id){
                R.id.chkrecordar -> {
                    if(!check){
                        if(verificarSharedPreferences()){
                            preferencias.edit().remove("recordardatos")
                                .apply()
                        }
                    }
                }
            }
        }
    }

    fun verificarSharedPreferences(): Boolean{
        return preferencias.getBoolean("recordardatos", false)
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
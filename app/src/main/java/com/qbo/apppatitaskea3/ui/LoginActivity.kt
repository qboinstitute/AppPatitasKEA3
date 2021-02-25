package com.qbo.apppatitaskea3.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.qbo.apppatitaskea3.R
import com.qbo.apppatitaskea3.databinding.ActivityLoginBinding
import com.qbo.apppatitaskea3.db.entity.PersonaEntity
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
        //Validar si existen la preferencia recordardatos
        if(verificarSharedPreferences()){
            binding.chkrecordar.isChecked = true
            binding.etusuario.isEnabled = false
            binding.etpassword.isEnabled = false
            binding.chkrecordar.text = getString(R.string.msgcambiarusuario)
            personaViewModel.obtener()
                    .observe(this, Observer {
                        persona->
                        persona?.let {
                            binding.etusuario.setText(persona.usuario)
                            binding.etpassword.setText(persona.password)
                        }
                    })
        }else{
            personaViewModel.eliminartodo()
        }
        //Actualizar preferencias con el checkrecordardatos
        binding.chkrecordar.setOnClickListener {
            setearValoresDeRecordar(it)
        }

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
                            personaViewModel.eliminartodo()
                            binding.etusuario.isEnabled = true
                            binding.etpassword.isEnabled = true
                            binding.chkrecordar.text = getString(R.string.valchkrecordar)
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
                        val personaEntity = PersonaEntity(
                                response.getString("idpersona").toInt(),
                                response.getString("nombres"),
                                response.getString("apellidos"),
                                response.getString("email"),
                                response.getString("celular"),
                                response.getString("usuario"),
                                response.getString("password"),
                                response.getString("esvoluntario")
                        )
                        if(verificarSharedPreferences()){
                            personaViewModel.actualizar(personaEntity)
                        }else{
                            personaViewModel.insertar(personaEntity)
                            if(binding.chkrecordar.isChecked){
                                preferencias.edit().putBoolean("recordardatos", true).apply()
                            }
                        }
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
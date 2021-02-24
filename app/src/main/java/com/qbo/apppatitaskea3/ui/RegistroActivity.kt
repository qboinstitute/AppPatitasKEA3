package com.qbo.apppatitaskea3.ui

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
import com.qbo.apppatitaskea3.R
import com.qbo.apppatitaskea3.databinding.ActivityRegistroBinding
import org.json.JSONObject

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegistroBinding
    private lateinit var colapeticiones : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        val vista = binding.root
        setContentView(vista)
        colapeticiones = Volley.newRequestQueue(this)
        binding.btnirlogin.setOnClickListener {
            startActivity(Intent(this,
                    LoginActivity::class.java))
            finish()
        }
        binding.btnregistrarusuario.setOnClickListener {
            binding.btnregistrarusuario.isEnabled = false
            if(validarFormulario(it)){
                registrarUsuarioApiRest(it)
            }else{
                binding.btnregistrarusuario.isEnabled = true
            }
        }

    }

    private fun registrarUsuarioApiRest(vista: View) {
        val urlapiregistro = "http://www.kreapps.biz/patitas/persona.php"
        val parametroJson = JSONObject()
        parametroJson.put("nombres", binding.etnombre.text.toString())
        parametroJson.put("apellidos", binding.etapellido.text.toString())
        parametroJson.put("email", binding.etemail.text.toString())
        parametroJson.put("celular", binding.etcelular.text.toString())
        parametroJson.put("usuario", binding.etusuarioreg.text.toString())
        parametroJson.put("password", binding.etpasswordreg.text.toString())
        val request = JsonObjectRequest(
                Request.Method.PUT,
                urlapiregistro,
                parametroJson,
                { response ->
                    if(response.getBoolean("rpta")){
                        setearControles()
                    }
                    mostrarMnesaje(vista, response.getString("mensaje"))
                    binding.btnregistrarusuario.isEnabled = true
                },{
                    Log.e("LOGREG", it.message.toString())
            binding.btnregistrarusuario.isEnabled = true
        })
        colapeticiones.add(request)
    }

    fun setearControles(){
        binding.etnombre.setText("")
        binding.etapellido.setText("")
        binding.etcelular.setText("")
        binding.etemail.setText("")
        binding.etusuarioreg.setText("")
        binding.etpasswordreg.setText("")
        binding.etpasswordreg2.setText("")
    }

    fun validarFormulario(vista: View): Boolean{
        var respuesta = true
        when{
            binding.etnombre.text.toString().trim().isEmpty() -> {
                binding.etnombre.isFocusableInTouchMode = true
                binding.etnombre.requestFocus()
                mostrarMnesaje(vista, getString(R.string.msgerrorregnombre))
                respuesta = false
            }
            binding.etapellido.text.toString().trim().isEmpty() -> {
                binding.etapellido.isFocusableInTouchMode = true
                binding.etapellido.requestFocus()
                mostrarMnesaje(vista, getString(R.string.msgerrorregape))
                respuesta = false
            }
            binding.etcelular.text.toString().trim().isEmpty() -> {
                binding.etcelular.isFocusableInTouchMode = true
                binding.etcelular.requestFocus()
                mostrarMnesaje(vista, getString(R.string.msgerrorregcel))
                respuesta = false
            }
            binding.etemail.text.toString().trim().isEmpty() -> {
                binding.etemail.isFocusableInTouchMode = true
                binding.etemail.requestFocus()
                mostrarMnesaje(vista, getString(R.string.msgerrorregemail))
                respuesta = false
            }
            binding.etusuarioreg.text.toString().trim().isEmpty() -> {
                binding.etusuarioreg.isFocusableInTouchMode = true
                binding.etusuarioreg.requestFocus()
                mostrarMnesaje(vista, getString(R.string.msgerrorregusu))
                respuesta = false
            }
            binding.etpasswordreg.text.toString().trim().isEmpty() -> {
                binding.etpasswordreg.isFocusableInTouchMode = true
                binding.etpasswordreg.requestFocus()
                mostrarMnesaje(vista, getString(R.string.msgerrorregpass))
                respuesta = false
            }
            binding.etpasswordreg.text.toString().trim().isNotEmpty() -> {
                if(binding.etpasswordreg.text.toString()
                        != binding.etpasswordreg2.text.toString()){
                    binding.etpasswordreg2.isFocusableInTouchMode = true
                    binding.etpasswordreg2.requestFocus()
                    mostrarMnesaje(vista, getString(R.string.msgerrorregpass2))
                    respuesta = false
                }
            }
        }

        return respuesta
    }

    fun mostrarMnesaje(vista: View, mensaje: String){
        Snackbar.make(vista, mensaje, Snackbar.LENGTH_LONG).show()
    }

}
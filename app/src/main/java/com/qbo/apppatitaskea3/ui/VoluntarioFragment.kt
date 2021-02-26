package com.qbo.apppatitaskea3.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.qbo.apppatitaskea3.R
import com.qbo.apppatitaskea3.databinding.FragmentVoluntarioBinding
import com.qbo.apppatitaskea3.db.entity.PersonaEntity
import com.qbo.apppatitaskea3.viewmodel.PersonaViewModel
import org.json.JSONObject

class VoluntarioFragment : Fragment() {

    private var _binding: FragmentVoluntarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var colapeticiones: RequestQueue
    private lateinit var personaEntity: PersonaEntity
    private lateinit var personaViewModel: PersonaViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVoluntarioBinding.inflate(inflater, container, false)
        val vista = binding.root
        colapeticiones = Volley.newRequestQueue(context)
        personaViewModel = ViewModelProvider(this).get(PersonaViewModel::class.java)
        personaViewModel.obtener()
                .observe(viewLifecycleOwner, Observer {
                    persona->
                    persona?.let {
                        if(persona.esvoluntario == "1"){
                            actualizarFormulario()
                        }else{
                            personaEntity = persona
                        }
                    }
                })
        binding.btnregvoluntario.setOnClickListener {
            if(binding.chkaceptocondiciones.isChecked){
                binding.btnregvoluntario.isEnabled = false
                registrarVoluntario(it)
            }else{
                mostrarMensaje(it, getString(R.string.msgerrorcheckcvol))
            }
        }

        return vista
    }

    private fun registrarVoluntario(vista: View) {
        val urlvoluntario = "http://www.kreapps.biz/patitas/personavoluntaria.php"
        val parametroJson = JSONObject()
        parametroJson.put("idpersona",personaEntity.id)
        val request = JsonObjectRequest(
                Request.Method.POST,
                urlvoluntario,
                parametroJson,
                {
                 response->
                 if(response.getBoolean("rpta")){
                     val nuevaPersonaEntity = PersonaEntity(
                             personaEntity.id,
                             personaEntity.nombres,
                             personaEntity.apellidos,
                             personaEntity.email,
                             personaEntity.celular,
                             personaEntity.usuario,
                             personaEntity.password,
                             "1"
                     )
                     personaViewModel.actualizar(nuevaPersonaEntity)
                     actualizarFormulario()
                 }
                    mostrarMensaje(vista, response.getString("mensaje"))
                    binding.btnregvoluntario.isEnabled = true
                },{
            binding.btnregvoluntario.isEnabled = true
        })
        colapeticiones.add(request)
    }
    fun actualizarFormulario(){
        binding.tvinfovoluntario.visibility = View.GONE
        binding.btnregvoluntario.visibility = View.GONE
        binding.chkaceptocondiciones.visibility = View.GONE
        binding.tvtitulovoluntario.text = getString(R.string.valtituloesvoluntario)
    }
    fun mostrarMensaje(vista: View, mensaje: String){
        Snackbar.make(vista, mensaje, Snackbar.LENGTH_LONG).show()
    }

}
package com.qbo.apppatitaskea3.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.qbo.apppatitaskea3.R
import com.qbo.apppatitaskea3.databinding.FragmentVoluntarioBinding
import org.json.JSONObject

class VoluntarioFragment : Fragment() {

    private var _binding: FragmentVoluntarioBinding? = null
    private val binding get() = _binding!!
    private lateinit var colapeticiones: RequestQueue


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVoluntarioBinding.inflate(inflater, container, false)
        val vista = binding.root
        colapeticiones = Volley.newRequestQueue(context)
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
        parametroJson.put("idpersona",1)
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
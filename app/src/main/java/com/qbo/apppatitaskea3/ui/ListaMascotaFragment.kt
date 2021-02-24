package com.qbo.apppatitaskea3.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.qbo.apppatitaskea3.R
import com.qbo.apppatitaskea3.adapters.MascotaAdapter
import com.qbo.apppatitaskea3.databinding.FragmentListaMascotaBinding
import com.qbo.apppatitaskea3.model.Mascota


class ListaMascotaFragment : Fragment() {

    private var _binding: FragmentListaMascotaBinding? = null
    private val binding get() = _binding!!
    private lateinit var colapeticiones : RequestQueue

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListaMascotaBinding.inflate(inflater, container, false)
        val vista = binding.root
        colapeticiones = Volley.newRequestQueue(context)
        binding.rvmascotas.layoutManager = LinearLayoutManager(context)
        listarMascotaPerdida(vista.context)
        return vista
    }

    private fun listarMascotaPerdida(context: Context) {
        val lstmascota : ArrayList<Mascota> = ArrayList()
        val urlapirest = "http://www.kreapps.biz/patitas/mascotaperdida.php"
        val request = JsonArrayRequest(
            Request.Method.GET,
            urlapirest,
            null,
            { response->
                for (i in 0 until response.length()){
                    val itemmascota = response.getJSONObject(i)
                    lstmascota.add(
                        Mascota(itemmascota["nommascota"].toString(),
                            itemmascota["fechaperdida"].toString(),
                            itemmascota["urlimagen"].toString(),
                            itemmascota["lugar"].toString(),
                            itemmascota["contacto"].toString())
                    )
                }
                binding.rvmascotas.adapter = MascotaAdapter(lstmascota, context)
            },{

            }
        )
        colapeticiones.add(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
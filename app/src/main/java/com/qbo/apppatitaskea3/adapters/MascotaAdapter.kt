package com.qbo.apppatitaskea3.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.qbo.apppatitaskea3.R
import com.qbo.apppatitaskea3.model.Mascota

class MascotaAdapter(private var lstmascota: List<Mascota>,
                    private val context: Context)
    : RecyclerView.Adapter<MascotaAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvnommascota : TextView = itemView.findViewById(R.id.tvnommascota)
        val tvlugar : TextView = itemView.findViewById(R.id.tvlugar)
        val tvcontacto : TextView = itemView.findViewById(R.id.tvcontacto)
        val tvfechaperdida : TextView = itemView.findViewById(R.id.tvfechaperdida)
        val ivmascota : ImageView = itemView.findViewById(R.id.ivmascota)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_mascota,
                parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lstmascota[position]
        holder.tvnommascota.text = item.nommascota
        holder.tvlugar.text = item.lugar
        holder.tvcontacto.text = item.contacto
        holder.tvfechaperdida.text = item.fechaperdida
        Glide.with(context).load(item.urlimagen).into(holder.ivmascota)
    }
    override fun getItemCount(): Int {
        return lstmascota.size
    }
}



package com.qbo.apppatitaskea3.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.qbo.apppatitaskea3.db.dao.PersonaDao
import com.qbo.apppatitaskea3.db.entity.PersonaEntity

@Database(entities = [PersonaEntity::class], version = 1)
public abstract class PatitasRoomDatabase : RoomDatabase() {

    abstract fun personaDao(): PersonaDao
    //Agrupar que todas las variables y métodos definidos sean estáticos
    companion object{
        @Volatile
        private var INSTANCE: PatitasRoomDatabase? = null

        fun getDatabase(context: Context) : PatitasRoomDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            //este método es utilizado solo por un hilo
            //haciendo que si otro lo necesite lo hace esperar.
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PatitasRoomDatabase::class.java,
                    "patitasdb"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
package com.freedman.getitdone.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//could add more tables by entities = [Task::class, some other class]
@Database(entities = [Task::class], version = 2) //version is for migrations
abstract class GetItDoneDatabase : RoomDatabase() {

    abstract fun getTaskDao(): TaskDao

    companion object{

        @Volatile //only store this in central/ main memory -> important info
        private var DATABASE_INSTANCE: GetItDoneDatabase? = null


        fun getDatabase(context: Context): GetItDoneDatabase {

            return DATABASE_INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    GetItDoneDatabase::class.java,
                    "get-it-done-database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                DATABASE_INSTANCE = instance
                return instance
            }




        }
    }

}


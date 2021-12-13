package org.lycancypher.exam_android_developer.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 2)
abstract class NewDb : RoomDatabase() {

    companion object {
        private var newDBInstance: NewDb? = null

        const val DB_NAME = "Nueva_DB"

        fun getInstance(context: Context) : NewDb? {
            if (newDBInstance == null) {

                synchronized(NewDb::class) {
                    newDBInstance = Room.databaseBuilder(
                        context.applicationContext,
                        NewDb::class.java,
                        DB_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return newDBInstance
        }
    }

    abstract fun userDao(): UserDao
}
package com.cavies.pokedex.data.local.room.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.File
import java.io.FileOutputStream

class DatabaseHelper(
    private val context: Context
) : SQLiteOpenHelper(context, "pokedex.db", null, 1) {

    private val dbPath = context.getDatabasePath("pokedex.db").path

    override fun onCreate(db: SQLiteDatabase?) {}
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
    override fun getReadableDatabase(): SQLiteDatabase {
        copyDatabaseIfNeeded()
        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
    }

    private fun copyDatabaseIfNeeded() {
        val dbFile = File(dbPath)
        if (dbFile.exists()) return

        dbFile.parentFile?.mkdirs()

        context.assets.open("databases/pokedex.db").use { input ->
            FileOutputStream(dbFile).use { output ->
                input.copyTo(output)
            }
        }
    }
}



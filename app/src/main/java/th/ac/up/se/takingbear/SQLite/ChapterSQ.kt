package th.ac.up.se.takingbear.SQLite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.fragment.app.FragmentActivity
import th.ac.up.se.takingbear.Data.ChapterCheck

class ChapterSQ(private var context: FragmentActivity, private var tableName: String, private var size: Int) : SQLiteOpenHelper(context, tableName, null, 1) {

    private var TABLE_NAME: String = tableName
    private var QUIZ: String = "QUIZ"
    private var PASSED: String = "PASSED"

    private var OPENED: String = "OPENED"

    //private var sqLiteDatabase: SQLiteDatabase

    companion object {
        fun convertName(name: String): String {
            return "QUIZ$name"
        }

        fun convertName(name: Int): String {
            return "QUIZ$name"
        }
    }

    init {
        checkStatus()
        //sqLiteDatabase = this.writableDatabase
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE $TABLE_NAME (_id INTEGER PRIMARY KEY AUTOINCREMENT, $QUIZ INTEGER,$PASSED INTEGER,$OPENED INTEGER)")
        db.initSize()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    private fun deleteAll() {
        var sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.execSQL("DELETE FROM $TABLE_NAME")
    }

    private fun setQuizPassed(number: Int, passed: Int) {
        var sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.execSQL("UPDATE $TABLE_NAME SET $PASSED = $passed WHERE $QUIZ = $number")
        if (number < getSize()) {
            openedQuiz(number + 1, true)

        }

        //log()
    }

    private fun setQuizOpened(number: Int, opened: Int) {
        var sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.execSQL("UPDATE $TABLE_NAME SET $OPENED = $opened WHERE $QUIZ = $number")
    }


    fun getData(): ArrayList<ChapterCheck> {
        val data = ArrayList<ChapterCheck>()
        val database = ChapterSQ(context, TABLE_NAME, size)
        var sqLiteDatabase = this.writableDatabase
        val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
        cursor.moveToFirst()
        if (cursor.count > 0) {
            do {
                val x = cursor.getInt(cursor.getColumnIndex(database.QUIZ))
                val y = cursor.getInt(cursor.getColumnIndex(database.PASSED))
                val z = cursor.getInt(cursor.getColumnIndex(database.OPENED))

                //val slot = ChapterCheck(x, y, z)
                //data.add(slot)
            } while (cursor.moveToNext())
        }
        return data
    }

    private fun resetTable() {
        deleteAll()
        var sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.initSize()
    }

    private fun checkStatus() {
        if (match()) resetTable()
    }

    private fun SQLiteDatabase.initSize() {
        var i = 0
        while (i < size) {
            if (i == 0) {
                this.execSQL("INSERT INTO $TABLE_NAME ($QUIZ,$PASSED,$OPENED) VALUES ($i,0,1)")
            } else {
                this.execSQL("INSERT INTO $TABLE_NAME ($QUIZ,$PASSED,$OPENED) VALUES ($i,0,0)")
            }

            i += 1
        }
    }

    fun getSize(): Int {
        //var database: ChapterSQ = ChapterSQ(context,TABLE_NAME,size)
        var sqLiteDatabase: SQLiteDatabase = this.writableDatabase
        //var cursor: Cursor = sqLiteDatabase.rawQuery(("SELECT " + database.COL_STYLE + " FROM " + database.TABLE_NAME), null)
        val cursor: Cursor = sqLiteDatabase.rawQuery("SELECT $QUIZ FROM $TABLE_NAME", null)
        cursor.moveToFirst()
        val x = cursor.count
        //var x = cursor.getInt(cursor.getColumnIndex(database.COL_STYLE))
        return x
    }

    fun passedQuiz(number: Int, passed: Boolean) {
        if (passed) {
            setQuizPassed(number, 1)
        } else {
            setQuizPassed(number, 0)
        }
    }

    fun openedQuiz(number: Int, opened: Boolean) {
        if (opened) {
            setQuizOpened(number, 1)
        } else {
            setQuizOpened(number, 0)
        }
    }

    fun clearAll() {
        val size = getSize()
        var i = 0
        while (i < size) {
            passedQuiz(i, false)
            i += 1
        }
    }

    private fun log() {
        var data = getData()
        data.forEach {
            Log.e(it.number.toString(), "OPENED ${it.opened} PASSED ${it.passed}")
        }
    }


    private fun match(): Boolean {
        return getSize() != size
    }

}
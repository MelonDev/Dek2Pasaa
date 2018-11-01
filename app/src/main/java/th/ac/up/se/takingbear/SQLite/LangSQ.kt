package th.ac.up.agr.thai_mini_chicken.SQLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import androidx.fragment.app.FragmentActivity
import th.ac.up.se.takingbear.R


class LangSQ(private var context: FragmentActivity) : SQLiteOpenHelper(context, Con.NAME, null, Con.VERSION) {

    var TABLE_NAME = "LANG_SQ"
    var LANG = "LANG"


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LANG + " INTEGER);")
        db.execSQL("INSERT INTO $TABLE_NAME ($LANG) VALUES (0);")
        /*
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_NAME + " TEXT, " + COL_PIECE_PRICE + " INTEGER, "
        + COL_CAKE_PRICE + " INTEGER);")
        */

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")

        onCreate(db)
    }

    fun update(lang:String) {
        //db!!.execSQL("UPDATE " + TABLE_NAME + " SET username='android', password='jellybean', level=99 WHERE username='sleeping' AND password='forless' AND level=19;\n")
        val cv = ContentValues()
        var sqLiteDatabase: SQLiteDatabase = this.writableDatabase

        if(lang.contentEquals(THAI)){
            cv.put(LANG, 0)
        }else {
            cv.put(LANG, 1)
        }
        sqLiteDatabase.update(TABLE_NAME, cv, "_id=" + 1, null)
    }

    fun read() :String {
        //var database: LangSQ = LangSQ(context)
        var sqLiteDatabase: SQLiteDatabase = this.writableDatabase
        var cursor: Cursor = sqLiteDatabase.rawQuery(("SELECT " + this.LANG + " FROM " + this.TABLE_NAME), null)
        cursor.moveToFirst()
        var x = cursor.getInt(cursor.getColumnIndex(this.LANG))
        if(x == 0){
            return "THAI"
        }else {
            return "ENG"
        }
    }

    class Con {
        companion object {
            val NAME = "LANG_SQ"
            val VERSION = 1
        }
    }

    companion object {
        val THAI = "THAI"
        val ENG = "ENG"
    }
}
package captemma.froggie.android.familytree.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context):
    SQLiteOpenHelper(context, DbConstants.DATABASE_NAME, null, DbConstants.DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase) {
        DbConstants.People.onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        DbConstants.People.onUpgrade(db, oldVersion, newVersion)
    }
}
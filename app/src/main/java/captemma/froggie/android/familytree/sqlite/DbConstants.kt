package captemma.froggie.android.familytree.sqlite

import android.database.sqlite.SQLiteDatabase

object DbConstants {
    const val DATABASE_NAME = "familytree.db"
    const val DATABASE_VERSION = 1

    object People{
        const val DATABASE_TABLE = "people"

        enum class Columns{
            ID, F_NAME, L_NAME, GENDER, PARENT1, PARENT2, HEIR
        }

        private val DATABASE_CREATE = """create table if not exists $DATABASE_TABLE(
            ${Columns.ID.name} integer primary key autoincrement,
            ${Columns.F_NAME.name} text not null,
            ${Columns.L_NAME.name} text not null
            ${Columns.GENDER.name} text not null,
            ${Columns.PARENT1.name} integer,
            ${Columns.PARENT2.name} integer,
            ${Columns.HEIR.name} integer not null
            );""".trimIndent()

        private const val DATABASE_DROP = "drop table if exists $DATABASE_TABLE;"

        fun onCreate(database: SQLiteDatabase){
            database.execSQL(DATABASE_CREATE)
        }

        fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int){
            database.execSQL(DATABASE_DROP)
            onCreate(database)
        }
    }
}
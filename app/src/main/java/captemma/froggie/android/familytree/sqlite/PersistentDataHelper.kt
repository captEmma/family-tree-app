package captemma.froggie.android.familytree.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import captemma.froggie.android.familytree.model.PersonRepository
import kotlin.jvm.Throws

class PersistentDataHelper(context: Context) {
    private var database: SQLiteDatabase? = null
    private val dbHelper: DbHelper = DbHelper(context)

    private val personColumns = arrayOf(
        DbConstants.People.Columns.ID.name,
        DbConstants.People.Columns.F_NAME.name,
        DbConstants.People.Columns.L_NAME.name,
        DbConstants.People.Columns.GENDER.name,
        DbConstants.People.Columns.PARENT1.name,
        DbConstants.People.Columns.PARENT2.name,
        DbConstants.People.Columns.HEIR.name,
    )

    @Throws(SQLiteException::class)
    fun open(){
        database = dbHelper.writableDatabase
    }

    fun close(){
        dbHelper.close()
    }

    fun persistPeople(personRepository: PersonRepository){
        for (person in personRepository.getPeople()){
            val values = ContentValues()
            values.put(DbConstants.People.Columns.F_NAME.name, person.firstName)
            values.put(DbConstants.People.Columns.L_NAME.name, person.lastName)
            values.put(DbConstants.People.Columns.GENDER.name, person.gender.name)
            values.put(DbConstants.People.Columns.PARENT1.name, person.parentIds.getOrNull(0))
            values.put(DbConstants.People.Columns.PARENT2.name, person.parentIds.getOrNull(1))
            values.put(DbConstants.People.Columns.HEIR.name, if (person.isHeir) 1 else 0)

            if(person.id==0){
                val newId = database?.insert(DbConstants.People.DATABASE_TABLE, null, values)!!.toInt()
                person.id = newId
            }
        }
    }
}
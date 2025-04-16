package captemma.froggie.android.familytree.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import androidx.compose.runtime.mutableStateListOf
import captemma.froggie.android.familytree.model.Gender
import captemma.froggie.android.familytree.model.Person
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

//    fun persistPeople(personRepository: PersonRepository){
//        for (person in personRepository.getPeople()){
//            val values = ContentValues()
//            values.put(DbConstants.People.Columns.F_NAME.name, person.firstName)
//            values.put(DbConstants.People.Columns.L_NAME.name, person.lastName)
//            values.put(DbConstants.People.Columns.GENDER.name, person.gender.name)
//            values.put(DbConstants.People.Columns.PARENT1.name, person.parentIds.getOrNull(0))
//            values.put(DbConstants.People.Columns.PARENT2.name, person.parentIds.getOrNull(1))
//            values.put(DbConstants.People.Columns.HEIR.name, if (person.isHeir) 1 else 0)
//
//            if(person.id==0){
//                val newId = database?.insert(DbConstants.People.DATABASE_TABLE, null, values)!!.toInt()
//                person.id = newId
//            }
//        }
//    }

    fun insertPerson(
        firstName: String,
        lastName: String,
        gender: Gender,
        parents: List<Int>,
        heir: Boolean
    ): Person {
        val values = ContentValues().apply {
            put(DbConstants.People.Columns.F_NAME.name, firstName)
            put(DbConstants.People.Columns.L_NAME.name, lastName)
            put(DbConstants.People.Columns.GENDER.name, gender.name)
            put(DbConstants.People.Columns.PARENT1.name, parents.getOrNull(0))
            put(DbConstants.People.Columns.PARENT2.name, parents.getOrNull(1))
            put(DbConstants.People.Columns.HEIR.name, if (heir) 1 else 0)
        }

        val newId = database?.insert(DbConstants.People.DATABASE_TABLE, null, values)?.toInt()
            ?: throw Exception("Failed to insert person into DB")

        return Person(
            id = newId,
            firstName = firstName,
            lastName = lastName,
            gender = gender,
            parentIds = parents.toMutableList(),
            isHeir = heir
        )
    }

    fun loadPeople(): List<Person> {
        val people = mutableListOf<Person>()
        val cursor = database?.query(
            DbConstants.People.DATABASE_TABLE,
            personColumns,
            null, null, null, null, null
        )
        cursor?.use{
            while(it.moveToNext()){
                val id = it.getInt(it.getColumnIndexOrThrow(DbConstants.People.Columns.ID.name))
                val firstName = it.getString(it.getColumnIndexOrThrow(DbConstants.People.Columns.F_NAME.name))
                val lastName = it.getString(it.getColumnIndexOrThrow(DbConstants.People.Columns.L_NAME.name))
                val gender = Gender.valueOf(it.getString(it.getColumnIndexOrThrow(DbConstants.People.Columns.GENDER.name)))
                val parent1 = it.getInt(it.getColumnIndexOrThrow(DbConstants.People.Columns.PARENT1.name))
                val parent2 = it.getInt(it.getColumnIndexOrThrow(DbConstants.People.Columns.PARENT2.name))
                val heir = it.getInt(it.getColumnIndexOrThrow(DbConstants.People.Columns.HEIR.name)) == 1

                val parents = listOfNotNull(
                    parent1.takeIf { parent1 != 0 },
                    parent2.takeIf { parent2 != 0 }
                )

                people.add(Person(
                    id,
                    firstName,
                    lastName,
                    gender,
                    parents.toMutableList(),
                    heir
                ))
            }
        }
        cursor?.close()
        return people
    }
}
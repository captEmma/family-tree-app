package captemma.froggie.android.familytree.model

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import captemma.froggie.android.familytree.sqlite.PersistentDataHelper

class PersonRepository(private val dataHelper: PersistentDataHelper) {
    private val people = mutableStateListOf<Person>()

    init {
        dataHelper.open()
        val dbPeople = dataHelper.loadPeople()
        people.addAll(dbPeople)
    }

    fun addPerson(
        firstName: String, lastName: String,
        gender: Gender = Gender.OTHER,
        parents: MutableList<Int> = mutableListOf(),
        heir: Boolean = false
    ): Person{
        val person = dataHelper.insertPerson(
            firstName,
            lastName,
            gender,
            parents,
            heir
        )

        people.add(person)
        Log.d("Debug", "Added person: $person")
        people.forEach{
            Log.d("Debug", "${person.id}. $person")
        }

        return person
    }

    fun addParentToPerson(personId: Int, parentId: Int){
        //TODO PERSISTENCE
        getPersonById(personId)?.let {
            if (it.canAddParent(parentId))
                it.parentIds.add(parentId)
        }
        Log.d("Debug", getPersonById(personId).toString())
    }

    fun addChildrenToPerson(personId: Int, childrenIds: List<Int>){
        getPeopleByIds(childrenIds).forEach{
            addParentToPerson(it.id, personId)
        }
    }

    fun removePerson(personId: Int){
        getPeopleByIds(findChildrenOf(personId))
            .forEach{it.parentIds.remove(personId)} //remove reference to person as parent from all children

        people.remove(getPersonById(personId))
        people.forEach{
            Log.d("Debug", it.getFullName())
        }
    }

    fun findChildrenOf(personId: Int):List<Int>{
        return people.filter { child -> personId in child.parentIds }
            .map { it.id }
    }

    fun getPersonById(id: Int): Person?{
        return people.find { person -> id == person.id }
    }

    fun getPeopleByIds(ids: List<Int>): List<Person>{
//        val personMap = people.associateBy { it.id }
//        return ids.mapNotNull { personMap[it] } //for maintaining order of people
        return people.filter { it.id in ids }   //doesn't maintain order of people
    }

    fun findIdByName(firstName: String, lastName: String) : Int?{
        return people.find { person ->
            firstName == person.firstName && lastName == person.lastName
        }?.id
    }

    fun getPeople(): List<Person> {
        //TODO changing this to persistent
        return people
    }

    fun getIds(): List<Int>{
        //TODO changing this to persistent
        return people.map { it.id }
    }
}
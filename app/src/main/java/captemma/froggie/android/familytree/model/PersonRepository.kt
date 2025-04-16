package captemma.froggie.android.familytree.model

import android.util.Log
import androidx.compose.runtime.mutableStateListOf

class PersonRepository {
    private val people = mutableStateListOf<Person>()
    private var nextID = 1

    fun addPerson(
        firstName: String, lastName: String,
        gender: Gender = Gender.OTHER,
        parents: MutableList<Int> = mutableListOf(),
        heir: Boolean = false
    ): Person{
        if (parents.size > 2) {
            throw IllegalArgumentException("A person can have maximum 2 biological parents")
        }

        val person = Person(
            id=nextID,
            firstName=firstName,
            lastName=lastName,
            gender=gender,
            parentIds=parents,
            isHeir=heir
        )

        people.add(person)
        nextID++
        Log.d("Debug", "Added person: $person")
        people.forEach{
            Log.d("Debug", it.getFullName())
        }

        return person
    }

    fun addParentToPerson(personId: Int, parentId: Int){
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
        return people
    }

    fun getIds(): List<Int>{
        return people.map { it.id }
    }
}
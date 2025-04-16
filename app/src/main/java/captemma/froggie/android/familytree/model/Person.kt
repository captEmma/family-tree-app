package captemma.froggie.android.familytree.model

enum class Gender{
    MALE,
    FEMALE,
    OTHER
}

data class Person(
    val id: Int,
    var firstName: String,
    var lastName: String,
    var gender: Gender= Gender.OTHER,
    var parentIds: MutableList<Int> = mutableListOf(),
    var isHeir: Boolean = false
){
    fun canAddParent(parentId : Int):Boolean{
        return parentIds.size<2 && !parentIds.contains(parentId) && parentId!=id
    }

    fun getFullName(): String{
        return "$firstName $lastName"
    }
}
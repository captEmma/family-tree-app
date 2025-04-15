package captemma.froggie.android.familytree.model

data class Person(
    val id: Int,
    var firstName: String,
    var lastName: String,
    var parentIds: MutableList<Int> = mutableListOf(),
    var isHeir: Boolean = false
){
    fun canAddParent(parentId : Int):Boolean{
        return parentIds.size<2 && !parentIds.contains(parentId)
    }
    fun getFullName(): String{
        return "$firstName $lastName"
    }
}
package th.ac.up.se.takingbear.Data

class ExamData {

    var question = ""
    var answerPosition = -1

    var scroll = false

    var choice = ArrayList<String>()

    fun addChoice(str: String){
        choice.add(str)
    }



}
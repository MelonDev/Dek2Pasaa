package th.ac.up.se.takingbear.Data

class QuizCard {

    var question: String = ""

    var choiceA: String = ""
    var choiceB: String = ""

    var answer: Int = -1

    var image :Int = 0

    constructor(question: String, choiceA: String, choiceB:String,answer: Int,image :Int) {

        this.question = question
        this.choiceA = choiceA
        this.choiceB = choiceB
        this.answer = answer
        this.image = image

    }


}
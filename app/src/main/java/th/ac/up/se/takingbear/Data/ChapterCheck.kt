package th.ac.up.se.takingbear.Data

class ChapterCheck {


    var number :Int = -1
    var passed :Boolean = false
    var opened :Boolean = false

    constructor(number: Int,passed :Boolean,opened :Boolean){
        this.number = number
        this.passed = passed
        this.opened = opened
    }

    constructor(number: Int,passed :Int,opened: Int){
        this.number = number


        if(passed == 0){
            this.passed = false
        }else if(passed == 1){
            this.passed = true
        }

        if(opened == 0){
            this.opened = false
        }else if(opened == 1){
            this.opened = true
        }
    }

}
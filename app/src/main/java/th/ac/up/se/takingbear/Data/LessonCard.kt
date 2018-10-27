package th.ac.up.se.takingbear.Data

class LessonCard {

    var nameThai : String = ""
    var nameEng : String = ""
    var nameThaiSpecial :String = ""
    var image :Int = 0
    var audioThai :Int = 0
    var audioEng :Int = 0

    var a_x :String = ""
    var a_y :String = ""
    var b_y :String = ""
    var b_x :String = ""

    constructor(nameThai :String,nameEng :String,nameThaiSpecial :String,image :Int,audioThai :Int,audioEng :Int){
        this.nameThai = nameThai
        this.nameEng = nameEng
        this.nameThaiSpecial = nameThaiSpecial
        this.image = image
        this.audioThai = audioThai
        this.audioEng = audioEng
    }

    constructor(a_x:String,a_y:String,b_x:String,b_y:String,image:Int){
        this.a_x = a_x
        this.a_y = a_y
        this.b_x = b_x
        this.b_y = b_y
        this.image = image
    }

}
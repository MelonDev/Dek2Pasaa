package th.ac.up.se.thaicardgame.DataArray

import android.support.v4.content.ContextCompat
import android.util.Log
import th.ac.up.se.takingbear.Data.Chapter
import th.ac.up.se.takingbear.Data.LessonCard
import th.ac.up.se.takingbear.Data.QuizCard
import th.ac.up.se.takingbear.R

class Quiz {

    fun getThaiChapter(): ArrayList<Chapter> {
        var arrayList = ArrayList<Chapter>()

        arrayList.apply {
            add(Chapter("ทักทาย", R.drawable.conver))
            //add(Chapter("ประเทศ", R.drawable.flag))
            add(Chapter("อาหาร", R.drawable.food))
            add(Chapter("สีสัน", R.drawable.color))
            add(Chapter("สถานที่", R.drawable.landmark))
            add(Chapter("อาชีพ", R.drawable.job))
            add(Chapter("นับเลข", R.drawable.number))
            add(Chapter("สัตว์", R.drawable.animal))
            add(Chapter("กริยา", R.drawable.walk))
            //add("2")
        }

        return arrayList
    }

    fun getEngChapter(): ArrayList<Chapter> {
        var arrayList = ArrayList<Chapter>()

        arrayList.apply {
            add(Chapter("Greet", R.drawable.conver))
            //add(Chapter("Country", R.drawable.flag))
            add(Chapter("Food", R.drawable.food))
            add(Chapter("Color", R.drawable.color))
            add(Chapter("Place", R.drawable.landmark))
            add(Chapter("Career", R.drawable.job))
            add(Chapter("Number", R.drawable.number))
            add(Chapter("Animal", R.drawable.animal))
            add(Chapter("Verb", R.drawable.walk))
            //add("2")
        }

        return arrayList
    }

    fun getChapterDemo(langID: Int): ArrayList<QuizCard> {

        if (langID == 0) {
            var arrayList = ArrayList<QuizCard>()
            arrayList.apply {

            }
            return arrayList
        } else if (langID == 1) {
            var arrayList = ArrayList<QuizCard>()
            arrayList.apply {

            }
            return arrayList
        } else {
            return ArrayList<QuizCard>()
        }

    }

    fun getChapter1(langID: Int): ArrayList<QuizCard> {

        if (langID == 0) {
            var arrayList = ArrayList<QuizCard>()
            arrayList.apply {
                add(QuizCard("คุณชื่ออะไร", "Kha ค่ะ ", "Kha คะ", 1, R.drawable.c_1_1))
                add(QuizCard("คุณชื่อเล่นว่าอะไร", "Kha ค่ะ ", "Kha คะ", 1, 0))
                add(QuizCard("สวัสดี______ผมชื่อเดวิด", "Khrap ครับ ", "Kha ค่ะ", 0, 0))
                add(QuizCard("สวัสดี______ฉันชื่อฟ้า", "Khrap ครับ ", "Kha ค่ะ", 1, 0))
                add(QuizCard("ฉันเป็น______", "คนไทย", "คนลาว", 1, 0))
                add(QuizCard("ฉันเป็น______", "คนญี่ปุ่น", "คนจีน", 1, 0))
                add(QuizCard("ฉันเป็น______", "คนอินโดนีเซียน", "คนอเมริกัน", 1, 0))
                add(QuizCard("ฉันเป็น______", "คนลาว", "คนญี่ปุ่น", 1, 0))
                add(QuizCard("ฉันเป็น______", "คนเวียดนาม", "ประเทศเวียดนาม", 1, 0))
                add(QuizCard("ฉันเป็น______", "คนกัมพูชา", "ประเทศเกาหลี", 1, 0))
                add(QuizCard("ฉันเป็น______", "คนพม่า	", "คนอินเดีย", 1, 0))
                add(QuizCard("ฉันเป็น______", "คนฝรั่งเศส", "คนสิงคโปร์", 1, 0))
                add(QuizCard("ฉันมาจากประเทศ______", "กัมพูชา", "สิงคโปร์", 1, 0))
                add(QuizCard("ฉันมาจากประเทศ______", "ลาว", "มาเลเซีย", 1, 0))
                add(QuizCard("ฉันมาจากประเทศ______", "บรูไน", "ฟิจิ", 1, 0))
                add(QuizCard("ฉันมาจากประเทศ______", "อินโดนีเซีย", "อินเดีย", 1, 0))
                add(QuizCard("ฉันมาจากประเทศลาว ฉันเป็น______", "คนเชียงใหม่", "คนลาว", 1, 0))
                add(QuizCard("ฉันมาจากประเทศฟิลิปปินส์ ฉันเป็น______", "ประเทศฟิจิ", "คนฟิลิปปินส์", 1, 0))
            }
            return arrayList
        } else if (langID == 1) {
            var arrayList = ArrayList<QuizCard>()
            arrayList.apply {

            }
            return arrayList
        } else {
            return ArrayList<QuizCard>()
        }

    }

    fun getChapter2(langID: Int): ArrayList<QuizCard> {

        if (langID == 0) {
            var arrayList = ArrayList<QuizCard>()
            arrayList.apply {
                add(QuizCard("หิวไหม______", "Kha ค่ะ", "Kha คะ", 1, 0))
                add(QuizCard("คุณชอบกินอะไร______", "Khrap ครับ ", "Kha ค่ะ", 1, 0))
                add(QuizCard("ฉันชอบกิน______", "แกงจืด ", "นิ้ว", 1, 0))
                add(QuizCard("ฉันชอบกิน______", "จาน", "ปลาทอด", 1, 0))
                add(QuizCard("ฉันชอบกิน______", "ไข่ ", "ปลา", 1, 0))
                add(QuizCard("ฉันชอบกิน______", "ไข่ทอด ", "ไข่เจียว", 1, 0))
                add(QuizCard("ฉันขอ______", "ไก่ 	", "ไข่", 1, 0))
                add(QuizCard("ฉันขอ______", "ผัดไทย ", "ผัดกะเพราหมู", 1, 0))
                add(QuizCard("ฉันขอ______", "ข้าวเหนียว", "ข้าวสวย", 1, 0))
                add(QuizCard("ฉันขอ______", "หมูทอด ", "หมูแดง", 1, 0))
                add(QuizCard("ฉันขอ______", "ก๋วยเตี๋ยว ", "ส้มตำ", 1, 0))
                add(QuizCard("นี่คืออะไร______", "Kha ค่ะ ", "Kha คะ", 1, 0))
                add(QuizCard("นี่คือ______", "อาหาร", "กาแฟ", 1, 0))
                add(QuizCard("นี่คือ______", "เบียร์", "นม", 1, 0))
                add(QuizCard("ฉันขอ______", "น้ำส้ม ", "น้ำชา", 1, 0))
                add(QuizCard("ฉันขอ______", "เหล้า", "น้ำเปล่า", 1, 0))
                add(QuizCard("ฉันขอ______", "น้ำปลา", "น้ำชา", 1, 0))
                add(QuizCard("ฉันขอ______", "นมปั่น", "ต้มจืด", 1, 0))
                add(QuizCard("ขอ__________ชิ้นหนึ่งค่ะ", "เค้ก", "สัปปะรด", 1, 0))
                add(QuizCard("ขอ__________สองกิโลค่ะ ", "มะม่วง", "มะขาม", 1, 0))
                add(QuizCard("ขอ__________สามลูกค่ะ ", "มะนาว", "แตง", 1, 0))
                add(QuizCard("ขอ__________ค่ะ ", "มังคุด", "มะม่วง", 1, 0))
                add(QuizCard("ฉันกิน__________สี่ลูกได้ ", "ละมุด", "แอปเปิล", 1, 0))
                add(QuizCard("มี________ห้าเม็ด ", "ลูกอม", "คน", 1, 0))
                add(QuizCard("ฉันชอบ______", "ทุเรียน", "เรียน", 1, 0))
                add(QuizCard("ฉันชอบ______", "ฝรั่ง", "กล้วย", 1, 0))
                add(QuizCard("มี____________หกแท่ง ", "ดินสอ", "ช็อกโกแลต", 1, 0))
                add(QuizCard("มี____________เจ็ดก้อน ", "ขนมปัง", "เตียง", 1, 0))
                add(QuizCard("กี่บาท_________? เท่าไหร่_________?", "Kha ค่ะ ", "Kha คะ", 1, 0))
                add(QuizCard("ทั้งหมด 800 บาท______", "Kha ค่ะ ", "Kha คะ", 1, 0))
                add(QuizCard("นี่เงินทอน______", "Kha ค่ะ ", "Kha คะ", 1, 0))
            }
            return arrayList
        } else if (langID == 1) {
            var arrayList = ArrayList<QuizCard>()
            arrayList.apply {

            }
            return arrayList
        } else {
            return ArrayList<QuizCard>()
        }

    }

    fun getChapter3(langID: Int): ArrayList<QuizCard> {

        if (langID == 0) {
            var arrayList = ArrayList<QuizCard>()
            arrayList.apply {
                add(QuizCard("นี่สีอะไร______", "Khrap ครับ", "Kha ค่ะ", 1, 0))
                add(QuizCard("นี่คือ______", "สีแดง", "สีเหลือง", 1, 0))
                add(QuizCard("นี่คือ______", "หลัง	", "สีดำ", 1, 0))
                add(QuizCard("ฉันชอบภาพ______", "ขาวดำ", "ดำ", 1, 0))
                add(QuizCard("ฉันชอบ______", "สีเขียว", "สีคราม", 1, 0))
                add(QuizCard("ฉันมีเสื้อเจ็ดตัว______", "สีส้ม", "สีเหลือง", 1, 0))
                add(QuizCard("เธอมีเสื้อแปดตัว______", "สีม่วง	", "สีฟ้า", 1, 0))
                add(QuizCard("ท้องฟ้าเป็น______", "สีเทา", "สีฟ้า", 1, 0))
                add(QuizCard("กระโปรง______", "สีหม่น", "สีเขียว", 1, 0))
                add(QuizCard("ผมของเธอ______", "สีน้ำตาล", "สีดำ", 1, 0))
                add(QuizCard("ผมของเขามี______", "สีดำ", "สีเทา", 1, 0))
                add(QuizCard("ดอกไม้เหล่านี้มี______", "สีสดใส", "สีขาว", 1, 0))
                add(QuizCard("ดาบเล่มนี้มี______", "สีเงิน", "สีทอง", 1, 0))
                add(QuizCard("เหรียญเหล่านี้มี______", "สีทอง", "ไป", 1, 0))
                add(QuizCard("นี่คือ______", "หมุด", "สีชมพู", 1, 0))
                add(QuizCard("นี่คือ______", "สีขาว", "ข้างขวา", 1, 0))
            }
            return arrayList
        } else if (langID == 1) {
            var arrayList = ArrayList<QuizCard>()
            arrayList.apply {

            }
            return arrayList
        } else {
            return ArrayList<QuizCard>()
        }

    }


    fun getChapter(langID: Int, position: Int): ArrayList<QuizCard> {

        //Log.e("pos", position.toString())

        return when (position) {
            1 -> {
                getChapter1(langID)
            }
            2 -> {
                getChapter2(langID)
            }
            3 -> {
                getChapter3(langID)
            }
            else -> {
                ArrayList<QuizCard>()
            }
        }
    }

}
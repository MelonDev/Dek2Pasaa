package th.ac.up.se.thaicardgame.DataArray

import th.ac.up.se.takingbear.Data.Chapter
import th.ac.up.se.takingbear.Data.LessonCard
import th.ac.up.se.takingbear.R

class Lesson {

    /*
    fun getThaiChapter(): ArrayList<Chapter> {
        var arrayList = ArrayList<Chapter>()

        arrayList.apply {
            add(Chapter("ทักทาย", R.drawable.conver))
            add(Chapter("ประเทศ", R.drawable.flag))
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
            add(Chapter("Country", R.drawable.flag))
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

    private fun getChapterDemo(): ArrayList<LessonCard> {

        var arrayList = ArrayList<LessonCard>()
        arrayList.apply {

        }

        return arrayList
    }

    private fun getChapter1(): ArrayList<LessonCard> {

        var arrayList = ArrayList<LessonCard>()
        arrayList.apply {
            add(LessonCard("สวัสดี", "Hi", "Sawatdee", R.drawable.c_1_1, R.raw.th_1_1, R.raw.en_1_1))
            add(LessonCard("ลาก่อน", "Goodbye", "Lakon", R.drawable.c_1_2, R.raw.th_1_2, R.raw.en_1_2))
            add(LessonCard("ฉัน", "I (female)", "Chan", R.drawable.c_1_3,  R.raw.th_1_3, R.raw.en_1_3))

            add(LessonCard("ผม", "I (male)", "Phom", R.drawable.c_1_4, R.raw.th_1_4, R.raw.en_1_3))
            add(LessonCard("คุณ", "You", "Khun", R.drawable.c_1_5,  R.raw.th_1_5, R.raw.en_1_5))
            add(LessonCard("ชื่อ", "Name", "Chue", R.drawable.c_1_6,  0, R.raw.en_1_6))
            add(LessonCard("ค่ะ/ คะ", "Final Particle (female)", "Kha",R.drawable.c_1_7, R.raw.th_1_7,  0))
            add(LessonCard("ครับ", "Final Particle (male)", "Khrap",R.drawable.c_1_8, R.raw.th_1_8,  0))
            add(LessonCard("อะไร? ", "What", "Arai", R.drawable.c_1_9,  R.raw.th_1_9, R.raw.en_1_9))

            add(LessonCard("จาก ", "Come from", "Maacaak", R.drawable.c_1_10,  0,0))
            add(LessonCard("ประเทศ ", "Country", "Prathet", R.drawable.c_1_11,  0, 0))

        }

        return arrayList
    }

    private fun getChapter2s(): ArrayList<LessonCard> {

        var arrayList = ArrayList<LessonCard>()
        arrayList.apply {


            add(LessonCard("ประเทศไทย", "Thailand", "คนไทย", "Thai", 0))
            add(LessonCard("ประเทศจีน", "China", "คนจีน", "Chinese", 0))
            add(LessonCard("ประเทศอเมริกา", "America", "คนอเมริกัน", "America", 0))
            add(LessonCard("ประเทศญี่ปุ่น", "Japan", "คนญี่ปุ่น", "Japanese", 0))
            add(LessonCard("ประเทศเวียดนาม", "Vietnam", "คนเวียดนาม", "Vietnamese", 0))
            add(LessonCard("ประเทศพม่า", "Myanmar", "คนพม่า", "Burmese", 0))
            add(LessonCard("ประเทศสิงคโปร์", "Singapore", "คนสิงคโปร์", "Singaporean", 0))
            add(LessonCard("ประเทศกัมพูชา", "Cambodia", "คนกัมพูชา/คนเขมร", "Cambodian", 0))
            add(LessonCard("ประเทศมาเลเซีย", "Malaysia", "คนมาเลเซีย", "Malaysian", 0))
            add(LessonCard("ประเทศอินโดนีเซีย", "Indonesia", "คนอินโดนีเซียน", "Indonesian", 0))
            add(LessonCard("ประเทศลาว", "Laos", "คนลาว", "Lao", 0))
            add(LessonCard("ประเทศฟิลิปปินส์", "Philippines", "คนฟิลิปปินส์", "Filipino", 0))
            add(LessonCard("ประเทศบรูไน", "Brunei", "คนบูรไน", "Bruneian", 0))
        }

        return arrayList
    }

    private fun getChapter2(): ArrayList<LessonCard> {

        var arrayList = ArrayList<LessonCard>()
        arrayList.apply {
            add(LessonCard("ประเทศไทย", "Thailand", "", R.drawable.c_1_12, R.raw.th_1_10, R.raw.en_1_10))
            add(LessonCard("คนไทย", "Thai", "", R.drawable.c_1_13, R.raw.th_1_11, R.raw.en_1_11))
            add(LessonCard("ประเทศจีน", "China", "", R.drawable.c_1_14, R.raw.th_1_12, R.raw.en_1_12))
            add(LessonCard("คนจีน", "Chinese", "", R.drawable.c_1_15, R.raw.th_1_13, R.raw.en_1_13))
            add(LessonCard("ประเทศอเมริกา", "America", "", R.drawable.c_1_16, R.raw.th_1_14, R.raw.en_1_14))
            add(LessonCard("คนอเมริกัน", "America", "", R.drawable.c_1_17, R.raw.th_1_15, R.raw.en_1_15))
            add(LessonCard("ประเทศญี่ปุ่น", "Japan", "", R.drawable.c_1_18, R.raw.th_1_16, R.raw.en_1_16))
            add(LessonCard("คนญี่ปุ่น", "Japanese", "", R.drawable.c_1_19, R.raw.th_1_17, R.raw.en_1_17))
            add(LessonCard("ประเทศเวียดนาม", "Vietnam", "", R.drawable.c_1_20, R.raw.th_1_18, R.raw.en_1_18))
            add(LessonCard("คนเวียดนาม", "Vietnamese", "", R.drawable.c_1_21, R.raw.th_1_19, R.raw.en_1_19))
            add(LessonCard("ประเทศพม่า", "Myanmar", "", R.drawable.c_1_22, R.raw.th_1_20, R.raw.en_1_20))
            add(LessonCard("คนพม่า", "Burmese", "", R.drawable.c_1_23, R.raw.th_1_21, R.raw.en_1_21))
            add(LessonCard("ประเทศสิงคโปร์", "Singapore", "", R.drawable.c_1_24, R.raw.th_1_22, R.raw.en_1_22))
            add(LessonCard("คนสิงคโปร์", "Singaporean", "", R.drawable.c_1_25, R.raw.th_1_23, R.raw.en_1_23))
            add(LessonCard("ประเทศกัมพูชา", "Cambodia", "", R.drawable.c_1_26, R.raw.th_1_24, R.raw.en_1_24))
            add(LessonCard("คนกัมพูชา/ คนเขมร", "Cambodian", "", R.drawable.c_1_27, R.raw.th_1_25, R.raw.en_1_25))
            add(LessonCard("ประเทศมาเลเซีย", "Malaysia", "", R.drawable.c_1_28, R.raw.th_1_26, R.raw.en_1_26))
            add(LessonCard("คนมาเลเซีย", "Malaysian", "", R.drawable.c_1_29, R.raw.th_1_27, R.raw.en_1_27))
            add(LessonCard("ประเทศอินโดนีเซีย", "Indonesia", "", R.drawable.c_1_30, R.raw.th_1_28, R.raw.en_1_28))
            add(LessonCard("คนอินโดนีเซียน", "Indonesian", "", R.drawable.c_1_31, R.raw.th_1_29, R.raw.en_1_29))
            add(LessonCard("ประเทศลาว", "Laos", "", R.drawable.c_1_32, R.raw.th_1_30, R.raw.en_1_30))
            add(LessonCard("คนลาว", "Lao", "", R.drawable.c_1_33, R.raw.th_1_31, R.raw.en_1_31))
            add(LessonCard("ประเทศฟิลิปปินส์", "Philippines", "", R.drawable.c_1_34, R.raw.th_1_32, R.raw.en_1_32))
            add(LessonCard("คนฟิลิปปินส์", "Filipino", "", R.drawable.c_1_35, R.raw.th_1_33, R.raw.en_1_33))
            add(LessonCard("ประเทศบรูไน", "Brunei", "", R.drawable.c_1_36, R.raw.th_1_34, R.raw.en_1_34))
            add(LessonCard("คนบูรไน", "Bruneian", "", R.drawable.c_1_37, R.raw.th_1_35, R.raw.en_1_35))
        }

        return arrayList
    }

    private fun getChapter3(): ArrayList<LessonCard> {

        var arrayList = ArrayList<LessonCard>()
        arrayList.apply {
            add(LessonCard("กิน", "eat", "Kin", R.drawable.c_2_1, 0, 0))
            add(LessonCard("หิว", "hungry", "Hiu", R.drawable.c_2_2, 0, 0))
            add(LessonCard("ชอบ", "like", "Choop", R.drawable.c_2_3, 0, 0))
            add(LessonCard("ขอ", "may I have", "Khoo", R.drawable.c_2_4, 0, 0))
            add(LessonCard("นี่คือ", "This is", "Nii Kheu", R.drawable.c_2_5, 0, 0))
            add(LessonCard("ปลา", "fish", "Plaa", R.drawable.c_2_6, 0, 0))
            add(LessonCard("ไข่", "egg", "Khai", R.drawable.c_2_7, 0, 0))
            add(LessonCard("ไก่", "chicken", "Kai", R.drawable.c_2_8, 0, 0))
            add(LessonCard("หมู", "pig", "Moo", R.drawable.c_2_9, 0, 0))
            add(LessonCard("น้ำ", "water", "Nam", R.drawable.c_2_10, 0, 0))
            add(LessonCard("กาแฟ", "coffee", "Kaafee", R.drawable.c_2_11, 0, 0))
            add(LessonCard("นม", "milk", "Nom", R.drawable.c_2_12, 0, 0))
            add(LessonCard("มะขาม", "tamarind", "Ma-khaam", R.drawable.c_2_13, 0, 0))
            add(LessonCard("สัปปะรด", "pineapple", "Sap-pa-rot", R.drawable.c_2_14, 0, 0))
            add(LessonCard("ทุเรียน", "durian", "Thu-rien", R.drawable.c_2_15, 0, 0))
        }

        return arrayList
    }

    private fun getChapter4(): ArrayList<LessonCard> {

        var arrayList = ArrayList<LessonCard>()
        arrayList.apply {
            add(LessonCard("สีแดง", "Red", "Siidaeng", R.drawable.c_3_1, 0, 0))
            add(LessonCard("สีดำ", "Black", "Siidam", R.drawable.c_3_2, 0, 0))
            add(LessonCard("สีเทา", "Grey", "Siithau", R.drawable.c_3_3, 0, 0))
            add(LessonCard("สีเขียว", "Green", "Siikhieu", R.drawable.c_3_4, 0, 0))
            add(LessonCard("สีส้ม", "Orange", "Siisom", R.drawable.c_3_5, 0, 0))
            add(LessonCard("สีเหลือง", "Yellow ", "Siilueng", R.drawable.c_3_6, 0, 0))
            add(LessonCard("สีม่วง", "Purple", "Siimuang", R.drawable.c_3_7, 0, 0))
            add(LessonCard("สีน้ำเงิน/ สีฟ้า", "Blue", "Sii-nam-ngueng", R.drawable.c_3_8, 0, 0))
            add(LessonCard("สีชมพู", "Pink", "Siichomphuu", R.drawable.c_3_9, 0, 0))
            add(LessonCard("สีน้ำตาล", "brown", "Siinamtaan", R.drawable.c_3_10, 0, 0))
        }

        return arrayList
    }

    fun getChapter(position: Int): ArrayList<LessonCard> {
        return when (position) {
            1 -> {
                getChapter1()
            }
            2 -> {
                getChapter2()
            }
            3 -> {
                getChapter3()
            }
            4 -> {
                getChapter4()
            }
            else -> {
                ArrayList<LessonCard>()
            }
        }
    }
*/
}
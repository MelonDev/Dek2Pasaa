package th.ac.up.se.takingbear

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth
import com.up.se.tkbcontrol.Data.PeopleInfo

import kotlinx.android.synthetic.main.activity_setting.*
import th.ac.up.agr.thai_mini_chicken.SQLite.LangSQ
import th.ac.up.agr.thai_mini_chicken.Tools.DeviceUtills
import th.ac.up.se.takingbear.Tools.FSTool
import th.ac.up.se.takingbear.Tools.MelonFirebaseProcess
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.ProgressParams
import com.mylhyl.circledialog.params.TextParams
import kotlinx.android.synthetic.main.dialog_add.view.*
import java.io.ByteArrayOutputStream


class SettingActivity : AppCompatActivity() {

    private var weight: Int = 0
    private var height: Int = 0

    lateinit var bundle: Bundle
    var color: Int = 0
    var title: String = ""
    var colorDark: Int = 0


    lateinit var sqlite: LangSQ

    lateinit var uploadDialog: DialogFragment
    lateinit var builder: CircleDialog.Builder
    var filepath: Uri? = null


    lateinit var data: ArrayList<PeopleInfo>
    private lateinit var task: MelonFirebaseProcess

    companion object {
        const val REQUEST_PERMISSION_GALLERY = 56001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        FSTool(window).loadFunction()

        weight = DeviceUtills(this.applicationContext).getScreenWidth()
        height = DeviceUtills(this.applicationContext).getScreenHeight()

        task = MelonFirebaseProcess()

        sqlite = LangSQ(this)

        bundle = intent.extras!!
        color = bundle.getInt("COLOR")
        colorDark = bundle.getInt("DARK")
        title = bundle.getString("TITLE")!!

        setting_back.setOnClickListener {
            finish()
        }

        if (sqlite.read().contentEquals(LangSQ.THAI)) {
            setting_back_text_a.text = "กลับ"
            setting_back_text_b.text = "กลับ"

            setting_a1_text.text = "เปลี่ยนรูปภาพประจำตัว"
            setting_a2_text.text = "เปลี่ยนชื่อผู้ใช้"
            setting_a3_text.text = "ติดต่อผู้ดูแล"
            setting_a4_text.text = "เกี่ยวกับ"
            setting_a5_text.text = "ลงชื่อออก"


        } else {
            setting_back_text_a.text = "Back"
            setting_back_text_b.text = "Back"

            setting_a1_text.text = "Change image profile"
            setting_a2_text.text = "Change name"
            setting_a3_text.text = "Contact us"
            setting_a4_text.text = "About"
            setting_a5_text.text = "Sign out"
        }

        setting_a3_layout.setOnClickListener {
            //https://m.me/LanguageandApplication

            val url = "https://m.me/LanguageandApplication"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        setting_a1_layout.setOnClickListener {
            getPermision(android.Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_GALLERY)
        }

        setting_a2_layout.setOnClickListener {
            showProgress()
            showNameDialog()
        }

        setting_a5_layout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
        }
    }

    fun showNameDialog() {
        val dialog = AlertDialog.Builder(this)


        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add, null)
        dialog.setView(dialogView)
        //dialog.setTitle(Title);
        //dialog.setMessage(Message);
        val editText = dialogView.custom_dialog_edittext
        editText.requestFocus()

        /*
        if (price > 0) {
            editText.setText(price.toString())
        } else {
            editText.setText("0.00")
        }
        */

        val abc = dialog.create()
        //abc.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        abc.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        val u = FirebaseAuth.getInstance().currentUser!!.uid.toString()

        FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("Info").addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Log.e("", "")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val profile = p0.getValue(PeopleInfo::class.java)!!
                    editText.setText(profile.name)

                    stopProgress()
                    abc.show()


                    dialogView.dialog_add_confirm.setOnClickListener {

                        profile.name = editText.text.toString()

                        showProgress()
                        abc.cancel()


                        FirebaseDatabase.getInstance().reference.child("Peoples").child(u).child("Info").setValue(profile).addOnSuccessListener {

                            stopProgress()

                        }

                    }

                } else {
                    editText.setText("")
                }
            }
        })

        editText.inputType = InputType.TYPE_CLASS_TEXT



        dialogView.dialog_add_cancel.setOnClickListener {
            abc.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            //activity.hideKeyB()
            editText.setText("")
            editText.clearFocus()
            abc.cancel()
        }

        dialogView.dialog_add_confirm.setOnClickListener {

            /*
            try {


                val a = editText.text.toString()

                val b = a.toDouble()

                if (b >= 0) {

                    showWaitDialog()

                    val firebase = FirebaseDatabase.getInstance().reference.child("Lessons").child(key).child("Info")

                    val postValues = HashMap<String, Any>()
                    postValues.put("/price", b)
                    if (b > 0) {
                        postValues.put("/type", "PAID")
                    } else {
                        postValues.put("/type", "FREE")
                    }

                    firebase.updateChildren(postValues).addOnSuccessListener {
                        //stopWaitDialog()
                        //showCompleteDialog5("บันทึกเรียบร้อย")

                        //this.price = b

                        lesson_change_price_status_text.text = "${b.toString()} Baht"
                    }
                } else {
                    showErrorDialog()
                }


                abc.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                abc.cancel()
            } catch (e: Exception) {
                showErrorDialog()
                abc.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
                abc.cancel()
            }
            */


        }
    }

    fun showProgress() {
        setting_loading_popup_layout.visibility = View.VISIBLE

        if (sqlite.read().contentEquals(LangSQ.THAI)) {
            setting_loading_text.text = "กำลังโหลด"
        } else {
            setting_loading_text.text = "Loading"
        }

        //Log.e("COLOR",Color.RED.toString())

        //list_loading_text.setTextColor(ContextCompat.getColor(this,color))
    }

    fun goToGallery() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "เลือกรูปจาก"), REQUEST_PERMISSION_GALLERY)
    }

    fun getPermision(permission: String, requestCode: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            } else {
                goToGallery()
            }
        }
    }

    fun bitmapToByteArray(bitmap: Bitmap, per: Int): ByteArray {
        var baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, per, baos)
        return baos.toByteArray()
    }

    fun imageCalculate(bitmap: Bitmap): ByteArray {
        val maxSize = 100

        var data = bitmapToByteArray(bitmap, 100)

        var count = maxSize
        var passed = false


        if ((data.size) / 1000 <= maxSize) {
            passed = true
        }

        while (((data.size) / 1000 > maxSize || !passed)) {
            if ((bitmapToByteArray(bitmap, count / 2).size) / 1000 < 100) {
                data = bitmapToByteArray(bitmap, count / 2)
                passed = true
            } else if ((bitmapToByteArray(bitmap, count / 4).size) / 1000 < 100) {
                data = bitmapToByteArray(bitmap, count / 4)
                passed = true
            } else {
                count /= 4
            }
        }

        return data
    }



    fun showUploadDialog(string: String) {
        builder = CircleDialog.Builder()
        uploadDialog = builder
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setProgressText(string)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(supportFragmentManager)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_GALLERY && resultCode == RESULT_OK) {
            filepath = data!!.data

            upload()

        }

    }

    fun upload() {
        if (filepath != null) {

            val bmp = MediaStore.Images.Media.getBitmap(contentResolver, filepath)


            val data = imageCalculate(bmp)

            Log.e("DATA", data.size.toString())

            showUploadDialog("กำลังอัปโหลด...")
            val ref = FirebaseStorage.getInstance().reference.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
            ref.putBytes(data)
                    .addOnSuccessListener { it ->
                        ref.downloadUrl.addOnSuccessListener {
                            //cover = it.toString()
                            FirebaseDatabase.getInstance().reference.child("Peoples").child(FirebaseAuth.getInstance().currentUser!!.uid.toString()).child("Info").child("image").setValue(it.toString()).addOnSuccessListener {
                                uploadDialog.dismiss()
                                showCompleteDialog5("อัพโหลดเสร็จสิ้น")
                            }
                            //Picasso.get().load(it).into(this@LessonActivity.lesson_layout_cover_image)
                            //userInfo.photoURL = it.toString()
                        }
                    }.addOnFailureListener {
                        uploadDialog.dismiss()
                        showCompleteDialog5("เกิดข้อผิดพลาด")
                    }.addOnCanceledListener {
                        uploadDialog.dismiss()
                        Log.e("UPLOAD", "CANCEL")
                    }.addOnProgressListener {
                        val progress = 100.0 * it.bytesTransferred / it.totalByteCount
                        Log.e("PROGRESS", progress.toString())
                    }
        }
    }

    fun showCompleteDialog5(status: String) {
        CircleDialog.Builder(this
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText(status)
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@SettingActivity, R.color.facebookColor)
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ") {
                    Log.e("", "")
                }
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@SettingActivity, R.color.colorText)
                }.show()


    }

    fun stopProgress() {
        setting_loading_popup_layout.visibility = View.GONE

    }

    override fun onStart() {
        super.onStart()
        FSTool(window).loadFunction()

    }

}

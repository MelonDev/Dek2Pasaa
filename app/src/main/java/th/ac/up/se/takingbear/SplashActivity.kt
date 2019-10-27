package th.ac.up.se.takingbear

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat

import kotlinx.android.synthetic.main.activity_splash.*
import th.ac.up.se.takingbear.Tools.FSTool
import com.facebook.AccessToken
import com.google.android.material.snackbar.Snackbar
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory
//import com.google.android.play.core.install.model.ActivityResult
//import com.google.android.play.core.install.model.AppUpdateType
//import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.callback.ConfigTitle
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.TextParams
import com.mylhyl.circledialog.params.TitleParams
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class SplashActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var delay_time: Long = 0
    private var time: Long = 1000L

    private var finish = false

    private val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val permissions = android.Manifest.permission.RECORD_AUDIO

    companion object {
        const val REQUEST_PERMISSION_CAMERA = 56000
        const val REQUEST_PERMISSION_GALLERY = 56001
        const val REQUEST_PERMISSION_STORAGE = 56001
        const val REQUEST_PERMISSION_AUDIO = 56002


    }

    private var isDialogShow = false

    //val PREFS_FILENAME = "th.ac.up.se.takingbear.prefs"
    //var prefs: SharedPreferences? = null

    //val DATE_PREFS = "DATE_PREF"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FSTool(this.window).loadFunction()


        handler = Handler()

        //prefs = this.getSharedPreferences(PREFS_FILENAME, 0)
        //val date_pref = prefs!!.getString(DATE_PREFS, "")

        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val nowDate = "$year-$month-$day"


        runnable = Runnable {

            finish = true

        }


        /*

        if (date_pref.isNotEmpty()) {
            if (!nowDate.contentEquals(date_pref)) {
                setCounter(year, month, day)
            }else {
                setCounter(year, month, day)
            }
        } else {
            setCounter(year, month, day)

        }*/


        //Log.e("sdfkdfk", date_pref.toString())


    }

    private fun setCounter(year: Int, month: Int, day: Int) {


        /*
        val reqString = (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES::class.java.fields[android.os.Build.VERSION.SDK_INT].name)


         */
        val firebase = FirebaseDatabase.getInstance().reference.child("Counter").child((year).toString()).child((month+1).toString()).child(day.toString()).child("count")

        //val nowDate = "$year-$month-$day"


        firebase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("", "")
                letStart()

            }

            override fun onDataChange(p0: DataSnapshot) {

                //Log.e("sads",p0.toString())

                if (p0.value != null) {

                    val info = p0.getValue(Int::class.java)!!
                    val infos = info + 1


                    firebase.setValue(infos).addOnSuccessListener {
                        letStart()
                        //letPreStart()
                    }.addOnFailureListener {
                        letStart()
                        //letPreStart()

                    }.addOnCanceledListener {
                        letStart()
                        //letPreStart()

                    }


                } else {

                    val info = 1


                    firebase.setValue(info).addOnSuccessListener {
                        letStart()
                        //letPreStart()

                    }.addOnFailureListener {
                        letStart()
                        //letPreStart()

                    }.addOnCanceledListener {
                        letStart()
                        //letPreStart()

                    }


                }
            }
        })


    }

    /*
    private fun letPreStart() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                            && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                    ) {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.FLEXIBLE,
                                this,
                                9999)
                    }
                }
            }
        }

    }
*/


    class DeviceInfo {
        var MANUFACTURER = ""
        var MODEL = ""
        var VERSION = ""
        var CODENAME = ""
        var COUNT = 0
    }

    override fun onStop() {
        super.onStop()
        if (finish) {
            finish()
        }


    }

    private fun letStart() {
        if (!isDialogShow) {
            isDialogShow = true
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //getPermision(android.Manifest.permission.CAMERA, REQUEST_PERMISSION_CAMERA)
                setQuestionDialog(0, "คำอธิบาย", "คุณจำเป็นต้องทำการขอสิทธิ์การใช้งานคลังภาพของคุณ โดยให้คุณกด \"ขอสิทธิ์\" แล้วกด \"ยอมรับ\" ตามลำดับ", REQUEST_PERMISSION_GALLERY, "ขอสิทธิ์", "ยกเลิก")


            } else {
                if (FirebaseAuth.getInstance().currentUser == null) {

                    val intent = Intent(this, GoogleSignInActivity::class.java)
                    //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

                    startActivity(intent)

                    finish()

                } else {

                    val intent = Intent(this, MainActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, splash_logo, "logo")

                    startActivity(intent, options.toBundle())

                    finish()

                }
            }
        }

    }


    fun getPermision(permission: String, requestCode: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission, permissions), requestCode)
            } else {
                if (FirebaseAuth.getInstance().currentUser == null) {

                    val intent = Intent(this, GoogleSignInActivity::class.java)
                    //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

                    startActivity(intent)

                    finish()

                } else {

                    val intent = Intent(this, MainActivity::class.java)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, splash_logo, "logo")

                    startActivity(intent, options.toBundle())

                    finish()

                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (requestCode) {
                REQUEST_PERMISSION_GALLERY -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (FirebaseAuth.getInstance().currentUser == null) {

                            val intent = Intent(this, GoogleSignInActivity::class.java)
                            //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

                            startActivity(intent)

                            finish()

                        } else {

                            val intent = Intent(this, MainActivity::class.java)
                            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, splash_logo, "logo")

                            startActivity(intent, options.toBundle())

                            finish()

                        }
                    } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        val showRationale = shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)
                        if (!showRationale) {
                            setQuestionDialog(1, "คำอธิบาย", "เนื่องจากคุณได้ทำการกด \"ไม่ต้องแสดงอีก\" ในหน้าขอสิทธิ์ หากคุณต้่องการจะขอสิทธิ์อีกครั้ง ให้กด \"ไปที่ตั้งค่า\" แล้วกดเลือก \"สิทธิ์ของแอป\" จากนั้นกดเปิดสิทธิ์ที่ต้องการ ", REQUEST_PERMISSION_GALLERY, "ไปที่ตั้งค่า", "ยกเลิก")
                        }
                    } else {
                        setErrorDialog("เกิดข้อผิดพลาด")
                    }

                }
                REQUEST_PERMISSION_AUDIO -> {
                    if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (FirebaseAuth.getInstance().currentUser == null) {

                            val intent = Intent(this, GoogleSignInActivity::class.java)
                            //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

                            startActivity(intent)

                            finish()

                        } else {

                            val intent = Intent(this, MainActivity::class.java)
                            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, splash_logo, "logo")

                            startActivity(intent, options.toBundle())

                            finish()

                        }
                    } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        val showRationale = shouldShowRequestPermissionRationale(android.Manifest.permission.RECORD_AUDIO)
                        if (!showRationale) {
                            setQuestionDialog(1, "คำอธิบาย", "เนื่องจากคุณได้ทำการกด \"ไม่ต้องแสดงอีก\" ในหน้าขอสิทธิ์ หากคุณต้่องการจะขอสิทธิ์อีกครั้ง ให้กด \"ไปที่ตั้งค่า\" แล้วกดเลือก \"สิทธิ์ของแอป\" จากนั้นกดเปิดสิทธิ์ที่ต้องการ ", REQUEST_PERMISSION_AUDIO, "ไปที่ตั้งค่า", "ยกเลิก")
                        }
                    } else {
                        setErrorDialog("เกิดข้อผิดพลาด")
                    }

                }
            }
        }
    }

    fun setErrorDialog(string: String) {
        CircleDialog.Builder(this
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText(string)
                .configText { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@SplashActivity, R.color.color_game_blue)
                    params.padding = intArrayOf(0, 0, 0, 0) //(Bottom,TOP,Right,Left)
                    params.height = 250
                }
                .setPositive("รับทราบ", {
                })
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@SplashActivity, R.color.colorText)
                }.show()


    }

    fun setQuestionDialog(ID: Int, title: String, sub: String, requestCode: Int, positive: String, negative: String) {
        CircleDialog.Builder(this
        )
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setText(sub)
                .configText { params ->
                    params!!.textSize = 50
                    params.textColor = ContextCompat.getColor(this@SplashActivity, R.color.colorText)
                    params.padding = intArrayOf(50, 10, 50, 70) //(Left,TOP,Right,Bottom)
                }
                .setTitle(title)
                .configTitle { params ->
                    params!!.textSize = 60
                    params.textColor = ContextCompat.getColor(this@SplashActivity, R.color.color_game_blue)
                }
                .setPositive(positive) {
                    if (ID == 0) {
                        when (requestCode) {
                            REQUEST_PERMISSION_AUDIO -> {
                                getPermision(android.Manifest.permission.RECORD_AUDIO, REQUEST_PERMISSION_AUDIO)
                            }
                            REQUEST_PERMISSION_GALLERY -> {
                                getPermision(android.Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_GALLERY)
                            }
                        }
                    } else if (ID == 1) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        intent.addCategory(Intent.CATEGORY_DEFAULT)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        val uri = Uri.fromParts("package", packageName, null)

                        intent.data = uri
                        startActivity(intent)
                    }

                }
                .configPositive { params ->
                    params.textSize = 50
                    params.textColor = ContextCompat.getColor(this@SplashActivity, R.color.color_game_blue)
                }
                .setNegative(negative, {
                    if (FirebaseAuth.getInstance().currentUser == null) {

                        val intent = Intent(this, GoogleSignInActivity::class.java)
                        //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

                        startActivity(intent)

                        finish()

                    } else {

                        val intent = Intent(this, MainActivity::class.java)
                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, splash_logo, "logo")

                        startActivity(intent, options.toBundle())

                        finish()

                    }
                })
                .configNegative { params ->
                    params.textSize = 50

                    params.textColor = ContextCompat.getColor(this@SplashActivity, R.color.colorText)
                }
                .show()


    }


    override fun onResume() {
        super.onResume()

        FSTool(this.window).loadFunction()
        delay_time = time

        checkCount()

        if (runnable != null) {
            handler.postDelayed(runnable, delay_time)
            time = System.currentTimeMillis()
        }

    }

    private fun checkCount() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        if (year > 2500) {
            val years = year - 543
            setCounter(years, month, day)

        } else {
            setCounter(year, month, day)

        }

        //setCounter(year, month, day)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }


}

package th.ac.up.se.takingbear

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mylhyl.circledialog.CircleDialog
import com.mylhyl.circledialog.callback.ConfigButton
import com.mylhyl.circledialog.callback.ConfigDialog
import com.mylhyl.circledialog.callback.ConfigText
import com.mylhyl.circledialog.params.ButtonParams
import com.mylhyl.circledialog.params.DialogParams
import com.mylhyl.circledialog.params.ProgressParams
import com.mylhyl.circledialog.params.TextParams
import com.up.se.tkbcontrol.Data.PeopleInfo

import kotlinx.android.synthetic.main.activity_google_sign_in.*
import kotlinx.android.synthetic.main.activity_splash.*
import th.ac.up.se.takingbear.Tools.FSTool
import java.util.ArrayList

class GoogleSignInActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 56000
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    lateinit var waitDialog: DialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_sign_in)

        FSTool(window).loadFunction()


        val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        //val client = GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi()
        googleSignInClient = GoogleSignIn.getClient(this, option)
        firebaseAuth = FirebaseAuth.getInstance()


        google_login_btn.setOnClickListener {
            //Toast.makeText(this, "PROCESS", Toast.LENGTH_SHORT).show()
            signIn()
        }

    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        setWaitDialog("กำลังดำเนินการ...")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Log.e("TEST", "TEST")
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                //Toast.makeText(this, "เกิดข้อผิดพลาด", Toast.LENGTH_SHORT).show()

                Toast.makeText(this,"${e.statusCode}: ${e.message}",Toast.LENGTH_LONG).show()

                FirebaseDatabase.getInstance().reference.child("Log").push().child("Message").setValue(e.toString())

                waitDialog.dismiss()
                //setErrorDialog("คำขอถูกยกเลิก")
                //Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
                //updateUI(null)
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val fa = FirebaseAuth.getInstance().currentUser!!

                        FirebaseDatabase.getInstance().reference.child("Peoples").child(fa.uid).child("Info").addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("","")
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if(p0.value != null){
                                    waitDialog.dismiss()
                                    val intent = Intent(this@GoogleSignInActivity, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }else {
                                    val user = PeopleInfo()
                                    user.apply {
                                        this.key = fa.uid
                                        this.name = fa.displayName.toString()
                                        this.image = fa.photoUrl.toString()
                                    }

                                    FirebaseDatabase.getInstance().reference.child("Peoples").child(fa.uid).child("Info").setValue(user).addOnSuccessListener {
                                        waitDialog.dismiss()
                                        val intent = Intent(this@GoogleSignInActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                        })





                        //startProcess()

                        //Log.e("PROVIDER",firebaseAuth.currentUser!!.providerId)
                        //Log.e("PROVIDER",firebaseAuth.currentUser!!.uid)

                        //val user = firebaseAuth.getCurrentUser()
                        //updateUI(user)
                    } else {
                        waitDialog.dismiss()
                        Toast.makeText(this, "เกิดข้อผิดพลาด", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Firebase Error", Toast.LENGTH_SHORT).show()

                        //updateUI(null)
                    }
                }
    }

    fun setWaitDialog(string: String) {
        waitDialog = CircleDialog.Builder()
                .configDialog { params -> params.canceledOnTouchOutside = false }
                .setProgressText(string)
                .setProgressStyle(ProgressParams.STYLE_SPINNER)
                .show(supportFragmentManager)
    }


    override fun onStart() {
        super.onStart()

        FSTool(window).loadFunction()


    }

}

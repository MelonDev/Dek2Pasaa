package th.ac.up.se.takingbear

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_facebook_login.*
import th.ac.up.se.takingbear.Tools.FSTool
import android.widget.Toast
import com.facebook.login.LoginResult
import com.facebook.login.LoginManager
import java.util.*
import android.content.Intent
import com.facebook.*
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.up.se.tkbcontrol.Data.PeopleInfo


class FacebookLoginActivity : AppCompatActivity() {

    private lateinit var callback: CallbackManager
    private lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth

    private val EMAIL = "email"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facebook_login)


        facebook_login_btn.setOnClickListener {
            facebookLogin()
        }


    }

    override fun onStart() {
        super.onStart()

        FSTool(window).loadFunction()


    }

    fun facebookLogin(){

        FacebookSdk.sdkInitialize(applicationContext)
        auth = FirebaseAuth.getInstance()


        val accessToken = AccessToken.getCurrentAccessToken()
        //val isLoggedIn = accessToken != null && !accessToken.isExpired
        val isLoggedIn = auth.currentUser

        //if (!isLoggedIn) {
        if (isLoggedIn == null) {


            callback = CallbackManager.Factory.create()


            facebook_login_btn.setOnClickListener {

                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"))

                LoginManager.getInstance().registerCallback(callback,
                        object : FacebookCallback<LoginResult> {
                            override fun onSuccess(loginResult: LoginResult) {
                                Log.e("Success", "Login")
                                handleFacebookAccessToken(loginResult.accessToken)
                            }

                            override fun onCancel() {
                                Toast.makeText(this@FacebookLoginActivity, "Login Cancel", Toast.LENGTH_LONG).show()
                            }

                            override fun onError(exception: FacebookException) {
                                Log.e("ERROR", exception.message)

                                Toast.makeText(this@FacebookLoginActivity, exception.message, Toast.LENGTH_LONG).show()
                            }
                        })


            }


        }else {
            val intent = Intent(this@FacebookLoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data)

        callback.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        Log.d("FACEBOOK", "handleFacebookAccessToken:$token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d("FACEBOOK", "signInWithCredential:success")

                        val fa = auth.currentUser!!

                        FirebaseDatabase.getInstance().reference.child("Peoples").child(fa.uid).child("Info").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                Log.e("","")
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if(p0.value != null){
                                    //waitDialog.dismiss()
                                    val intent = Intent(this@FacebookLoginActivity, MainActivity::class.java)
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
                                        //waitDialog.dismiss()
                                        val intent = Intent(this@FacebookLoginActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                        })

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("FACEBOOK", "signInWithCredential:failure", task.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }

                }
    }



}

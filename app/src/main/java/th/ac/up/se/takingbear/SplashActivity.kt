package th.ac.up.se.takingbear

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat

import kotlinx.android.synthetic.main.activity_splash.*
import th.ac.up.se.takingbear.Tools.FSTool
import com.facebook.AccessToken
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var delay_time: Long = 0
    private var time: Long = 1000L

    private var finish = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        FSTool(this.window).loadFunction()

        handler = Handler()

        runnable = Runnable {

/*
            val accessToken = AccessToken.getCurrentAccessToken()
            //Log.e("AC",accessToken.toString())
            val isLoggedIn = accessToken != null && !accessToken.isExpired

            if(isLoggedIn){
                val intent = Intent(this, MainActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

                startActivity(intent, options.toBundle())
            } else {
                val intent = Intent(this, FacebookLoginActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

                startActivity(intent, options.toBundle())
            }
*/

            if(FirebaseAuth.getInstance().currentUser == null){

                val intent = Intent(this, GoogleSignInActivity::class.java)
                //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

                startActivity(intent)

                finish()

            }else {

                val intent = Intent(this, MainActivity::class.java)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

                startActivity(intent, options.toBundle())

                finish()

            }



            finish = true

        }

    }

    override fun onStop() {
        super.onStop()
        if(finish){
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        FSTool(this.window).loadFunction()
        delay_time = time
        handler.postDelayed(runnable, delay_time)
        time = System.currentTimeMillis()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable);
        time = delay_time - (System.currentTimeMillis() - time);
    }

}

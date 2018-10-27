package th.ac.up.se.takingbear

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_splash.*
import th.ac.up.se.takingbear.Tools.FSTool

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

            val intent = Intent(this, MainActivity::class.java)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,splash_logo , "logo")

            startActivity(intent, options.toBundle())
            //finish()

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

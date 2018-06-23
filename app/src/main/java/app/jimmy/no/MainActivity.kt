package app.jimmy.no

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = this@MainActivity.javaClass.simpleName
    private val mAuth = FirebaseAuth.getInstance()
    private var level = 0
    private var divideBy = 1f
    private var coefficient = 100 / divideBy
    private var total = 0f
    lateinit var animation:ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        add.setOnClickListener(this)
        logout.setOnClickListener(this)
        FirebaseInstanceId.getInstance().token
        Log.d(TAG,"Token: "+FirebaseInstanceId.getInstance().token)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.add -> {
                total += coefficient
                setUpAnimation()
            }
            R.id.logout -> {
                mAuth.signOut()
                val intent=Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun setUpAnimation() {
        animation = ObjectAnimator.ofInt(progressBar, "progress", Math.round(total)) // see this max value coming back here, we animate towards that value
        animation.duration = 500 // in milliseconds
        animation.interpolator = DecelerateInterpolator()
        progress_text.text = (Math.round(total)).toString()
        animation.start()
        animation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if(progressBar.progress>=100) {
                    level++
                    divideBy *= 2
                    coefficient = 100 / divideBy
                    total=0f
                    progressBar.setProgress(0)
                    progress_text.text = "0"
                    level_text.text = level.toString()
                }
                animation?.removeAllListeners()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                progress_text.text = (Math.round(total)).toString()

            }
        })
    }
}

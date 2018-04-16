package app.jimmy.no

import android.animation.Animator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.DecelerateInterpolator
import android.animation.ObjectAnimator



class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val level = 0
    private var divideBy = 1f
    private var coefficient = 100 / divideBy
    private var total = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        add.setOnClickListener(this)
//        progressText = findViewById(R.id.progress_text)
//        levelChange = findViewById(R.id.level_change_text)
////        plus1 = findViewById(R.id.add)
//        animationView = findViewById(R.id.animation_view)
//        plus1!!.setOnClickListener(this)
//        circularProgressBar!!.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when (v.id) {
            R.id.add -> {
                val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100) // see this max value coming back here, we animate towards that value
                animation.duration = 3000 // in milliseconds
                animation.interpolator = DecelerateInterpolator()
                if (total == 0f) {
                    progressBar.progress = 0
                }
                total += coefficient
                if (total >= 100.0f) {
                    animation.start()
//                    progressText!!.text = "100 %"
//                    circularProgressBar!!.setProgressWithAnimation(100f)
//                    animationView!!.visibility = View.VISIBLE
//                    animationView!!.playAnimation()
//                    plus1!!.visibility = View.GONE
//                    levelChange!!.visibility = View.VISIBLE
//                    animationView!!.addAnimatorListener(object : Animator.AnimatorListener {
//                        override fun onAnimationStart(animation: Animator) {
//
//                        }
//
//                        override fun onAnimationEnd(animation: Animator) {
//                            animationView!!.visibility = View.GONE
//                            plus1!!.visibility = View.VISIBLE
//                            levelChange!!.visibility = View.GONE
//                        }
//
//                        override fun onAnimationCancel(animation: Animator) {
//                            animationView!!.visibility = View.GONE
//                            plus1!!.visibility = View.VISIBLE
//                            levelChange!!.visibility = View.GONE
//                        }
//
//                        override fun onAnimationRepeat(animation: Animator) {
//
//                        }
//                    })
                    total = 0f
                    divideBy *= 2
                    coefficient = 100 / divideBy
                } else {
//                    progressText!!.text = total.toString() + " %"
//                    circularProgressBar!!.setProgressWithAnimation(total)

                }
            }
        }
    }
}

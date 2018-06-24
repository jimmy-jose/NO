package app.jimmy.no

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_progress.*

class ProgressFragment: Fragment(),View.OnClickListener {
    private val TAG = this@ProgressFragment.javaClass.simpleName
    private var level = 0
    private var divideBy = 1f
    private var coefficient = 100 / divideBy
    private var total = 0f
    private lateinit var animation: ObjectAnimator
    private lateinit var mContext:Context

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mContext = context!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_progress,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        add.setOnClickListener(this)
        logout.setOnClickListener(this)
        FirebaseInstanceId.getInstance().token
        Log.d(TAG,"Token: "+ FirebaseInstanceId.getInstance().token)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.add -> {
                total += coefficient
                setUpAnimation()
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
                actionImage.visibility = View.VISIBLE
                if(total>=100){

                    showActionImage(true)
                }
                progress_text.text = (Math.round(total)).toString()

            }
        })
    }

    /**
     * shows the image on level up or down
     * @param positive true if level up f
     */
    fun showActionImage(positive: Boolean){
        TransitionManager.beginDelayedTransition(root)
        if (positive){
            actionImage.setImageDrawable(ContextCompat.getDrawable(mContext.applicationContext,R.drawable.badge))
        }else{
            actionImage.setImageDrawable(ContextCompat.getDrawable(mContext.applicationContext,R.drawable.leveldown))
        }

    }
}
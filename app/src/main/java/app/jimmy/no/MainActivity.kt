package app.jimmy.no

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import android.view.animation.DecelerateInterpolator
import android.animation.ObjectAnimator
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = this@MainActivity.javaClass.simpleName
    private val mAuth = FirebaseAuth.getInstance()
    private val level = 0
    private var divideBy = 1f
    private var coefficient = 100 / divideBy
    private var total = 100f

    val db : DocumentReference = FirebaseFirestore.getInstance().collection("UserStats").document("gob8G17dqF6wsFvEqat5")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        add.setOnClickListener(this)
        logout.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        FirebaseAuth.getInstance().signOut()
        when (v.id) {
            R.id.add -> {
                val animation = ObjectAnimator.ofInt(progressBar, "progress", 0, Math.round(total)) // see this max value coming back here, we animate towards that value
                animation.duration = 3000 // in milliseconds
                animation.interpolator = DecelerateInterpolator()
                if (total == 0f) {
                    progressBar.progress = 0
                }
                total += coefficient
                if (total >= 100.0f) {
                    animation.start()
                    total = 0f
                    divideBy *= 2
                    coefficient = 100 / divideBy
                }else{
                    animation.start()
                }
                progress_text.text = total.toString()
                val dataToSave = HashMap<String,Float>()
                dataToSave.put("totalVal",total)
                db.set(dataToSave as Map<String, Any>).addOnSuccessListener { Log.d(TAG,"updated fb firestore") }.addOnFailureListener { Log.d(TAG,"Failed fb firestore update") }

            }
            R.id.logout -> {
                mAuth.signOut()
                val intent=Intent(this@MainActivity,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}

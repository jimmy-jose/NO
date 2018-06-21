package app.jimmy.no

import android.content.Intent
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit


class LoginActivity: AppCompatActivity(),View.OnClickListener {

    private val db :FirebaseFirestore= FirebaseFirestore.getInstance()
    private val TAG = this@LoginActivity.javaClass.simpleName
    private val mAuth = FirebaseAuth.getInstance()
    private var mVerificationId =""
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            Log.d(TAG,"onVerificationCompleted"+p0)
            signInWithCredentials(p0)
            Log.d(TAG,mAuth.currentUser.toString())
            mAuth.addAuthStateListener {
                if(mAuth.currentUser!=null) {
                    checkUser(it.currentUser!!.uid)
                    progressBar.visibility=View.GONE
                }
            }
        }

        override fun onVerificationFailed(p0: FirebaseException?) {
            Log.d(TAG,"onVerificationFailed"+p0)
            progressBar.visibility=View.GONE
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken?) {
            Log.d(TAG,"onCodeSent")
            mVerificationId = p0
            moveToOtp()
        }

        override fun onCodeAutoRetrievalTimeOut(p0: String?) {
            moveToOtp()
            Log.d(TAG,"onCodeAutoRetrievalTimeOut")
            Log.d(TAG,"onCodeSent")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sentOtp.setOnClickListener(this)
        verify.setOnClickListener(this)
        back.setOnClickListener(this)
        resent.setOnClickListener(this)
    }

    fun signInWithCredentials(credential: PhoneAuthCredential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
            override fun onComplete(p0: Task<AuthResult>) {
                progressBar.visibility = View.GONE
                if (p0.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = p0.getResult().getUser()
                    Log.d(TAG, "user is "+user)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.e(TAG, "signInWithCredential:failure", p0.getException())
                    if (p0.getException() is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
        })
    }

    override fun onBackPressed() {
        if(otp_card_view.visibility==View.VISIBLE){
            backToPhoneNumber()
        }else{
            super.onBackPressed()
        }
    }
    override fun onClick(v: View) {
        when(v.id){
            R.id.sentOtp->{
                sentMessage()
            }
            R.id.verify-> {
                val otp = code.editText?.text.toString()
                progressBar.visibility = View.GONE
                if (!mVerificationId.isEmpty() && !otp.isEmpty()){
                    Log.d(TAG, "verification id: " + mVerificationId)
                    val credential = PhoneAuthProvider.getCredential(mVerificationId, otp)
                    signInWithCredentials(credential)
                    mAuth.addAuthStateListener {
                        if(mAuth.currentUser!=null) {
                            checkUser(it.currentUser!!.uid)
                        }
                    }
                }else{
                    Toast.makeText(this,"please enter valid otp",Toast.LENGTH_SHORT).show()
                }
            }
            R.id.back->{
                backToPhoneNumber()
            }
            R.id.resent->{
                sentMessage()
            }
        }
    }

    private fun moveToOtp(){
        progressBar.visibility=View.GONE
        TransitionManager.beginDelayedTransition(root)
        phone_card_view.visibility = View.GONE
        otp_card_view.visibility = View.VISIBLE
    }

    private fun sentMessage() {
        val phone="+91"+ phone_num.editText?.text
        if(phone.length!=13){
            Log.e(TAG,"Wrong phone number!")
            Toast.makeText(this,"Please enter a valid phone number",Toast.LENGTH_LONG).show()
        }else {
            progressBar.visibility=View.VISIBLE
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone,                                  // Phone number to verify
                    60,                                     // Timeout duration
                    TimeUnit.SECONDS,                       // Unit of timeout
                    this,                                   // Activity (for callback binding)
                    callbacks)
        }
    }

    private fun backToPhoneNumber() {
        TransitionManager.beginDelayedTransition(root)
        phone_card_view.visibility = View.VISIBLE
        otp_card_view.visibility = View.GONE
    }

    fun checkUser(uId : String){
        val user = db.collection(Constants.Keys.USERS)
        user.document(uId).get().addOnSuccessListener {
            if(it.exists()) {
                Toast.makeText(this, getString(R.string.welcome_back), Toast.LENGTH_LONG).show()
            }else{
                val data = HashMap<String,Any>()
                data.put(Constants.Keys.PHONE_NUMBER,phone_num.editText?.text.toString())
                data.put(Constants.Keys.NAME,"Test")
                val newUserRef = user.document(uId)
                newUserRef.set(data).addOnSuccessListener {
                    Toast.makeText(this, getString(R.string.welcome), Toast.LENGTH_LONG).show()
                    Log.d(TAG,"FireStore updated")
                }
            }
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
            progressBar.visibility=View.GONE
        }
    }
}
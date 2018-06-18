package app.jimmy.no

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
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
            val intent = Intent(this@LoginActivity,MainActivity::class.java)
            signInWithCredentials(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException?) {
            Log.d(TAG,"onVerificationFailed"+p0)

        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken?) {
            Log.d(TAG,"onCodeSent")
            mVerificationId = p0

        }

        override fun onCodeAutoRetrievalTimeOut(p0: String?) {
            Log.d(TAG,"onCodeAutoRetrievalTimeOut")
            Log.d(TAG,"onCodeSent")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sentOtp.setOnClickListener(this)
        verify.setOnClickListener(this)
    }

    fun signInWithCredentials(credential: PhoneAuthCredential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, object : OnCompleteListener<AuthResult>{
            override fun onComplete(p0: Task<AuthResult>) {
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
    override fun onClick(v: View) {
        when(v.id){
            R.id.sentOtp->{
                PhoneAuthProvider.getInstance().
                        verifyPhoneNumber(
                        phone.text.toString(),       // Phone number to verify
                        0,                                     // Timeout duration
                        TimeUnit.SECONDS,                       // Unit of timeout
                        this,                                   // Activity (for callback binding)
                        callbacks)
            }
            R.id.verify->{
                val credential = PhoneAuthProvider.getCredential(mVerificationId,otp.text.toString())
                val user = db.collection("Users")
                if(user.document("surld81PadBAuQcSLMxi").get().result.exists()){
                    Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show()
                }
                signInWithCredentials(credential)
            }
        }
    }
}
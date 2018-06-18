package app.jimmy.no

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent:Intent
        if(FirebaseAuth.getInstance().currentUser!=null){
            intent = Intent(this, MainActivity::class.java)
        }else{
            intent = Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
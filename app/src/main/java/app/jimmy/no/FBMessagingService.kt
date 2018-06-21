package app.jimmy.no

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * @author Jimmy
 * Created on 21/6/18.
 */
class FBMessagingService : FirebaseMessagingService() {
    private val TAG = this@FBMessagingService.javaClass.simpleName
    override fun onMessageReceived(p0: RemoteMessage?) {
        Log.d(TAG, p0?.data.toString())

        super.onMessageReceived(p0)
    }
}
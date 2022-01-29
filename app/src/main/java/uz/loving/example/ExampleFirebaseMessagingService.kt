package uz.loving.example

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class ExampleFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("TAG", "onNewToken: $token")
    }
}
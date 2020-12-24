package pratilipi.demo.utils.callreceiver

import android.content.Context
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.work.*
import pratilipi.demo.database.CustomerListEntity
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class MyPhoneStateListener : PhoneStateListener() {
    private val tag: String? = "Sync"

    fun onCallStateChanged(
        context: Context?,
        state: Int,
        phoneNumber: String
    ) {
        if (lastState == state) { //No change, debounce extras
            return
        }
        //println("Number inside onCallStateChange : $phoneNumber")
        when (state) {
            TelephonyManager.CALL_STATE_RINGING -> {
                isIncoming = true
                callStartTime = Date()
                println("Incoming Call Ringing $phoneNumber")
                Toast.makeText(context, "Incoming Call Ringing $phoneNumber", Toast.LENGTH_SHORT)
                    .show()
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                isIncoming = false
                callStartTime = Date()
                println("Outgoing Call Started $phoneNumber")
                Toast.makeText(context, "Outgoing Call Started $phoneNumber", Toast.LENGTH_SHORT)
                    .show()
            }
            TelephonyManager.CALL_STATE_IDLE ->  //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) { //Ring but no pickup-  a miss
                    println("Ringing but no pickup" + phoneNumber + " Call time " + callStartTime + " Date " + Date())
//                    Toast.makeText(context, "Ringing but no pickup" + phoneNumber + " Call time " + callStartTime + " Date " + Date(), Toast.LENGTH_SHORT).show()
                } else if (isIncoming) {
                }
        }
        lastState = state
    }

    companion object {
        private var lastState = TelephonyManager.CALL_STATE_IDLE
        private var callStartTime: Date? = null
        private var isIncoming = false
    }


}
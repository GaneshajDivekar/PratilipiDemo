package pratilipi.demo.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.widget.Toast
import dagger.hilt.EntryPoints
import dagger.hilt.android.AndroidEntryPoint
import pratilipi.demo.database.CustomerListEntity
import pratilipi.demo.db.PratilipiDao
import pratilipi.demo.utils.MyPhoneStateListener
import javax.inject.Inject

@AndroidEntryPoint
public class CallReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val customPhoneListener = MyPhoneStateListener()
        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE)
        val bundle = intent.extras
        val phone_number = bundle!!.getString("incoming_number")
        val stateStr = intent.extras!!.getString(TelephonyManager.EXTRA_STATE)
        // String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        var state = 0
        if (stateStr == TelephonyManager.EXTRA_STATE_IDLE) {
            state = TelephonyManager.CALL_STATE_IDLE
        } else if (stateStr == TelephonyManager.EXTRA_STATE_OFFHOOK) {
            state = TelephonyManager.CALL_STATE_OFFHOOK
        } else if (stateStr == TelephonyManager.EXTRA_STATE_RINGING) {
            state = TelephonyManager.CALL_STATE_RINGING
        }
        if (phone_number == null || "" == phone_number) {
            return
        }

        customPhoneListener.onCallStateChanged(context, state, phone_number)
        Toast.makeText(context, "Phone Number $phone_number", Toast.LENGTH_SHORT).show()
    }
}
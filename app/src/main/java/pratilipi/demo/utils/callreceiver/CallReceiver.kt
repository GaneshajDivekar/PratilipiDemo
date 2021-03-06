package pratilipi.demo.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint
import pratilipi.demo.db.PratilipiDao
import pratilipi.demo.utils.callreceiver.HiltBroadcastReceiver
import pratilipi.demo.utils.callreceiver.MyPhoneStateListener
import javax.inject.Inject


@AndroidEntryPoint
public class CallReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var pratilipiDao: PratilipiDao

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
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


        var customerListEntity = pratilipiDao.getContacts(phone_number)
        if (customerListEntity.customer_mobile != null && customerListEntity.call_status.equals("1")) {
            try {
                val telecomManager =
                    context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                telecomManager.endCall()

                Toast.makeText(
                    context,
                    "Blocked Phone Number thats why call Rejected $phone_number",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("unable", "msg cant dissconect call....")
            }
        } else {

        }


        customPhoneListener.onCallStateChanged(context, state, phone_number)
        Toast.makeText(context, "Phone Number $phone_number", Toast.LENGTH_SHORT).show()
    }

}
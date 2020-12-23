package pratilipi.demo.base

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import pratilipi.demo.R
import java.util.HashMap

/**
 * Abstract Activity which binds [ViewModel] [VM] and [ViewBinding] [VB]
 */
abstract class BaseActivity<VM : ViewModel, VB : ViewBinding> : AppCompatActivity() {

    protected abstract val mViewModel: VM
    private var permissionArraySize: Int = 0
    private val GOOGLE_CAMERA_PERMISSION = 1
    protected lateinit var mViewBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewBinding = getViewBinding()
    }

    /**
     * It returns [VB] which is assigned to [mViewBinding] and used in [onCreate]
     */
    abstract fun getViewBinding(): VB
    fun launchPermission() {

        permissionArraySize = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE

        ).size
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_CONTACTS,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG,
                        Manifest.permission.READ_PHONE_STATE
                    ), GOOGLE_CAMERA_PERMISSION
                )
            }
        }
    }

    open fun showPermissioDialog(
        title: String?,
        msg: String?,
        positiveLable: String?,
        positiveOnclick: DialogInterface.OnClickListener?
    ): AlertDialog? {
        val builder =
            AlertDialog.Builder(this@BaseActivity)
        builder.setTitle(title)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setCancelable(false)
        builder.setMessage(msg)
        builder.setPositiveButton(positiveLable, positiveOnclick)
        val alert = builder.create()
        alert.show()
        return alert
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            GOOGLE_CAMERA_PERMISSION -> {
                if (grantResults.size > 0 && grantResults.size == permissionArraySize) {
                    val permissionResults =
                        HashMap<String, Int>()
                    var deniedCount = 0
                    var deniedPermission = false
                    var deniedPermission1 = false
                    for (i in 0..grantResults.size - 1) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            permissionResults[permissions[i]] = grantResults[i]
                            deniedCount++
                        }
                    }

                    if (deniedCount == 0) {
                    } else {
                        for ((perName, perResult) in permissionResults) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(
                                    this@BaseActivity,
                                    perName
                                )
                            ) {
                                deniedPermission = true
                                break
                            } else {
                                deniedPermission1 = true
                                break
                            }
                        }
                        if (deniedPermission == true) {
                            showPermissioDialog("Pratiliipi permissions.",
                                "All the permissions are required to do the functions of SmartMart as intended, if the permissions are declined, it will not work properly",
                                "Allow Permissions",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                    dialog.cancel()
                                    launchPermission()
                                })
                        } else if (deniedPermission1 == true) {
                            showPermissioDialog("Pratiliipi permissions.",
                                "All the permissions are required to do the functions of SmartMart as intended, if the permissions are declined, it will not work properly." +
                                        "Allow all the permissions at [Setting] > [Permissions]",
                                "Go to Settings",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                    dialog.cancel()
                                    val intent = Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts(
                                            "package",
                                            packageName,
                                            null
                                        )
                                    )
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                })
                        }
                    }


                } else {
                    Toast.makeText(this, "Please grant permission", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


}

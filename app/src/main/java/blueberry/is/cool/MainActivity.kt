    package blueberry.`is`.cool

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    val REQUEST_ID_MULTIPLE_PERMISSIONS = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val PERMISSIONS: MutableList<String> = ArrayList()
        PERMISSIONS.add(Manifest.permission.INTERNET)
        checkAndRequestPermissions(PERMISSIONS);

    }

    fun goToDiscord(view: View)
    {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/SVT57Zb"))
        startActivity(browserIntent)
    }


    private fun checkAndRequestPermissions(PERMISSIONS: MutableList<String>): Boolean {
        val listPermissionsNeeded: MutableList<String> = ArrayList()
        PERMISSIONS.forEach {
           val Permissiongranted: Int = ContextCompat.checkSelfPermission(this, it)
            if(Permissiongranted != PackageManager.PERMISSION_GRANTED)
                listPermissionsNeeded += it;
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
            return false
        }
        return true
    }
}

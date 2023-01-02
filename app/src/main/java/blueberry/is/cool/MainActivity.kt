    package blueberry.`is`.cool

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


    class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val PERMISSIONS: Array<String> = when(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            true ->arrayOf<String>(Manifest.permission.INTERNET,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
            false ->arrayOf<String>(Manifest.permission.INTERNET,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if(checkPermissions(PERMISSIONS)) {
            val box: LinearLayout = findViewById(R.id.Permbox)
            box.visibility = View.GONE;
        }
        val sharedPref = this.getSharedPreferences("MyPref",0);
        val Lat : EditText  = findViewById(R.id.Lattitude);
        val Lon : EditText  = findViewById(R.id.longitude);
        val But : SwitchCompat = findViewById(R.id.ToggleManual);
        Lat.setText(sharedPref.getString("Lat", "Lattitude"), TextView.BufferType.EDITABLE);
        Lon.setText(sharedPref.getString("Lon", "Longitude"), TextView.BufferType.EDITABLE);
        But.isChecked = sharedPref.getBoolean("Custom", false);
    }

    fun goToDiscord(view: View)
    {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/SVT57Zb"))
        startActivity(browserIntent)
    }
    fun goToWebsite(view: View)
    {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://fruitbasket.games"))
        startActivity(browserIntent)
    }


    fun deletandshow(view: View){
        val PERMISSIONS: Array<String> = when(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            true ->arrayOf<String>(Manifest.permission.INTERNET,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
            false ->arrayOf<String>(Manifest.permission.INTERNET,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
        }
        checkAndRequestPermissions(PERMISSIONS)
        val box: LinearLayout = findViewById(R.id.Permbox)
        box.visibility = View.GONE;
    }
    fun goToKofi(view: View)
    {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com/bluebstopcat"))
        startActivity(browserIntent)
    }
    fun updatelatlon(view: View){
        val sharedPref = this.getSharedPreferences("MyPref",0);
        val Lat : EditText  = findViewById(R.id.Lattitude);
        val Lon : EditText  = findViewById(R.id.longitude);
        val But : SwitchCompat = findViewById(R.id.ToggleManual);
        val editor: SharedPreferences.Editor = sharedPref.edit();
        editor.putString("Lat", Lat.text.toString())
        editor.putString("Lon", Lon.text.toString())
        editor.putBoolean("Custom", But.isChecked)

        editor.apply();
        val toast = Toast.makeText(view.context, "Saved", Toast.LENGTH_LONG)
        toast.show()
    }

    private fun checkAndRequestPermissions(PERMISSIONS: Array<String>): Boolean {
        for(s: String in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, s)
                == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(s), PackageManager.PERMISSION_GRANTED)
                if (ContextCompat.checkSelfPermission(this, s)
                    == PackageManager.PERMISSION_DENIED
                ){
                    val toast = Toast.makeText(this, "Persona 5 widget needs access to all permissions to function properly", Toast.LENGTH_LONG)
                    toast.show()
                    return false
                }
            }
        }
        return true;
    }
        private fun checkPermissions(PERMISSIONS: Array<String>): Boolean {
            for(s: String in PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(this, s)
                    == PackageManager.PERMISSION_DENIED
                ) {
                   return false;
                }
            }
            return true;
        }
}

package blueberry.`is`.cool

import android.Manifest
import android.R.*
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.transition.Transition
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.AppWidgetTarget
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.android.gms.location.LocationServices
import java.time.LocalDateTime
import java.util.*


class NewAppWidget : AppWidgetProvider() {

    @Volatile var currentframe: Int = 0
    @Volatile var weather: String = ""
    @Volatile var currentwidth = 250;
    @Volatile var currentheight = 110;
    private val handler: Handler = Handler();
    private val runnable : AnimRunnable = object : AnimRunnable() {
        override fun run() {
            currentframe++;

            if(currentframe > 2)
                currentframe = 0;

            for (appWidgetId in this.ids) {
                val options = this.awm.getAppWidgetOptions(appWidgetId);
                currentwidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
                currentheight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
                updateAppWidget(this.c, this.awm, appWidgetId);
            }
            if(fsp.get()) {
                this.h.removeCallbacks(this)
            }
            else {
                this.h.postDelayed(this, 500)
            }
        }
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle?) {
        // See the dimensions and
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        // Get min width and height.
        currentwidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        currentheight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        AnimRunnable.fsp.set(true);
        super.onDeleted(context, appWidgetIds)
    }

    override fun onEnabled(context: Context?) {
        val prefs: SharedPreferences = context!!.getSharedPreferences("MyPref",0)
        if(prefs.getBoolean("Custom", false))
            getCurrentWeatherCustom(context, prefs.getString("lat","0.00")!!, prefs.getString("lon","0.00")!!){String -> weather = String }
        else
            getCurrentWeather(context){String -> weather = String }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val prefs: SharedPreferences = context.getSharedPreferences("MyPref",0)
        AnimRunnable.fsp.set(false);
        runnable.c = context;
        runnable.h = handler;
        runnable.awm = appWidgetManager;
        runnable.ids = appWidgetIds;
        //handler.postDelayed(runnable, 500)
        Log.d("Anim", "Created and Posted Runnable");
        if(prefs.getBoolean("Custom", false))
            getCurrentWeatherCustom(context, prefs.getString("lat","0.00")!!, prefs.getString("lon","0.00")!!){String -> weather = String }
        else
            getCurrentWeather(context){String -> weather = String }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
      }

    fun getCurrentWeather(context: Context, callbacks: (String) -> Unit) {
        //Get User Location
        var output: String = weather
       if (ActivityCompat.checkSelfPermission(
               context,
               Manifest.permission.ACCESS_FINE_LOCATION
           ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
               context,
               Manifest.permission.ACCESS_COARSE_LOCATION
           ) != PackageManager.PERMISSION_GRANTED
       ) {

           val toast = Toast.makeText(context, "Persona 5 Widget can't update location, switch to manual in the app or give permission in settings", Toast.LENGTH_LONG)
           toast.show()
           return
       }
       val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
       fusedLocationClient.lastLocation
           .addOnSuccessListener { location ->
               try {
                   val lat = location.latitude.toString().substring(0,8)
                   val long = location.longitude.toString().substring(0,8)
                   val Weathersite = "https://wttr.in/${lat},${long}?format=%C"
                   Log.d("Weather", Weathersite)
                   Weathersite.httpGet()
                       .responseString { _, _, result ->
                           when (result) {
                               is Result.Failure -> {
                                   val ex = result.getException()
                                   Log.wtf(
                                       "Weather",
                                       result.toString() + ex.toString() + Weathersite
                                   )
                                   callbacks(output)
                               }
                               is Result.Success -> {
                                   output = result.get()
                                   Log.i("Weather", "Weather is $output")
                                   callbacks(output)
                               }
                           }
                       }
               } catch (ex: java.lang.Exception) {
                   callbacks(output)
               }
           }
        }

    fun getCurrentWeatherCustom(context: Context, lat: String, long: String, callbacks: (String) -> Unit) {
        //Get User Location
        var output: String = weather
                try {
                    val Weathersite = "https://wttr.in/${lat},${long}?format=%C"
                    Log.d("Weather", Weathersite)
                    Weathersite.httpGet()
                        .responseString { _, _, result ->
                            when (result) {
                                is Result.Failure -> {
                                    val ex = result.getException()
                                    Log.wtf(
                                        "Weather",
                                        result.toString() + ex.toString() + Weathersite
                                    )
                                    callbacks(output)
                                }
                                is Result.Success -> {
                                    output = result.get()
                                    Log.i("Weather", "Weather is $output")
                                    callbacks(output)
                                }
                            }
                        }
                } catch (ex: java.lang.Exception) {
                    callbacks(output)
                }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val now = LocalDateTime.now()
        var time: Int = R.drawable.filler

        when (now.hour) {
            in 0..7 -> time = R.drawable.earlymorning
            in 8..10 -> time = R.drawable.morning
            in 11..12 -> time = R.drawable.lunchtime
            in 13..14 -> time = R.drawable.daytime
            in 15..17 -> time = R.drawable.afternoon
            in 16..23 -> time = R.drawable.evening
        }

        val monthWhitebg: Int = R.drawable.monthbasewhite_1 + now.monthValue - 1;
        val monthBlackbg: Int = R.drawable.monthbaseblack_1 + now.monthValue - 1;
        val monthFg: Int = R.drawable.month_1 + now.monthValue - 1;

        val weekdayint = now.dayOfWeek.value - 1;

        val weekday: Int = R.drawable.weekday_1 + weekdayint;
        val weekdaywhitebg: Int = R.drawable.weekdaywhite_1 + weekdayint;
        val weekdayblackbg: Int = R.drawable.weekdayblack_1 + weekdayint;

        //<editor-fold desc ="Day">
        val day = now.dayOfMonth;
        val dayfirstdigit = if (day > 10) day.toString().substring(0, 1).toInt() else 0;
        val dayseconddigit = if (day > 10) day.toString().substring(1, 2).toInt() else day;

        val daydigit1 = R.drawable.white_0 + dayfirstdigit;
        val daydigitbg1 = R.drawable.baseblack_0 + dayfirstdigit;
        val daydigitbigbg1 = R.drawable.basewhite_0 + dayfirstdigit;

        val daydigit2 = R.drawable.white_0 + dayseconddigit;
        val daydigitbg2 = R.drawable.baseblack_0 +dayseconddigit;
        val daydigitbigbg2 = R.drawable.basewhite_0 + dayseconddigit;

        //</editor-fold>

        //<editor-fold desc ="Weather">
        val weathergif = when (weather.toLowerCase(Locale.ROOT)) {
            "clear", "mostly sunny", "partly sunny", "mostly clear", "partly clear", "sunny" -> R.drawable.sunny_0 + currentframe
            "cloudy", "fog", "haze", "mostly cloudy", "overcast", "partly cloudy", "chance of showers", "chance of snow", "chance of storm",  "mist", "dust" -> R.drawable.cloudy_0 + currentframe
            "rain and snow", "light snow", "freezing drizzle", "sleet", "snow", "icy", "flurries", "snow showers", "hail" -> R.drawable.snow_0 + currentframe
            "light rain", "rain", "rain showers", "showers", "thunderstorm", "scattered showers", "storm", "scattered thunderstorms" -> R.drawable.rainy_0 + currentframe
            else -> R.drawable.sunny_0 + currentframe
        }
        //</editor-fold>


        // Construct the RemoteViews object, scaling appropriately
        val views : RemoteViews = when {
            currentwidth in 0..160 || currentheight in 0..55 -> RemoteViews(context.packageName, R.layout.persona5widget_improved_smallest)
            currentwidth in 160..225 || currentheight in 55..70 -> RemoteViews(context.packageName, R.layout.persona5widget_improved_small)
            currentwidth in 225..350 || currentheight in 70..100 -> RemoteViews(context.packageName, R.layout.persona5widget_improved)
            else -> RemoteViews(context.packageName, R.layout.persona5widget_improved_big)
        }

        //Weather
        val appWidgetTarget : AppWidgetTarget = object : AppWidgetTarget(context, R.id.Weather, views, appWidgetId){
        };
        views.setImageViewResource(R.id.WeatherBaseWhite, R.drawable.weather_base_white)
        Glide.with(context.applicationContext).asBitmap().load(R.drawable.sunny).into(appWidgetTarget);

        //Month
        views.setImageViewResource(R.id.MonthBaseWhite, monthWhitebg)
        views.setImageViewResource(R.id.MonthBaseBlack, monthBlackbg)
        views.setImageViewResource(R.id.Month, monthFg)

        //Digits
        views.setImageViewResource(R.id.DigitOneBaseWhite, daydigitbigbg1)
        views.setImageViewResource(R.id.DigitTwoBaseWhite, daydigitbigbg2)
        views.setImageViewResource(R.id.DigitOneBaseBlack, daydigitbg1)
        views.setImageViewResource(R.id.DigitTwoBaseBlack, daydigitbg2)
        views.setImageViewResource(R.id.DigitOne, daydigit1)
        views.setImageViewResource(R.id.DigitTwo, daydigit2)

        //Weekday
        views.setImageViewResource(R.id.CurrentTime, time)
        views.setImageViewResource(R.id.WeekDayBaseWhite, weekdaywhitebg)
        views.setImageViewResource(R.id.WeekDayBaseBlack, weekdayblackbg)
        views.setImageViewResource(R.id.WeekDay, weekday)
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}


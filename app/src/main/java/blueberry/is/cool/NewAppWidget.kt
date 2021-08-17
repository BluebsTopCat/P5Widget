package blueberry.`is`.cool

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.RemoteViews
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.*
import org.json.JSONObject
import java.time.DayOfWeek
import java.time.LocalDateTime


/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    var currentframe: Int = 0

    override fun onAppWidgetOptionsChanged(
        context: Context,
        appWidgetManager: AppWidgetManager, appWidgetId: Int, newOptions: Bundle?
    ) {

        // See the dimensions and
        val options = appWidgetManager.getAppWidgetOptions(appWidgetId)

        // Get min width and height.
        val minWidth = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)
        val minHeight = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context /* Activity context */)
        with(sharedPreferences.edit()) {
            putString(
                "Size",
                getCellsForSize(minHeight).toString() + "x" + getCellsForSize(minWidth).toString()
            );
            Log.i("Size",  getCellsForSize(minHeight).toString() + "x" + getCellsForSize(minWidth).toString() )
            apply()
        }

        getCurrentWeather(context){ String ->
                Log.i("Weather", String)
                updateAppWidget(context, appWidgetManager, appWidgetId, String)
        }
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        getCurrentWeather(context){ String ->
            Log.i("Weather", String)
            for (appWidgetId in appWidgetIds) {
                Log.i("AnimatingWeather", "Content")
                updateAppWidget(context, appWidgetManager, appWidgetId, String)
            }

        }

      }

   fun getCurrentWeather(context: Context, callbacks: (String) -> Unit) {
        //Get User Location

        var output: String = "DefaultVar"
        //Then Plug it into the Weather Api Async
       try {
           val Weathersite = "https://wttr.in/?format=%C"
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
        } catch (ex: java.lang.Exception){
           output = "Error Fetching Weather"
        }
        }

    internal fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        weather: String
    )
    {
        val now = LocalDateTime.now()
        val hour = now.hour
        var time: Int = R.drawable.filler

        val weekday: Int
        val weekdaywhitebg: Int
        val weekdayblackbg: Int

        val monthWhitebg: Int
        val monthBlackbg: Int
        val monthFg: Int

        when (hour) {
            in 0..7 -> time = R.drawable.earlymorning
            in 8..10 -> time = R.drawable.morning
            in 11..12 -> time = R.drawable.lunchtime
            in 13..14 -> time = R.drawable.daytime
            in 15..17 -> time = R.drawable.afternoon
            in 16..21 -> time = R.drawable.evening
            in 22..23 -> time = R.drawable.evening
        }
        when (now.monthValue) {
            1 -> {
                monthWhitebg = R.drawable.monthbasewhite_1
                monthBlackbg = R.drawable.monthbaseblack_1
                monthFg = R.drawable.month_1
            }
            2 -> {
                monthWhitebg = R.drawable.monthbasewhite_2
                monthBlackbg = R.drawable.monthbaseblack_2
                monthFg = R.drawable.month_2
            }
            3 -> {
                monthWhitebg = R.drawable.monthbasewhite_3
                monthBlackbg = R.drawable.monthbaseblack_3
                monthFg = R.drawable.month_3
            }
            4 -> {
                monthWhitebg = R.drawable.monthbasewhite_4
                monthBlackbg = R.drawable.monthbaseblack_4
                monthFg = R.drawable.month_4
            }
            5 -> {
                monthWhitebg = R.drawable.monthbasewhite_5
                monthBlackbg = R.drawable.monthbaseblack_5
                monthFg = R.drawable.month_5
            }
            6 -> {
                monthWhitebg = R.drawable.monthbasewhite_6
                monthBlackbg = R.drawable.monthbaseblack_6
                monthFg = R.drawable.month_6
            }
            7 -> {
                monthWhitebg = R.drawable.monthbasewhite_7
                monthBlackbg = R.drawable.monthbaseblack_7
                monthFg = R.drawable.month_7
            }
            8 -> {
                monthWhitebg = R.drawable.monthbasewhite_8
                monthBlackbg = R.drawable.monthbaseblack_8
                monthFg = R.drawable.month_8
            }
            9 -> {
                monthWhitebg = R.drawable.monthbasewhite_9
                monthBlackbg = R.drawable.monthbaseblack_9
                monthFg = R.drawable.month_9
            }
            10 -> {
                monthWhitebg = R.drawable.monthbasewhite_10
                monthBlackbg = R.drawable.monthbaseblack_10
                monthFg = R.drawable.month_10
            }
            11 -> {
                monthWhitebg = R.drawable.monthbasewhite_11
                monthBlackbg = R.drawable.monthbaseblack_11
                monthFg = R.drawable.month_11
            }
            12 -> {
                monthWhitebg = R.drawable.monthbasewhite_12
                monthBlackbg = R.drawable.monthbaseblack_12
                monthFg = R.drawable.month_12
            }
            else -> {
                monthWhitebg = R.drawable.monthbasewhite_12
                monthBlackbg = R.drawable.monthbaseblack_12
                monthFg = R.drawable.month_12
            }

        }
        when (now.dayOfWeek) {
            DayOfWeek.TUESDAY -> {
                weekday = R.drawable.tuesday
                weekdaywhitebg = R.drawable.basewhite_tuesday
                weekdayblackbg = R.drawable.baseblack_tuesday
            }
            DayOfWeek.WEDNESDAY -> {
                weekday = R.drawable.wednesday
                weekdaywhitebg = R.drawable.basewhite_wednesday
                weekdayblackbg = R.drawable.baseblack_wednesday
            }
            DayOfWeek.THURSDAY -> {
                weekday = R.drawable.thursday
                weekdaywhitebg = R.drawable.basewhite_thursday
                weekdayblackbg = R.drawable.baseblack_thursday
            }
            DayOfWeek.FRIDAY -> {
                weekday = R.drawable.friday
                weekdaywhitebg = R.drawable.basewhite_friday
                weekdayblackbg = R.drawable.baseblack_friday
            }
            DayOfWeek.SATURDAY -> {
                weekday = R.drawable.saturday
                weekdaywhitebg = R.drawable.basewhite_saturday
                weekdayblackbg = R.drawable.baseblack_saturday
            }
            DayOfWeek.SUNDAY -> {
                weekday = R.drawable.sunday
                weekdaywhitebg = R.drawable.basewhite_sunday
                weekdayblackbg = R.drawable.baseblack_sunday
            }
            else -> {
                weekday = R.drawable.monday
                weekdaywhitebg = R.drawable.basewhite_monday
                weekdayblackbg = R.drawable.baseblack_monday
            }


        }
        val daydigit1 = R.drawable.white_0 + now.dayOfMonth / 10
        val daydigitbg1 = R.drawable.baseblack_0 + now.dayOfMonth / 10
        val daydigitbigbg1 = R.drawable.basewhite_0 + now.dayOfMonth / 10

        val daydigit2 = R.drawable.white_0 + now.dayOfMonth % 10
        val daydigitbg2 = R.drawable.baseblack_0 + now.dayOfMonth % 10
        val daydigitbigbg2 = R.drawable.basewhite_0 + now.dayOfMonth % 10

        Log.i("Widget", "The Weather is $weather")
        val Weathergif = when (weather) {
            "Clear", "Mostly Sunny", "Mostly Clear", "Sunny" -> R.drawable.sunny_0 + currentframe
            "Clouds", "Partly Cloudy", "Grey", "Fog", "Foggy" -> R.drawable.cloudy_0 + currentframe
            "Snow", "Snowy" -> R.drawable.snow_0 + currentframe
            "Rain", "Thunderstorm", "Drizzle", "Downpour" -> R.drawable.rainy_0 + currentframe
            else -> R.drawable.sunny_0 + currentframe
        }
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context /* Activity context */)
        val size = sharedPreferences.getString("Size", "");
        // Construct the RemoteViews object
        val views = when (size) {
            "1x1", "2x1", "3x1", "4x1", "5x1","6x1","7x1","8x1" -> RemoteViews(
                context.packageName,
                R.layout.new_app_widget1x1
            )
            "1x2", "2x2", "1x3", "1x4", "1x5","1x6", "1x7", "1x8", "3x2", "5x2" -> RemoteViews(
                context.packageName,
                R.layout.new_app_widget2x1
            )
            "2x3", "3x3" -> RemoteViews(context.packageName, R.layout.new_app_widget3x2)
            "2x4", "2x5" -> RemoteViews(context.packageName, R.layout.new_app_widget2x4)
            else ->RemoteViews(context.packageName, R.layout.new_app_widget)
        }
        views.setImageViewResource(R.id.WeatherBg, R.drawable.weather_base_white)
        views.setImageViewResource(R.id.CurrentTime, time)
        //Month
        views.setImageViewResource(R.id.WeatherAnimated, Weathergif)
        views.setImageViewResource(R.id.MonthBaseWhite, monthWhitebg)
        views.setImageViewResource(R.id.DigitoneBaseWhite, daydigitbigbg1)
        views.setImageViewResource(R.id.DigittwoBaseWhite, daydigitbigbg2)

        views.setImageViewResource(R.id.monthbaseblack, monthBlackbg)
        views.setImageViewResource(R.id.DigitOneBase2, daydigitbg1)
        views.setImageViewResource(R.id.DigitTwoBase2, daydigitbg2)
        views.setImageViewResource(R.id.month, monthFg)
        views.setImageViewResource(R.id.Digit1, daydigit1)
        views.setImageViewResource(R.id.Digit2, daydigit2)

        views.setImageViewResource(R.id.Weekday, weekday)
        views.setImageViewResource(R.id.WeekDayBaseBlack, weekdayblackbg)
        views.setImageViewResource(R.id.WeekDayBaseWhite, weekdaywhitebg)
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getCellsForSize(size: Int): Int {
        var n = 2
        while (74 * n - 30 < size) {
            ++n
        }
        return n - 1
    }
}


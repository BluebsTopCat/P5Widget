package blueberry.is.cool;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.os.Handler;

import java.util.concurrent.atomic.AtomicBoolean;
public abstract class AnimRunnable implements Runnable {
    public static ForceStopPass fsp = new ForceStopPass();
    public Context c;
    public AppWidgetManager awm;
    public int[] ids;
    public Handler h;
    public abstract void run();
    public AnimRunnable(){};
}

package blueberry.`is`.cool

import android.util.Log
import java.util.concurrent.atomic.AtomicBoolean

class ForceStopPass {
    private val ForceStop : AtomicBoolean = AtomicBoolean(false);
    @Synchronized fun get(): Boolean{
        return this.ForceStop.get();
    }
    @Synchronized fun set(b: Boolean){
        Log.d("FSP", "Set to " + b)
       this.ForceStop.set(b);
    }
};
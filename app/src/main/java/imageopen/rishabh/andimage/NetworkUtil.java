package imageopen.rishabh.andimage;
import android.app.Application;
import android.util.Log;

/**
 * Created by Rishabh on 2/23/2017.
 */

public class NetworkUtil extends Application {

    private static NetworkUtil mInstance;
    private static final String TAG=NetworkUtil.class.getSimpleName();

    NetworkUtil(){
        Log.i(TAG, "NetworkUtil() method fired ");
        mInstance=this;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate() method fired ");
        mInstance = this;
    }

    public static synchronized NetworkUtil getInstance() {
        Log.i(TAG, "getInstance() method fired ");
        return mInstance;
    }

    public void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener listener) {
        Log.i(TAG, "setConnectivityListener() method fired.");
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }


}

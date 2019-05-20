package imageopen.rishabh.andimage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Rishabh on 2/23/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    private static final String TAG=NetworkChangeReceiver.class.getSimpleName();
    Context context;
    public NetworkChangeReceiver() {
        super();
        Log.i(TAG, "NetworkChangeReceiver() method fired ");
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        Log.i(TAG, "onReceive() method fired ");

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }

        Log.i(TAG, "onReceive() method fired, network available: "+isConnected);
    }

    public static boolean isConnected() {
        Log.i(TAG, "isConnected() method fired ");
        ConnectivityManager
                cm = (ConnectivityManager) NetworkUtil.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}

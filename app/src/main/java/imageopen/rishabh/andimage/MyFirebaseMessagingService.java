package imageopen.rishabh.andimage;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


/**
 * Created by Rishabh on 2/27/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final String TAG=MyFirebaseMessagingService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.i(TAG, "onMessageReceived() method fired ");
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        Toast.makeText(getBaseContext(),"From: "+ remoteMessage.getFrom(),Toast.LENGTH_LONG).show();

    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }
}

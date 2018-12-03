package maveric.collector;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;


public class MonitorService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public MonitorService() {
        super("test");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Info", "Entered Service");

        sendNotification();

    }

    protected void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification n = new Notification.Builder(this)
                .setContentTitle("Test Notification.")
                .setContentText("Testing notifications from service")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        notificationManager.notify(0, n);

    }
}

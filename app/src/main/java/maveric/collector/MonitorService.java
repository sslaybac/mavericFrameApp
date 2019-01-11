package maveric.collector;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.Runtime;

import eu.chainfire.libsuperuser.Shell;



public class MonitorService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    Context msContext;

    protected void setContext(Context context){
        this.msContext = context;
    }
    public MonitorService() {
        super("test");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Info", "Entered Service");
        setContext(getApplicationContext());
        collectData();
        notifyAPI();

    }

    protected void sendNotification(String notice) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification n = new Notification.Builder(this)
                .setContentTitle("Test Notification.")
                .setContentText(notice)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build();

        notificationManager.notify(0, n);

    }

    protected void notifyAPI() {
        try {
            URL url = new URL(getBaseContext().getString(R.string.api_url));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            Log.i("Standard: ", stringBuilder.toString());

            sendNotification(stringBuilder.toString());

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    protected void collectData() {
        // Data Collection Code goes here
        // Ideally, the code should collect data for 1 of {Power, Network, System Calls}, then write
        // the data to a preset filename. the NotifyAPI function will be modified to send that file
        // to a REST API. Create additional classes as needed. If root privileges are require, then
        // root privileges will be arranged. Bonus points if they aren't needed.

        //Collect data on System Calls: once start be sure to run the app(s) you want to collect

        bgSystemCallCapture collect = new bgSystemCallCapture();
        collect.onCreate();

    }
}

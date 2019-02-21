package maveric.net;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APICall extends AsyncTask<Void, Void, String> {
    private Exception exception;
    private TextView responseView;
    private  Context callerContext;
    private URL url;


    public APICall(Context context, View textBox){
       callerContext = context;
       responseView = (TextView) textBox;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            url = new URL(callerContext.getString(R.string.api_url));
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            return stringBuilder.toString();

        } catch (java.io.IOException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    protected void onPostExecute(String response) {
        responseView.setText(response);
    }
}

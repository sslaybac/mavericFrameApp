package maveric.collector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APICall messenger = new APICall(this, findViewById(R.id.responseView));

        messenger.execute();

        Toast.makeText(this, R.string.api_url, Toast.LENGTH_SHORT).show();
    }
}

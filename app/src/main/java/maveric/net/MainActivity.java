package maveric.net;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, R.string.api_url, Toast.LENGTH_SHORT).show();

        Intent svc = new Intent(this, MonitorService.class);
        startService(svc);
    }
}

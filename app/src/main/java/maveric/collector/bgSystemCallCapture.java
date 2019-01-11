package maveric.collector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class bgSystemCallCapture extends Activity {
    private class bgCapture extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            if(!Shell.SU.available())
                Log.e("SU is not available","Try running the super root script 1st");
            else{
                Shell.SU.run("timeout 60 strace -c -f -S calls -p `ps | grep zygote | cut -d \" \" -f 7` -o /data/local/tmp/testCode.txt");
            }
            return null;
        }
    }

    public void onCreate() {
        
        (new bgCapture()).execute();
    }
}

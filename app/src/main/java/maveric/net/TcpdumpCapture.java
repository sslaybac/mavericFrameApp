// credit to https://github.com/yadavdev/android-packet-capture for the help - pretty much all this code is from there

package maveric.net;

import android.app.ProgressDialog;
import android.util.Log;

import eu.chainfire.libsuperuser.Shell;
import java.util.List;

public class TcpdumpCapture {
    private static Shell.Interactive rootTcpdumpShell;
    private static ProgressDialog progressBox;
    private static boolean isInitialised = false;

    public static void initialiseCapture() {
        if (rootTcpdumpShell != null) {
            if(!isInitialised)
                Log.i("reply","error with roottcpdump shell");
            startCapture();
        }
        else {
            rootTcpdumpShell = new Shell.Builder().
                    useSU().
                    setWantSTDERR(false).
                    setMinimalLogging(true).
                    open(new Shell.OnCommandResultListener() {
                        @Override
                        public void onCommandResult(int commandVal, int exitVal, List<String> out) {
                            //Callback checking successful shell start.
                            if (exitVal == Shell.OnCommandResultListener.SHELL_RUNNING) {
                                isInitialised = true;
                                Log.i("reply","sucessfull shell start - starting TCPdump capture");
                                startCapture();
                            }
                            else {
                                Log.i("reply","error starting root shell");
                            }
                        }
                    });
        }
    }
    private static void startCapture(){
        try{
            List<String> out = Shell.SH.run("ps | grep tcpdump");
            if(out.size() > 0){
                //One process already running. Don't start another.
                return;
            }
            Log.i("reply","Attempting to start tcpdump");
            // run tcpdump for 60 seconds then output to /mnt/sdcard/meep.pcap
            rootTcpdumpShell.addCommand("tcpdump -i any -w /mnt/sdcard/meep.pcap -G 60", 0, new Shell.OnCommandLineListener() {
                @Override
                public void onCommandResult(int commandVal, int exitVal) {
                    if (exitVal < 0) {
                        Log.i("reply","error returned from shell command");
                    }
                }
                @Override
                public void onLine(String line) {
                    // errors if we dont override this function - wanted to delete this.
                    Log.i("reply",line);
                }
            });
        }
        catch(Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}

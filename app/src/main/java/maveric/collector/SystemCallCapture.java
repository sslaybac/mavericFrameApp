//System Call Capture
package maveric.collector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class SystemCallCapture {
    private static Shell.Interactive rootSysCallShell;
    private static boolean isInitialised = false;

    public static void initialCapture(){
        if(rootSysCallShell != null){
            if(!isInitialised)
                Log.i("Reply","error with rootSysCallShell");
            startCapture();
        }
        else{
            rootSysCallShell = new Shell.Builder().
                    useSU().
                    setWantSTDERR(false).
                    setMinimalLogging(true).
                    open(new Shell.OnCommandResultListener() {
                        @Override
                        public void onCommandResult(int commandVal, int exitVal, List<String> output) {
                            //Callback Checking if successful shell start.
                            if(exitVal == Shell.OnCommandResultListener.SHELL_RUNNING){
                                isInitialised = true;
                                Log.i("reply","sucessfull shell start - starting System Call Capture w/ strace");
                                startCapture();
                            }
                            else {
                                Log.e("reply","error starting root shell!");
                            }
                        }
                    });
        }
    }
    //Start to apture the System Calls [todo: capture the calls and update a file]
    private static void startCapture() {

        try{
            List<String> out = Shell.SH.run("ps | grep strace"); //check if strace is running
            if(out.size()>0){
                //Process already running, let's not start another one
                Log.i("reply","strace still running");
                return;
            }
            Log.i("reply","Attempting to start strace");
            rootSysCallShell.addCommand("mkdir /data/local/tmp/strace");
            rootSysCallShell.addCommand("chmod 777 /data/local/tmp/strace");
            rootSysCallShell.addCommand("timeout 60 strace -c -f -S calls -p `ps | grep zygote | cut -d \" \" -f 7` -o /data/local/local/testCode.txt",
                    0,
                    new Shell.OnCommandLineListener() {
                        @Override
                        public void onCommandResult(int commandVal, int exitVal) {
                            if(exitVal < 0){
                                Log.e("Error","Error return from command");
                            }
                        }

                        @Override
                        public void onLine(String line) {
                                Log.i("reply","line Data: " + line);
                        }
                    });

            //todo: Use ps to get list of PIDs -> for each run strace -> output to file -> put files made this way

            //option 1
            //todo: This will be a long process, this class should be ran in the background

            //todo: run strace with a timeout <specify time w/ variable> for each pid

            //command to get each unique system call from a strace output result todo: cut -f 1 -d '(' <strace file> | sort | uniq

            //option 2
            //todo: run strace -c -p <pid> "This will should results in a table that includes time / usecs/call
                //note This is just like option 1 expect results are in a well formated table.
                //note: `try -S <sortby>` to sorty by time, calls, or name (default time)
                //note: looks like the zygote process is the parent process (inital process) for most apps (if not all) and what systems calls they make
                        //note cont.: can be seen by strace (hopefully)

        }
        catch (Exception e){
            Log.e("Error Reply","failed to capture data: " + e.toString());
        }
        finally {
            Log.i("Info reply","Start Capture complete");
        }
    }
}
/*
fail
Log.i("reply","Attempting to start strace");
            rootSysCallShell.addCommand("adb shell setenforce 0");
            //rootSysCallShell.addCommand("adb shell mkdir /data/local/tmp/strace");
            //rootSysCallShell.addCommand("adb shell chmod 777 /data/local/tmp/strace");
            //Currently checking the browser app todo: change this to grab a specific app [user input?]
            rootSysCallShell.addCommand("adb shell setprop wrap.com.google.android.browser \"logwrapper strace -f -o strace.txt\"");
            Intent launch = new Intent(Intent.ACTION_MAIN);
            launch.addCategory(Intent.CATEGORY_LAUNCHER);
            launch.setPackage("com.google.android.browser");
            mContext.startActivity(launch);
            Shell.SH.run("adb shell am start -n com.google.android.browser");

 */
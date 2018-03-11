package com.eko.speedtest;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by Eko on 19.07.2017.
 */

public class ActivityRecognizedService extends IntentService {


    public ActivityRecognizedService() {
        super("ActivityRecognizedService");
    }
    private MediaScannerConnection mediaScannerConnection;
    
    private File motionLogFile;
    private String textLine;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (ActivityRecognitionResult.hasResult(intent)) {
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            handleDetectedActivities(result.getProbableActivities());
        }
    }

    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {

        if(motionLogFile == null) {
            File root = Environment.getExternalStorageDirectory();
            File dir = new File(root.getAbsolutePath(), "/Google Recognition Test");
            dir.mkdirs();
               motionLogFile = new File(dir, "RecognitionLog.txt");
            Uri contentUri = Uri.fromFile(motionLogFile);
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);

        }
        FileOutputStream outputStream = null;


            try {
                outputStream = new FileOutputStream(motionLogFile, true);
                OutputStreamWriter myOutWriter = new OutputStreamWriter(outputStream);
                for (DetectedActivity activity : probableActivities) {

                    Date date = new Date();
                    switch (activity.getType()) {
                        case DetectedActivity.IN_VEHICLE: {
                            Log.e("ActivityRecogition", "In Vehicle: " + activity.getConfidence());
                            textLine += date.toString()   + " ActivityRecogition" + " In Vehicle: " + activity.getConfidence() + "\n\r";
                            break;
                        }
                        case DetectedActivity.ON_BICYCLE: {
                            Log.e("ActivityRecogition", "On Bicycle: " + activity.getConfidence());
                            textLine += date.toString() + " ActivityRecogition" + " On Bicycle: " + activity.getConfidence() + "\n\r";
                            break;
                        }
                        case DetectedActivity.ON_FOOT: {
                            Log.e("ActivityRecogition", "On Foot: " + activity.getConfidence());
                            textLine += date.toString() + " ActivityRecogition" + " On Foot: " + activity.getConfidence() + "\n\r";
                        }
                        case DetectedActivity.RUNNING: {
                            Log.e("ActivityRecogition", "Running: " + activity.getConfidence());
                            textLine += date.toString() + " ActivityRecogition" + " Running: " + activity.getConfidence() + "\n\r";
                            break;
                        }
                        case DetectedActivity.STILL: {
                            Log.e("ActivityRecogition", "Still: " + activity.getConfidence());

                            textLine += date.toString() + " ActivityRecogition" + " Still:" + activity.getConfidence() + "\n\r";
                            break;
                        }
                        case DetectedActivity.TILTING: {
                            Log.e("ActivityRecogition", "Tilting: " + activity.getConfidence());
                            textLine += date.toString() + " ActivityRecogition " + " Tilting: " + activity.getConfidence() + "\n\r";
                            break;
                        }
                        case DetectedActivity.WALKING: {
                            Log.e("ActivityRecogition", "Walking: " + activity.getConfidence());
                            textLine += date.toString() + " ActivityRecogition " + " Walking: " + activity.getConfidence() + "\n\r";
                            break;
                        }
                        case DetectedActivity.UNKNOWN: {
                            Log.e("ActivityRecogition", "Unknown: " + activity.getConfidence());
                            textLine += date.toString() + " ActivityRecogition" + " Unknown: " + activity.getConfidence() + "\n\r";
                            break;
                        }
                    }
                }
                myOutWriter.write(textLine);
                myOutWriter.flush();
                myOutWriter.close();
                Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_LONG).show();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


}

package com.example.matthew.livestyle2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.scandit.barcodepicker.BarcodePicker;
import com.scandit.barcodepicker.OnScanListener;
import com.scandit.barcodepicker.ScanSession;
import com.scandit.barcodepicker.ScanSettings;
import com.scandit.barcodepicker.ScanditLicense;
import com.scandit.recognition.Barcode;

public class ScanBarcodeActivity extends AppCompatActivity implements OnScanListener {

    // The main object for recognizing a displaying barcodes.
    private BarcodePicker mBarcodePicker;

    // Enter your Scandit SDK App key here.
    // Your Scandit SDK App key is available via your Scandit SDK web account.
    public static final String sScanditSdkAppKey = "kdSB2g9tMFnoLq+iAbSsnc+cxybszEsXiqISPUCQ5Vo";

    Toast mToast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scan_barcode);

        ScanditLicense.setAppKey(sScanditSdkAppKey);

        // Initialize and start the bar code recognition.
        initializeAndStartBarcodeScanning();
    }

    @Override
    protected void onPause() {
        // When the activity is in the background immediately stop the
        // scanning to save resources and free the camera.
        mBarcodePicker.stopScanning();

        super.onPause();
    }

    @Override
    protected void onResume() {
        // Once the activity is in the foreground again, restart scanning.
        mBarcodePicker.startScanning();
        super.onResume();
    }

    /**
     * Initializes and starts the bar code scanning.
     */
    public void initializeAndStartBarcodeScanning() {
        // Switch to full screen.


        // The scanning behavior of the barcode picker is configured through scan
        // settings. We start with empty scan settings and enable a very generous
        // set of symbologies. In your own apps, only enable the symbologies you
        // actually need.
        ScanSettings settings = ScanSettings.create();
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_EAN13, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_UPCA, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_EAN8, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_UPCE, true);

        settings.setCameraFacingPreference(ScanSettings.CAMERA_FACING_BACK);


        // Some Android 2.3+ devices do not support rotated camera feeds. On these devices, the
        // barcode picker emulates portrait mode by rotating the scan UI.
        boolean emulatePortraitMode = !BarcodePicker.canRunPortraitPicker();
        if (emulatePortraitMode) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        BarcodePicker picker = new BarcodePicker(this, settings);

        setContentView(picker);
        mBarcodePicker = picker;

        // Register listener, in order to be notified about relevant events
        // (e.g. a successfully scanned bar code).
        mBarcodePicker.setOnScanListener(this);

    }

    /**
     *  Called when a barcode has been decoded successfully.
     */
    public void didScan(ScanSession session) {
        String message = "";
        for (Barcode code : session.getNewlyRecognizedCodes()) {
            String data = code.getData();
            // truncate code to certain length
            String cleanData = data;
            /*if (data.length() > 30) {
                cleanData = data.substring(0, 25) + "[...]";
            }
            if (message.length() > 0) {
                message += "\n\n\n";
            }*/
            message += cleanData;
            //message += "\n\n(" + code.getSymbologyName().toUpperCase() + ")";
        }

        Intent goToScanResultsActivity = new Intent(ScanBarcodeActivity.this, ScanResultsActivity.class);
        goToScanResultsActivity.putExtra("barcode",message);
        ScanBarcodeActivity.this.startActivity(goToScanResultsActivity);

        /*if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        mToast.show();*/
    }

    @Override
    public void onBackPressed() {
        mBarcodePicker.stopScanning();
        finish();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan_barcode, menu);
        return true;
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}

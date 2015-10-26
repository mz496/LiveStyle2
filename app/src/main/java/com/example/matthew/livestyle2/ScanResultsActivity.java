package com.example.matthew.livestyle2;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ScanResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_results);

        // custom action bar
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        // get barcode scan result
        Intent fromScanBarcodeActivity = getIntent();
        String barcodeData = fromScanBarcodeActivity.getStringExtra("barcode");
        for (int i = 0; i < Global.products.length; i++) {
            if (Global.products[i][0].equals(barcodeData)) {
                ((TextView) findViewById(R.id.scan_results_name)).setText(Global.products[i][1]);
                ((TextView) findViewById(R.id.scan_results_price)).setText(Global.products[i][2]);
                ImageView pic = ((ImageView) findViewById(R.id.scan_results_image));
                pic.setImageDrawable(getResources().getDrawable(Integer.valueOf(Global.products[i][3]), getTheme()));

            }
        }

        // outfits containing this item
        ImageAdapter adapter = new ImageAdapter(this);
        adapter.addThumb(R.drawable.placeholder);
        adapter.addThumb(R.drawable.landscape);
        adapter.addThumb(R.drawable.landscape);
        adapter.addThumb(R.drawable.placeholder);
        
        GridView gridview = (GridView) findViewById(R.id.outfits_scan_gridview);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                System.out.println(Integer.toString(position));
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan_results, menu);
        return true;
    }

    @Override
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

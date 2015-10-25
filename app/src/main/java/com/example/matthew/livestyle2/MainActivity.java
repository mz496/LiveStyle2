package com.example.matthew.livestyle2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements OutfitFeedFragment.OnFragmentInteractionListener, ScanFeedFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // custom action bar
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.main_activity_custom_actionbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        // tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Scans"));
        tabLayout.addTab(tabLayout.newTab().setText("Outfits"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // viewpager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final MainActivityPagerAdapter adapter = new MainActivityPagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // buttons
        final AppCompatImageButton giftButton = (AppCompatImageButton) findViewById(R.id.gift_button);
        giftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToGiftActivity = new Intent(MainActivity.this, GiftActivity.class);
                MainActivity.this.startActivity(goToGiftActivity);
            }
        });

        final AppCompatImageButton checkoutButton = (AppCompatImageButton) findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToCheckoutActivity = new Intent(MainActivity.this, CheckoutActivity.class);
                MainActivity.this.startActivity(goToCheckoutActivity);
            }
        });



        Pubnub pubnub = new Pubnub("pub-c-2c119045-b31f-4ada-85e9-060cd47b7c27",
                "sub-c-d33774fa-3311-11e5-a033-0619f8945a4f");


        try {
            pubnub.subscribe("feed", new com.pubnub.api.Callback() {
                public void successCallback(String channel, Object message) {
                    System.out.println(message);
                    JSONObject messageJSON = (JSONObject) message;
                    try{
                        String receivedImage = messageJSON.getString("IMAGE");
                        String name = messageJSON.getString("NAME");
                        String price = messageJSON.getString("PRICE");

                        //ScanFeedFragment newsff = new ScanFeedFragment()

                        final LayoutInflater layout_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final LinearLayout feed = (LinearLayout) findViewById(R.id.scan_feed_content);

                        LinearLayout feed_item = (LinearLayout) layout_inflater.inflate(R.layout.feed_item, null);
                        ((TextView) feed_item.findViewById(R.id.feed_item_name)).setText(name);
                        ((TextView) feed_item.findViewById(R.id.feed_item_time_ago)).setText("just now");
                        ((TextView) feed_item.findViewById(R.id.feed_item_brand)).setText("?");
                        ((TextView) feed_item.findViewById(R.id.feed_item_price)).setText(price);

                        final ImageView pic = (ImageView) feed_item.findViewById(R.id.feed_item_image);
                        try {
                            URL url = new URL(receivedImage);
                            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                            pic.setImageBitmap(bmp);
                        } catch (java.net.MalformedURLException e) {
                            Log.e("Malformed", e.getMessage());
                        } catch (java.io.IOException e) {
                            Log.e("IOexception",e.getMessage());
                        }
                        /*new Thread() {
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        Picasso.with(getActivity().getBaseContext()).load(receivedImage).into(pic);
                                    }
                                });
                            }
                        }.start();*/


                        /*final String url = (!data[entry][5].startsWith("http://") && !data[entry][5].startsWith("https://")) ?
                                "http://" + data[entry][5] : data[entry][5];

                        pic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);
                            }
                        });*/

                        feed.addView(feed_item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                public void errorCallback(String channel, PubnubError error) {
                    System.out.println(error.getErrorString());
                }
            });
        } catch (PubnubException e) {
            e.printStackTrace();
        }

    }

    public void onScanBarcodeButtonPressed() {
        Intent goToScanBarcodeActivity = new Intent(MainActivity.this, ScanBarcodeActivity.class);
        MainActivity.this.startActivity(goToScanBarcodeActivity);
    }

    public void onAddOutfitButtonPressed(Bitmap b) {
        Intent goToAddOutfitActivity = new Intent(MainActivity.this, AddOutfitActivity.class);
        goToAddOutfitActivity.putExtra("selected_image",b);
        MainActivity.this.startActivity(goToAddOutfitActivity);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

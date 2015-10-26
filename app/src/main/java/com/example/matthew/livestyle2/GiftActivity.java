package com.example.matthew.livestyle2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GiftActivity extends AppCompatActivity {

    private static String accountID2 = "f852e580-7353-453e-93ca-d9ffdcb6fa32";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);

        TextAdapter adapter = new TextAdapter(this);
        adapter.addText("$10 off at Macy's");
        adapter.addText("$15 off at H&M");
        adapter.addText("$5 off at Gucci");
        adapter.addText("$20 off at Louis Vuitton");
        adapter.addText("$10 off at Dior");
        adapter.addText("$15 off at Hugo Boss");

        GridView gridview = (GridView) findViewById(R.id.gift_gridview);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String requestURI = "/YiiModo/api_v2/gift/send";
                long timeStamp = System.currentTimeMillis();
//            timeStamp = 14457623100000l;
//            HashMap<String,Object> requestMap = new HashMap<String,Object>();
//            requestMap.put("is_modo_terms_agree", true);
//            requestMap.put("phone","2142507450");
//            requestMap.put("should_send_modo_descript", true);
//            requestMap.put("should_send_password",true);
//            requestMap.put("verify_password_url","https://valuezilla.com");
                String requestBody = "account_id="+accountID2+ "&gift_amount=15&should_notify_sender=1&should_notify_receiver=1&receiver_phone=9723724305";
//            String requestBody2 = "{\"is_modo_terms_agree\":true,\"phone\":\"2142507450\",\"should_send_modo_descript\":true,\"should_send_password\":true,\"verify_password_url\":\"https://valuezilla.com\"}";
//            String requestBody = ((new JSONObject(requestMap)).toString());
                Log.d("REQUESTBODY", requestBody);
                String key = ModoConfig.API_KEY;
                String secret = ModoConfig.API_SECRET;
                String signatureString = ModoPayments.createSignature(requestURI, timeStamp, requestBody, key, secret);
                ModoService pdService = ModoPayments.getService();
//            User newUser = new User("2142507450", "https://valuezilla.com", 1, 1, 1);
                pdService.sendGift(timeStamp, signatureString, accountID2, 15,1, 1, "9723724305", new Callback<Response>() {
                    @Override
                    public void success(Response userResponse, Response response) {

//                    accountID = userResponse.getAccount_id();
//                    System.out.println(accountID);
//                    Try to get response body
                        BufferedReader reader = null;
                        StringBuilder sb = new StringBuilder();
                        try {

                            reader = new BufferedReader(new InputStreamReader(response.getBody().in()));

                            String line;

                            try {
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        String result = sb.toString();
                        Log.d("JSON", result);

//                    Log.d("accountID", userResponse.getAccount_id());
//                    Log.d("regCode", userResponse.getRegCode());
                        //probably need to add a case here if there are 0 products
//                    if (productResponse != null && productResponse.getResults() != null &&
//                            productResponse.getResults().getProducts() != null &&
//                            productResponse.getResults().getProducts().getProductList() != null) {
//                        loadProducts(productResponse.getResults().getProducts().getProductList());
//                    } else {
//                        Log.d("error", "could not retrieve product list");
//                    }
                        System.out.println(response.toString());

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("SEND_GIFT", "Error: " + error);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gift, menu);
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
    }
}

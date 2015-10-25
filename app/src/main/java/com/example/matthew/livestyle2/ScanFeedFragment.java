package com.example.matthew.livestyle2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFeedFragment newInstance(String param1, String param2) {
        ScanFeedFragment fragment = new ScanFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ScanFeedFragment() {
        // Required empty public constructor
    }

    String[][] data = {
            {"A","20m","Nike","$1",Integer.toString(R.drawable.placeholder),"http://www.google.com"},
            {"B","21m","Nikee","$2",Integer.toString(R.drawable.placeholder),"http://www.google.com"}};

    View root_view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root_view = inflater.inflate(R.layout.fragment_scan_feed, container, false);

        final FloatingActionButton scanBarcodeButton = (FloatingActionButton) root_view.findViewById(R.id.scan_barcode);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onScanBarcodeButtonPressed();
            }
        });

        final LayoutInflater layout_inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout feed = (LinearLayout) root_view.findViewById(R.id.scan_feed_content);

        for (int entry = 0; entry < data.length; entry++) {
            LinearLayout feed_item = (LinearLayout) layout_inflater.inflate(R.layout.feed_item, null);
            ((TextView) feed_item.findViewById(R.id.feed_item_name)).setText(data[entry][0]);
            ((TextView) feed_item.findViewById(R.id.feed_item_time_ago)).setText(data[entry][1]);
            ((TextView) feed_item.findViewById(R.id.feed_item_brand)).setText(data[entry][2]);
            ((TextView) feed_item.findViewById(R.id.feed_item_price)).setText(data[entry][3]);

            ImageView pic = (ImageView) feed_item.findViewById(R.id.feed_item_image);
            pic.setImageDrawable(getResources().getDrawable(Integer.valueOf(data[entry][4]), getContext().getTheme()));

            final String url = (!data[entry][5].startsWith("http://") && !data[entry][5].startsWith("https://")) ?
                "http://" + data[entry][5] : data[entry][5];

            pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            });

            feed.addView(feed_item);
        }

        /*Pubnub pubnub = new Pubnub("pub-c-2c119045-b31f-4ada-85e9-060cd47b7c27",
                "sub-c-d33774fa-3311-11e5-a033-0619f8945a4f");

        try {
            pubnub.subscribe("feed", new com.pubnub.api.Callback() {
                public void successCallback(String channel, Object message) {
                    System.out.println(message);
                    JSONObject messageJSON = (JSONObject) message;
                    try{
                        final String receivedImage = messageJSON.getString("IMAGE");
                        final String name = messageJSON.getString("NAME");
                        final String price = messageJSON.getString("PRICE");

                        final LayoutInflater layout_inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final LinearLayout feed = (LinearLayout) root_view.findViewById(R.id.scan_feed_content);

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
                            Log.e("Malformed",e.getMessage());
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

                        /*feed.addView(feed_item);

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
        }*/

        return root_view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onScanBarcodeButtonPressed() {
        if (mListener != null) {
            mListener.onScanBarcodeButtonPressed();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onScanBarcodeButtonPressed();
    }



}

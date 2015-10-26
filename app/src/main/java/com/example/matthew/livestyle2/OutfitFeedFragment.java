package com.example.matthew.livestyle2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OutfitFeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OutfitFeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OutfitFeedFragment extends Fragment {
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
     * @return A new instance of fragment OutfitFeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OutfitFeedFragment newInstance(String param1, String param2) {
        OutfitFeedFragment fragment = new OutfitFeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OutfitFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public static final int RESULT_OK = -1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                System.out.println("DONE");
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(
                        selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                Global.img = BitmapFactory.decodeFile(filePath);
                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);

                onAddOutfitButtonPressed(yourSelectedImage);
            }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_outfit_feed, container, false);

        final FloatingActionButton scanBarcodeButton = (FloatingActionButton) v.findViewById(R.id.add_outfit);
        scanBarcodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
                //onAddOutfitButtonPressed();
            }
        });

        final LayoutInflater layout_inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final LinearLayout feed = (LinearLayout) v.findViewById(R.id.outfit_feed_content);

        String[][] data = {
                {"Kevin Yang","","30m","",Integer.toString(R.drawable.outfit2)},
                {"Matthew Zhu","","3d","",Integer.toString(R.drawable.outfit2)}};

        for (int entry = 0; entry < data.length; entry++) {
            LinearLayout feed_item = (LinearLayout) layout_inflater.inflate(R.layout.feed_item, null);
            ((TextView) feed_item.findViewById(R.id.feed_item_name)).setText(data[entry][0]);
            ((TextView) feed_item.findViewById(R.id.feed_item_time_ago)).setText(data[entry][1]);
            ((TextView) feed_item.findViewById(R.id.feed_item_brand)).setText(data[entry][2]);
            ((TextView) feed_item.findViewById(R.id.feed_item_price)).setText(data[entry][3]);
            ((ImageView) feed_item.findViewById(R.id.feed_item_image)).setImageDrawable(getResources().getDrawable(Integer.valueOf(data[entry][4]), getContext().getTheme()));
            feed.addView(feed_item);
        }

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onAddOutfitButtonPressed(Bitmap b) {
        if (mListener != null) {
            mListener.onAddOutfitButtonPressed(b);
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
        public void onAddOutfitButtonPressed(Bitmap b);
    }

}

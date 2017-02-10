package com.shopclues.com.smartmessages;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.*;


public class SettingsFragment extends Fragment {
    Activity activity;
    ListView categoryListView;
    List categories = new ArrayList();

    SharedPreferences sharedPreference;


    private OnFragmentInteractionListener mListener;
    public static ArrayList<String> category_list;
    public static ArrayList<String> from_list;
    private LinearLayout l1;
    private LinearLayout l2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container,
                false);
        findViewsById(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void findViewsById(View view) {
        l1 = (LinearLayout) view.findViewById(R.id.l1);
        l2 = (LinearLayout) view.findViewById(R.id.l2);

       /* for(int i=0; i<3; i++){
            category_list.add("List One "+(i+1));
        }

        ArrayList<String> dnasjdna1 = new ArrayList<String>();
        for(int i=0; i<3; i++){
            dnasjdna1.add("List Two "+(i+1));
        }*/

        setFirstList();

        setSecondList();

    }

    private void setFirstList(){
        for(int i=0; i<category_list.size(); i++){
            l1.removeAllViews();
            TextView textView = new TextView(getActivity());
            textView.setText(category_list.get(i));
            l1.addView(textView);
        }
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        final EditText editText = new EditText(getActivity());
        linearLayout.addView(editText);

        Button btn = new Button(getActivity());
        linearLayout.addView(btn);
        l1.addView(linearLayout);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category_list.add(editText.getText().toString().trim());
                setFirstList();
            }
        });
    }

    private void setSecondList() {
        for(int i=0; i<from_list.size(); i++){
            l2.removeAllViews();
            TextView textView = new TextView(getActivity());
            textView.setText(from_list.get(i));
            l2.addView(textView);
        }

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        final EditText editText_one = new EditText(getActivity());
        linearLayout.addView(editText_one);

        Button btn_one = new Button(getActivity());
        linearLayout.addView(btn_one);
        l2.addView(linearLayout);

        btn_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from_list.add(editText_one.getText().toString().trim());
                setSecondList();
            }
        });


    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package com.shopclues.com.smartmessages;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String[] from_sms;
    private String[] bucket_names;
    public ArrayList<Map<String, String>> listOfSms;
    public ArrayList<Map<String, String>> listOfBuckets;
    private BucketListArrayAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        from_sms = getResources().getStringArray(R.array.from_sms);
        bucket_names = getResources().getStringArray(R.array.buckets);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        listOfBuckets = new ArrayList<Map<String, String>>();
        for (int j = 0; j < bucket_names.length; j++) {
            Map<String, String> tempBucket = new HashMap<>();
            tempBucket.put("name", bucket_names[j]);
            tempBucket.put("count", "0");
            listOfBuckets.add(tempBucket);
        }
        Log.d("SM/lb", listOfBuckets.toString());

        read_all_sms();
        bucketize_sms_from_api();

    }

    private void bucketize_sms_from_api() {

        try {
            JSONObject mainObj = new JSONObject();
            mainObj.put("uid", 12111111);
            mainObj.put("predict", "True");
        } catch (JSONException e) {

        }


        JSONArray dataArray = new JSONArray();

        for (int i = 0; i < listOfSms.size(); i++) {
            JSONObject tempSms = new JSONObject();
            tempSms.put()
        }


        for (int i = 0; i < listOfSms.size(); i++) {
            Map<String, String> tempSms = new HashMap<>();
            tempSms.put("from", listOfSms.get(i).get("from"));
            tempSms.put("body", listOfSms.get(i).get("body"));
            tempSms.put("time", listOfSms.get(i).get("time"));
            tempSms.put("bucketId", String.valueOf(2));
            listOfSms.set(i, tempSms);



            Map<String, String> tempBucket = new HashMap<>();
            tempBucket.put("name", listOfBuckets.get(2).get("name"));
            tempBucket.put("count", String.valueOf(Integer.parseInt(listOfBuckets.get(2).get("count")) + 1));
            listOfBuckets.set(2, tempBucket);

        }
        Log.d("SM/lb", listOfSms.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_BUCKETS_LIST = "temp_buckets_list";
        private static final String ARG_SMS_LIST = "temp_sms_list";
        private static BucketListArrayAdapter bucketItemsAdapter;
        private static SmsListArrayAdapter itemsAdapter;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, ArrayList<Map<String, String>> tempListOfSms, ArrayList<Map<String, String>> tempListOfBuckets) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable(ARG_SMS_LIST, tempListOfSms);
            Log.d("QWERTY", tempListOfBuckets.toString());
            args.putSerializable(ARG_BUCKETS_LIST, tempListOfBuckets);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 0:
                    return createBucketsView(inflater, container, savedInstanceState);
                case 1:
                    return createSmsView(inflater, container, savedInstanceState);
            }

            // if not both
            View rootView = inflater.inflate(R.layout.fragment_show, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        private View createBucketsView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
            bucketItemsAdapter = new BucketListArrayAdapter(this.getContext(), (ArrayList<Map<String, String>>)getArguments().getSerializable(ARG_BUCKETS_LIST));
            View rootView = inflater.inflate(R.layout.buckets_tab, container, false);
            RecyclerView listView = (RecyclerView) rootView.findViewById(R.id.buckets_list_view);
            listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            listView.setHasFixedSize(true);
            listView.setAdapter(bucketItemsAdapter);
            return rootView;
        }


        private View createSmsView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
            itemsAdapter = new SmsListArrayAdapter(this.getContext(), (ArrayList<Map<String, String>>)getArguments().getSerializable(ARG_SMS_LIST));
            View rootView = inflater.inflate(R.layout.sms_tab, container, false);
            RecyclerView listView = (RecyclerView) rootView.findViewById(R.id.sms_list_view);
            listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            listView.setHasFixedSize(true);
            listView.setAdapter(itemsAdapter);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position, listOfSms, listOfBuckets);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Buckets";
                case 1:
                    return "SMS";
            }
            return null;
        }
    }

    private void read_all_sms() {
        final String SMS_URI_INBOX = "content://sms/inbox";
        listOfSms = new ArrayList<Map<String, String>>();
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[] { "_id", "address", "body", "date" };
            String searchString = "read = 0 AND address IN (";
            for( int i = 0; i <= from_sms.length - 1; i++)
            {
                if (i == from_sms.length - 1) {
                    searchString += "'" + from_sms[i] + "')";
                } else {
                    searchString += "'" + from_sms[i] + "', ";
                }
            }
            Log.d("SM/searchString", searchString);
            Cursor cur = getContentResolver().query(uri, projection, searchString, null, "date desc");
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                do {
                    String strAddress = cur.getString(index_Address);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);

                    Map tempSms = new HashMap();
                    tempSms.put("from", strAddress);
                    tempSms.put("body", strbody);
                    tempSms.put("time", String.valueOf(longDate));
//                    tempSms.put("key", (strAddress + strbody).hashCode());

                    listOfSms.add(tempSms);

                } while (cur.moveToNext());

                Log.d("SM/SizeMsgs", String.valueOf(listOfSms.size()));

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (111) : {
                if (resultCode == RESULT_OK) {
                    Integer bucketId = data.getIntExtra("bucketId", 0);
                    Integer smsPosition = data.getIntExtra("smsPosition", 0);
                    Integer oldBucketId = data.getIntExtra("old_bucket_id", 0);

                    Log.d("SH/asdf", String.valueOf(bucketId)+String.valueOf(smsPosition)+String.valueOf(oldBucketId));

                    Map<String, String> tempSms = new HashMap<>();
                    tempSms.put("from", listOfSms.get(smsPosition).get("from"));
                    tempSms.put("body", listOfSms.get(smsPosition).get("body"));
                    tempSms.put("time", listOfSms.get(smsPosition).get("time"));
                    tempSms.put("bucketId", String.valueOf(bucketId));
                    listOfSms.set(smsPosition, tempSms);

                    Map<String, String> tempBucket = new HashMap<>();
                    tempBucket.put("name", listOfBuckets.get(oldBucketId).get("name"));
                    tempBucket.put("count", String.valueOf(Integer.parseInt(listOfBuckets.get(oldBucketId).get("count")) + 1));
                    listOfBuckets.set(oldBucketId, tempBucket);


                }
                break;
            }
        }
    }

}

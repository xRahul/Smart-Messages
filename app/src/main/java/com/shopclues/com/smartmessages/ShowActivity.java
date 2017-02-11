package com.shopclues.com.smartmessages;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowActivity extends AppCompatActivity {

    private String[] from_sms;
    public ArrayList<Map<String, String>> listOfSms;
    public ArrayList<Map<String, String>> listOfBuckets;
    public JSONObject jsonPredictData;
    public JSONObject jsonTrainData;
    private String android_id;
    private Boolean predicted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        from_sms = getResources().getStringArray(R.array.from_sms);
        String[] bucket_names = getResources().getStringArray(R.array.buckets);
        android_id = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        listOfBuckets = new ArrayList<>();
        for (String bucket_name : bucket_names) {
            Map<String, String> tempBucket = new HashMap<>();
            tempBucket.put("name", bucket_name);
            tempBucket.put("count", "0");
            listOfBuckets.add(tempBucket);
        }
        Log.d("SM/lb", listOfBuckets.toString());

        read_all_sms();
        bucketize_sms_from_api();

    }

    private void bucketize_sms_from_api()  {

        createPredictData();

        updateSmsListFromPredictions();

        if (predicted == false) {
            for (int i = 0; i < listOfSms.size(); i++) {
                Map<String, String> tempSms = new HashMap<>();
                tempSms.put("from", listOfSms.get(i).get("from"));
                tempSms.put("body", listOfSms.get(i).get("body"));
                tempSms.put("time", listOfSms.get(i).get("time"));
                tempSms.put("bucketId", String.valueOf(0));
                listOfSms.set(i, tempSms);


                Map<String, String> tempBucket = new HashMap<>();
                tempBucket.put("name", listOfBuckets.get(0).get("name"));
                tempBucket.put("count", String.valueOf(Integer.parseInt(listOfBuckets.get(0).get("count")) + 1));
                listOfBuckets.set(0, tempBucket);

            }
        }
        Log.d("SM/lb", listOfSms.toString());
    }

    private void updateSmsListFromPredictions() {
        new HttpAsyncTask1().execute("http://fqa10.shopclues.com/tools/Smart-Messages/SmartMessages.php");
    }

    private void hitTrainApi() {
        new HttpAsyncTask2().execute("http://fqa10.shopclues.com/tools/Smart-Messages/SmartMessages.php");
    }


    public String POST(String urlStr, JSONObject data) {
        try {
            BufferedReader br;
            URL url = new URL(urlStr); //Enter URL here
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(data.toString());
            wr.flush();
            wr.close();

            if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
            } else {
                br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
            }

            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }
            Log.d("qwqwqwqw", sb.toString());
            Log.d("asasasas", android_id);
            Log.d("mmmmmmmm", data.toString());

            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                    ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(mViewPager);
                }
            });

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public class HttpAsyncTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            predicted = false;
            return POST(urls[0],jsonPredictData);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(getBaseContext(), "prediction failed!", Toast.LENGTH_LONG).show();
            } else {
                try {
                    JSONArray predict = new JSONArray(result);
                    for (int i = 0; i < listOfSms.size(); i++)
                    {
                        Map<String, String> tempSms = new HashMap<>();
                        tempSms.put("from", listOfSms.get(i).get("from"));
                        tempSms.put("body", listOfSms.get(i).get("body"));
                        tempSms.put("time", listOfSms.get(i).get("time"));
                        tempSms.put("bucketId", String.valueOf(predict.get(i)));
                        listOfSms.set(i, tempSms);

                        Map<String, String> tempBucket = new HashMap<>();
                        tempBucket.put("name", listOfBuckets.get(predict.getInt(i)).get("name"));
                        tempBucket.put("count", String.valueOf(Integer.parseInt(listOfBuckets.get(predict.getInt(i)).get("count")) + 1));
                        listOfBuckets.set(predict.getInt(i), tempBucket);

                        tempBucket = new HashMap<>();
                        tempBucket.put("name", listOfBuckets.get(0).get("name"));
                        tempBucket.put("count", String.valueOf(Integer.parseInt(listOfBuckets.get(0).get("count")) - 1));
                        listOfBuckets.set(0, tempBucket);

                        predicted = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class HttpAsyncTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return POST(urls[0],jsonTrainData);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(getBaseContext(), "train failed!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), "train success!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void createPredictData() {
        try {
            JSONObject mainObj = new JSONObject();
            mainObj.put("uid", android_id);
            mainObj.put("predict", "True");

            JSONArray dataArray = new JSONArray();

            for (int i = 0; i < listOfSms.size(); i++) {
                JSONObject tempSms = new JSONObject();
                tempSms.put("msg_from", listOfSms.get(i).get("from"));
                tempSms.put("msg", listOfSms.get(i).get("body"));
                dataArray.put(tempSms);
            }

            mainObj.put("data", dataArray);
            jsonPredictData = mainObj;

        } catch (JSONException e) {
            // empty
        }
    }

    private void createTrainData(Map<String, String> trainMap) {
        try {
            JSONObject mainObj = new JSONObject();
            mainObj.put("uid", android_id);
            mainObj.put("predict", "False");

            JSONArray dataArray = new JSONArray();

            JSONObject tempSms = new JSONObject();
            tempSms.put("msg_from", trainMap.get("from"));
            tempSms.put("msg", trainMap.get("body"));
            tempSms.put("bucket_id", trainMap.get("bucketId"));
            dataArray.put(tempSms);

            mainObj.put("data", dataArray);
            jsonTrainData = mainObj;

        } catch (JSONException e) {
            // empty
        }
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
                    return createBucketsView(inflater, container);
                case 1:
                    return createSmsView(inflater, container);
            }

            // if not both
            View rootView = inflater.inflate(R.layout.fragment_show, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        private View createBucketsView(LayoutInflater inflater, ViewGroup container) {
            BucketListArrayAdapter bucketItemsAdapter = new BucketListArrayAdapter(this.getContext(), (ArrayList<Map<String, String>>)getArguments().getSerializable(ARG_BUCKETS_LIST));
            View rootView = inflater.inflate(R.layout.buckets_tab, container, false);
            RecyclerView listView = (RecyclerView) rootView.findViewById(R.id.buckets_list_view);
            listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            listView.setHasFixedSize(true);
            listView.setAdapter(bucketItemsAdapter);
            return rootView;
        }


        private View createSmsView(LayoutInflater inflater, ViewGroup container) {
            SmsListArrayAdapter smsListAdapter = new SmsListArrayAdapter(this.getContext(), (ArrayList<Map<String, String>>)getArguments().getSerializable(ARG_SMS_LIST));
            View rootView = inflater.inflate(R.layout.sms_tab, container, false);
            RecyclerView listView = (RecyclerView) rootView.findViewById(R.id.sms_list_view);
            listView.setLayoutManager(new LinearLayoutManager(this.getContext()));
            listView.setHasFixedSize(true);
            listView.setAdapter(smsListAdapter);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
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
        listOfSms = new ArrayList<>();
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

                    createTrainData(tempSms);
                    hitTrainApi();

                    Map<String, String> tempBucket = new HashMap<>();
                    tempBucket.put("name", listOfBuckets.get(bucketId).get("name"));
                    tempBucket.put("count", String.valueOf(Integer.parseInt(listOfBuckets.get(bucketId).get("count")) + 1));
                    listOfBuckets.set(bucketId, tempBucket);
                    tempBucket = new HashMap<>();
                    tempBucket.put("name", listOfBuckets.get(oldBucketId).get("name"));
                    tempBucket.put("count", String.valueOf(Integer.parseInt(listOfBuckets.get(oldBucketId).get("count")) - 1));
                    listOfBuckets.set(oldBucketId, tempBucket);


                }
                break;
            }
        }
    }

}

package com.shopclues.com.smartmessages;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String[] from_sms;
    private List<Map<String, String>> listOfMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        from_sms = getResources().getStringArray(R.array.from_sms);

        read_all_sms();
    }

    private void read_all_sms() {
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_ALL = "content://sms/";
        listOfMaps = new ArrayList<Map<String, String>>();
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            String searchString = "read = 0 AND address IN (";
            for( int i = 0; i <= from_sms.length - 1; i++)
            {
                if (i == from_sms.length - 1) {
                    searchString += "'" + from_sms[i] + "')";
                } else {
                    searchString += "'" + from_sms[i] + "', ";
                }
            }
            Log.d("SM", searchString);
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
                    tempSms.put("date", longDate);

                    listOfMaps.add(tempSms);

                } while (cur.moveToNext());

                Log.d("SM", String.valueOf(listOfMaps.size()) + listOfMaps.toString());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {

            } // end if
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }
    }
}

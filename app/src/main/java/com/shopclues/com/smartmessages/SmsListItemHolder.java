package com.shopclues.com.smartmessages;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;


class SmsListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView smsBodyTextView;
    private final TextView smsFromTextView;
    private final TextView smsTimeTextView;
    private final Button smsBucketButton;

    private Map<String, String> sms;
    private Context context;
    private String[] bucket_names;

    SmsListItemHolder(Context contextTemp, View itemView) {
        super(itemView);

        // 1. Set the context
        context = contextTemp;

        // 2. Set up the UI widgets of the holder
        smsBodyTextView = (TextView) itemView.findViewById(R.id.sms_body_textview);
        smsFromTextView = (TextView) itemView.findViewById(R.id.sms_from_textview);
        smsTimeTextView = (TextView) itemView.findViewById(R.id.sms_time_textview);
        smsBucketButton = (Button) itemView.findViewById(R.id.bucket_button);
        bucket_names = context.getResources().getStringArray(R.array.buckets);

        // 3. Set the "onClick" listener of the holder
        itemView.setOnClickListener(this);
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    void bindSms(Map<String, String> smsTemp) {
        sms = smsTemp;
        // 4. Bind the data to the ViewHolder
        smsBodyTextView.setText(sms.get("body"));
        smsFromTextView.setText(sms.get("from"));
        smsTimeTextView.setText(getDate(Long.parseLong(sms.get("time")), "dd/MM/yyyy hh:mm:ss a"));
        smsBucketButton.setText(bucket_names[Integer.parseInt(sms.get("bucketId"))] + " - " + "Change Bucket");

        if (bucket_names[Integer.parseInt(sms.get("bucketId"))] == "Critical") {
            smsBucketButton.setBackgroundResource(R.color.red);
        }
        else if (bucket_names[Integer.parseInt(sms.get("bucketId"))] == "Info") {
            smsBucketButton.setBackgroundResource(R.color.blue);
        }
        else if (bucket_names[Integer.parseInt(sms.get("bucketId"))] == "Debug") {
            smsBucketButton.setBackgroundResource(R.color.green);
        }
        else if (bucket_names[Integer.parseInt(sms.get("bucketId"))] == "Personal") {
            smsBucketButton.setBackgroundResource(R.color.yellow);
        }

        if (bucket_names[Integer.parseInt(sms.get("bucketId"))].equals("Critical"))
        {
            smsBucketButton.setBackgroundColor(Color.RED);
        }
        else if (bucket_names[Integer.parseInt(sms.get("bucketId"))].equals("None"))
        {
            smsBucketButton.setBackgroundColor(Color.GRAY);
        }
        else if (bucket_names[Integer.parseInt(sms.get("bucketId"))].equals("Info"))
        {
            smsBucketButton.setBackgroundColor(Color.GREEN);
        }
        else if (bucket_names[Integer.parseInt(sms.get("bucketId"))].equals("Debug"))
        {
            smsBucketButton.setBackgroundColor(Color.BLUE);
        }
        else if (bucket_names[Integer.parseInt(sms.get("bucketId"))].equals("Error"))
        {
            smsBucketButton.setBackgroundColor(Color.YELLOW);
        }

        smsBucketButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == smsBucketButton.getId()){
            Intent i = new Intent(context, ChooseBucketActivity.class);
            i.putExtra("sms_position", getAdapterPosition());
            i.putExtra("old_bucket_id", Integer.parseInt(sms.get("bucketId")));
            ((AppCompatActivity) context).startActivityForResult(i, 111);
            Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
    }

}

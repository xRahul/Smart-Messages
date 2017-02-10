package com.shopclues.com.smartmessages;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;


class SmsListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView smsBodyTextView;
    private final TextView smsFromTextView;
    private final TextView smsTimeTextView;

    private Map<String, String> sms;
    private Context context;

    SmsListItemHolder(Context contextTemp, View itemView) {
        super(itemView);

        // 1. Set the context
        context = contextTemp;

        // 2. Set up the UI widgets of the holder
        smsBodyTextView = (TextView) itemView.findViewById(R.id.sms_body_textview);
        smsFromTextView = (TextView) itemView.findViewById(R.id.sms_from_textview);
        smsTimeTextView = (TextView) itemView.findViewById(R.id.sms_time_textview);

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
        this.smsBodyTextView.setText(sms.get("body"));
        this.smsFromTextView.setText(sms.get("from"));
        this.smsTimeTextView.setText(getDate(Long.parseLong(sms.get("time")), "dd/MM/yyyy hh:mm:ss a"));
//        this.smsTimeTextView.setText(sms.get("time"));
    }

    @Override
    public void onClick(View v) {
        // 5. Handle the onClick event for the ViewHolder
//        if (this.sms != null) {
//            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("sms", this.sms.get("body"));
//            clipboard.setPrimaryClip(clip);
//            Toast.makeText(this.context, "Copied Sms- \n" + this.sms.get("body"), Toast.LENGTH_SHORT ).show();
//        }
    }
}

package com.shopclues.com.smartmessages;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;


class BucketListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView bucketNameTextView;
    private final TextView bucketCountTextView;

    private Map<String, String> bucket;
    private Context context;

    BucketListItemHolder(Context contextTemp, View itemView) {
        super(itemView);

        // 1. Set the context
        context = contextTemp;

        // 2. Set up the UI widgets of the holder
        bucketNameTextView = (TextView) itemView.findViewById(R.id.bucket_name_textview);
        bucketCountTextView = (TextView) itemView.findViewById(R.id.bucket_count_textview);

        // 3. Set the "onClick" listener of the holder
        itemView.setOnClickListener(this);
    }


    void bindBucket(Map<String, String> bucketTemp) {
        bucket = bucketTemp;
        Log.d("asdfasf", bucket.toString());
        Log.d("asdfasf", bucketNameTextView.toString());
        // 4. Bind the data to the ViewHolder
        bucketNameTextView.setText(bucket.get("name"));
        bucketCountTextView.setText(bucket.get("count"));
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

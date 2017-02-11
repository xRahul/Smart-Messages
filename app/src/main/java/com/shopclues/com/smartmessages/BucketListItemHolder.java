package com.shopclues.com.smartmessages;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;


class BucketListItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final TextView bucketNameTextView;
    private final TextView bucketCountTextView;
    private Map<String, String> bucket;
    private Context context;
    private final RelativeLayout bucket_list_view_parent;
    BucketListItemHolder(Context contextTemp, View itemView) {
        super(itemView);

        // 1. Set the context
        context = contextTemp;

        // 2. Set up the UI widgets of the holder
        bucketNameTextView = (TextView) itemView.findViewById(R.id.bucket_name_textview);
        bucketCountTextView = (TextView) itemView.findViewById(R.id.bucket_count_textview);
        bucket_list_view_parent = (RelativeLayout) itemView.findViewById(R.id.bucket_list_parent);

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

        if (bucket.get("name").equals("Critical"))
        {
            bucket_list_view_parent.setBackgroundColor(Color.RED);
        }
        else if (bucket.get("name").equals("None"))
        {
            bucket_list_view_parent.setBackgroundColor(Color.GRAY);
        }
        else if (bucket.get("name").equals("Info"))
        {
            bucket_list_view_parent.setBackgroundColor(Color.GREEN);
        }
        else if (bucket.get("name").equals("Debug"))
        {
            bucket_list_view_parent.setBackgroundColor(Color.BLUE);
        }
        else if (bucket.get("name").equals("Error"))
        {
            bucket_list_view_parent.setBackgroundColor(Color.YELLOW);
        }

    }

    @Override
    public void onClick(View v) {
        // 5. Handle the onClick event for the ViewHolder
//        if (Integer.parseInt(bucket.get("count")_ != 0) {
//            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("sms", this.sms.get("body"));
//            clipboard.setPrimaryClip(clip);
//            Toast.makeText(this.context, "Copied Sms- \n" + this.sms.get("body"), Toast.LENGTH_SHORT ).show();
//        }
    }
}

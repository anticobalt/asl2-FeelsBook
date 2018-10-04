/* Derived from user2450263's answer on StackOverflow [https://stackoverflow.com/a/21833388]
        which ultimately came from Nithya Vasudevan's tutorial on
        [http://theopentutorials.com/tutorials/android/listview/
        android-custom-listview-with-image-and-text-using-arrayadapter/]
* */

package com.cmput301.assignment1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class LogsAdapter extends ArrayAdapter<Log> {

    private Context context;

    public LogsAdapter(Context context, int log_id, ArrayList<Log> items) {
        super(context, log_id, items);
        this.context = context;
    }

    private class ViewHolder {
        ImageView emoji;
        TextView message;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Log rowItem = getItem(position);

        // Create view and bind image and text
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.log, null);
            holder = new ViewHolder();
            holder.message = convertView.findViewById(R.id.message);
            holder.emoji = convertView.findViewById(R.id.emoji);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set message and emoji
        holder.message.setText(rowItem.getDateTimeAsString());
        holder.emoji.setImageResource(rowItem.getEmotion().getEmojiReference());

        return convertView;
    }

}

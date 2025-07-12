package com.example.bepnhataapp.features.message;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bepnhataapp.R;
import com.example.bepnhataapp.common.model.NotificationItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends ArrayAdapter<NotificationItem> {
    private final LayoutInflater inflater;
    private final SimpleDateFormat sdfToday = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat sdfOther = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());

    public NotificationAdapter(@NonNull Context ctx, @NonNull List<NotificationItem> data){
        super(ctx, 0, data);
        inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        if(convertView==null){
            convertView = inflater.inflate(R.layout.item_notification, parent, false);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }
        NotificationItem it = getItem(position);
        if(it!=null){
            // icon mapping
            int res = R.drawable.ic_truck;
            if(it.getStatus()!=null){
                switch(it.getStatus()){
                    case "WAIT_CONFIRM": res=R.drawable.baseline_access_time_filled_24; break;
                    case "WAIT_PICKUP": res=R.drawable.ic_box; break;
                    case "OUT_FOR_DELIVERY": res=R.drawable.ic_truck; break;
                    case "DELIVERED": res=R.drawable.ic_check_orange; break;
                    case "CANCELED": res=R.drawable.ic_cancel; break;
                    case "RETURNED": res=R.drawable.ic_refund; break;
                }
            }
            if(vh.icon!=null) vh.icon.setImageResource(res);
            vh.tvTitle.setText(it.getTitle());
            vh.tvBody.setText(it.getBody());
            String timeTxt;
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTimeInMillis(it.getTimestamp());
            java.util.Calendar now = java.util.Calendar.getInstance();
            boolean isToday = cal.get(java.util.Calendar.YEAR)==now.get(java.util.Calendar.YEAR) &&
                    cal.get(java.util.Calendar.DAY_OF_YEAR)==now.get(java.util.Calendar.DAY_OF_YEAR);
            timeTxt = (isToday?"Hôm nay ":"") + (isToday? sdfToday.format(new Date(it.getTimestamp())) : sdfOther.format(new Date(it.getTimestamp())));
            vh.tvTime.setText(timeTxt);
            vh.dot.setVisibility(it.isRead()?View.INVISIBLE:View.VISIBLE);
        }
        return convertView;
    }

    static class ViewHolder{
        ImageView icon;
        TextView tvTitle, tvBody, tvTime;
        View dot;
        ViewHolder(View v){
            icon = v.findViewById(R.id.imgIcon);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvBody = v.findViewById(R.id.tvBody);
            tvTime = v.findViewById(R.id.tvTime);
            dot = v.findViewById(R.id.viewUnreadDot);
        }
    }
} 

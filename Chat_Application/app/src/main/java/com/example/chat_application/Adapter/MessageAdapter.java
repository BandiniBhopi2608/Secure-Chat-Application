package com.example.chat_application.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chat_application.Model.Message;
import com.example.chat_application.Model.User;
import com.example.chat_application.R;

import java.util.List;

/**
 * Created by BANDINI on 02-05-2017.
 */

public class MessageAdapter extends ArrayAdapter<Message> {

    private int intReceiverID, intSenderID;

    public MessageAdapter(Context context, int resource, List<Message> objects, int intReceiverID, int intSenderID) {
        super(context, resource, objects);
        this.intReceiverID = intReceiverID;
        this.intSenderID = intSenderID;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_message, parent, false);
            holder = new Holder();
            holder.message = (TextView) view.findViewById(R.id.textview_message);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Message objMessage = getItem(position);
        holder.message.setText(objMessage.getMessage());

        if (objMessage.getFrom() != intSenderID) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.message.getLayoutParams());
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.message.setLayoutParams(params);
            Drawable d = holder.message.getBackground();
            d.setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC);
        } else {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(holder.message.getLayoutParams());
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.message.setLayoutParams(params);
            Drawable d = holder.message.getBackground();
            d.setColorFilter(Color.parseColor("#ff33b5e5"), PorterDuff.Mode.SRC);
        }

        return view;
    }

    class Holder {
        TextView message;
    }
}

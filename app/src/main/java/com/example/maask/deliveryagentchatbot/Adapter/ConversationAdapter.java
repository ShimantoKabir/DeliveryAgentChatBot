package com.example.maask.deliveryagentchatbot.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.maask.deliveryagentchatbot.PojoClass.Conversation;
import com.example.maask.deliveryagentchatbot.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Maask on 8/5/2018.
 */

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Conversation> conversationList;
    private Context context;
    public static final int USER_QUERY_LAYOUT = 1;
    public static final int BOT_RESPONSE_LAYOUT = 2;
    public int itemLastPosition = -1;

    public ConversationAdapter(ArrayList<Conversation> conversationList, Context context) {
        this.conversationList = conversationList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder = null;

        if (viewType == USER_QUERY_LAYOUT){

            View v = inflater.inflate(R.layout.user_query,parent,false);
            viewHolder = new ConversationAdapter.ConversationViewHolder(v);
            return viewHolder;

        }else {

            View v = inflater.inflate(R.layout.bot_response,parent,false);
            viewHolder = new ConversationAdapter.ConversationViewHolder(v);
            return viewHolder;

        }

    }

    @Override
    public int getItemViewType(int position) {
        if (conversationList.get(position).getSpeaker().equals("user")){
            return USER_QUERY_LAYOUT;
        }else {
            return BOT_RESPONSE_LAYOUT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ConversationViewHolder viewHolder = (ConversationViewHolder) holder;
        viewHolder.conversationTV.setText(conversationList.get(position).getExpression());
        setAnimation(holder.itemView,position);

    }

    @Override
    public int getItemCount() {
        return conversationList.size();
    }

    private class ConversationViewHolder extends RecyclerView.ViewHolder {

        TextView conversationTV;

        public ConversationViewHolder(View v) {
            super(v);
            conversationTV = v.findViewById(R.id.conversation_tv);
        }
    }

    public void instantDataChang(ArrayList<Conversation> conversationList) {
        this.conversationList = conversationList;
        notifyDataSetChanged();
    }

    public void setAnimation(View viewToAnimate, int position) {
        if (position > itemLastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(300);
            viewToAnimate.startAnimation(anim);
            itemLastPosition = position;
        }
    }

}

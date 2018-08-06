package com.example.maask.deliveryagentchatbot.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.maask.deliveryagentchatbot.PojoClass.Conversation;
import com.example.maask.deliveryagentchatbot.R;

import java.util.ArrayList;

/**
 * Created by Maask on 8/5/2018.
 */

public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Conversation> conversationList;
    private Context context;
    public static final int USER_QUERY_LAYOUT = 1;
    public static final int BOT_RESPONSE_LAYOUT = 2;

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
        if (conversationList.get(position).isMe){
            return USER_QUERY_LAYOUT;
        }else {
            return BOT_RESPONSE_LAYOUT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ConversationViewHolder viewHolder = (ConversationViewHolder) holder;
        viewHolder.conversationTV.setText(conversationList.get(position).getConversation());

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

}

package com.ntlodw002.mychatbot;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


/**
 * Created by ntlodw002 from https://android.jlelse.eu/building-chatbot-with-firebase-and-api-ai-5b787fe693a9
 */

public class ChatRecord extends RecyclerView.ViewHolder  {



    TextView leftText,rightText;

    public ChatRecord(View itemView){
        super(itemView);

        leftText = (TextView)itemView.findViewById(R.id.leftText);
        rightText = (TextView)itemView.findViewById(R.id.rightText);


    }
}

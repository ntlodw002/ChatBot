package com.ntlodw002.mychatbot;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

public class MainActivity extends AppCompatActivity implements AIListener{
    private AIService aiService;
    RecyclerView recyclerView;
    EditText editText;
    RelativeLayout addBtn;
    DatabaseReference ref;
    FirebaseRecyclerAdapter<Message,ChatRecord> adapter;
    Boolean flagFab = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        editText = (EditText)findViewById(R.id.editText);
        addBtn = (RelativeLayout)findViewById(R.id.addBtn);

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new    LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //firebase stuff
        ref = FirebaseDatabase.getInstance().getReference();
        ref.keepSynced(true);

        final AIConfiguration config = new AIConfiguration("Client access token", AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        //ai stuff
        aiService = AIService.getService(this, config);
        aiService.setListener(this);

        final AIDataService aiDataService = new AIDataService(config);

        final AIRequest aiRequest = new AIRequest();



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString().trim();

                if (!message.equals("")) {

                    //firebase stuff
                    Message chatMessage = new Message(message, "user");
                    ref.child("chat").push().setValue(chatMessage);

                    //ai stuff
                    aiRequest.setQuery(message);
                    new AsyncTask<AIRequest,Void,AIResponse>(){

                        @Override
                        protected AIResponse doInBackground(AIRequest... aiRequests) {
                            final AIRequest request = aiRequests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(AIResponse response) {
                            if (response != null) {

                                Result result = response.getResult();
                                String reply = result.getFulfillment().getSpeech();
                                Message chatMessage = new Message(reply, "bot");
                                ref.child("chat").push().setValue(chatMessage);
                            }
                        }
                    }.execute(aiRequest);


                } else {
                    aiService.startListening();
                }

                editText.setText("");

            }
        });


        adapter = new FirebaseRecyclerAdapter<Message, ChatRecord>(Message.class,R.layout.msglist,ChatRecord.class,ref.child("chat")) {
            @Override
            protected void populateViewHolder(ChatRecord viewHolder, Message model, int position) {

                if (model.getMsgUser().equals("user")) {


                    viewHolder.rightText.setText(model.getMsgText());

                    viewHolder.rightText.setVisibility(View.VISIBLE);
                    viewHolder.leftText.setVisibility(View.GONE);
                }
                else {
                    viewHolder.leftText.setText(model.getMsgText());

                    viewHolder.rightText.setVisibility(View.GONE);
                    viewHolder.leftText.setVisibility(View.VISIBLE);
                }
            }
        };


        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onResult(ai.api.model.AIResponse response) {


        Result result = response.getResult();

        String message = result.getResolvedQuery();
        Message chatMessage0 = new Message(message, "user");
        ref.child("chat").push().setValue(chatMessage0);


        String reply = result.getFulfillment().getSpeech();
        Message chatMessage = new Message(reply, "bot");
        ref.child("chat").push().setValue(chatMessage);


    }
    //implements AI stuff
    @Override
    public void onError(ai.api.model.AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }

}
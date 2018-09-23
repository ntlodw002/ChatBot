package com.ntlodw002.mychatbot;

/**
 * Created by ntlodw002 on 2018/09/23 and modified from https://android.jlelse.eu/building-chatbot-with-firebase-and-api-ai-5b787fe693a9
 */

public class Message {
    private String msgText;
    private String msgUser;



    public Message(String msgText, String msgUser){
        this.msgText = msgText;
        this.msgUser = msgUser;

    }


    public Message(){

    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getMsgUser() {
        return msgUser;
    }

    public void setMsgUser(String msgUser) {
        this.msgUser = msgUser;
    }
}

package com.ntlodw002.mychatbot;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ntlodw002 on 2018/09/24.
 */

public class FirebaseApp extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

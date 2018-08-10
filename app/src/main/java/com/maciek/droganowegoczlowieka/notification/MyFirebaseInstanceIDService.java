package com.maciek.droganowegoczlowieka.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Geezy on 10.08.2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private String TAG = "Registration";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "refreshed token: " + refreshedToken);
    }
}

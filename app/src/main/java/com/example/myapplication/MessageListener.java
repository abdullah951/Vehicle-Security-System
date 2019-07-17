package com.example.myapplication;


import android.content.Context;

public interface MessageListener {
    /**
     * To call this method when new message received and send back
     * @param message Message
     */
    void messageReceived(Context context, MessageBodyModel message);
}
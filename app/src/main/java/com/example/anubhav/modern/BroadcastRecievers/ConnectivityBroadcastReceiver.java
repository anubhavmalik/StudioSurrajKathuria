package com.example.anubhav.modern.BroadcastRecievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.anubhav.modern.Networking.NetworkConnection;

public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

    private ConnectivityReceiverListener mConnectivityReceiverListener;

    public ConnectivityBroadcastReceiver() {
    }

    public ConnectivityBroadcastReceiver(ConnectivityReceiverListener mConnectivityReceiverListener) {
        this.mConnectivityReceiverListener = mConnectivityReceiverListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mConnectivityReceiverListener != null && NetworkConnection.isConnected(context))
            mConnectivityReceiverListener.onNetworkConnectionConnected();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionConnected();
    }


}

package com.example.licznikv10;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.net.wifi.WifiManager;
import android.widget.Button;
import android.widget.EditText;

import java.time.format.TextStyle;

public class MainActivity extends AppCompatActivity {


    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntent;
    Button wifi, connect;
    EditText p2p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

    }

    public void connect(View view) {

    }

    public void wifiOnOff(View view) {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            wifi.setText("Wi-fi off");
            wifi.setTextColor(0xFFDD0A57);
        } else {
            wifiManager.setWifiEnabled(true);
            wifi.setText("Wi-fi on");
            wifi.setTextColor(0xFF00574B);
        }

    }

    public void initialize() {
        wifi = findViewById(R.id.wifi);
        connect = findViewById(R.id.connect);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        mIntent = new IntentFilter();
        mIntent.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntent.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntent.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntent.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        p2p = findViewById(R.id.P2Pcheck);
        p2p.setVisibility(View.INVISIBLE);
        if (wifiManager.isWifiEnabled()) {
            wifi.setText("Wi-fi on");
            wifi.setTextColor(0xFF00574B);
        } else {
            wifi.setText("Wi-fi off");
            wifi.setTextColor(0xFFDD0A57);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntent);

    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}

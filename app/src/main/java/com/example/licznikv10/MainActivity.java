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
import android.content.Intent;


import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity  {


    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntent;
    Button wifi, connect;
    EditText p2p;
    int rozmiar = 0;
    String currentDateTimeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

    }

    public void connect(View view) {

    }

    public void wifiOnOff(View view) {
       /* if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            wifi.setText("Wi-fi off");
            wifi.setTextColor(0xFFDD0A57);
        } else {
            wifiManager.setWifiEnabled(true);
            wifi.setText("Wi-fi on");
            wifi.setTextColor(0xFF00574B);
        }*/


        /* Dodawaniae kolejnych liczb naturalnych w celach testowych,
           docelowo gdy dane będą w postaci string czas, double pomiar będzie coś w stylu
           DisplayChart.axisValues.add(rozmiar, new AxisValue(rozmiar).setLabel(czas));
           DisplayChart.yAxisValues.add(rozmiar,new PointValue(rozmiar,pomiar));
        */
        currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        DisplayChart.axisValues.add(rozmiar,new AxisValue(rozmiar).setLabel(currentDateTimeString));
        DisplayChart.yAxisValues.add(rozmiar,new PointValue(rozmiar,rozmiar));
        rozmiar = rozmiar + 1;


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

    public void showChart(View view)
    {
        Intent intent = new Intent(this, DisplayChart.class);

        startActivity(intent);


    }
    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntent);

    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}

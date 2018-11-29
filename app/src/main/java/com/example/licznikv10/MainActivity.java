package com.example.licznikv10;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.net.wifi.WifiManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    WifiManager wifiManager;
    Button wifi, connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();

    }

    public void connect(View view)
    {

    }
    public void wifiOnOff (View view)
    {
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
            wifi.setText("Wi-fi off");
            wifi.setTextColor(0xFFDD0A57);
        }
        else
        {
            wifiManager.setWifiEnabled(true);
            wifi.setText("Wi-fi on");
            wifi.setTextColor(0xFF00574B);
        }

    }

    public void initialize()
    {
        wifi = findViewById(R.id.wifi);
        connect = findViewById(R.id.connect);
        wifiManager =  (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
        {
            wifi.setText("Wi-fi on");
            wifi.setTextColor(0xFF00574B);
        }
        else
        {
            wifi.setText("Wi-fi off");
            wifi.setTextColor(0xFFDD0A57);
        }

    }
}

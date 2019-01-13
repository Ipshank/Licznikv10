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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.widget.TextView;

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
    Button wykres, connect;
    EditText p2p;
    int rozmiar = 0;
    int rozmiar2 = 0;
    int rozmiar3 = 0;
    int rozmiar4 = 0;
    float value = 0;
    String IPn = "10.1.0.74";
    String currentDateTimeString;
    String path;
    private EditText radiation;
    private EditText impulses;
    private  EditText IPnum;
    ArrayList<String> list = new ArrayList<String>();
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        initialize();

    }

    public void connect(View view) {
        if (wifiManager.isWifiEnabled()) {

            p2p.setText("wi-fi is on");
            connect.setClickable(false);
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    getWebsite();
                }
            }, 0, 1, TimeUnit.SECONDS);

        } else {


            p2p.setText("turn on wi-fi");
            connect.setClickable(true);


        }
    }



    public void initialize() {

        connect = findViewById(R.id.connect);
        connect.setClickable(false);
        radiation = findViewById(R.id.editText3);
        impulses = findViewById(R.id.editText4);
        IPnum = findViewById(R.id.editText2);
        path = getApplicationContext().getFilesDir().getPath() + "/geiger1.txt";
        IPnum.setText(IPn);
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
        wykres = findViewById(R.id.button);
        wykres.setVisibility(View.INVISIBLE);
        loadState();
        wykres.setVisibility(View.VISIBLE);
        if (wifiManager.isWifiEnabled()) {
            p2p.setVisibility(View.VISIBLE);
            p2p.setText("wi-fi is on");
            connect.setClickable(false);
            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    getWebsite();
                }
            }, 0, 1, TimeUnit.SECONDS);

        } else {

            p2p.setVisibility(View.VISIBLE);
            p2p.setText("turn on wi-fi");
            connect.setClickable(true);


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


    private void getWebsite() {


            if (wifiManager.isWifiEnabled()) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final StringBuilder builder = new StringBuilder();
                        final StringBuilder myBuilder = new StringBuilder();
                        try{
                        Document doc = Jsoup.connect("http://" + IPnum.getText()).get();

                        Elements links = doc.getElementsByTag("p");


                        for (Element link : links) {
                            builder.append(link);
                        }
                        }catch (IOException e){}

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {


                                    String myOne = builder.toString();
                                    myOne = myOne.replace("<p>", "");
                                    myOne = myOne.replace("LEV:", "x");
                                    myOne = myOne.replace("IMP:", "");
                                    myOne = myOne.replace("</p>", "");
                                    myOne = myOne.replace("S:", "x");
                                    myOne = myOne.replace("uS/h", "");
                                    myOne = myOne.replace(" ", "");
                                    String[] myOne2 = myOne.split("x");
                                    String radiation1 = myOne2[2];
                                    String impulses1 = myOne2[0];
                                    radiation.setText(radiation1);
                                    impulses.setText(impulses1);
                                    float radiationf = Float.parseFloat(radiation1);
                                    currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
                                    DisplayChart.axisValues.add(rozmiar, new AxisValue(rozmiar).setLabel(currentDateTimeString));
                                    DisplayChart.yAxisValues.add(rozmiar, new PointValue(rozmiar, radiationf));
                                    rozmiar = rozmiar + 1;
                                    if(rozmiar%5 == 0)
                                    {
                                        DisplayChart.axisValues2.add(rozmiar2,new AxisValue(rozmiar).setLabel(currentDateTimeString));
                                        DisplayChart.yAxisValues2.add(rozmiar2,new PointValue(rozmiar2,radiationf));
                                        rozmiar2 = rozmiar2 + 1;
                                    }
                                    if(rozmiar%15==0)
                                    {
                                        DisplayChart.axisValues3.add(rozmiar3,new AxisValue(rozmiar).setLabel(currentDateTimeString));
                                        DisplayChart.yAxisValues3.add(rozmiar3,new PointValue(rozmiar3,radiationf));
                                        rozmiar3 = rozmiar3 + 1;
                                    }
                                    if(rozmiar%60==0)
                                    {
                                        DisplayChart.axisValues4.add(rozmiar4,new AxisValue(rozmiar).setLabel(currentDateTimeString));
                                        DisplayChart.yAxisValues4.add(rozmiar4,new PointValue(rozmiar4,radiationf));
                                        rozmiar4 = rozmiar4 + 1;
                                    }
                                    myBuilder.append(radiation1).append("x").append(currentDateTimeString);
                                    String temp = myBuilder.toString();
                                    list.add(temp);
                                    p2p.setText("OK");
                                }catch (IndexOutOfBoundsException e)
                                {
                                    p2p.setText("connection error");
                                }

                            }
                        });
                    }
                }).start();
            } else {


                p2p.setText("turn on wifi");
            }

    }
    public void save(View view)
    {
        FileOutputStream outStream = null;
        try {
            File f = new File( path);
            if(f.exists())
            f.delete();
            outStream = new FileOutputStream(f);
            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);


            objectOutStream.writeObject(list);
            objectOutStream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }
    public void delete(View view)
    {


            File f = new File( path);
            if(f.exists())
            f.delete();





    }

    private void loadState()
    {
        ArrayList<String> s =null;
        FileInputStream inStream = null;

        try {
            File f = new File( path);
            inStream = new FileInputStream(f);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);

            s = ((ArrayList<String>) objectInStream.readObject());
            objectInStream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        if(s!=null)

        {
            for(String x : s)
            {
                String myTemp[] = x.split("x");
                DisplayChart.axisValues.add(rozmiar, new AxisValue(rozmiar).setLabel(myTemp[1]));
                DisplayChart.yAxisValues.add(rozmiar, new PointValue(rozmiar, Float.parseFloat(myTemp[0])));
                rozmiar = rozmiar + 1;

                if(rozmiar%5 == 0)
                {
                    DisplayChart.axisValues2.add(rozmiar2,new AxisValue(rozmiar).setLabel(myTemp[1]));
                    DisplayChart.yAxisValues2.add(rozmiar2,new PointValue(rozmiar2, Float.parseFloat(myTemp[0])));
                    rozmiar2 = rozmiar2 + 1;
                }
                if(rozmiar%15==0)
                {
                    DisplayChart.axisValues3.add(rozmiar3,new AxisValue(rozmiar).setLabel(myTemp[1]));
                    DisplayChart.yAxisValues3.add(rozmiar3,new PointValue(rozmiar3,Float.parseFloat(myTemp[0])));
                    rozmiar3 = rozmiar3 + 1;
                }
                if(rozmiar%60==0)
                {
                    DisplayChart.axisValues4.add(rozmiar4,new AxisValue(rozmiar).setLabel(myTemp[1]));
                    DisplayChart.yAxisValues4.add(rozmiar4,new PointValue(rozmiar4,Float.parseFloat(myTemp[0])));
                    rozmiar4 = rozmiar4 + 1;
                }


                list.add(x);

            }
        }
        else
        {

        }
    }
}


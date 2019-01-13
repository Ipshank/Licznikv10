package com.example.licznikv10;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.icu.util.RangeValueIterator;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class DisplayChart extends AppCompatActivity {


    LineChartView lineChartView;
    Button onOff;
    boolean flag = true;

/*Przykładowe dane do wykresu - chwilowo niepotrzebne i kłopotliwe przy dodawaniu danych z mainactivity
     String[] axisData = {};
    double[] yAxisData1 = {};

    float[] yAxisData = {};
    */

    //Listy w których będą dane
    static List yAxisValues = new ArrayList();
    static List axisValues = new ArrayList();

    static List yAxisValues2 = new ArrayList();
    static List axisValues2 = new ArrayList();

    static List yAxisValues3 = new ArrayList();
    static List axisValues3 = new ArrayList();

    static List yAxisValues4 = new ArrayList();
    static List axisValues4 = new ArrayList();

    List lines = new ArrayList();
    Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0")); //linia dla wszystkich próbek
    Line line2 = new Line(yAxisValues2).setColor(Color.RED); //linia dla co trzeciej próbki
    Line line3 = new Line(yAxisValues3).setColor(Color.BLUE); //linia dla co trzeciej próbki
    Line line4 = new Line(yAxisValues4).setColor(Color.GREEN); //linia dla co trzeciej próbki

    Axis axis = new Axis();



    static LineChartData data = new LineChartData();
    ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_chart);

        //Powiązanie z elementem chart(zadeklarowany w activity_display_chart.xml
        lineChartView = findViewById(R.id.chart);

        onOff = findViewById(R.id.button2);










                //Linia wykresu łącząca punkty okreslone przez dane z listy



        /*Wpisanie danych z tablic do list, przy próbach dodania czegoś z mainactivity, gdy coś jest na liście, rysował się jakby drugi wykres,
          zresztą w wersji finalnej dane wstępne nie będą potrzebne
        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));

        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }*/


        //Lista linii (chociaż my i tak będziemy mieć  raczej jedną)

        lines.add(line);

        //Dodanie linii do wykresu
        //LineChartData data = new LineChartData();
        data.setLines(lines);

        //Ustawienia osi x (legenda)

        axis.setName("Czas");
        axis.setValues(axisValues);
        axis.setMaxLabelChars(10);
        axis.setHasLines(true); // zaznacza do którego punktu odnosi się label pod osią
        axis.setTextSize(12);
        //axis.setHasTiltedLabels(true); // to robi, że wartości są pod kątem względem osi
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);

        //Ustawienia osi y (legenda)
        Axis yAxis = new Axis();

        yAxis.setName("uSv/h");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(12);
        yAxis.setInside(true); //wartości na osi y wewnątr wykresu (nie zasłaniają jednostki)
        //yAxis.setHasTiltedLabels(true);
        data.setAxisYLeft(yAxis);

        //Wyświetlenia wykresu




            scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    if(flag)
                    lineChartView.setLineChartData(data);
                    else{}
                }
            }, 0, 1, TimeUnit.SECONDS);



    }
public void OnOff(View view)
{
    flag = !flag;
    if(flag)onOff.setText("STOP");
    else onOff.setText("PLAY");
}






    public void time1(View view)
    {
        axis.setMaxLabelChars(10);//ustawienie odstępu - potestować dla duzego zbioru danych
        // w sumie może nie trzeba zmieniać, zależy jak dużo danych będzie  do narysowania
        //zawsze można przyblizyc okreslony punkt i bedzie widac dokladna date
        lines.clear();  //czyszcenie zbioru linii
        lines.add(line);//dodanie do zbioru linii tej którą chcemy mieć
        lineChartView.setLineChartData(data);//odświezenia wykrsu
    }

    public void time3(View view)
    {
        axis.setMaxLabelChars(4);//ustawienie odstępu - potestować dla duzego zbioru danych
        lines.clear();//czyszcenie zbioru linii
        lines.add(line2);//dodanie do zbioru linii tej którą chcemy mieć

        lineChartView.setLineChartData(data);//odświezenia wykrsu
    }

    public void time15(View view)
    {
        axis.setMaxLabelChars(4);//ustawienie odstępu - potestować dla duzego zbioru danych
        lines.clear();//czyszcenie zbioru linii
        lines.add(line3);//dodanie do zbioru linii tej którą chcemy mieć

        lineChartView.setLineChartData(data);//odświezenia wykrsu
    }

    public void time60(View view)
    {
        axis.setMaxLabelChars(4);//ustawienie odstępu - potestować dla duzego zbioru danych
        lines.clear();//czyszcenie zbioru linii
        lines.add(line4);//dodanie do zbioru linii tej którą chcemy mieć

        lineChartView.setLineChartData(data);//odświezenia wykrsu
    }

}

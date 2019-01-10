package com.example.licznikv10;

import android.app.Application;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;


import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class DisplayChart extends AppCompatActivity {


    LineChartView lineChartView;


    /*Przykładowe dane do wykresu - chwilowo niepotrzebne i kłopotliwe przy dodawaniu danych z mainactivity
     String[] axisData = {};
    double[] yAxisData1 = {};

    float[] yAxisData = {};
    */

    //Listy w których będą dane
    static List yAxisValues = new ArrayList();
    static List axisValues = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_chart);

        //Powiązanie z elementem chart(zadeklarowany w activity_display_chart.xml
        lineChartView = findViewById(R.id.chart);




        //Linia wykresu łącząca punkty okreslone przez dane z listy
        Line line = new Line(yAxisValues).setColor(Color.parseColor("#9C27B0"));


        /*Wpisanie danych z tablic do list, przy próbach dodania czegoś z mainactivity, gdy coś jest na liście, rysował się jakby drugi wykres,
          zresztą w wersji finalnej dane wstępne nie będą potrzebne
        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));

        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }*/


        //Lista linii (chociaż my i tak będziemy mieć  raczej jedną)
        List lines = new ArrayList();
        lines.add(line);

        //Dodanie linii do wykresu
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //Ustawienia osi x (legenda)
        Axis axis = new Axis();
        axis.setName("Czas");
        axis.setValues(axisValues);
        axis.setTextSize(12);
        axis.setHasTiltedLabels(true); // to robi, że wartości są pod kątem względem osi
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
        lineChartView.setLineChartData(data);
    }


}

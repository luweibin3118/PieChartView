package com.example.administrator.piechartview;

import android.app.Activity;
import android.os.Bundle;

import com.lwb.piechart.PieChartView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PieChartView pieChartView = findViewById(R.id.pie_chart_view);
        pieChartView.addItemType(new PieChartView.ItemType("苹果", 25, 0xff20B2AA));
        pieChartView.addItemType(new PieChartView.ItemType("华为", 17, 0xff68228B));
        pieChartView.addItemType(new PieChartView.ItemType("小米", 13, 0xff8B5A00));
        pieChartView.addItemType(new PieChartView.ItemType("三星", 8, 0xffCD3700));
        pieChartView.addItemType(new PieChartView.ItemType("OPPO", 6, 0xff8968CD));
        pieChartView.addItemType(new PieChartView.ItemType("VIVO", 5, 0xff437145));
//        pieChartView.addItemType(new PieChartView.ItemType("魅族", 4, 0xff8DB6CD));
//        pieChartView.addItemType(new PieChartView.ItemType("联想", 2, 0xff6B8E23));
        pieChartView.addItemType(new PieChartView.ItemType("其他品牌", 20, 0xff999999));
////        pieChartView.setCell(5);
//        pieChartView.setInnerRadius(0.4f);
        pieChartView.setPieCell(10);
        pieChartView.setBackGroundColor(0xffFFFFE0);
        pieChartView.setItemTextSize(30);
        pieChartView.setTextPadding(10);
    }
}


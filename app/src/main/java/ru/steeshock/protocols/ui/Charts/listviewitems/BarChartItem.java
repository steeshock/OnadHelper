package ru.steeshock.protocols.ui.Charts.listviewitems;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import ru.steeshock.protocols.R;
import ru.steeshock.protocols.utils.StageValueFormatter;

public class BarChartItem extends ChartItem {

    IAxisValueFormatter formatter;
    String description;

    /*public DisplayMetrics displayMetrics;

    public BarChartItem(ChartData<?> cd, Context c) {
        super(cd);
        displayMetrics = new DisplayMetrics();


        ((Activity) c).getWindowManager()
                .getDefaultDisplay().getMetrics(displayMetrics);
    }*/

    public BarChartItem(ChartData<?> cd, String description, IAxisValueFormatter formatter) {
        super(cd);

        this.formatter = formatter;
        this.description = description;
    }

    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_barchart, null);
            holder.chart = convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);

        // своя ось X
        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);

        // restrict interval to 1 (minimum).
        // Данная строчка делает так, что при тапе на столбец, происходит его нормально масштабирование.
        // Это обязательно нужно, если ось X не числовая
        xAxis.setGranularity(1f);

        //xAxis.setDrawLimitLinesBehindData(true);
        //xAxis.setCenterAxisLabels(true);
        //xAxis.setDrawLabels(false);
        xAxis.setValueFormatter(formatter);


        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        // описание
        holder.chart.getDescription().setEnabled(true);
        holder.chart.getDescription().setPosition(550, 50);
        holder.chart.getDescription().setTextAlign(Paint.Align.CENTER);
        holder.chart.getDescription().setTextSize(14);
        holder.chart.getDescription().setText(description);

        holder.chart.getLegend().setEnabled(false);


        // set data
        holder.chart.setData((BarData) mChartData);
        holder.chart.setFitBars(true);

        // do not forget to refresh the chart
        //holder.chart.invalidate();
        holder.chart.animateY(700);

        return convertView;
    }

    private static class ViewHolder {
        BarChart chart;
    }
}

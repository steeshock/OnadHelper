package ru.steeshock.protocols.ui.Charts.listviewitems;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.Context;
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

    /*public DisplayMetrics displayMetrics;

    public BarChartItem(ChartData<?> cd, Context c) {
        super(cd);
        displayMetrics = new DisplayMetrics();


        ((Activity) c).getWindowManager()
                .getDefaultDisplay().getMetrics(displayMetrics);
    }*/

    public BarChartItem(ChartData<?> cd, Context c, IAxisValueFormatter formatter) {
        super(cd);

        this.formatter = formatter;
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
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);
        holder.chart.setExtraOffsets(0, 5, 0, 0);

        // своя ось X

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setValueFormatter(formatter);

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)


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

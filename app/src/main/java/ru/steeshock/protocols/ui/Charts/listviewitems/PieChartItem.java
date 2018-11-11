
package ru.steeshock.protocols.ui.Charts.listviewitems;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import ru.steeshock.protocols.R;

public class PieChartItem extends ChartItem {

    //private final SpannableString mCenterText;
    private String mCenterText;

    public PieChartItem(ChartData<?> cd, String centerText) {
        super(cd);
        mCenterText = centerText;
        //mCenterText = generateCenterText();
    }

    @Override
    public int getItemType() {
        return TYPE_PIECHART;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_piechart, null);
            holder.chart = convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setHoleRadius(42f);
        holder.chart.setTransparentCircleRadius(47f);
        holder.chart.setCenterText(mCenterText);
        holder.chart.setCenterTextSize(12f);
        holder.chart.setUsePercentValues(true);
        //holder.chart.setExtraOffsets(5, 10, 50, 10);
        holder.chart.setExtraOffsets(0, 5, 10, 0);

        mChartData.setValueFormatter(new PercentFormatter());
        mChartData.setValueTextSize(12f);
        mChartData.setValueTextColor(Color.WHITE);


        // set data
        holder.chart.setData((PieData) mChartData);

        Legend l = holder.chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateY(900);

        return convertView;
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("MPAndroidChart\ncreated by\nPhilipp Jahoda");
        s.setSpan(new RelativeSizeSpan(1.6f), 0, 14, 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.VORDIPLOM_COLORS[0]), 0, 14, 0);
        s.setSpan(new RelativeSizeSpan(.9f), 14, 25, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, 25, 0);
        s.setSpan(new RelativeSizeSpan(1.4f), 25, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 25, s.length(), 0);
        return s;
    }

    private static class ViewHolder {
        PieChart chart;
    }
}

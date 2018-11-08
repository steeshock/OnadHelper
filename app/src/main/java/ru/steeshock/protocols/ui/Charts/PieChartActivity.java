package ru.steeshock.protocols.ui.Charts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

import ru.steeshock.protocols.AppDelegate;
import ru.steeshock.protocols.R;
import ru.steeshock.protocols.data.database.RecordDao;

/*
    Диаграмма производительности сотрудников
 */

public class PieChartActivity extends AppCompatActivity {

    List<Integer> usersProtocols = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bar_chart);

        PieChart chart = findViewById(R.id.chart);

        RecordDao mRecordDao = ((AppDelegate) getApplicationContext()).getRecordDatabase().getRecordDao();
        int allProtocols = mRecordDao.getRecords().size();
        usersProtocols.add(mRecordDao.getRecordsByUsername("onad001").size());
        usersProtocols.add(mRecordDao.getRecordsByUsername("onad013").size());
        usersProtocols.add(mRecordDao.getRecordsByUsername("onad015").size());
        usersProtocols.add(mRecordDao.getRecordsByUsername("onad017").size());
        usersProtocols.add(mRecordDao.getRecordsByUsername("onad019").size());


        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(usersProtocols.get(0).floatValue(), "onad001: " + usersProtocols.get(0)));
        entries.add(new PieEntry(usersProtocols.get(1).floatValue(), "onad013: " + usersProtocols.get(1)));
        entries.add(new PieEntry(usersProtocols.get(2).floatValue(), "onad015: " + usersProtocols.get(2)));
        entries.add(new PieEntry(usersProtocols.get(3).floatValue(), "onad017: " + usersProtocols.get(3)));
        entries.add(new PieEntry(usersProtocols.get(4).floatValue(), "onad019: " + usersProtocols.get(4)));

        PieDataSet dataSet = new PieDataSet(entries, "Результаты работы");

        setColorsToPieDiagramm(dataSet);

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    private void setColorsToPieDiagramm (PieDataSet dataSet){

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
    }
}

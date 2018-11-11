
package ru.steeshock.protocols.ui.Charts;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import ru.steeshock.protocols.AppDelegate;
import ru.steeshock.protocols.R;
import ru.steeshock.protocols.data.database.RecordDao;
import ru.steeshock.protocols.data.model.RecordHelper;

/**
 * Demonstrates the use of charts inside a ListView. IMPORTANT: provide a
 * specific height attribute for the chart inside your ListView item
 *
 * @author Philipp Jahoda
 */
public class ListViewMultiChartActivity extends AppCompatActivity {

    private static final String TAG = "MyLog";
    private RecordDao mRecordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_listview_chart);

        mRecordDao = ((AppDelegate) getApplicationContext()).getRecordDatabase().getRecordDao();

        ListView lv = findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<>();

        list.add(new PieChartItem(makeStatsProtocolWorkersDataPie(), getApplicationContext(),"Всего: " + mRecordDao.getRecords().size()));
        list.add(new BarChartItem(makeStatsStagesDataPie(), getApplicationContext()));

        // 30 items
        for (int i = 0; i < 10; i++) {
            if(i % 3 == 0) {
                list.add(new LineChartItem(generateDataLine(i + 1), getApplicationContext()));
            } else if(i % 3 == 1) {
                list.add(new BarChartItem(generateDataBar(i + 1), getApplicationContext()));
            } else if(i % 3 == 2) {
                list.add(new PieChartItem(generateDataPie(), getApplicationContext(), "TEST"));
            }
        }

        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
        lv.setAdapter(cda);
    }

    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //noinspection ConstantConditions
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            ChartItem ci = getItem(position);
            return ci != null ? ci.getItemType() : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Line data
     */
    private LineData generateDataLine(int cnt) {

        ArrayList<Entry> values1 = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            values1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(values1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            values2.add(new Entry(i, values1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(values2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        return new LineData(sets);
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Bar data
     */
    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return Pie data
     */
    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        return new PieData(d);
    }


    /**
     * Создаем диаграмму статистики работы сотрудников отдела / диаграмма производительности отдела
     *
     * @return Pie data
     */
    private PieData makeStatsProtocolWorkersDataPie() {

        List<Integer> usersProtocols = new ArrayList<>();
        int allProtocols = mRecordDao.getRecords().size();

        usersProtocols.add(mRecordDao.getRecordsByUsername("onad001").size());
        usersProtocols.add(mRecordDao.getRecordsByUsername("onad013").size());
        usersProtocols.add(mRecordDao.getRecordsByUsername("onad015").size());
        usersProtocols.add(mRecordDao.getRecordsByUsername("onad017").size());
        usersProtocols.add(mRecordDao.getRecordsByUsername("onad019").size());

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(usersProtocols.get(0).floatValue(), "onad001: " + usersProtocols.get(0)));
        entries.add(new PieEntry(usersProtocols.get(1).floatValue(), "onad013: " + usersProtocols.get(1)));
        entries.add(new PieEntry(usersProtocols.get(2).floatValue(), "onad015: " + usersProtocols.get(2)));
        entries.add(new PieEntry(usersProtocols.get(3).floatValue(), "onad017: " + usersProtocols.get(3)));
        entries.add(new PieEntry(usersProtocols.get(4).floatValue(), "onad019: " + usersProtocols.get(4)));

        PieDataSet dataSet = new PieDataSet(entries, "");

        // space between slices
        dataSet.setSliceSpace(2f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        return new PieData(dataSet);
    }

    /**
     * Создаем диаграмму статистики по типу отказа
     *
     * @return Pie data
     */
    private BarData makeStatsStagesDataPie() {

        List<Integer> protocolsByStages = new ArrayList<>();
        List<Integer> sortedList = new ArrayList<>();

        for (int i = 0;i < RecordHelper.LIST_OF_STAGES.length;i++){
            protocolsByStages.add(mRecordDao.getRecordsByStage(i).size());
            Log.d(TAG, RecordHelper.LIST_OF_STAGES[i] + protocolsByStages.get(i));
        }

        for (int i = 0;i < protocolsByStages.size();i++){
            if (protocolsByStages.get(i) != 0)
                sortedList.add(protocolsByStages.get(i));
        }

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < sortedList.size(); i++) {
            entries.add(new BarEntry(i, sortedList.get(i)));
        }

        BarDataSet d = new BarDataSet(entries, "Отказы по этапам");
        setColorsToDiagram(d);
        d.setHighLightAlpha(150);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    private void setColorsToDiagram (DataSet dataSet){

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

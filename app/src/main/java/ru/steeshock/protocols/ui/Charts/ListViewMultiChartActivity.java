
package ru.steeshock.protocols.ui.Charts;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import ru.steeshock.protocols.R;
import ru.steeshock.protocols.ui.Charts.listviewitems.BarChartItem;
import ru.steeshock.protocols.ui.Charts.listviewitems.ChartItem;
import ru.steeshock.protocols.ui.Charts.listviewitems.PieChartItem;
import ru.steeshock.protocols.utils.FailureTypeValueFormatter;
import ru.steeshock.protocols.utils.StageValueFormatter;

/**
 * Demonstrates the use of charts inside a ListView. IMPORTANT: provide a
 * specific height attribute for the chart inside your ListView item
 *
 * @author Philipp Jahoda
 */
public class ListViewMultiChartActivity extends AppCompatActivity {

    private ChartsDataProvider mDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_listview_chart);

        // Главная сущность, поставляющая данные для всех графиков
        mDataProvider = new ChartsDataProvider(this);

        ListView lv = findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<>();

        list.add(new PieChartItem(mDataProvider.makeStatsProtocolWorkersDataPie(),"Всего: " + mDataProvider.mRecordDao.getRecords().size()));
        list.add(new BarChartItem(mDataProvider.makeStatsStagesDataPie(), "Статистика отказов по этапам", new StageValueFormatter()));
        list.add(new BarChartItem(mDataProvider.makeStatsFailureTypeDataPie(), "Статистика отказов по типу отказа", new FailureTypeValueFormatter()));

        // 30 items
        /*for (int i = 0; i < 10; i++) {
            if(i % 3 == 0) {
                list.add(new LineChartItem(mDataProvider.generateDataLine(i + 1), getApplicationContext()));
            } else if(i % 3 == 1) {
                list.add(new BarChartItem(mDataProvider.generateDataBar(i + 1), getApplicationContext()));
            } else if(i % 3 == 2) {
                list.add(new PieChartItem(mDataProvider.generateDataPie(), getApplicationContext(), "TEST"));
            }
        }*/

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


}

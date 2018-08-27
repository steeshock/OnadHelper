package ru.steeshock.protocols.model;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.steeshock.protocols.R;
import ru.steeshock.protocols.database.RecordDao;
import ru.steeshock.protocols.ui.UpdateRecordActivity;


public class RecordAdapter extends RecyclerView.Adapter<RecordHolder>{

    private final List<Record> mRecords = new ArrayList<>();
    private final List<Record> mHideRecords = new ArrayList<>();
    public static String RECORD_ID = "RECORD_ID";
    private RecordDao recordDao;

    public RecordAdapter(RecordDao _recordDao) {
        recordDao = _recordDao;
    }

    @NonNull
    @Override
    public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.li_item, parent, false);
        return new RecordHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecordHolder holder, final int position) {
        final Record record = mRecords.get(position);
        holder.bind(record);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Предупреждение")
                        .setMessage("Протокол #" + mRecords.get(position).getProtocolNumber()+  " будет безвозвратно удален из базы данных. Вы уверены?")
                        .setCancelable(false)
                        .setIcon(R.drawable.cancel)
                        .setNegativeButton("ДА",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        // удаляем запись
                                        mRecords.remove(position);
                                        recordDao.deleteRecord(record);
                                        notifyDataSetChanged();
                                    }
                                })
                        .setPositiveButton("НЕТ",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startUpdateActivity = new Intent(holder.itemView.getContext(), UpdateRecordActivity.class);
                startUpdateActivity.putExtra(RECORD_ID, record.getId());
                holder.itemView.getContext().startActivity (startUpdateActivity);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mRecords.size();
    }


    public void addRecords(List<Record> records, boolean isRefreshed) {
        if (isRefreshed) {
            mRecords.clear();
        }

        mRecords.addAll(records);
        notifyDataSetChanged();
    }

    public void addFilteredRecords(List<Record> records, boolean isRefreshed) {
        if (isRefreshed) {
            mRecords.clear();
            mHideRecords.clear();
        }
        mRecords.addAll(records);
        for (Record rec:
             mRecords) {
            if(!rec.getStatusStr().equals("Готов")){
                mHideRecords.add(rec);
            }
        }
        mRecords.clear();
        mRecords.addAll(mHideRecords);

        notifyDataSetChanged();
    }


}

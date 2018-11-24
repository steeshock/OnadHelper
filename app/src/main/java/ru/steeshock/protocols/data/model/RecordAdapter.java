package ru.steeshock.protocols.data.model;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.steeshock.protocols.R;
import ru.steeshock.protocols.data.database.RecordDao;
import ru.steeshock.protocols.ui.Records.UpdateRecordActivity;
import ru.steeshock.protocols.utils.UserSettings;


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
            mHideRecords.clear();
        }

        mRecords.addAll(records);
        mHideRecords.addAll(records);

        if (UserSettings.HIDE_AUTHOR_FLAG) {
            for (Record rec:
                    mRecords) {
                if(!rec.getUserToken().equals(UserSettings.USER_TOKEN)){
                    mHideRecords.remove(rec);
                }
            }
        }

        mRecords.clear();
        mRecords.addAll(mHideRecords);

        if (UserSettings.HIDE_RECORDS_FLAG){
            for (Record rec:
                    mRecords) {
                if(rec.getStatusNum() == 3||rec.getStatusNum() == 4){
                    mHideRecords.remove(rec);
                }
            }
        }

        mRecords.clear();
        mRecords.addAll(mHideRecords);

        //сортируем списки,если они уже были отсортированы ранее

        if(UserSettings.SORT_RECORDS_BY_PROTOCOL)
            sortRecordsByProtocolNumber(true);

        if(UserSettings.SORT_RECORDS_BY_DESCRIPTION)
            sortRecordsByDescription(true);

        if(UserSettings.SORT_RECORDS_BY_STATUS)
            sortRecordsByStatus(true);


        notifyDataSetChanged();
    }

    public void sortRecordsByProtocolNumber(boolean order) {

        UserSettings.SORT_RECORDS_BY_PROTOCOL = true;
        UserSettings.SORT_RECORDS_BY_DESCRIPTION= false;
        UserSettings.SORT_RECORDS_BY_STATUS = false;

        if (mRecords.size() > 0) {
            if (order){
                Collections.sort(mRecords, new Comparator<Record>() {
                    @Override
                    public int compare(final Record object1, final Record object2) {
                        return object1.getProtocolNumber().compareTo(object2.getProtocolNumber());
                    }
                });
                UserSettings.SORT_ORDER = true;
            } else {
                Collections.sort(mRecords, new Comparator<Record>() {
                    @Override
                    public int compare(final Record object1, final Record object2) {
                        return object2.getProtocolNumber().compareTo(object1.getProtocolNumber());
                    }
                });
                UserSettings.SORT_ORDER = false;
            }

        }

        notifyDataSetChanged();
    }

    public void sortRecordsByDescription(boolean order) {

        UserSettings.SORT_RECORDS_BY_PROTOCOL = false;
        UserSettings.SORT_RECORDS_BY_DESCRIPTION= true;
        UserSettings.SORT_RECORDS_BY_STATUS = false;

        if (mRecords.size() > 0) {
            if (order){
                Collections.sort(mRecords, new Comparator<Record>() {
                    @Override
                    public int compare(final Record object1, final Record object2) {
                        return object1.getDescription().compareTo(object2.getDescription());
                    }
                });
                UserSettings.SORT_ORDER = true;
            } else {
                Collections.sort(mRecords, new Comparator<Record>() {
                    @Override
                    public int compare(final Record object1, final Record object2) {
                        return object2.getDescription().compareTo(object1.getDescription());
                    }
                });
                UserSettings.SORT_ORDER = false;
            }
        }

        notifyDataSetChanged();
    }

    public void sortRecordsByStatus(boolean order) {

        UserSettings.SORT_RECORDS_BY_PROTOCOL = false;
        UserSettings.SORT_RECORDS_BY_DESCRIPTION= false;
        UserSettings.SORT_RECORDS_BY_STATUS = true;

        if (mRecords.size() > 0) {
            if(order) {
                Collections.sort(mRecords, new Comparator<Record>() {
                    @Override
                    public int compare(final Record object1, final Record object2) {
                        return RecordHelper.getStatusStr(object1).compareTo(RecordHelper.getStatusStr(object2));
                    }
                });
                UserSettings.SORT_ORDER = true;
            } else {
                Collections.sort(mRecords, new Comparator<Record>() {
                    @Override
                    public int compare(final Record object1, final Record object2) {
                        return RecordHelper.getStatusStr(object2).compareTo(RecordHelper.getStatusStr(object1));
                    }
                });
                UserSettings.SORT_ORDER = false;
            }
        }

        notifyDataSetChanged();
    }



}

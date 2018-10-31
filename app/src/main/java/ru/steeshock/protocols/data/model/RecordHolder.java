package ru.steeshock.protocols.data.model;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.steeshock.protocols.R;

public class RecordHolder extends RecyclerView.ViewHolder{


    private TextView mProtocolNumber;
    //private TextView mActNumber;
    private TextView mDescription;
    private TextView mStatusStr;

    public RecordHolder(View itemView) {
        super(itemView);

        mProtocolNumber = itemView.findViewById(R.id.tvProtocolNumber);
        //mActNumber = itemView.findViewById(R.id.tvActNumber);
        mDescription = itemView.findViewById(R.id.tvDescription);
        mStatusStr = itemView.findViewById(R.id.tvStatus);
    }

    public void bind(Record record) {
        mProtocolNumber.setText(record.getProtocolNumber());
        //mActNumber.setText(record.getActNumber());
        //mDescription.setText(record.getUserToken());
        mDescription.setText(record.getDescription());
        mStatusStr.setText(record.getStatusStr());

        switch (record.getStatusStr()){
            case "Готов": mStatusStr.setBackgroundColor(Color.GREEN); break;
            case "Подготовка": mStatusStr.setBackgroundColor(Color.RED); break;
            case "Заказчик": mStatusStr.setBackgroundColor(Color.BLUE); break;
            case "Канцелярия": mStatusStr.setBackgroundColor(Color.MAGENTA); break;
            case "Отмена": mStatusStr.setBackgroundColor(Color.GRAY); break;
        }
    }

}

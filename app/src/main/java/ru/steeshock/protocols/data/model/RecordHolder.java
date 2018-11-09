package ru.steeshock.protocols.data.model;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.steeshock.protocols.R;

public class RecordHolder extends RecyclerView.ViewHolder{


    private TextView mProtocolNumber;
    private TextView mDescription;
    private TextView mStatusStr;

    public RecordHolder(View itemView) {
        super(itemView);

        mProtocolNumber = itemView.findViewById(R.id.tvProtocolNumber);
        mDescription = itemView.findViewById(R.id.tvDescription);
        mStatusStr = itemView.findViewById(R.id.tvStatus);
    }

    public void bind(Record record) {
        mProtocolNumber.setText(record.getProtocolNumber());
        mDescription.setText(record.getDescription());
        mStatusStr.setText(RecordHelper.getStatusStr(record));

        switch ((int)record.getStatusNum()){
            case 0: mStatusStr.setBackgroundColor(Color.RED); break;
            case 2: mStatusStr.setBackgroundColor(Color.BLUE); break;
            case 1: mStatusStr.setBackgroundColor(Color.MAGENTA); break;
            case 3: mStatusStr.setBackgroundColor(Color.GREEN); break;
            case 4: mStatusStr.setBackgroundColor(Color.GRAY); break;
        }
    }

}

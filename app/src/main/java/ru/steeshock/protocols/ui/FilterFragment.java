package ru.steeshock.protocols.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;

import ru.steeshock.protocols.R;
import ru.steeshock.protocols.common.IFilterFields;
import ru.steeshock.protocols.ui.Records.MainActivity;
import ru.steeshock.protocols.utils.UserSettings;

public class FilterFragment extends Fragment {

    private IFilterFields mFilterFields;

    public static FilterFragment newInstance() {

        Bundle args = new Bundle();

        FilterFragment fragment = new FilterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFilterFields = (MainActivity)context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fr_filter, container, false);
        ImageButton imageButton = view.findViewById(R.id.imageButtonClose);
        Switch switch1 = view.findViewById(R.id.sw1);
        Switch switch2 = view.findViewById(R.id.sw2);

        switch1.setChecked(UserSettings.HIDE_RECORDS_FLAG);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    UserSettings.HIDE_RECORDS_FLAG = true;
                    mFilterFields.onRefresh();
                } else {
                    UserSettings.HIDE_RECORDS_FLAG = false;
                    mFilterFields.onRefresh();
                }
            }
        });

        switch2.setChecked(UserSettings.HIDE_AUTHOR_FLAG);
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    UserSettings.HIDE_AUTHOR_FLAG = true;
                    mFilterFields.onRefresh();
                } else {
                    UserSettings.HIDE_AUTHOR_FLAG = false;
                    mFilterFields.onRefresh();
                }
            }
        });

        if (getActivity() != null) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
        }


        return view;
    }
}

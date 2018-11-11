package ru.steeshock.protocols.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StageValueFormatter implements IAxisValueFormatter
{
    public static List<String> listOfActualStages = new ArrayList<>();

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return listOfActualStages.get((int)value);
    }
}

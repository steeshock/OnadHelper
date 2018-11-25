package ru.steeshock.protocols.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class FailureTypeValueFormatter implements IAxisValueFormatter
{
    public static List<String> listOfActualFailureTypes = new ArrayList<>();

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return listOfActualFailureTypes.get((int)value);
    }
}

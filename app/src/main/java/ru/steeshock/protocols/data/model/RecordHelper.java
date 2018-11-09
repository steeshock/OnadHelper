package ru.steeshock.protocols.data.model;

public class RecordHelper {

    public static final String[] LIST_OF_STATUSES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};
    public static final String[] LIST_OF_STAGES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};
    public static final String[] LIST_OF_FAILURE_TYPES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};

    public static String getStatusStr(Record record) {
        return LIST_OF_STATUSES[(int)record.getStatusNum()];
    }

}

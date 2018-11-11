package ru.steeshock.protocols.data.model;

public class RecordHelper {

    public static final String[] LIST_OF_STATUSES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};

    public static final String[] LIST_OF_STAGES = {"Регулировка", "Испытания", "ШМР", "Эксплуатация", "Серв. обслуж.",
            "Сборка", "Вх. контроль", "Прогон", "Техтряска" };

    // список сокращенных обозначений для использования в диаграммах
    public static final String[] LIST_OF_SHORT_STAGES = {"Р", "И", "ШМР", "Э", "СО", "СБ", "Вх.К", "ПР", "ТТ" };

    public static final String[] LIST_OF_FAILURE_TYPES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};

    public static String getStatusStr(Record record) {
        return LIST_OF_STATUSES[(int)record.getStatusNum()];
    }

    public static String getStageStr(Record record) {
        return LIST_OF_STAGES[(int)record.getStage()];
    }

}

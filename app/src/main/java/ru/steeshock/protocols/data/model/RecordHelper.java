package ru.steeshock.protocols.data.model;

public class RecordHelper {

    public static final String[] LIST_OF_STATUSES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};

    public static final String[] LIST_OF_FULL_STAGES = {"Регулировка", "Испытания", "ШМР", "Эксплуатация", "Серв. обслуж.",
            "Сборка", "Вх. контроль", "Прогон", "Техтряска" };

    public static final String[] LIST_OF_FULL_FAILURE_TYPES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};

    // Список сокращенных обозначений для использования в диаграммах
    public static final String[] LIST_OF_SHORT_STAGES = {"Р", "И", "ШМР", "Э", "СО", "СБ", "Вх.К", "ПР", "ТТ" };

    public static final String[] LIST_OF_SHORT_FAILURE_TYPES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};


    // Функции для преобразования из числовых форматов, хранящихся в БД, в строковые

    public static String getStatusStr(Record record) {
        return LIST_OF_STATUSES[(int)record.getStatusNum()];
    }

    public static String getStageStr(Record record) {
        return LIST_OF_FULL_STAGES[(int)record.getStage()];
    }

}

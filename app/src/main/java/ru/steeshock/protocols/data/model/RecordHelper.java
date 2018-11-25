package ru.steeshock.protocols.data.model;

public class RecordHelper {

    public static final String[] LIST_OF_STATUSES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};

    public static final String[] LIST_OF_FULL_STAGES = {"Регулировка", "Испытания", "ШМР", "Эксплуатация", "Серв. обслуж.",
            "Сборка", "Вх. контроль", "Прогон", "Техтряска" };

    public static final String[] LIST_OF_FULL_FAILURE_TYPES = {"Технологический", "Конструктивный", "Эксплуатационный", "ПКИ", "Производственный",
            "Неподтвердившийся", "Технологический, Конструктивный", "Технологический, Производственный", "Производственный, Конструктивный",
                "Технологический, Конструктивный, Производственный", "ПКИ, Конструктивный", "ПКИ, Производственный", "ПКИ, Эксплуатационный", "Неустановленный"};

    // Список сокращенных обозначений для использования в диаграммах
    public static final String[] LIST_OF_SHORT_STAGES = {"Р", "И", "ШМР", "Э", "СО", "СБ", "Вх.К", "ПР", "ТТ" };

    public static final String[] LIST_OF_SHORT_FAILURE_TYPES = {"Т", "К", "Э", "ПКИ", "ПР", "Н/П", "Т-К", "Т-П", "П-К", "Т-К-П", "ПКИ-К", "ПКИ-ПР", "ПКИ-Э", "Н/У"};


    // Функции для преобразования из числовых форматов, хранящихся в БД, в строковые

    public static String getStatusStr(Record record) {
        return LIST_OF_STATUSES[(int)record.getStatusNum()];
    }

    public static String getStageStr(Record record) {
        return LIST_OF_FULL_STAGES[(int)record.getStage()];
    }
}

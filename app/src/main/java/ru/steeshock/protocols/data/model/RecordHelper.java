package ru.steeshock.protocols.data.model;

public class RecordHelper {

    public static final String[] LIST_OF_STATUSES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};
    public static final String[] LIST_OF_STAGES = {"Регулировка", "Испытания", "ШМР", "Эксплуатация", "Серв. обслуж.",
            "Сборка", "Вх. контроль", "Прогон", "Техтряска" };
    public static final String[] LIST_OF_FAILURE_TYPES = {"Подготовка", "Канцелярия", "Заказчик", "Готов", "Отмена"};

    public static String getStatusStr(Record record) {
        return LIST_OF_STATUSES[(int)record.getStatusNum()];
    }

    public static String getStageStr(Record record) {
        return LIST_OF_STAGES[(int)record.getStage()];
    }

}

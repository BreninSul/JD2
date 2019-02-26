package com.gmail.breninsul.jd2.config;

public class ProductTypes {
    private static final String[] PRODUCT_TYPE = new String[50];

    static {
        PRODUCT_TYPE[0] = "Прочее";
        PRODUCT_TYPE[1] = "БЫТОВАЯ РАДИОЭЛЕКТРОННАЯ АППАРАТУРА";
        PRODUCT_TYPE[2] = "АККУМУЛЯТОРЫ И АККУМУЛЯТОРНЫЕ БАТАРЕИ";
        PRODUCT_TYPE[3] = "СРЕДСТВА ИНДИВИДУАЛЬНОЙ ЗАЩИТЫ";
        PRODUCT_TYPE[4] = "ПРОДУКЦИЯ ЛЕГКОЙ ПРОМЫШЛЕННОСТИ";
        PRODUCT_TYPE[5] = "ТАБАЧНЫЕ ИЗДЕЛИЯ";
        PRODUCT_TYPE[6] = "МЕБЕЛЬ";
        PRODUCT_TYPE[7] = "УПАКОВКА";
        PRODUCT_TYPE[8] = "ПОСУДА";
        PRODUCT_TYPE[9] = "КОРМА ДЛЯ ЖИВОТНЫХ, ПТИЦ И РЫБ";
        PRODUCT_TYPE[10] = "НЕФТЕПРОДУКТЫ";
        PRODUCT_TYPE[11] = "СРЕДСТВА МОЮЩИЕ, ИЗДЕЛИЯ ХОЗЯЙСТВЕННЫЕ";
        PRODUCT_TYPE[12] = "ТОВАРЫ БЫТОВОЙ ХИМИИ, УДОБРЕНИЯ";
        PRODUCT_TYPE[13] = "РЫБНЫЕ ПРОДУКТЫ";
        PRODUCT_TYPE[14] = "ПИЩЕВАЯ ПРОДУКЦИЯ";
        PRODUCT_TYPE[15] = "СОКОВАЯ ПРОДУКЦИЯ ИЗ ФРУКТОВ И ОВОЩЕЙ";
        PRODUCT_TYPE[16] = "МАСЛОЖИРОВАЯ ПРОДУКЦИЯ";
        PRODUCT_TYPE[17] = "МОЛОЧНАЯ ПРОДУКЦИЯ";
        PRODUCT_TYPE[18] = "ПРОДУКЦИЯ МЯСНОЙ И МЯСОПЕРЕРАБАТЫВАЮЩЕЙ ПРОМЫШЛЕННОСТИ";
        PRODUCT_TYPE[19] = "ЗЕРНОВЫЕ КУЛЬТУРЫ";
        PRODUCT_TYPE[20] = "ПАРФЮМЕРНО-КОСМЕТИЧЕСКАЯ ПРОДУКЦИЯ";
        PRODUCT_TYPE[21] = "МЕДИЦИНСКОЕ ОБОРУДОВАНИЕ, МАТЕРИАЛЫ И ИНСТРУМЕНТЫ";
        PRODUCT_TYPE[22] = "ПИЩЕВЫЕ ДОБАВКИ";
        PRODUCT_TYPE[23] = "НИЗКОВОЛЬТНОЕ ОБОРУДОВАНИЕ";
        PRODUCT_TYPE[24] = "ОБОРУДОВАНИЕ ВЫСОКОВОЛЬТНОЕ И ВЗРЫВОЗАЩИЩЕННОЕ";
        PRODUCT_TYPE[25] = "Кабели силовые для нестационарной прокладки на напряжение свыше 1 кВ";
        PRODUCT_TYPE[26] = "ОБОРУДОВАНИЕ, РАБОТАЮЩЕЕ НА ГАЗООБРАЗНОМ ТОПЛИВЕ";
        PRODUCT_TYPE[27] = "ОБОРУДОВАНИЕ, РАБОТАЮЩЕЕ ПОД ИЗБЫТОЧНЫМ ДАВЛЕНИЕМ";
        PRODUCT_TYPE[28] = "ПРОЧАЯ ПРОМЫШЛЕННАЯ ПРОДУКЦИЯ";
        PRODUCT_TYPE[29] = "ПРОДУКЦИЯ, ПРИМЕНЯЕМАЯ В ДОРОЖНОЙ ОТРАСЛИ";
        PRODUCT_TYPE[30] = "СЕЛЬСКОХОЗЯЙСТВЕННАЯ ТЕХНИКА";
        PRODUCT_TYPE[31] = "МАШИНЫ И ОБОРУДОВАНИЕ";
        PRODUCT_TYPE[32] = "ПРОДУКЦИЯ, ПРЕДНАЗНАЧЕННАЯ ДЛЯ ДЕТЕЙ И ПОДРОСТКОВ";
        PRODUCT_TYPE[33] = "ИГРУШКИ";
        PRODUCT_TYPE[34] = "МАЛОМЕРНЫЕ СУДА";
        PRODUCT_TYPE[35] = "ГАЗ ГОРЮЧИЙ ПРИРОДНЫЙ И ПОСТАВЛЯЕМЫЙ В МАГИСТРАЛЬНЫЕ ГАЗОПРОВОДЫ, ГАЗ ИСКУССТВЕННЫЙ, КОНДЕНСАТ ГАЗОВЫЙ, ГЕЛИЙ";
        PRODUCT_TYPE[36] = "ЭЛЕКТРОЭНЕРГИЯ";
        PRODUCT_TYPE[37] = "АВТОМОБИЛИ, МОТОРНЫЕ ТРАНСПОРТНЫЕ СРЕДСТВА";
        PRODUCT_TYPE[38] = "ЛИФТЫ";
        PRODUCT_TYPE[39] = "ПИРОТЕХНИЧЕСКАЯ ПРОДУКЦИЯ";
        PRODUCT_TYPE[40] = "ВЗРЫВЧАТЫЕ ВЕЩЕСТВА И ИЗДЕЛИЯ НА ИХ ОСНОВЕ";
        PRODUCT_TYPE[41] = "ОРУЖИЕ И ПАТРОНЫ, СПОРТИВНОЕ ОРУЖИЕ";
        PRODUCT_TYPE[42] = "ПОЖАРНАЯ ТЕХНИКА И МАТЕРИАЛЫ";
        PRODUCT_TYPE[43] = "ПРОДУКЦИЯ ЖЕЛЕЗНОДОРОЖНОГО ТРАНСПОРТА (ПОД ТЕХНИЧЕСКИЕ РЕГЛАМЕНТЫ ТАМОЖЕННОГО СОЮЗА)";
        PRODUCT_TYPE[44] = "ХИМИКО-ФАРМАЦЕВТИЧЕСКАЯ ПРОДУКЦИЯ. ВАКЦИНЫ. СРЕДСТВА ДЕЗИНФЕКЦИОННЫЕ, ДЕЗИНСЕКЦИОННЫЕ И ДЕРАТИЗАЦИОННЫЕ";
        PRODUCT_TYPE[45] = "ПРИБОРЫ НЕРАЗРУШАЮЩЕГО КОНТРОЛЯ; УСТРОЙСТВА ЯДЕРНЫЕ И РАДИОИЗОТОПНЫЕ";
        PRODUCT_TYPE[46] = "ОБОРУДОВАНИЕ ЭКСПЛУАТАЦИОННОЕ ДЛЯ АТОМНЫХ ЭЛЕКТРОСТАНЦИЙ";

    }

    public static String get(int i) {
        if (i < 47 && i > -1) {
            return PRODUCT_TYPE[i];
        }
        return "Прочее";
    }
}
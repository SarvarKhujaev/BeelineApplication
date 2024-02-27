package com.beeline.beelineapplication.constants.postgres;

/*
хранит все типы Транзакций
*/
public final class PostgreTransactionTypes {
    /*
    Оператор видит только те строки, которые были зафиксированы до начала его выполнения. Этот уровень устанавливается по умолчанию.
     */
    public static final String READ_COMMITTED = "READ COMMITTED";

    /*
    Все операторы текущей транзакции видят только те строки,
    которые были зафиксированы перед первым запросом на выборку или изменение данных, выполненным в этой транзакции.
     */
    public static final String REPEATABLE_READ = "REPEATABLE READ;";

    /*
    Все операторы текущей транзакции видят только те строки,
    которые были зафиксированы перед первым запросом на выборку или изменение данных,
    выполненным в этой транзакции.
    Если наложение операций чтения и записи параллельных сериализуемых транзакций может привести к ситуации,
    невозможной при последовательном их выполнении (когда одна транзакция выполняется за другой),
    произойдёт откат одной из транзакций с ошибкой serialization_failure (сбой сериализации).
     */
    public static final String SERIALIZABLE = "SERIALIZABLE";

    public static final String READ_WRITE = "READ WRITE";

    public static final String READ_ONLY = "READ ONLY";

    public static final String DEFERRABLE = "DEFERRABLE";
}

package com.beeline.beelineapplication.constants.postgres;

public final class PostgreTablesOperations {
    /*
    https://postgrespro.ru/docs/postgresql/9.6/tutorial-fk <- docs

    Давайте рассмотрим следующую задачу: вы хотите добиться, чтобы никто не мог вставить в таблицу weather строки,
    для которых не находится соответствующая строка в таблице cities. Это называется обеспечением ссылочной целостности
    данных. В простых СУБД это пришлось бы реализовать (если это вообще возможно) так: сначала явно проверить,
    есть ли соответствующие записи в таблице cities, а затем отклонить или вставить новые записи в таблицу weather.
    Этот подход очень проблематичен и неудобен, поэтому всё это PostgreSQL может сделать за вас.

    CREATE TABLE orders (
        order_id integer PRIMARY KEY,
        product_no integer REFERENCES products (product_no),
        quantity integer
    );
    */
    public final static String REFERENCES = "REFERENCES %s ( %s )";

    /*
    https://postgrespro.ru/docs/postgresql/9.6/ddl-constraints <- docs

    Ограничение-проверка — наиболее общий тип ограничений.
    В его определении вы можете указать, что значение данного столбца должно удовлетворять логическому выражению
    (проверке истинности). Например, цену товара можно ограничить положительными значениями так

    Как вы видите, ограничение определяется после типа данных, как и значение по умолчанию.
    Значения по умолчанию и ограничения могут указываться в любом порядке.
    Ограничение-проверка состоит из ключевого слова CHECK, за которым идёт выражение в скобках.
    Это выражение должно включать столбец, для которого задаётся ограничение, иначе оно не имеет большого смысла.

    Example:
        CREATE TABLE products (
            product_no integer,
            name text,
            price numeric CHECK (price > 0)
        );

        CHECK ( x = 1 )
        CHECK ( outletID BETWEEN 100 AND 200 )
        CHECK ( outletID >= 100 AND outletID < 200 )
        CHECK ( county IN ( 'Oxfordshire', 'Buckinghamshire', 'Warwickshire' ))

    Вы можете также присвоить ограничению отдельное имя.
    Это улучшит сообщения об ошибках и позволит вам ссылаться на это ограничение, когда вам понадобится изменить его.

    Сделать это можно так:
        CREATE TABLE products (
            product_no integer,
            name text,
            price numeric CONSTRAINT positive_price CHECK (price > 0)
        );

    CREATE TABLE products (
        product_no integer,
        name text,
        price numeric CHECK (price > 0),
        discounted_price numeric CHECK (discounted_price > 0),
        CHECK (price > discounted_price)
    );
     */
    public final static String CHECK = "CHECK";

    public final static String CONSTRAINT = "CONSTRAINT";

    /*
    Ограничение NOT NULL всегда записывается как ограничение столбца и функционально эквивалентно ограничению
    CHECK (имя_столбца IS NOT NULL), но в PostgreSQL явное ограничение NOT NULL работает более эффективно.
    Хотя у такой записи есть недостаток — назначить имя таким ограничениям нельзя.

    Для ограничения NOT NULL есть и обратное: ограничение NULL.
    Оно не означает, что столбец должен иметь только значение NULL, что конечно было бы бессмысленно.
    Суть же его в простом указании, что столбец может иметь значение NULL (это поведение по умолчанию).
    Ограничение NULL отсутствует в стандарте SQL и использовать его в переносимых приложениях не следует.
    (Оно было добавлено в PostgreSQL только для совместимости с некоторыми другими СУБД.)
    Однако некоторые пользователи любят его использовать, так как оно позволяет легко переключать ограничения
    в скрипте. Например, вы можете начать с:

    CREATE TABLE products (
        product_no integer NULL,
        name text NULL,
        price numeric NULL
    );
     */
    public final static String NOT_NULL = "NOT NULL";

    /*
    Ограничения уникальности гарантируют, что данные в определённом столбце или группе столбцов
    уникальны среди всех строк таблицы. Ограничение записывается так:

    CREATE TABLE products (
        product_no integer UNIQUE,
        name text,
        price numeric
    );

    Чтобы определить ограничение уникальности для группы столбцов, запишите его в виде ограничения таблицы,
    перечислив имена столбцов через запятую:

    CREATE TABLE example (
        a integer,
        b integer,
        c integer,
        UNIQUE (a, c)
    );
     */
    public final static String UNIQUE = "UNIQUE";

    /*
    Ограничение первичного ключа означает, что образующий его столбец или группа столбцов может быть
    уникальным идентификатором строк в таблице. Для этого требуется, чтобы значения были одновременно
    уникальными и отличными от NULL. Таким образом,

    CREATE TABLE products (
        product_no integer PRIMARY KEY,
        name text,
        price numeric
    );

    Первичные ключи могут включать несколько столбцов; синтаксис похож на запись ограничений уникальности

    При добавлении первичного ключа автоматически создаётся уникальный индекс-B-дерево для столбца или группы столбцов,
    перечисленных в первичном ключе, и данные столбцы помечаются как NOT NULL.

    Таблица может иметь максимум один первичный ключ.
    (Ограничений уникальности и ограничений NOT NULL, которые функционально почти равнозначны первичным ключам,
    может быть сколько угодно, но назначить ограничением первичного ключа можно только одно.)
    Теория реляционных баз данных говорит, что первичный ключ должен быть в каждой таблице.
    В PostgreSQL такого жёсткого требования нет, но обычно лучше ему следовать.
     */
    public final static String PRIMARY_KEY = "PRIMARY KEY";

    /*
    Ограничению внешнего ключа можно назначить имя стандартным способом.
    Внешний ключ также может ссылаться на группу столбцов. В этом случае его нужно записать в виде обычного ограничения таблицы.

    Например:
        CREATE TABLE t1 (
          a integer PRIMARY KEY,
          b integer,
          c integer,
          FOREIGN KEY (b, c) REFERENCES other_table (c1, c2)
        );
    */
    public final static String FOREIGN_KEY = "FOREIGN KEY( %s )";

    /*
    NO ACTION означает, что если зависимые строки продолжают существовать при проверке ограничения,
    возникает ошибка (это поведение по умолчанию). (Главным отличием этих двух вариантов является то,
    что NO ACTION позволяет отложить проверку в процессе транзакции, а RESTRICT — нет.)
     */
    public final static String NO_ACTION = "ON DELETE NO ACTION";

    /*
    CASCADE указывает, что при удалении связанных строк зависимые от них будут так же автоматически удалены.
     */
    public final static String ON_DELETE_CASCADE = "ON DELETE CASCADE";

    /*
    RESTRICT предотвращает удаление связанной строки

    Есть ещё два варианта: SET NULL и SET DEFAULT.
    При удалении связанных строк они назначают зависимым столбцам в подчинённой таблице значения NULL или значения по умолчанию, соответственно.
    Заметьте, что это не будет основанием для нарушения ограничений.
    Например, если в качестве действия задано SET DEFAULT,
    но значение по умолчанию не удовлетворяет ограничению внешнего ключа, операция закончится ошибкой.
     */
    public final static String ON_DELETE_RESTRICT = "ON DELETE RESTRICT";

    /*
    PostgreSQL реализует наследование таблиц, что может быть полезно для проектировщиков баз данных.

    https://postgrespro.ru/docs/postgresql/9.6/ddl-inherit
     */
    public final static String INHERITS = "INHERITS ( %s )";

    /*
    CREATE TYPE mood AS ENUM ('sad', 'ok', 'happy');

    CREATE TYPE someType AS (
        name TEXT,
        age INT2
    );

    INSERT INTO test ( temp, st )
    VALUES(
        '{1, 2, 3, 4}',
        '("smth", 12)'
    )

    INSERT INTO test( temp, st.name )
    VALUES(
        '{1, 2, 3, 4}',
        'dasdsa'
    )

    UPDATE test
    SET st.age = (st).age + 16
    WHERE (st).name = 'smth';
    */
    public final static String CREATE_TYPE = "CREATE TYPE %s AS %s ( %s );";

    /*
    https://www.postgresqltutorial.com/postgresql-triggers/creating-first-trigger-postgresql/

    CREATE FUNCTION price_extension(inventory_item, integer) RETURNS numeric
        AS 'SELECT $1.price * $2' LANGUAGE SQL;

    SELECT price_extension(item, 10) FROM on_hand;

    CREATE FUNCTION test_table_status()
      RETURNS TRIGGER
      LANGUAGE PLPGSQL
      AS
    $$
    BEGIN
        IF NEW.status = 'arrived' THEN
             INSERT INTO log( name, age, status )
             VALUES( OLD.name, OLD.age, OLD.status );
        END IF;

        RETURN NEW;
    END;
    $$
     */
    public final static String CREATE_FUNCTION = "CREATE FUNCTION %s AS %s ( %s );";

    /*
    https://postgrespro.ru/docs/postgresql/9.6/tutorial-views <- docs

    Вы можете создать представление по данному запросу, фактически присвоить имя запросу, а затем обращаться к нему как к обычной таблице:

    Активное использование представлений — это ключевой аспект хорошего проектирования баз данных SQL.
    Представления позволяют вам скрыть внутреннее устройство ваших таблиц,
    которые могут меняться по мере развития приложения, за надёжными интерфейсами.

    Представления можно использовать практически везде, где можно использовать обычные таблицы.
    И довольно часто представления создаются на базе других представлений.

    Example:
        CREATE VIEW myview AS
            SELECT name, temp_lo, temp_hi, prcp, date, location
                FROM weather, cities
                WHERE city = name;

        SELECT * FROM myview;
     */
    public final static String CREATE_VIEW = "CREATE VIEW %s AS %s;";

    /*
    Удалить схему со всеми содержащимися в ней объектами можно так:
        DROP SCHEMA myschema CASCADE;
     */
    public final static String DROP_SCHEMA = "DROP SCHEMA %s %s;";

    /*
    https://postgrespro.ru/docs/postgresql/9.6/ddl-schemas <- docs

    Часто бывает нужно создать схему, владельцем которой будет другой пользователь
    (это один из способов ограничения пользователей пространствами имён).

    Сделать это можно так:
        CREATE SCHEMA имя_схемы AUTHORIZATION имя_пользователя;
     */
    public final static String CREATE_SCHEMA = "CREATE SCHEMA IF NOT EXISTS %s AUTHORIZATION %s;";

    /*
    Если вы больше не хотите использовать вашу базу данных, вы можете удалить её.
    Например, если вы владелец (создатель) базы данных mydb, вы можете уничтожить её, выполнив следующую команду:

    (Эта команда не считает именем БД по умолчанию имя текущего пользователя, вы должны явно указать его.)
    В результате будут физически удалены все файлы, связанные с базой данных, и так как отменить это действие нельзя,
    не выполняйте его, не подумав о последствиях.

    https://postgrespro.ru/docs/postgresql/9.6/app-dropdb
    */
    public final static String DROP_DATABASE = "DROPDB %s";

    /*
    Имена баз данных должны начинаться с буквы и быть не длиннее 63 символов.

    https://postgrespro.ru/docs/postgresql/9.6/app-createdb
    https://postgrespro.ru/docs/postgresql/9.6/sql-createdatabase

    CREATE DATABASE имя
    [ [ WITH ] [ OWNER [=] имя_пользователя ]
           [ TEMPLATE [=] шаблон ]
           [ ENCODING [=] кодировка ]
           [ LC_COLLATE [=] категория_сортировки ]
           [ LC_CTYPE [=] категория_типов_символов ]
           [ TABLESPACE [=] табл_пространство ]
           [ ALLOW_CONNECTIONS [=] разр_подключения ]
           [ CONNECTION LIMIT [=] предел_подключений ]
           [ IS_TEMPLATE [=] это_шаблон ] ]
    */
    public final static String CREATE_DATABASE = "CREATE DATABASE IF NOT EXISTS %s";

    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS";

    /*
    https://postgrespro.ru/docs/postgresql/9.6/functions-trigger <- DOCS

    https://postgrespro.ru/docs/postgresql/9.6/sql-createtrigger

    CREATE TRIGGER создаёт новый триггер.
    Триггер будет связан с указанной таблицей, представлением или сторонней таблицей и будет выполнять заданную функцию имя_функции при определённых событиях.

    Триггер можно настроить так, чтобы он срабатывал до операции со строкой
    (до проверки ограничений и попытки выполнить INSERT, UPDATE или DELETE)
    или после её завершения (после проверки ограничений и выполнения INSERT, UPDATE или DELETE),
    либо вместо операции (при добавлении, изменении и удалении строк в представлении).
    Если триггер срабатывает до или вместо события, он может пропустить операцию с текущей строкой,
    либо изменить добавляемую строку (только для операций INSERT и UPDATE).
    Если триггер срабатывает после события, он «видит» все изменения, включая результат действия других триггеров.

    Триггер с пометкой FOR EACH ROW вызывается один раз для каждой строки, изменяемой в процессе операции.
    Например, операция DELETE, удаляющая 10 строк, приведёт к срабатыванию всех триггеров ON DELETE в целевом отношении 10 раз подряд,
    по одному разу для каждой удаляемой строки.
    Триггер с пометкой FOR EACH STATEMENT, напротив, вызывается только один раз для конкретной операции,
    вне зависимости от того, как много строк она изменила (в частности, при выполнении операции, изменяющей ноль строк,
    всё равно будут вызваны все триггеры FOR EACH STATEMENT).
    Заметьте, что при выполнении INSERT с предложением ON CONFLICT DO UPDATE сработают оба триггера уровня операторов, для INSERT и для UPDATE.

    Триггеры, срабатывающие в режиме INSTEAD OF, должны быть помечены FOR EACH ROW и могут быть определены только для представлений.
    Триггеры BEFORE и AFTER для представлений должны быть помечены FOR EACH STATEMENT.

    Кроме того, триггеры можно определить и для команды TRUNCATE, но только типа FOR EACH STATEMENT.
     */
    public final static String CREATE_TRIGGER = "CREATE TRIGGER %s";

    /*
    https://postgrespro.ru/docs/postgresql/9.6/indexes-intro <- DOCS

    https://postgrespro.ru/docs/postgresql/9.6/indexes-types <- DOCS

    https://postgrespro.ru/docs/postgresql/9.6/indexes-multicolumn <- DOCS

    B-деревья могут работать в условиях на равенство и в проверках диапазонов с данными, которые можно отсортировать в некотором порядке.
    Точнее, планировщик запросов PostgreSQL может задействовать индекс-B-дерево,
    когда индексируемый столбец участвует в сравнении с одним из следующих операторов:
        <
        <=
        =
        >=
        >

    При обработке конструкций, представимых как сочетание этих операторов, например BETWEEN и IN,
    так же может выполняться поиск по индексу-B-дереву.
    Кроме того, такие индексы могут использоваться и в условиях IS NULL и IS NOT NULL по индексированным столбцам.

    Также оптимизатор может использовать эти индексы в запросах с операторами сравнения по шаблону LIKE и ~,
    если этот шаблон определяется константой и он привязан к началу строки — например,
    col LIKE 'foo%' или col ~ '^foo', но не col LIKE '%bar'. Но если ваша база данных использует не локаль C,
    для поддержки индексирования запросов с шаблонами вам потребуется создать индекс со специальным классом операторов;
    Индексы-B-деревья можно использовать и для ILIKE и ~*, но только если шаблон начинается не с алфавитных символов,
    то есть символов, не подверженных преобразованию регистра.

    B-деревья могут также применяться для получения данных, отсортированных по порядку.
    Это не всегда быстрее простого сканирования и сортировки, но иногда бывает полезно.

    Хеш-индексы работают только с простыми условиями равенства.
    Планировщик запросов может применить хеш-индекс, только если индексируемый столбец участвует в сравнении с оператором =.
    Создать такой индекс можно следующей командой:

    CREATE INDEX имя ON таблица USING HASH (столбец);

    GiST-индексы представляют собой не просто разновидность индексов, а инфраструктуру,
    позволяющую реализовать много разных стратегий индексирования.
    Как следствие, GiST-индексы могут применяться с разными операторами, в зависимости от стратегии индексирования (класса операторов).
    Например, стандартный дистрибутив PostgreSQL включает классы операторов GiST для нескольких двумерных типов геометрических данных,
    что позволяет применять индексы в запросах с операторами:
        <<
        &<
        &>
        >>
        <<|
        &<|
        |&>
        |>>
        @>
        <@
        ~=
        &&

    CREATE INDEX test2_info_nulls_low ON test2 (info NULLS FIRST);
    CREATE INDEX test3_desc_index ON test3 (id DESC NULLS LAST);
    */
    public final static String CREATE_INDEX = "CREATE INDEX IF NOT EXISTS %s ON %s ( %s );";

    public final static String NULLS_LAST = "NULLS LAST";

    public final static String NULLS_FIRST = "NULLS FIRST";

    public final static String DEFAULT = "DEFAULT";
}

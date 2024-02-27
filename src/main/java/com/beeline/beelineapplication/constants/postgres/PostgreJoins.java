package com.beeline.beelineapplication.constants.postgres;

/*
https://www.postgresqltutorial.com/postgresql-tutorial/postgresql-joins/ <- DOCS

T1 { [INNER] | { LEFT | RIGHT | FULL } [OUTER] } JOIN T2
  ON логическое_выражение
T1 { [INNER] | { LEFT | RIGHT | FULL } [OUTER] } JOIN T2
  USING ( список столбцов соединения )
T1 NATURAL { [INNER] | { LEFT | RIGHT | FULL } [OUTER] } JOIN T2

Слова INNER и OUTER необязательны во всех формах.
По умолчанию подразумевается INNER (внутреннее соединение), а при указании LEFT, RIGHT и FULL — внешнее соединение.

Условие соединения указывается в предложении ON или USING, либо неявно задаётся ключевым словом NATURAL.
Это условие определяет, какие строки двух исходных таблиц считаются «соответствующими» друг другу.

https://postgrespro.ru/docs/postgresql/9.6/queries-table-expressions <- docs
*/
public final class PostgreJoins {
    /*
    https://postgrespro.ru/docs/postgresql/9.6/queries-table-expressions <- docs

    USING — это сокращённая запись условия, полезная в ситуации, когда с обеих сторон соединения столбцы имеют одинаковые имена.
    Она принимает список общих имён столбцов через запятую и формирует условие соединения с равенством этих столбцов.
    Например, запись соединения T1 и T2 с USING (a, b) формирует условие ON T1.a = T2.a AND T1.b = T2.b.

    Более того, при выводе JOIN USING исключаются избыточные столбцы:
        оба сопоставленных столбца выводить не нужно, так как они содержат одинаковые значения.

    Тогда как JOIN ON выдаёт все столбцы из T1, а за ними все столбцы из T2,
    JOIN USING выводит один столбец для каждой пары (в указанном порядке),
    за ними все оставшиеся столбцы из T1 и, наконец, все оставшиеся столбцы T2.
    */
    public final static String USING = "USING( %s )";

    /*
    Наконец, NATURAL — сокращённая форма USING: она образует список USING из всех имён столбцов,
    существующих в обеих входных таблицах.
    Как и с USING, эти столбцы оказываются в выходной таблице в единственном экземпляре.
    Если столбцов с одинаковыми именами не находится,
    NATURAL JOIN действует как JOIN ... ON TRUE и выдаёт декартово произведение строк.

    WARNING:
    Предложение USING разумно защищено от изменений в соединяемых отношениях, так как оно связывает только явно перечисленные столбцы.
    NATURAL считается более рискованным, так как при любом изменении схемы в одном или другом отношении,
    когда появляются столбцы с совпадающими именами, при соединении будут связываться и эти новые столбцы.
    */
    public final static String NATURAL = "NATURAL( %s )";

    /*
    https://postgrespro.ru/docs/postgresql/9.6/queries-table-expressions <- docs

    Соединённую таблицу образуют все возможные сочетания строк из T1 и T2 (т. е. их декартово произведение),
    а набор её столбцов объединяет в себе столбцы T1 со следующими за ними столбцами T2.

    Если таблицы содержат N и M строк, соединённая таблица будет содержать N * M строк.

    FROM T1 CROSS JOIN T2 равнозначно FROM T1 INNER JOIN T2 ON TRUE.
    Эта запись также равнозначна FROM T1, T2.
    */
    public final static String CROSS_JOIN = "CROSS JOIN";

    /*
    Для каждой строки R1 из T1 в результирующей таблице содержится строка для каждой строки в T2, удовлетворяющей условию соединения с R1.

    The inner join examines each row in the first table (basket_a).
    It compares the value in the fruit_a column with the value in the fruit_b column
    of each row in the second table (basket_b).
    If these values are equal, the inner join creates a new row that contains columns
    from both tables and adds this new row to the result set.

    Example:
        SELECT
            a,
            fruit_a,
            b,
            fruit_b
        FROM
            basket_a
        INNER JOIN basket_b
            ON fruit_a = fruit_b;
     */
    public final static String INNER_JOIN = "INNER JOIN %s ON %s = %s";

    /*
    Сначала выполняется внутреннее соединение (INNER JOIN).
    Затем в результат добавляются все строки из T1, которым не соответствуют никакие строки в T2,
    а вместо значений столбцов T2 вставляются NULL.
    Таким образом, в результирующей таблице всегда будет минимум одна строка для каждой строки из T1.

    The following statement uses the left join clause to join the basket_a table with the basket_b table.
    In the left join context, the first table is called the left table and the second table is called the right table.

    Example:
        SELECT
            a,
            fruit_a,
            b,
            fruit_b
        FROM
            basket_a
        LEFT JOIN basket_b
           ON fruit_a = fruit_b;
    */
    public final static String LEFT_JOIN = "LEFT OUTER JOIN %s ON %s = %s";

    /*
    Сначала выполняется внутреннее соединение (INNER JOIN).
    Затем в результат добавляются все строки из T2, которым не соответствуют никакие строки в T1,
    а вместо значений столбцов T1 вставляются NULL. Это соединение является обратным к левому (LEFT JOIN):
    в результирующей таблице всегда будет минимум одна строка для каждой строки из T2.

    The right join is a reversed version of the left join.
    The right join starts selecting data from the right table.
    It compares each value in the fruit_b column of every row in the right table with each value
    in the fruit_a column of every row in the fruit_a table.

    If these values are equal, the right join creates a new row that contains columns from both tables.

    In case these values are not equal,
    the right join also creates a new row that contains columns from both tables.
    However, it fills the columns in the left table with NULL.

    SELECT
        a,
        fruit_a,
        b,
        fruit_b
    FROM
        basket_a
    RIGHT JOIN basket_b ON fruit_a = fruit_b;
    */
    public final static String RIGHT_JOIN = "RIGHT OUTER JOIN %s ON %s = %s";

    /*
    Сначала выполняется внутреннее соединение.
    Затем в результат добавляются все строки из T1, которым не соответствуют никакие строки в T2,
    а вместо значений столбцов T2 вставляются NULL. И наконец, в результат включаются все строки из T2,
    которым не соответствуют никакие строки в T1, а вместо значений столбцов T1 вставляются NULL.

    The full outer join or full join returns a result set that contains all rows from both left and right tables,
    with the matching rows from both sides if available. In case there is no match,
    the columns of the table will be filled with NULL.

    Example:
        SELECT
            a,
            fruit_a,
            b,
            fruit_b
        FROM
            basket_a
        FULL OUTER JOIN basket_b
            ON fruit_a = fruit_b;
     */
    public final static String FULL_OUTER_JOIN = "FULL OUTER JOIN %s ON %s = %s";

    /*
    Результаты двух запросов можно обработать, используя операции над множествами:
    объединение, пересечение и вычитание.
    Эти операции записываются соответственно так:

        запрос1 UNION [ALL] запрос2
        запрос1 INTERSECT [ALL] запрос2
        запрос1 EXCEPT [ALL] запрос2
    */

    /*
    UNION по сути добавляет результаты второго запроса к результатам первого
    (хотя никакой порядок возвращаемых строк при этом не гарантируется).
    Более того, эта операция убирает дублирующиеся строки из результата так же,
    как это делает DISTINCT, если только не указано UNION ALL.
    */
    public final static String UNION = "UNION %s";

    /*
    INTERSECT возвращает все строки, содержащиеся в результате и первого, и второго запроса.
    Дублирующиеся строки отфильтровываются, если не указано ALL.
    */
    public final static String EXCEPT = "EXCEPT %s";

    /*
    EXCEPT возвращает все строки, которые есть в результате первого запроса, но отсутствуют в результате второго.
    (Иногда это называют разницей двух запросов.) И здесь дублирующиеся строки отфильтровываются, если не указано ALL.
    */
    public final static String INTERSECT = "INTERSECT %s";

    /*
    Для определения места значений NULL можно использовать указания NULLS FIRST и NULLS LAST,
    которые помещают значения NULL соответственно до или после значений не NULL.
    По умолчанию значения NULL считаются больше любых других,
    то есть подразумевается NULLS FIRST для порядка DESC и NULLS LAST в противном случае.
    */
    public final static String NULLS_FIRST = "NULLS FIRST";

    /*
    Для определения места значений NULL можно использовать указания NULLS FIRST и NULLS LAST,
    которые помещают значения NULL соответственно до или после значений не NULL.
    По умолчанию значения NULL считаются больше любых других,
    то есть подразумевается NULLS FIRST для порядка DESC и NULLS LAST в противном случае.
    */
    public final static String NULLS_LAST = "NULLS LAST";

    /*
    WARNING:
    Если указано и OFFSET, и LIMIT, сначала система пропускает OFFSET строк, а затем начинает подсчитывать строки для ограничения LIMIT.

    Применяя LIMIT, важно использовать также предложение ORDER BY, чтобы строки результата выдавались в определённом порядке.
    Иначе будут возвращаться непредсказуемые подмножества строк.
    Вы можете запросить строки с десятой по двадцатую, но какой порядок вы имеете в виду? Порядок будет неизвестен, если не добавить ORDER BY.

    Оптимизатор запроса учитывает ограничение LIMIT, строя планы выполнения запросов,
    поэтому вероятнее всего планы (а значит и порядок строк) будут меняться при разных LIMIT и OFFSET.
    Таким образом, различные значения LIMIT/OFFSET, выбирающие разные подмножества результатов запроса,
    приведут к несогласованности результатов, если не установить предсказуемую сортировку с помощью ORDER BY.
    Это не ошибка, а неизбежное следствие того, что SQL не гарантирует вывод результатов запроса в некотором порядке,
    если порядок не определён явно предложением ORDER BY.

    Строки, пропускаемые согласно предложению OFFSET, тем не менее должны вычисляться на сервере.
    Таким образом, при больших значениях OFFSET работает неэффективно.
    */

    /*
    https://postgrespro.ru/docs/postgresql/9.6/queries-limit <- docs

    Если указывается число LIMIT, в результате возвращается не больше заданного числа строк
    (меньше может быть, если сам запрос выдал меньшее количество строк).
    LIMIT ALL равносильно отсутствию указания LIMIT, как и LIMIT с аргументом NULL.
    */
    public final static String LIMIT = "LIMIT %d";

    /*
    https://postgrespro.ru/docs/postgresql/9.6/queries-limit <- docs

    OFFSET указывает пропустить указанное число строк, прежде чем начать выдавать строки.
    OFFSET 0 равносильно отсутствию указания OFFSET, как и OFFSET с аргументом NULL.
    */
    public final static String OFFSET = "OFFSET %d";

    /*
    WITH предоставляет способ записывать дополнительные операторы для применения в больших запросах.
    Эти операторы, которые также называют общими табличными выражениями (Common Table Expressions, CTE),
    можно представить как определения временных таблиц, существующих только для одного запроса.
    Дополнительным оператором в предложении WITH может быть SELECT, INSERT, UPDATE или DELETE,
    а само предложение WITH присоединяется к основному оператору, которым также может быть SELECT, INSERT, UPDATE или DELETE.

    https://postgrespro.ru/docs/postgresql/9.6/queries-with <- docs

    Основное предназначение SELECT в предложении WITH заключается в разбиении сложных запросов на простые части.
    Например, запрос:
        WITH regional_sales AS (
            SELECT region, SUM(amount) AS total_sales
            FROM orders
            GROUP BY region
           ), top_regions AS (
            SELECT region
            FROM regional_sales
            WHERE total_sales > (SELECT SUM(total_sales)/10 FROM regional_sales)
           )
        SELECT region,
           product,
           SUM(quantity) AS product_units,
           SUM(amount) AS product_sales
        FROM orders
        WHERE region IN (SELECT region FROM top_regions)
        GROUP BY region, product;
     */
    public final static String WITH = "WITH %s AS %s";

    /*
    https://postgrespro.ru/docs/postgresql/9.6/queries-with

    Необязательное указание RECURSIVE превращает WITH из просто удобной синтаксической конструкции
    в средство реализации того, что невозможно в стандартном SQL.
    Используя RECURSIVE, запрос WITH может обращаться к собственному результату.
    Очень простой пример, суммирующий числа от 1 до 100:

    WITH RECURSIVE t(n) AS (
        VALUES (1)
          UNION ALL
            SELECT n+1 FROM t WHERE n < 100
        )
    SELECT sum(n) FROM t;
     */
    public final static String RECURSIVE = "RECURSIVE %s AS %s";
}

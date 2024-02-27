CREATE TABLE IF NOT EXISTS entities.common_params (
    id UUID DEFAULT uuid_generate_v4()
);

CREATE TABLE IF NOT EXISTS entities.common_params_with_timestamp (
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- время регистрации
);


CREATE TABLE IF NOT EXISTS entities.users (
    email TEXT NOT NULL
        CONSTRAINT entities_patruls_email_must_be_unique CHECK ( users.email <> '' ),
    name TEXT NOT NULL
        CONSTRAINT entities_patruls_name_must_be_unique CHECK ( users.name <> '' ),
    surname TEXT NOT NULL
        CONSTRAINT entities_patruls_surname_must_be_unique CHECK ( users.surname <> '' ),
    phoneNumber TEXT NOT NULL
        CONSTRAINT entities_patruls_phoneNumber_must_be_unique CHECK ( users.phoneNumber <> '' ),

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params, entities.common_params_with_timestamp );

CREATE TABLE IF NOT EXISTS auth_schema.user_authorization (
    user_id UUID REFERENCES entities.users ( id ),

    login TEXT,
    password TEXT NOT NULL CONSTRAINT entities_patruls_auth_table_password_cannot_be_empty CHECK ( password <> '' ),

    PRIMARY KEY ( login )
);

CREATE TABLE IF NOT EXISTS entities.products (
    category entities_enums.categories NOT NULL DEFAULT 'FOR_MEN',

    description TEXT NOT NULL,
    productName TEXT NOT NULL,

    price INT8 NOT NULL DEFAULT 0,
    totalCount INT8 NOT NULL DEFAULT 0,
    productWasSoldCount INT8 NOT NULL DEFAULT 0,

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params, entities.common_params_with_timestamp );

CREATE TABLE IF NOT EXISTS entities.orders (
    total_order_sum INT8 NOT NULL DEFAULT 0,
    total_count_of_products_in_order INT8 NOT NULL DEFAULT 0,

    product_list entities_types.product ARRAY NOT NULL,

    userId UUID NOT NULL REFERENCES entities.users( id ),

    orderStatus entities_enums.order_status NOT NULL DEFAULT 'CREATED',

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params, entities.common_params_with_timestamp );
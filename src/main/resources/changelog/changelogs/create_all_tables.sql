CREATE TABLE IF NOT EXISTS entities.common_params (
    id UUID DEFAULT uuid_generate_v4()
);

CREATE TABLE IF NOT EXISTS entities.location_params (
    latitude FLOAT4 NOT NULL DEFAULT 41.0
        CONSTRAINT car_latitude_must_be_larger_than_zero CHECK ( latitude BETWEEN 37.0 AND 45.0 ),

    longitude FLOAT4 NOT NULL DEFAULT 69.27
        CONSTRAINT car_longitude_must_be_larger_than_zero CHECK ( longitude BETWEEN 55.0 AND 74.0 )
);

CREATE TABLE IF NOT EXISTS entities.common_params_with_timestamp (
    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP -- время регистрации
);


CREATE TABLE IF NOT EXISTS entities.patruls (
    rank TEXT NOT NULL
        CONSTRAINT entities_patruls_rank_must_be_unique CHECK ( patruls.rank <> '' ),
    email TEXT NOT NULL
        CONSTRAINT entities_patruls_email_must_be_unique CHECK ( patruls.email <> '' ),
    organ_name TEXT NOT NULL
        CONSTRAINT entities_patruls_organ_name_must_be_unique CHECK ( patruls.organ_name <> '' ),
    police_type TEXT NOT NULL
        CONSTRAINT entities_patruls_police_type_must_be_unique CHECK ( patruls.police_type <> '' ),
    date_of_birth TEXT NOT NULL
        CONSTRAINT entities_patruls_date_of_birth_must_be_unique CHECK ( patruls.date_of_birth <> '' ),
    patrul_image_link TEXT NOT NULL
        CONSTRAINT entities_patruls_patrul_image_link_must_be_unique CHECK ( patruls.patrul_image_link <> '' ),
    passport_number VARCHAR(10) NOT NULL UNIQUE
        CONSTRAINT entities_patruls_passport_number_must_be_unique CHECK ( patruls.passport_number <> '' ),

    total_activity_time INT8 NOT NULL DEFAULT 0,

    in_polygon BOOLEAN NOT NULL DEFAULT false,
    tuple_permission BOOLEAN NOT NULL DEFAULT false,

    patrul_fio_data entities_types.patrul_fio_data NOT NULL,
    patrul_car_info entities_types.patrul_car_info NOT NULL,
    patrul_date_data entities_types.patrul_date_data NOT NULL,
    patrul_auth_data entities_types.patrul_auth_data NOT NULL,
    patrul_task_info entities_types.patrul_task_info NOT NULL,
    patrul_token_info entities_types.patrul_token_info NOT NULL,
    patrul_region_data entities_types.patrul_region_data NOT NULL,
    patrul_location_data entities_types.patrul_location_data NOT NULL,
    patrul_unique_values entities_types.patrul_unique_values NOT NULL,
    patrul_mobile_app_info entities_types.patrul_mobile_app_info NOT NULL,

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params );

CREATE TABLE IF NOT EXISTS entities.patruls_auth_table (
    patrul_id UUID REFERENCES entities.patruls ( id ),

    login TEXT,
    password TEXT NOT NULL CONSTRAINT entities_patruls_auth_table_password_cannot_be_empty CHECK ( password <> '' ),

    PRIMARY KEY ( login )
);

CREATE TABLE IF NOT EXISTS entities.patruls_status_table (
    patrul_id UUID REFERENCES entities.patruls ( id ),

    status entities_enums.status NOT NULL,

    message TEXT NOT NULL,
    total_activity_time INT8 NOT NULL DEFAULT 0,
    info_updated_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY ( patrul_id, info_updated_date, status )
);

CREATE TABLE IF NOT EXISTS entities.android_version_table (
    id TEXT,
    link INET NOT NULL,
    version TEXT NOT NULL,

    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS entities.car_total_data (
    car_number TEXT, -- номер машины
    serialized_object TEXT NOT NULL, -- сериализованный объект CarTotalData

    camera_image INET NOT NULL, -- ссылка на фото машины

    violations_info_list entities_types.violations_info ARRAY NOT NULL DEFAULT '{}', -- список всех штрафов машины

    PRIMARY KEY ( car_number )
);

CREATE TABLE IF NOT EXISTS entities.patrul_cars (
    patrul_id UUID NOT NULL UNIQUE REFERENCES entities.patruls (id), -- ID патрульного привязанного к машине, создаем ссылку на таблицу с патрульными

    car_model TEXT NOT NULL DEFAULT 'cobalt', -- модель машины
    car_number TEXT NOT NULL UNIQUE, -- уникальный номер машины
    tracker_id VARCHAR(14) NOT NULL UNIQUE, -- уникальный ID трекера установленного на машине
    car_image_link TEXT NOT NULL, -- ссылка на фото самой машины

    average_fuel_consumption FLOAT4 NOT NULL DEFAULT 10.0 CHECK ( average_fuel_consumption > 0.0 ),

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params_with_timestamp, entities.common_params );

CREATE TABLE IF NOT EXISTS entities.police_type_table (
    icon TEXT NOT NULL,
    icon2 TEXT NOT NULL,
    police_type TEXT NOT NULL UNIQUE,

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params );

CREATE TABLE IF NOT EXISTS entities.polygon (
    organ UUID NOT NULL,

    regionId INT2 NOT NULL DEFAULT 65,
    mahallaId INT2 NOT NULL DEFAULT 65,
    districtId INT2 NOT NULL DEFAULT 65,

    name TEXT NOT NULL,
    color TEXT NOT NULL DEFAULT 'red',

    polygon_type entities_types.polygon_type NOT NULL,

    latlngs entities_types.polygon_entity ARRAY,
    patrul_list UUID ARRAY,

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params );

CREATE TABLE IF NOT EXISTS entities.lustra (
    lustra_name TEXT NOT NULL UNIQUE,
    car_gos_number TEXT NOT NULL UNIQUE REFERENCES entities.patrul_cars ( car_number ),

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params );

CREATE TABLE IF NOT EXISTS entities.tablets_usage_table (
    patrul_id UUID NOT NULL UNIQUE REFERENCES entities.patruls (id), -- ID патрульного привязанного к машине, создаем ссылку на таблицу с патрульными

    sim_card_number TEXT NOT NULL,
    total_activity_time INT8 NOT NULL DEFAULT 0,

    startedToUse TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lastActiveDate TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY ( patrul_id, sim_card_number )
);

CREATE TABLE IF NOT EXISTS entities.notification (
    type TEXT NOT NULL,
    title TEXT NOT NULL,
    address TEXT NOT NULL,
    task_id TEXT NOT NULL UNIQUE,
    car_number TEXT,
    police_type TEXT REFERENCES entities.police_type_table ( police_type ),
    passport_number TEXT NOT NULL REFERENCES entities.patruls ( passport_number ),
    patrul_full_name TEXT NOT NULL,

    status entities_enums.status NOT NULL,
    task_status entities_enums.status NOT NULL,
    task_types entities_enums.task_types NOT NULL,

    was_read BOOLEAN NOT NULL DEFAULT FALSE,

    PRIMARY KEY ( id, task_id, created_date, task_types )
) INHERITS ( entities.common_params_with_timestamp, entities.common_params );


CREATE TABLE IF NOT EXISTS tracker.trackers_info (
    imei VARCHAR(14) CHECK ( imei <> '' ), -- уникальный IMEI трекера

    car_id UUID REFERENCES entities.patrul_cars( id ),

    last_signal_was_received_date TIMESTAMP DEFAULT NULL, -- время последнего сигнала от трекера

    PRIMARY KEY ( imei )
) INHERITS ( entities.location_params, entities.common_params_with_timestamp );


CREATE TABLE IF NOT EXISTS escort.polygon_for_escort (
    name TEXT NOT NULL UNIQUE,

    uuid_of_escort UUID,

    totalTime INT2 NOT NULL,
    routeIndex INT2 NOT NULL,
    totalDistance INT2 NOT NULL,

    points_list entities_types.points ARRAY NOT NULL,
    latlngs entities_types.polygon_entity ARRAY NOT NULL,

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params );

CREATE TABLE IF NOT EXISTS escort.countries (
    flag TEXT NOT NULL DEFAULT 'NOT FOUND'
        CONSTRAINT escort_countries_flag_cannot_be_empty CHECK ( flag <> '' ),

    symbol TEXT NOT NULL DEFAULT 'NOT FOUND'
        CONSTRAINT escort_countries_symbol_cannot_be_empty CHECK ( symbol <> '' ),

    country_name_uz TEXT NOT NULL UNIQUE
        CONSTRAINT escort_countries_country_name_uz_cannot_be_empty CHECK ( country_name_uz <> '' ),

    country_name_ru TEXT NOT NULL UNIQUE
        CONSTRAINT escort_countries_country_name_ru_cannot_be_empty CHECK ( country_name_ru <> '' ),

    country_name_en TEXT NOT NULL UNIQUE,

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params );

CREATE TABLE IF NOT EXISTS escort.tuple_of_escort (
    country_id UUID REFERENCES escort.countries ( id ), -- ID страны для которой создан эскорт
    uuid_of_polygon UUID REFERENCES escort.polygon_for_escort ( id ), -- ID полигона привязанного для этого эскорт

    patrul_list UUID ARRAY NOT NULL DEFAULT '{}', -- список из ID патрульных привязанных к этому Эскорту
    tuple_of_cars_list UUID ARRAY NOT NULL DEFAULT '{}', -- список из ID машин привязанных к этому Эскорту

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params );


CREATE TABLE IF NOT EXISTS tasks.tasks_storage_table (
    task_id UUID NOT NULL,

    task_type entities_enums.task_types NOT NULL,

    serialized_object TEXT NOT NULL, -- сериализованный объект задачи

    PRIMARY KEY ( id, task_id, task_type )
) INHERITS ( entities.common_params );

CREATE TABLE IF NOT EXISTS tasks.active_tasks (
    id TEXT NOT NULL,
    serialized_object TEXT NOT NULL, -- сериализованный объект ActiveTask

    PRIMARY KEY ( id )
);

CREATE TABLE IF NOT EXISTS tasks.tasks_timing_table (
    task_id TEXT NOT NULL,

    status entities_enums.status NOT NULL,
    task_types entities_enums.task_types NOT NULL,

    patrul_id UUID REFERENCES entities.patruls ( id ),

    total_time_consumption INT8 NOT NULL DEFAULT 0,
    time_wasted_to_arrive INT8 NOT NULL DEFAULT 0,

    date_of_coming TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    in_time BOOLEAN NOT NULL DEFAULT FALSE,
    position_info_list entities_types.position_info ARRAY NOT NULL DEFAULT '{}',

    PRIMARY KEY ( task_id, patrul_id, task_types )
);


CREATE TABLE IF NOT EXISTS sos.patrul_sos_table (
    patrul_id UUID REFERENCES entities.patruls ( id ),

    address TEXT NOT NULL,

    sosWasClosed TIMESTAMP,

    status entities_enums.status NOT NULL DEFAULT 'CREATED',

    patrul_statuses JSONB, -- хранит статусы всех патрульных которые получили этот СОС сигнал

    PRIMARY KEY ( id )
) INHERITS ( entities.common_params_with_timestamp, entities.location_params, entities.common_params );

CREATE TABLE IF NOT EXISTS sos.patrul_sos_list (
    patrul_id UUID REFERENCES entities.patruls ( id ),

    sentSosList UUID ARRAY NOT NULL DEFAULT '{}',
    attachedSosList UUID ARRAY NOT NULL DEFAULT '{}',
    acceptedSosList UUID ARRAY NOT NULL DEFAULT '{}',
    cancelledSosList UUID ARRAY NOT NULL DEFAULT '{}',

    PRIMARY KEY ( patrul_id )
);

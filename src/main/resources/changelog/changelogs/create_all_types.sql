CREATE TYPE entities_types.patrul_fio_data AS (
    name TEXT,
    surname TEXT,
    father_name TEXT,
    patrul_full_name TEXT
);

CREATE TYPE entities_types.patrul_car_info AS (
    car_model TEXT,
    car_number TEXT
);

CREATE TYPE entities_types.patrul_date_data AS (
    task_date TIMESTAMP,
    last_active_date TIMESTAMP,
    started_to_work_date TIMESTAMP,
    date_of_registration TIMESTAMP
);

CREATE TYPE entities_types.patrul_auth_data AS (
    login TEXT,
    password TEXT
);

CREATE TYPE entities_types.patrul_task_info AS (
    taskId TEXT,
    status entities_enums.status,
    task_types entities_enums.task_types,
    list_of_tasks JSONB
);

CREATE TYPE entities_types.patrul_token_info AS (
    special_token TEXT,
    token_for_login TEXT
);

CREATE TYPE entities_types.patrul_region_data AS (
     region_id FLOAT4,
     mahalla_id FLOAT4,
     district_id FLOAT4,

     region_name TEXT,
     district_name TEXT
);

CREATE TYPE entities_types.patrul_location_data AS (
    distance FLOAT4,
    latitude FLOAT4,
    longitude FLOAT4,
    latitude_of_task FLOAT4,
    longitude_of_task FLOAT4
);

CREATE TYPE entities_types.patrul_unique_values AS (
    organ UUID,
    sos_id UUID,
    uuid_of_escort UUID,
    uuid_for_patrul_car UUID,
    uuid_for_escort_car UUID
);

CREATE TYPE entities_types.patrul_mobile_app_info AS (
    phone_number TEXT,
    sim_card_number TEXT,
    battery_level INT2
);

CREATE TYPE entities_types.polygon_type AS (
    id UUID,
    name TEXT
);

CREATE TYPE entities_types.polygon_entity AS (
    longitude FLOAT4,
    latitude FLOAT4
);

CREATE TYPE entities_types.points AS (
    longitude FLOAT4,
    latitude FLOAT4,

    point_id UUID,
    point_name TEXT
);

CREATE TYPE entities_types.violations_info AS (
    decreeStatus INT2,
    amount INT2,

    decreeSerialNumber TEXT,
    violation TEXT,
    division TEXT,
    payDate TEXT,
    address TEXT,
    article TEXT,
    owner TEXT,
    model TEXT,
    bill TEXT
);

CREATE TYPE entities_types.position_info AS (
    longitude FLOAT4,
    latitude FLOAT4
);

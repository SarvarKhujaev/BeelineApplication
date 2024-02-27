CREATE INDEX IF NOT EXISTS entities_patruls_index ON entities.patruls
    ( passport_number NULLS LAST );

CREATE INDEX IF NOT EXISTS entities_patrul_cars_index ON entities.patrul_cars
    ( tracker_id NULLS LAST, car_number NULLS LAST, patrul_id NULLS LAST );

CREATE INDEX IF NOT EXISTS entities_lustra_index ON entities.lustra
    ( car_gos_number NULLS LAST );


CREATE INDEX IF NOT EXISTS tracker_trackers_info_index ON tracker.trackers_info
    ( car_id NULLS LAST );


CREATE INDEX IF NOT EXISTS escort_countries_index ON escort.countries
    ( country_name_uz NULLS LAST, country_name_ru NULLS LAST, country_name_en NULLS LAST );

CREATE INDEX IF NOT EXISTS escort_tuple_of_escort_index ON escort.tuple_of_escort
    ( country_id NULLS LAST, uuid_of_polygon NULLS LAST );

CREATE INDEX IF NOT EXISTS sos_patrul_sos_table_index ON sos.patrul_sos_table
    ( status NULLS LAST );

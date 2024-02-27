CREATE TYPE entities_enums.categories AS ENUM (
    'GAMES',
    'BOOKS',
    'BEAUTY',
    'REMONT',
    'CLOTHES',
    'FOR_CAR',
    'FOR_MEN',
    'FOR_HOUSE',
    'FOR_WOMEN',
    'SELF_MADE',
    'SANTEXNIKA',
    'FOR_CHILDREN',
    'ADULT_PRODUCT',
    'ENTERTAINMENT'
);

CREATE TYPE entities_enums.order_status AS ENUM (
    'PAID',
    'CLOSED',
    'CREATED',
    'ARRIVED',
    'CANCELED'
)

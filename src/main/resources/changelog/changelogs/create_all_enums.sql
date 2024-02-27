CREATE TYPE entities_enums.status AS ENUM (
    'LOGIN',
    'LOGOUT',
    'START_TO_WORK',
    'STOP_TO_WORK',
    'SET_IN_PAUSE',
    'RETURNED_TO_WORK',
    'CREATED',
    'CANCEL',
    'ACCEPTED',
    'LATE',
    'IN_TIME',
    'FREE',
    'ARRIVED',
    'ATTACHED',
    'FINISHED',
    'ACTIVE',
    'IN_ACTIVE',
    'OPTIONAL',
    'FORCE',
    'LAST'
);

CREATE TYPE entities_enums.task_types AS ENUM (
    'CARD_102',
    'SELF_EMPLOYMENT',
    'FREE',
    'ESCORT',
    'ACTIVE_TASK',
    'CARD_DETAILS',
    'FIND_FACE_PERSON',
    'FIND_FACE_CAR',
    'FIND_FACE_EVENT_BODY',
    'FIND_FACE_EVENT_CAR',
    'FIND_FACE_EVENT_FACE'
)

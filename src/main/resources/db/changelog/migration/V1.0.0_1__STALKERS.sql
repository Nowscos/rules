CREATE TABLE stalkers
(
    id               BIGSERIAL                              NOT NULL,
    uuid             UUID                                   NOT NULL,
    actual           BOOLEAN                  DEFAULT FALSE NOT NULL,
    created          TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    tested           TIMESTAMP WITH TIME ZONE,
    user_name        VARCHAR(255),
    stalker_name     VARCHAR(255),
    group_name       VARCHAR(255),
    state            VARCHAR(255),
    first_name       VARCHAR(255)                           NOT NULL,
    last_name        VARCHAR(255),
    chat_id          BIGSERIAL                              NOT NULL,
    message_text     VARCHAR(255),
    last_message_id  INT,
    current_answers  INT,
    attempts         INT,
    passed_questions VARCHAR[],
    CONSTRAINT pk_stalkers_id PRIMARY KEY (id),
    CONSTRAINT uq_stalkers_uuid UNIQUE (uuid),
    CONSTRAINT uq_stalkers_chat_id UNIQUE (chat_id)
);
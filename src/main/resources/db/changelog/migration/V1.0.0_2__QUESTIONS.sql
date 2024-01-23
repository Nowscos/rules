CREATE TABLE questions
(
    id                BIGSERIAL                              NOT NULL,
    uuid              UUID                                   NOT NULL,
    actual            BOOLEAN                   DEFAULT TRUE NOT NULL,
    created           TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    updated           TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    question          VARCHAR(255)                           NOT NULL,
    right_answer      VARCHAR(255)                           NOT NULL,
    CONSTRAINT pk_questions_id PRIMARY KEY (id),
    CONSTRAINT uq_questions_uuid UNIQUE (uuid)
);
CREATE TABLE answers
(
    id                BIGSERIAL                              NOT NULL,
    uuid              UUID                                   NOT NULL,
    actual            BOOLEAN                   DEFAULT TRUE NOT NULL,
    created           TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    updated           TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    answer            VARCHAR(255)                           NOT NULL,
    question_id       BIGINT,
    CONSTRAINT pk_answers_id PRIMARY KEY (id),
    CONSTRAINT uq_answers_uuid UNIQUE (uuid),
    CONSTRAINT fk_answers_question_id FOREIGN KEY (question_id) REFERENCES questions (id)
);
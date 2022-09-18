CREATE TABLE user_code
(
    id               bigserial NOT NULL,
    value            char(20)  NOT NULL,
    date_of_creation timestamp NOT NULL,
    user_id          int8      NOT NULL,
    active           boolean   NOT NULL DEFAULT true,
    CONSTRAINT user_code_pk PRIMARY KEY (id)
);

-- Reference: user_code_user (table: user_code)
ALTER TABLE user_code
    ADD CONSTRAINT user_code_user
        FOREIGN KEY (user_id)
            REFERENCES "user" (id)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;
-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2022-08-30 16:35:09.205

-- tables
-- Table: color
CREATE TABLE color (
                       id serial  NOT NULL,
                       name varchar(20)  NOT NULL,
                       color_code char(6)  NOT NULL,
                       CONSTRAINT color_ak_1 UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                       CONSTRAINT color_pk PRIMARY KEY (id)
);

-- Table: comments_under_order
CREATE TABLE comments_under_order (
                                      id bigserial  NOT NULL,
                                      message varchar(200)  NOT NULL,
                                      user_id int8  NOT NULL,
                                      needlework_order_id int8  NOT NULL,
                                      date_of_creation timestamp  NOT NULL,
                                      CONSTRAINT comments_under_order_pk PRIMARY KEY (id)
);

-- Table: countries
CREATE TABLE countries (
                           name varchar(60)  NOT NULL,
                           CONSTRAINT countries_ak_1 UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                           CONSTRAINT countries_pk PRIMARY KEY (name)
);

-- Table: image
CREATE TABLE image (
                       id bigserial  NOT NULL,
                       url varchar(200)  NOT NULL,
                       name varchar(20)  NOT NULL,
                       user_order_id int8  NOT NULL,
                       CONSTRAINT image_pk PRIMARY KEY (id)
);

-- Table: material
CREATE TABLE material (
                          id serial  NOT NULL,
                          name varchar(60)  NOT NULL,
                          cost_one_square_meter int  NOT NULL,
                          CONSTRAINT material_ak_1 UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                          CONSTRAINT material_pk PRIMARY KEY (id)
);

-- Table: part_size
CREATE TABLE part_size (
                           id bigserial  NOT NULL,
                           name varchar(60)  NOT NULL,
                           width int  NULL DEFAULT null,
                           volume int  NULL DEFAULT null,
                           length int  NULL DEFAULT null,
                           height int  NULL DEFAULT null,
                           user_order_id int8  NOT NULL,
                           CONSTRAINT part_size_pk PRIMARY KEY (id)
);

-- Table: role
CREATE TABLE role (
                      id int  NOT NULL,
                      name varchar(20)  NOT NULL,
                      CONSTRAINT name_uk UNIQUE (name) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                      CONSTRAINT role_pk PRIMARY KEY (id)
);

-- Table: tailoring_order
CREATE TABLE tailoring_order (
                                 id bigserial  NOT NULL,
                                 address_for_send varchar(200)  NOT NULL,
                                 order_description varchar(2000)  NOT NULL,
                                 order_status varchar(60)  NOT NULL,
                                 order_payment_status varchar(60)  NOT NULL,
                                 date_of_creation timestamp  NOT NULL,
                                 is_from_template boolean  NOT NULL,
                                 end_date date  NOT NULL,
                                 cost int  NOT NULL,
                                 count_of_order int  NOT NULL,
                                 material_id int  NOT NULL,
                                 color_id int  NOT NULL,
                                 user_id int8  NOT NULL,
                                 CONSTRAINT tailoring_order_pk PRIMARY KEY (id)
);

-- Table: tailoring_templates
CREATE TABLE tailoring_templates (
                                     id bigserial  NOT NULL,
                                     name varchar(60)  NOT NULL,
                                     active boolean  NOT NULL,
                                     date_registration timestamp  NOT NULL,
                                     cost int  NOT NULL,
                                     type_template varchar(60)  NOT NULL,
                                     images json  NOT NULL,
                                     colour_ids json  NOT NULL,
                                     material_ids json  NOT NULL,
                                     part_size json  NOT NULL,
                                     template_description varchar(2000)  NOT NULL,
                                     CONSTRAINT tailoring_templates_pk PRIMARY KEY (id)
);

-- Table: template_orders
CREATE TABLE template_orders (
                                 user_order_id int8  NOT NULL,
                                 tailoring_templates_id int8  NOT NULL,
                                 CONSTRAINT template_orders_ak_1 UNIQUE (user_order_id, tailoring_templates_id) NOT DEFERRABLE  INITIALLY IMMEDIATE
);

-- Table: user
CREATE TABLE "user" (
                        id bigserial  NOT NULL,
                        phone_number varchar(15)  NOT NULL,
                        password varchar(60)  NOT NULL,
                        city varchar(60)  NULL,
                        country varchar(60)  NULL,
                        email varchar(60)  NULL,
                        firstname varchar(60)  NOT NULL,
                        lastname varchar(60)  NOT NULL,
                        date_registration timestamp  NOT NULL,
                        active boolean  NOT NULL,
                        male boolean  NOT NULL DEFAULT true,
                        user_state varchar(20)  NOT NULL,
                        role_id int  NOT NULL,
                        CONSTRAINT user_pk PRIMARY KEY (id)
);

-- Table: user_code
CREATE TABLE user_code (
                           id bigserial  NOT NULL,
                           value char(20)  NOT NULL,
                           date_of_creation timestamp  NOT NULL,
                           user_id int8  NOT NULL,
                           active boolean  NOT NULL DEFAULT 1,
                           CONSTRAINT user_code_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: comments_under_order_needlework_order (table: comments_under_order)
ALTER TABLE comments_under_order ADD CONSTRAINT comments_under_order_needlework_order
    FOREIGN KEY (needlework_order_id)
        REFERENCES tailoring_order (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: comments_under_order_user (table: comments_under_order)
ALTER TABLE comments_under_order ADD CONSTRAINT comments_under_order_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: image_user_order (table: image)
ALTER TABLE image ADD CONSTRAINT image_user_order
    FOREIGN KEY (user_order_id)
        REFERENCES tailoring_order (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: needlework_order_color (table: tailoring_order)
ALTER TABLE tailoring_order ADD CONSTRAINT needlework_order_color
    FOREIGN KEY (color_id)
        REFERENCES color (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: needlework_order_material (table: tailoring_order)
ALTER TABLE tailoring_order ADD CONSTRAINT needlework_order_material
    FOREIGN KEY (material_id)
        REFERENCES material (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: needlework_order_user (table: tailoring_order)
ALTER TABLE tailoring_order ADD CONSTRAINT needlework_order_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: part_size_user_order (table: part_size)
ALTER TABLE part_size ADD CONSTRAINT part_size_user_order
    FOREIGN KEY (user_order_id)
        REFERENCES tailoring_order (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: template_orders_tailoring_templates (table: template_orders)
ALTER TABLE template_orders ADD CONSTRAINT template_orders_tailoring_templates
    FOREIGN KEY (tailoring_templates_id)
        REFERENCES tailoring_templates (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: template_orders_user_order (table: template_orders)
ALTER TABLE template_orders ADD CONSTRAINT template_orders_user_order
    FOREIGN KEY (user_order_id)
        REFERENCES tailoring_order (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_code_user (table: user_code)
ALTER TABLE user_code ADD CONSTRAINT user_code_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_role (table: user)
ALTER TABLE "user" ADD CONSTRAINT user_role
    FOREIGN KEY (role_id)
        REFERENCES role (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- End of file.


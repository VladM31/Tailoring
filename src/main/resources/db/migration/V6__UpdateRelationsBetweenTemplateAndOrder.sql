DROP TABLE template_orders;
ALTER TABLE tailoring_order
    ADD tailoring_templates_id int8 NULL;

ALTER TABLE tailoring_order
    ADD CONSTRAINT tailoring_order_tailoring_templates
        FOREIGN KEY (tailoring_templates_id)
            REFERENCES tailoring_templates (id)
            NOT DEFERRABLE
                INITIALLY IMMEDIATE
;

ALTER TABLE tailoring_templates RENAME COLUMN date_registration TO date_of_creation;

ALTER TABLE tailoring_order DROP COLUMN is_from_template;
DROP TABLE part_size;
DROP TABLE image;
ALTER TABLE tailoring_order ADD part_sizes json  NOT NULL;
ALTER TABLE tailoring_order ADD images json  NOT NULL;
ALTER TABLE tailoring_templates RENAME COLUMN images TO image_names;
ALTER TABLE tailoring_templates RENAME COLUMN part_size TO template_part_sizes ;

-- V2__update_topic_table.sql

ALTER TABLE topico
    DROP FOREIGN KEY topico_ibfk_1,
    DROP FOREIGN KEY topico_ibfk_2;

ALTER TABLE topico
    DROP COLUMN autor_id,
    DROP COLUMN curso_id,
    ADD COLUMN autor VARCHAR(255),
    ADD COLUMN curso VARCHAR(255);

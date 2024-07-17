-- V3__update_usuario_table.sql

ALTER TABLE usuario
ADD COLUMN correo_electronico VARCHAR(255) NOT NULL UNIQUE;

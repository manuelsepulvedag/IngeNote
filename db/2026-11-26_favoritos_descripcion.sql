-- =====================================================================
-- Descripción de recursos y usuarios favoritos.
-- Aditiva: no modifica ni borra datos existentes.
-- =====================================================================

ALTER TABLE tbl_recurso ADD COLUMN IF NOT EXISTS descripcion text NULL;

CREATE TABLE IF NOT EXISTS tbl_favorito (
    id_favorito          uuid      NOT NULL DEFAULT uuid_generate_v4() PRIMARY KEY,
    id_usuario           uuid      NOT NULL REFERENCES tbl_usuario (id_usuario) ON DELETE CASCADE,
    id_usuario_favorito  uuid      NOT NULL REFERENCES tbl_usuario (id_usuario) ON DELETE CASCADE,
    fecha_creacion       timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_favorito UNIQUE (id_usuario, id_usuario_favorito),
    CONSTRAINT ck_favorito_distinto CHECK (id_usuario <> id_usuario_favorito)
);

-- Igual que el resto de tablas: sin acceso directo desde la API pública de
-- Supabase; el backend se conecta como propietario.
ALTER TABLE tbl_favorito ENABLE ROW LEVEL SECURITY;

-- =====================================================================
-- Migración para la integración front-back (26/11/2026).
-- Pegar completo en Supabase > SQL Editor y ejecutar ANTES de desplegar
-- el backend. Es seguro ejecutarlo más de una vez.
-- =====================================================================

-- 1) Valoraciones de 1 a 5 estrellas.
--    La restricción actual solo admite -1 y 1; con ella cualquier valoración
--    de 2 a 5 estrellas falla. (Al revisarlo había 0 valoraciones guardadas.)
ALTER TABLE tbl_valoracion DROP CONSTRAINT IF EXISTS tbl_valoracion_voto_check;
DELETE FROM tbl_valoracion WHERE voto NOT BETWEEN 1 AND 5;
ALTER TABLE tbl_valoracion
    ADD CONSTRAINT tbl_valoracion_voto_check CHECK (voto BETWEEN 1 AND 5);

-- 2) Respuesta marcada como solución en el foro.
--    La columna ya existe en la base actual; esta línea no hace nada si ya está.
ALTER TABLE tbl_foro_hilo
    ADD COLUMN IF NOT EXISTS id_respuesta_solucion uuid NULL
        REFERENCES tbl_foro_respuesta (id_foro_respuesta) ON DELETE SET NULL;

-- 3) Asignaturas de Ingeniería de Sistemas para la demo (solo existía
--    "Álgebra Lineal"). Ajusten nombres y semestres a su plan de estudios;
--    no se insertan duplicados por nombre.
INSERT INTO tbl_asignatura (id_asignatura, nombre, semestre)
SELECT gen_random_uuid(), v.nombre, v.semestre
FROM (VALUES
    ('Cálculo Diferencial', 1),
    ('Lógica y Representación I', 1),
    ('Cálculo Integral', 2),
    ('Lógica y Representación II', 2),
    ('Física Mecánica', 2),
    ('Estructuras de Datos', 3),
    ('Matemáticas Discretas', 3),
    ('Bases de Datos', 4),
    ('Arquitectura de Computadores', 4),
    ('Ingeniería de Software', 5),
    ('Sistemas Operativos', 5),
    ('Redes y Telecomunicaciones', 6)
) AS v(nombre, semestre)
WHERE NOT EXISTS (SELECT 1 FROM tbl_asignatura a WHERE a.nombre = v.nombre);

-- Verificación
SELECT pg_get_constraintdef(oid) AS check_valoracion
FROM pg_constraint WHERE conname = 'tbl_valoracion_voto_check';
SELECT nombre, semestre FROM tbl_asignatura ORDER BY semestre, nombre;

-- =====================================================================
-- Carreras de la Facultad de Ingeniería y asignaturas iniciales.
-- Basado en el Plan de Estudios V.5 de Ingeniería de Sistemas (UdeA):
--   * Área Básica (Ciencias Exactas y Naturales, Ingeniería Básica):
--     tronco común, se asocia a TODAS las carreras.
--   * Área Profesional: solo Ingeniería de Sistemas.
--   * Semestre = nivel del pensum.
--   * Se omiten cursos genéricos: electivas, líneas de énfasis, cursos
--     alternativos, práctica académica y cursos institucionales.
-- No renombra asignaturas existentes (se reutilizan sus nombres, p. ej.
-- "Bases de Datos"); solo borra "Ingeniería de Software" y "Redes y
-- Telecomunicaciones". Es seguro ejecutarlo
-- más de una vez. Pegar completo en Supabase > SQL Editor.
-- =====================================================================

-- 1) Las 18 carreras.
INSERT INTO tbl_carrera (nombre)
SELECT v.nombre
FROM (VALUES
    ('Bioingeniería'),
    ('Ingeniería Aeroespacial'),
    ('Ingeniería Agroindustrial'),
    ('Ingeniería Ambiental'),
    ('Ingeniería Bioquímica'),
    ('Ingeniería Civil'),
    ('Ingeniería de Materiales'),
    ('Ingeniería de Sistemas'),
    ('Ingeniería de Telecomunicaciones'),
    ('Ingeniería Eléctrica'),
    ('Ingeniería Electrónica'),
    ('Ingeniería Energética'),
    ('Ingeniería Industrial'),
    ('Ingeniería Mecánica'),
    ('Ingeniería Oceanográfica'),
    ('Ingeniería Química'),
    ('Ingeniería Sanitaria'),
    ('Ingeniería Urbana')
) AS v(nombre)
WHERE NOT EXISTS (SELECT 1 FROM tbl_carrera c WHERE c.nombre = v.nombre);

-- 2) "Ingeniería de Software" y "Redes y Telecomunicaciones" no están en el
--    pensum: se borran. Solo si no tienen recursos ni hilos (borrar una
--    asignatura borra en cascada sus recursos).
DELETE FROM tbl_asignatura a
WHERE a.nombre IN ('Ingeniería de Software', 'Redes y Telecomunicaciones')
  AND NOT EXISTS (SELECT 1 FROM tbl_recurso r WHERE r.id_asignatura = a.id_asignatura)
  AND NOT EXISTS (SELECT 1 FROM tbl_foro_hilo h WHERE h.id_asignatura = a.id_asignatura);

--    Lista de asignaturas: (nombre, semestre, comun = tronco común).
DROP TABLE IF EXISTS pensum;
CREATE TEMP TABLE pensum (nombre varchar, semestre int, comun boolean);
INSERT INTO pensum (nombre, semestre, comun) VALUES
    -- Área Básica: tronco común de todas las ingenierías
    ('Descubriendo la Física', 1, true),
    ('Geometría Vectorial y Analítica', 1, true),
    ('Cálculo Diferencial', 1, true),
    ('Álgebra y Trigonometría', 1, true),
    ('Física Mecánica', 2, true),
    ('Álgebra Lineal', 2, true),
    ('Cálculo Integral', 2, true),
    ('Teoría de la Probabilidad y Colas', 3, true),
    ('Ecuaciones Diferenciales y Métodos Numéricos', 4, true),
    ('Métodos Estadísticos', 4, true),
    -- Área Profesional: Ingeniería de Sistemas
    ('Introducción a la Ingeniería de Sistemas', 1, false),
    ('English I', 1, false),
    ('Matemáticas Discretas', 2, false),
    ('Lógica y Representación I', 2, false),
    ('Lectoescritura', 2, false),
    ('English II', 2, false),
    ('Matemáticas Discretas II', 3, false),
    ('Lógica y Representación II', 3, false),
    ('Técnicas de Programación y Laboratorio', 3, false),
    ('English III', 3, false),
    ('Lógica y Representación III', 4, false),
    ('Análisis y Diseño de Sistemas I', 4, false),
    ('Formación Ciudadana y Constitucional', 4, false),
    ('English IV', 4, false),
    ('Teoría de Lenguajes y Laboratorio', 5, false),
    ('Estructuras de Datos', 5, false),
    ('Análisis y Diseño de Sistemas II', 5, false),
    ('English V', 5, false),
    ('Bases de Datos', 6, false),
    ('Arquitectura de Software', 6, false),
    ('Modelos y Simulación de Sistemas I', 6, false),
    ('Proyecto Integrador I', 6, false),
    ('Comunicaciones y Laboratorio', 6, false),
    ('Arquitectura de Computadores', 7, false),
    ('Fundamentos de Sistemas de Información', 7, false),
    ('Calidad de Software', 7, false),
    ('Modelos y Simulación de Sistemas II', 7, false),
    ('Sistemas Operativos', 8, false),
    ('Comunicaciones II', 8, false),
    ('Gestión de Proyectos de Sistemas de Información', 8, false),
    ('Fundamentos de Investigación', 8, false),
    ('Proyecto Integrador II', 8, false),
    ('Ética Profesional', 9, false);

--    Crea solo las que faltan (las existentes se dejan como están).
INSERT INTO tbl_asignatura (nombre, semestre)
SELECT p.nombre, p.semestre FROM pensum p
WHERE NOT EXISTS (SELECT 1 FROM tbl_asignatura a WHERE a.nombre = p.nombre);

-- 3) Relación carrera-asignatura.
--    Tronco común -> todas las carreras.
INSERT INTO tbl_carrera_asignatura (id_carrera, id_asignatura)
SELECT c.id_carrera, a.id_asignatura
FROM tbl_carrera c
CROSS JOIN pensum p
JOIN tbl_asignatura a ON a.nombre = p.nombre
WHERE p.comun
ON CONFLICT (id_carrera, id_asignatura) DO NOTHING;

--    Área profesional -> Ingeniería de Sistemas.
INSERT INTO tbl_carrera_asignatura (id_carrera, id_asignatura)
SELECT c.id_carrera, a.id_asignatura
FROM tbl_carrera c
JOIN pensum p ON NOT p.comun
JOIN tbl_asignatura a ON a.nombre = p.nombre
WHERE c.nombre = 'Ingeniería de Sistemas'
ON CONFLICT (id_carrera, id_asignatura) DO NOTHING;

DROP TABLE IF EXISTS pensum;

-- Opcional: asignar Ingeniería de Sistemas a las cuentas que ya existían
-- sin carrera (quitar los guiones para ejecutarlo).
-- UPDATE tbl_usuario SET id_carrera = (SELECT id_carrera FROM tbl_carrera WHERE nombre = 'Ingeniería de Sistemas')
-- WHERE id_carrera IS NULL;

-- Para agregar materias propias de otra carrera:
--   INSERT INTO tbl_asignatura (nombre, semestre) VALUES ('Mecánica de Fluidos', 4);
--   INSERT INTO tbl_carrera_asignatura (id_carrera, id_asignatura)
--   SELECT c.id_carrera, a.id_asignatura FROM tbl_carrera c, tbl_asignatura a
--   WHERE c.nombre = 'Ingeniería Mecánica' AND a.nombre = 'Mecánica de Fluidos';

-- Verificación: debe dar 18 carreras, Sistemas con 43 asignaturas y las demás con 10.
SELECT c.nombre, count(ca.id_asignatura) AS asignaturas
FROM tbl_carrera c
LEFT JOIN tbl_carrera_asignatura ca ON ca.id_carrera = c.id_carrera
GROUP BY c.nombre
ORDER BY c.nombre;

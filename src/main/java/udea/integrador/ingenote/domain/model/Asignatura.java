package udea.integrador.ingenote.domain.model;

import java.util.UUID;

/** Modelo de negocio; no conoce Spring ni persistencia. */
public record Asignatura(UUID id, String nombre, Integer semestre) {
}
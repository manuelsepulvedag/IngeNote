package udea.integrador.ingenote.domain.model;

import java.util.UUID;

/**
 * Promedio de estrellas de un recurso (0 si no tiene valoraciones), cuántas
 * valoraciones lo forman y la del usuario actual (null si no lo ha valorado).
 */
public record ResumenValoracion(UUID recursoId, double promedio, long cantidadValoraciones,
                                Short valoracionUsuarioActual) {

    public static ResumenValoracion vacio(UUID recursoId) {
        return new ResumenValoracion(recursoId, 0, 0, null);
    }
}

package udea.integrador.ingenote.domain.model;

import java.util.UUID;

public record ResumenValoracion(UUID recursoId, long puntajeNeto, long cantidadVotos,
                                Short votoUsuarioActual) { }

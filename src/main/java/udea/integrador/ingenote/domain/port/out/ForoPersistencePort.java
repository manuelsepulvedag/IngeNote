package udea.integrador.ingenote.domain.port.out;

import udea.integrador.ingenote.domain.model.ForoHilo;
import udea.integrador.ingenote.domain.model.ForoRespuesta;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ForoPersistencePort {
    ForoHilo guardarHilo(ForoHilo hilo);
    Optional<ForoHilo> buscarHilo(UUID id);
    List<ForoHilo> listarHilos(UUID asignaturaId, int pagina, int tamano);
    long contarHilos(UUID asignaturaId);
    long contarRespuestas(UUID hiloId);
    void eliminarHilo(UUID id);
    ForoRespuesta guardarRespuesta(ForoRespuesta respuesta);
    Optional<ForoRespuesta> buscarRespuesta(UUID id);
    List<ForoRespuesta> listarRespuestas(UUID hiloId);
    void eliminarRespuesta(UUID id);
}

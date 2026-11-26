package udea.integrador.ingenote.application.port.in;
import udea.integrador.ingenote.domain.model.ResumenValoracion; import java.util.UUID;
public interface ValoracionUseCase { ResumenValoracion votar(String correo, UUID recurso, short voto); ResumenValoracion retirar(String correo, UUID recurso); ResumenValoracion resumen(String correo, UUID recurso); }

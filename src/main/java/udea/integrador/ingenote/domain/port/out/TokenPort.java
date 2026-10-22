package udea.integrador.ingenote.domain.port.out;

import udea.integrador.ingenote.domain.model.Usuario;

public interface TokenPort {
    String generar(Usuario usuario);
}

package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.exception.CredencialesInvalidasException;
import udea.integrador.ingenote.application.port.in.IniciarSesionCommand;
import udea.integrador.ingenote.application.port.in.IniciarSesionUseCase;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.PasswordCipherPort;
import udea.integrador.ingenote.domain.port.out.TokenPort;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;

import java.util.Locale;

public class InicioSesionService implements IniciarSesionUseCase {
    private final UsuarioPersistencePort usuarios;
    private final PasswordCipherPort passwordCipher;
    private final TokenPort tokenPort;

    public InicioSesionService(UsuarioPersistencePort usuarios, PasswordCipherPort passwordCipher, TokenPort tokenPort) {
        this.usuarios = usuarios;
        this.passwordCipher = passwordCipher;
        this.tokenPort = tokenPort;
    }

    @Override
    public String iniciarSesion(IniciarSesionCommand command) {
        Usuario usuario = usuarios.buscarPorCorreo(command.correo().trim().toLowerCase(Locale.ROOT))
                .orElseThrow(CredencialesInvalidasException::new);
        if (!passwordCipher.coincide(command.contrasena(), usuario.contrasenaHash())) throw new CredencialesInvalidasException();
        return tokenPort.generar(usuario);
    }
}

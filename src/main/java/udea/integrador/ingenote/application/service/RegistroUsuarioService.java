package udea.integrador.ingenote.application.service;

import udea.integrador.ingenote.application.exception.ReglaDeNegocioException;
import udea.integrador.ingenote.application.port.in.RegistrarUsuarioUseCase;
import udea.integrador.ingenote.application.port.in.RegistroUsuarioCommand;
import udea.integrador.ingenote.domain.model.Rol;
import udea.integrador.ingenote.domain.model.Usuario;
import udea.integrador.ingenote.domain.port.out.PasswordCipherPort;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.regex.Pattern;

public class RegistroUsuarioService implements RegistrarUsuarioUseCase {
    private static final Pattern CORREO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private final UsuarioPersistencePort usuarios;
    private final PasswordCipherPort passwordCipher;

    public RegistroUsuarioService(UsuarioPersistencePort usuarios, PasswordCipherPort passwordCipher) {
        this.usuarios = usuarios;
        this.passwordCipher = passwordCipher;
    }

    @Override
    public Usuario registrar(RegistroUsuarioCommand command) {
        String correo = command.correo().trim().toLowerCase(Locale.ROOT);
        if (!CORREO.matcher(correo).matches()) throw new ReglaDeNegocioException("El correo no tiene un formato válido");
        if (usuarios.existePorCorreo(correo)) throw new ReglaDeNegocioException("El correo ya está registrado");
        Rol estudiante = usuarios.buscarRolPorNombre("ESTUDIANTE")
                .orElseThrow(() -> new ReglaDeNegocioException("No existe el rol ESTUDIANTE"));
        Usuario usuario = new Usuario(null, estudiante, command.nombre().trim(), correo,
                passwordCipher.cifrar(command.contrasena()), LocalDateTime.now());
        return usuarios.guardar(usuario);
    }
}

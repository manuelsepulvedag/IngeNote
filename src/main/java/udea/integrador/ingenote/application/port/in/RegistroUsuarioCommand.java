package udea.integrador.ingenote.application.port.in;

import java.util.UUID;

/** idCarrera es opcional: la carrera que estudia el usuario. */
public record RegistroUsuarioCommand(String nombre, String correo, String contrasena, UUID idCarrera) {
}

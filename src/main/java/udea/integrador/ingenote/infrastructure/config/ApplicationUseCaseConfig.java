package udea.integrador.ingenote.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import udea.integrador.ingenote.application.port.in.IniciarSesionUseCase;
import udea.integrador.ingenote.application.port.in.ListarRecursosUseCase;
import udea.integrador.ingenote.application.port.in.RegistrarUsuarioUseCase;
import udea.integrador.ingenote.application.port.in.SubirRecursoUseCase;
import udea.integrador.ingenote.application.service.InicioSesionService;
import udea.integrador.ingenote.application.service.ListarRecursosService;
import udea.integrador.ingenote.application.service.RegistroUsuarioService;
import udea.integrador.ingenote.application.service.SubirRecursoService;
import udea.integrador.ingenote.domain.port.out.ArchivoStoragePort;
import udea.integrador.ingenote.domain.port.out.PasswordCipherPort;
import udea.integrador.ingenote.domain.port.out.RecursoPersistencePort;
import udea.integrador.ingenote.domain.port.out.TokenPort;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;
import udea.integrador.ingenote.domain.port.out.ForoPersistencePort;
import udea.integrador.ingenote.domain.port.out.ValoracionPersistencePort;
import udea.integrador.ingenote.application.port.in.ForoUseCase;
import udea.integrador.ingenote.application.port.in.ValoracionUseCase;
import udea.integrador.ingenote.application.service.ForoService;
import udea.integrador.ingenote.application.service.ValoracionService;
import udea.integrador.ingenote.application.port.in.ConsultarPerfilUseCase;
import udea.integrador.ingenote.application.port.in.EliminarRecursoUseCase;
import udea.integrador.ingenote.application.port.in.ListarAsignaturasUseCase;
import udea.integrador.ingenote.application.service.ConsultarPerfilService;
import udea.integrador.ingenote.application.service.EliminarRecursoService;
import udea.integrador.ingenote.application.service.ListarAsignaturasService;

@Configuration
public class ApplicationUseCaseConfig {
    @Bean
    RegistrarUsuarioUseCase registrarUsuarioUseCase(UsuarioPersistencePort usuarios, PasswordCipherPort passwordCipher) {
        return new RegistroUsuarioService(usuarios, passwordCipher);
    }

    @Bean
    IniciarSesionUseCase iniciarSesionUseCase(UsuarioPersistencePort usuarios, PasswordCipherPort passwordCipher, TokenPort tokenPort) {
        return new InicioSesionService(usuarios, passwordCipher, tokenPort);
    }

    @Bean
    SubirRecursoUseCase subirRecursoUseCase(
            UsuarioPersistencePort usuarios,
            RecursoPersistencePort recursos,
            ArchivoStoragePort storage
    ) {
        return new SubirRecursoService(usuarios, recursos, storage);
    }

    @Bean
    ListarRecursosUseCase listarRecursosUseCase(RecursoPersistencePort recursos) {
        return new ListarRecursosService(recursos);
    }
    @Bean ForoUseCase foroUseCase(ForoPersistencePort foros, RecursoPersistencePort recursos, UsuarioPersistencePort usuarios) { return new ForoService(foros, recursos, usuarios); }
    @Bean ValoracionUseCase valoracionUseCase(ValoracionPersistencePort votos, UsuarioPersistencePort usuarios) { return new ValoracionService(votos, usuarios); }

    @Bean
    ConsultarPerfilUseCase consultarPerfilUseCase(UsuarioPersistencePort usuarios) {
        return new ConsultarPerfilService(usuarios);
    }

    @Bean
    ListarAsignaturasUseCase listarAsignaturasUseCase(RecursoPersistencePort recursos) {
        return new ListarAsignaturasService(recursos);
    }

    @Bean
    EliminarRecursoUseCase eliminarRecursoUseCase(RecursoPersistencePort recursos, UsuarioPersistencePort usuarios) {
        return new EliminarRecursoService(recursos, usuarios);
    }
}

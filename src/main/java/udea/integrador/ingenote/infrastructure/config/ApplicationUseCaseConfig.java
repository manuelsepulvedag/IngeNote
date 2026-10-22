package udea.integrador.ingenote.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import udea.integrador.ingenote.application.port.in.IniciarSesionUseCase;
import udea.integrador.ingenote.application.port.in.RegistrarUsuarioUseCase;
import udea.integrador.ingenote.application.service.InicioSesionService;
import udea.integrador.ingenote.application.service.RegistroUsuarioService;
import udea.integrador.ingenote.domain.port.out.PasswordCipherPort;
import udea.integrador.ingenote.domain.port.out.TokenPort;
import udea.integrador.ingenote.domain.port.out.UsuarioPersistencePort;

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
}

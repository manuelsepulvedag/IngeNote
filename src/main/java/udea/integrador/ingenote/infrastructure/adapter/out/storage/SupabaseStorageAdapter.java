package udea.integrador.ingenote.infrastructure.adapter.out.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import udea.integrador.ingenote.application.exception.AlmacenamientoException;
import udea.integrador.ingenote.domain.port.out.ArchivoStoragePort;

import java.text.Normalizer;
import java.util.UUID;

@Component
public class SupabaseStorageAdapter implements ArchivoStoragePort {
    private final RestClient restClient;
    private final String supabaseUrl;
    private final String serviceRoleKey;
    private final String bucket;

    public SupabaseStorageAdapter(
            @Value("${supabase.url}") String supabaseUrl,
            @Value("${supabase.service-role-key}") String serviceRoleKey,
            @Value("${supabase.storage.bucket:recursos-academicos}") String bucket
    ) {
        this.restClient = RestClient.create();
        this.supabaseUrl = supabaseUrl == null ? "" : supabaseUrl.trim().replaceAll("/+$", "");
        this.serviceRoleKey = serviceRoleKey == null ? "" : serviceRoleKey.trim();
        this.bucket = bucket == null ? "recursos-academicos" : bucket.trim();
    }

    @Override
    public String subir(String carpeta, String nombreOriginal, String contentType, byte[] contenido) {
        if (supabaseUrl.isBlank() || serviceRoleKey.isBlank()) {
            throw new AlmacenamientoException(
                    "Faltan las variables SUPABASE_URL o SUPABASE_SERVICE_ROLE_KEY en la configuración"
            );
        }

        String nombreSeguro = limpiarNombreArchivo(nombreOriginal);
        String rutaObjeto = carpeta + "/" + UUID.randomUUID() + "_" + nombreSeguro;
        String endpointSubida = supabaseUrl + "/storage/v1/object/" + bucket + "/" + rutaObjeto;

        try {
            restClient.post()
                    .uri(endpointSubida)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + serviceRoleKey)
                    .header("apikey", serviceRoleKey)
                    .header(HttpHeaders.CONTENT_TYPE, contentType)
                    .body(contenido)
                    .retrieve()
                    .toBodilessEntity();

            return supabaseUrl + "/storage/v1/object/public/" + bucket + "/" + rutaObjeto;
        } catch (RestClientResponseException ex) {
            throw new AlmacenamientoException(
                    "Error de Supabase Storage (" + ex.getStatusCode().value() + "): " + ex.getResponseBodyAsString(),
                    ex
            );
        } catch (Exception ex) {
            throw new AlmacenamientoException("No fue posible conectar con Supabase Storage", ex);
        }
    }

    private String limpiarNombreArchivo(String nombreOriginal) {
        if (nombreOriginal == null || nombreOriginal.isBlank()) {
            return "archivo";
        }
        String sinTildes = Normalizer.normalize(nombreOriginal, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        return sinTildes.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
package udea.integrador.ingenote.domain.port.out;

public interface ArchivoStoragePort {
    String subir(String carpeta, String nombreOriginal, String contentType, byte[] contenido);
}
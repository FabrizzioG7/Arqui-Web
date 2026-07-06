package com.noistop.noistop.services;

import com.noistop.noistop.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final List<String> EXTENSIONES_PERMITIDAS =
            List.of("jpg", "jpeg", "png", "gif", "webp");

    private static final long TAMANO_MAXIMO_BYTES = 5L * 1024 * 1024; // 5MB

    private final Path uploadDir;

    public FileStorageService(@Value("${app.upload.dir:uploads}") String directorio) {
        this.uploadDir = Paths.get(directorio).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de evidencias: " + directorio, e);
        }
    }

    /**
     * Valida y guarda el archivo con un nombre único. Retorna el nombre
     * generado (no la ruta completa), que es lo que se guarda en la BD.
     */
    public String guardar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Debes seleccionar una imagen de evidencia.");
        }
        if (file.getSize() > TAMANO_MAXIMO_BYTES) {
            throw new IllegalArgumentException("La imagen supera el tamaño máximo permitido (5MB).");
        }

        String nombreOriginal = StringUtils.cleanPath(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "archivo");
        String extension = "";
        int idx = nombreOriginal.lastIndexOf('.');
        if (idx >= 0) {
            extension = nombreOriginal.substring(idx + 1).toLowerCase();
        }

        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new IllegalArgumentException(
                    "Formato no permitido. Solo se aceptan imágenes: " + String.join(", ", EXTENSIONES_PERMITIDAS));
        }

        String nombreGuardado = UUID.randomUUID() + "." + extension;

        try {
            Path destino = this.uploadDir.resolve(nombreGuardado).normalize();
            Files.copy(file.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo guardar el archivo de evidencia.", e);
        }

        return nombreGuardado;
    }

    public Resource cargarComoResource(String nombreArchivo) {
        try {
            Path archivo = this.uploadDir.resolve(nombreArchivo).normalize();
            Resource resource = new UrlResource(archivo.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("No se encontró el archivo de evidencia: " + nombreArchivo);
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("No se encontró el archivo de evidencia: " + nombreArchivo);
        }
    }

    public void eliminar(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isBlank()) return;
        try {
            Path archivo = this.uploadDir.resolve(nombreArchivo).normalize();
            Files.deleteIfExists(archivo);
        } catch (IOException e) {
            // No queremos que falle el borrado del registro en BD si el archivo físico ya no existe
        }
    }
}

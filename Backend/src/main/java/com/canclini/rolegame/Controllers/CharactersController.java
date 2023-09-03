package com.canclini.rolegame.Controllers;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") // Allows the client consume this API
@RequestMapping("/characters")
public class CharactersController {

    @GetMapping("/{raceName}/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable String raceName, @PathVariable String imageId) {
        try {
            // Cargar la imagen desde el sistema de archivos
            Resource resource = new ClassPathResource("characters/" + raceName + "/" + imageId);
            // Verificar que la imagen existe
            if (resource.exists()) {
                // Construir la respuesta con la imagen y el tipo de contenido adecuado
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) // Cambia el tipo de contenido según el formato de tus imágenes
                        .body(resource);
            } else {
                // Si la imagen no existe, devolver una respuesta de error
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Manejar cualquier excepción que ocurra al cargar la imagen
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

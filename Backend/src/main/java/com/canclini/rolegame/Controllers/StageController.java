package com.canclini.rolegame.Controllers;
import com.canclini.rolegame.Game.Entities.Stage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*") // Allows the client consume this API
@RequestMapping("/stages")
public class StageController {
    @GetMapping
    public Map<Integer, Stage> getStageList(){
        return RoomController.stageList;
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        try {
            // Cargar la imagen desde el sistema de archivos
            Resource resource = new ClassPathResource("stages/images/" + imageName);

            // Verificar que la imagen existe
            if (resource.exists()) {
                // Construir la respuesta con la imagen y el tipo de contenido adecuado
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // Cambia el tipo de contenido según el formato de tus imágenes
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

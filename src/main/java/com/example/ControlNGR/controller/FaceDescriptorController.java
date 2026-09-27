package com.example.ControlNGR.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ControlNGR.dto.FaceDescriptorDTO;
import com.example.ControlNGR.dto.FaceEnrollRequestDTO;
import com.example.ControlNGR.service.FaceDescriptorService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/face")
public class FaceDescriptorController {

    @Autowired
    private FaceDescriptorService faceDescriptorService;

    @PostMapping("/enroll")
    public ResponseEntity<?> enrollFace(@RequestBody FaceEnrollRequestDTO request) {
        try {
            faceDescriptorService.enrollFaceDescriptors(request);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Rostro registrado exitosamente"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/descriptors/{empleadoId}")
    public ResponseEntity<?> getDescriptors(@PathVariable Integer empleadoId) {
        try {
            List<FaceDescriptorDTO> descriptors = faceDescriptorService.getDescriptorsByEmpleado(empleadoId);
            return ResponseEntity.ok(descriptors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener descriptores: " + e.getMessage()));
        }
    }

    @GetMapping("/enrolled/{empleadoId}")
    public ResponseEntity<?> checkEnrollment(@PathVariable Integer empleadoId) {
        try {
            boolean enrolled = faceDescriptorService.isEnrolled(empleadoId);
            return ResponseEntity.ok(Map.of("enrolled", enrolled));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al verificar registro: " + e.getMessage()));
        }
    }

    @DeleteMapping("/descriptors/{empleadoId}")
    public ResponseEntity<?> deleteDescriptors(@PathVariable Integer empleadoId) {
        try {
            faceDescriptorService.deleteDescriptors(empleadoId);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Descriptores faciales eliminados"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al eliminar descriptores: " + e.getMessage()));
        }
    }
}

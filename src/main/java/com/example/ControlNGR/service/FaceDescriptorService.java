package com.example.ControlNGR.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ControlNGR.dto.FaceDescriptorDTO;
import com.example.ControlNGR.dto.FaceEnrollRequestDTO;
import com.example.ControlNGR.entity.Empleado;
import com.example.ControlNGR.entity.FaceDescriptor;
import com.example.ControlNGR.repository.FaceDescriptorRepository;
import com.example.ControlNGR.repository.EmpleadoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FaceDescriptorService {

    private static final Logger logger = LoggerFactory.getLogger(FaceDescriptorService.class);

    @Autowired
    private FaceDescriptorRepository faceDescriptorRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void enrollFaceDescriptors(FaceEnrollRequestDTO request) {
        if (request.getEmpleadoId() == null) {
            throw new RuntimeException("ID de empleado requerido");
        }
        if (request.getDescriptors() == null || request.getDescriptors().isEmpty()) {
            throw new RuntimeException("Se requiere al menos un descriptor facial");
        }

        Optional<Empleado> empleadoOpt = empleadoRepository.findById(request.getEmpleadoId());
        if (!empleadoOpt.isPresent()) {
            throw new RuntimeException("Empleado no encontrado");
        }

        Empleado empleado = empleadoOpt.get();

        // Desactivar descriptores anteriores
        List<FaceDescriptor> oldDescriptors = faceDescriptorRepository
                .findByEmpleadoIdAndActivo(empleado.getId(), true);
        for (FaceDescriptor old : oldDescriptors) {
            old.setActivo(false);
        }
        faceDescriptorRepository.saveAll(oldDescriptors);

        // Guardar nuevos descriptores
        for (double[] descriptorArray : request.getDescriptors()) {
            try {
                String descriptorJson = objectMapper.writeValueAsString(descriptorArray);
                FaceDescriptor descriptor = new FaceDescriptor();
                descriptor.setEmpleado(empleado);
                descriptor.setDescriptor(descriptorJson);
                descriptor.setFechaRegistro(LocalDateTime.now());
                descriptor.setActivo(true);
                descriptor.setComentario(request.getComentario());
                faceDescriptorRepository.save(descriptor);
            } catch (Exception e) {
                logger.warn("Error al serializar descriptor facial: {}", e.getMessage());
                throw new RuntimeException("Error al guardar descriptor facial");
            }
        }

        logger.info("Registrados {} descriptores faciales para empleado {}",
                request.getDescriptors().size(), empleado.getNombre());
    }

    @Transactional(readOnly = true)
    public List<FaceDescriptorDTO> getDescriptorsByEmpleado(Integer empleadoId) {
        List<FaceDescriptor> descriptors = faceDescriptorRepository
                .findByEmpleadoIdAndActivo(empleadoId, true);

        return descriptors.stream().map(d -> {
            try {
                FaceDescriptorDTO dto = new FaceDescriptorDTO();
                dto.setId(d.getId());
                dto.setEmpleadoId(empleadoId);
                dto.setDescriptor(objectMapper.readValue(d.getDescriptor(), double[].class));
                dto.setComentario(d.getComentario());
                return dto;
            } catch (Exception e) {
                logger.warn("Error al deserializar descriptor {}: {}", d.getId(), e.getMessage());
                throw new RuntimeException("Error al leer descriptor facial");
            }
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isEnrolled(Integer empleadoId) {
        Long count = faceDescriptorRepository.countByEmpleadoIdAndActivo(empleadoId, true);
        return count != null && count > 0;
    }

    @Transactional
    public void deleteDescriptors(Integer empleadoId) {
        faceDescriptorRepository.deleteByEmpleadoId(empleadoId);
        logger.info("Eliminados descriptores faciales del empleado ID {}", empleadoId);
    }
}

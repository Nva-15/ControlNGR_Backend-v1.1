package com.example.ControlNGR.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.ControlNGR.entity.FaceDescriptor;
import java.util.List;

@Repository
public interface FaceDescriptorRepository extends JpaRepository<FaceDescriptor, Integer> {

    List<FaceDescriptor> findByEmpleadoIdAndActivo(Integer empleadoId, Boolean activo);

    Long countByEmpleadoIdAndActivo(Integer empleadoId, Boolean activo);

    void deleteByEmpleadoId(Integer empleadoId);
}

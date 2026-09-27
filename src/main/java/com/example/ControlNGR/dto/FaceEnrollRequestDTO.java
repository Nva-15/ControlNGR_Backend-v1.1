package com.example.ControlNGR.dto;

import java.util.List;

public class FaceEnrollRequestDTO {
    private Integer empleadoId;
    private List<double[]> descriptors;
    private String comentario;

    public Integer getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }

    public List<double[]> getDescriptors() { return descriptors; }
    public void setDescriptors(List<double[]> descriptors) { this.descriptors = descriptors; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}

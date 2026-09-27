package com.example.ControlNGR.dto;

public class FaceDescriptorDTO {
    private Integer id;
    private Integer empleadoId;
    private double[] descriptor;
    private String comentario;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }

    public double[] getDescriptor() { return descriptor; }
    public void setDescriptor(double[] descriptor) { this.descriptor = descriptor; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}

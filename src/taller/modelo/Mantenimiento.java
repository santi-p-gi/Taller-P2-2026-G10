package taller.modelo;

import java.time.LocalDateTime;

import taller.enumeraciones.TipoMantenimiento;

public class Mantenimiento {
    private static int generadorId;
    private final int idMantenimiento;
    private LocalDateTime fecha;
    private String descripcion;
    private TipoMantenimiento tipo;
    private Sensor sensor;
    private ResponsableTecnico responsableTecnico;

    public Mantenimiento() {
        this.idMantenimiento = generadorId++;
    }

    public Mantenimiento(LocalDateTime fecha, String descripcion, TipoMantenimiento tipo,
            Sensor sensor, ResponsableTecnico responsableTecnico) {
        this.idMantenimiento = generadorId++;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.sensor = sensor;
        this.responsableTecnico = responsableTecnico;

    }

    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoMantenimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMantenimiento tipo) {
        this.tipo = tipo;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public ResponsableTecnico getResponsableTecnico() {
        return responsableTecnico;
    }

    public void setResponsableTecnico(ResponsableTecnico responsableTecnico) {
        this.responsableTecnico = responsableTecnico;
    }

}

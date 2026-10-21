package taller.modelo;

import java.util.ArrayList;
import java.util.List;

public class ResponsableTecnico extends Persona {
    private String especialidad;
    private String numeroLicencia;
    private EquipoRespuesta equipoRespuesta;
    private List<EstacionMeteorologica> estacionesAsignadas;
    private List<Mantenimiento> mantenimientosAsignados;

    public ResponsableTecnico(String dni, String nombre, String correoElectronico, String celular) {
        super(dni, nombre, correoElectronico, celular);
    }

    public ResponsableTecnico(String dni, String nombre, String correoElectronico, String celular, String especialidad,
            String numeroLicencia) {
        super(dni, nombre, correoElectronico, celular);
        this.especialidad = especialidad;
        this.numeroLicencia = numeroLicencia;
        this.estacionesAsignadas = new ArrayList<>();
        this.mantenimientosAsignados = new ArrayList<>();
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public EquipoRespuesta getEquipoRespuesta() {
        return equipoRespuesta;
    }

    public void setEquipoRespuesta(EquipoRespuesta equipoRespuesta) {
        this.equipoRespuesta = equipoRespuesta;
    }

    public List<EstacionMeteorologica> getEstacionesAsignadas() {
        return estacionesAsignadas;
    }

    public void setEstacionesAsignadas(List<EstacionMeteorologica> estacionesAsignadas) {
        this.estacionesAsignadas = estacionesAsignadas;
    }

    public List<Mantenimiento> getMantenimientosAsignados() {
        return mantenimientosAsignados;
    }

    public void setMantenimientosAsignados(List<Mantenimiento> mantenimientosAsignados) {
        this.mantenimientosAsignados = mantenimientosAsignados;
    }

}

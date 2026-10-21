package taller.modelo;

import java.util.List;
import java.util.ArrayList;

public class EquipoRespuesta {
    private String idEquipo;
    private String nombre;
    private boolean disponible;
    private List<Alerta> alertasAsignadas;
    private List<ResponsableTecnico> ResponsablesAsignados;

    public EquipoRespuesta() {
        this.disponible = true;
        this.alertasAsignadas = new ArrayList<>();
        this.ResponsablesAsignados = new ArrayList<>();
    }

    public EquipoRespuesta(String idEquipo, String nombre, boolean disponible) {
        this.idEquipo = idEquipo;
        this.nombre = nombre;
        this.disponible = disponible;
        this.alertasAsignadas = new ArrayList<>();
        this.ResponsablesAsignados = new ArrayList<>();
    }

    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public List<Alerta> getAlertasAsignadas() {
        return alertasAsignadas;
    }

    public void setAlertasAsignadas(List<Alerta> alertasAsignadas) {
        this.alertasAsignadas = alertasAsignadas;
    }

    public List<ResponsableTecnico> getResponsablesAsignados() {
        return ResponsablesAsignados;
    }

    public void setResponsablesAsignados(List<ResponsableTecnico> responsablesAsignados) {
        ResponsablesAsignados = responsablesAsignados;
    }

    public void agregarAlerta(Alerta alerta) {
        this.alertasAsignadas.add(alerta);
    }

    public void agregarResponsable(ResponsableTecnico responsable) {
        this.ResponsablesAsignados.add(responsable);
    }

    public void eliminarAlerta(Alerta alerta) {
        this.alertasAsignadas.remove(alerta);
    }

}

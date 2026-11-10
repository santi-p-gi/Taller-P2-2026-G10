package taller.modelo;

import java.util.List;
import java.util.ArrayList;

public class EquipoRespuesta {
    private static int generadorId;
    private final int idEquipo;
    private String nombre;
    private boolean disponible;
    private List<Alerta> alertasAsignadas;
    private List<ResponsableTecnico> responsablesAsignados;

    public EquipoRespuesta() {
        this.idEquipo = generadorId++;
        this.disponible = true;
        this.alertasAsignadas = new ArrayList<>();
        this.responsablesAsignados = new ArrayList<>();
    }

    public EquipoRespuesta(String nombre, boolean disponible) {
        this.idEquipo = generadorId++;
        this.nombre = nombre;
        this.disponible = disponible;
        this.alertasAsignadas = new ArrayList<>();
        this.responsablesAsignados = new ArrayList<>();
    }

    public int getIdEquipo() {
        return idEquipo;
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
        return responsablesAsignados;
    }

    public void setResponsablesAsignados(List<ResponsableTecnico> responsablesAsignados) {
        this.responsablesAsignados = responsablesAsignados;
    }

    public void agregarAlerta(Alerta alerta) {
        this.alertasAsignadas.add(alerta);
    }

    public void eliminarAlerta(Alerta alerta) {
        this.alertasAsignadas.remove(alerta);
    }

    public void agregarIntegrante(ResponsableTecnico i) {
        if (i.getEquipoRespuesta() == this) {
            return;
        }
        if (i.getEquipoRespuesta() != null) {
            throw new IllegalStateException("El tecnico ya pertenece a un equipo");
        }
        this.responsablesAsignados.add(i);
        i.setEquipoRespuesta(this);
    }

    public boolean tieneIntegrantes() {
        return !this.responsablesAsignados.isEmpty();
    }

    public void quitarIntegrante(ResponsableTecnico i) {
        if (this.responsablesAsignados.remove(i)) {
            i.setEquipoRespuesta(null);
        }
    }

    public String toString() {
        return "EquipoRespuesta [idEquipo=" + idEquipo + ", nombre=" + nombre + ", disponible=" + disponible
                + ", integrantes=" + responsablesAsignados.size() + ", alertasAsignadas=" + alertasAsignadas.size()
                + "]";
    }

}

package taller.modelo;

import java.util.List;
import java.util.ArrayList;

/**
 * Equipo de respuesta tecnica para emergencias, integrado por uno o mas responsables tecnicos.
 */
public class EquipoRespuesta {
    private static int generadorId;
    private final int idEquipo;
    private String nombre;
    private boolean disponible;
    private List<Alerta> alertasAsignadas;
    private List<ResponsableTecnico> responsablesAsignados;

    /**
     * Crea un equipo disponible y sin integrantes, con ID autogenerado.
     */
    public EquipoRespuesta() {
        this.idEquipo = ++generadorId;
        this.disponible = true;
        this.alertasAsignadas = new ArrayList<>();
        this.responsablesAsignados = new ArrayList<>();
    }

    /**
     * Crea un equipo sin integrantes, con ID autogenerado.
     *
     * @param nombre     nombre del equipo
     * @param disponible si el equipo puede recibir alertas
     */
    public EquipoRespuesta(String nombre, boolean disponible) {
        this.idEquipo = ++generadorId;
        this.nombre = nombre;
        this.disponible = disponible;
        this.alertasAsignadas = new ArrayList<>();
        this.responsablesAsignados = new ArrayList<>();
    }

    /** {@return el identificador del equipo} */
    public int getIdEquipo() {
        return idEquipo;
    }

    /** {@return el nombre del equipo} */
    public String getNombre() {
        return nombre;
    }

    /**
     * Modifica el nombre del equipo.
     *
     * @param nombre el nuevo valor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** {@return true si el equipo esta disponible} */
    public boolean isDisponible() {
        return disponible;
    }

    /**
     * Indica si el equipo esta disponible.
     *
     * @param disponible true si el equipo esta disponible
     */
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    /** {@return las alertas asignadas al equipo} */
    public List<Alerta> getAlertasAsignadas() {
        return alertasAsignadas;
    }

    /**
     * Modifica las alertas asignadas al equipo.
     *
     * @param alertasAsignadas el nuevo valor
     */
    public void setAlertasAsignadas(List<Alerta> alertasAsignadas) {
        this.alertasAsignadas = alertasAsignadas;
    }

    /** {@return los integrantes del equipo} */
    public List<ResponsableTecnico> getResponsablesAsignados() {
        return responsablesAsignados;
    }

    /**
     * Modifica los integrantes del equipo.
     *
     * @param responsablesAsignados el nuevo valor
     */
    public void setResponsablesAsignados(List<ResponsableTecnico> responsablesAsignados) {
        this.responsablesAsignados = responsablesAsignados;
    }

    /**
     * Agrega una alerta a la lista de alertas asignadas al equipo.
     *
     * @param alerta alerta a agregar
     */
    public void agregarAlerta(Alerta alerta) {
        this.alertasAsignadas.add(alerta);
    }

    /**
     * Quita una alerta de la lista de alertas asignadas al equipo.
     *
     * @param alerta alerta a quitar
     */
    public void eliminarAlerta(Alerta alerta) {
        this.alertasAsignadas.remove(alerta);
    }

    /**
     * Agrega un tecnico al equipo. Si ya integra este equipo, no hace nada.
     *
     * @param i tecnico a agregar
     * @throws IllegalStateException si el tecnico ya integra otro equipo
     */
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

    /**
     * Indica si el equipo tiene al menos un integrante.
     *
     * @return true si tiene integrantes
     */
    public boolean tieneIntegrantes() {
        return !this.responsablesAsignados.isEmpty();
    }

    /**
     * Quita un tecnico del equipo, si lo integraba.
     *
     * @param i tecnico a quitar
     */
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

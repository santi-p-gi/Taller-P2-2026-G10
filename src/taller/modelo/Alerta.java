package taller.modelo;

import java.time.LocalDateTime;

import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.TipoEventoClimatico;
import taller.excepciones.AlertaYaAtendidaException;
import taller.excepciones.EquipoSinIntegrantesException;

/**
 * Alerta emitida por una zona geografica. Las de nivel NARANJA o ROJA requieren un equipo de respuesta.
 */
public class Alerta {
    private static int generadorId;
    private final int idAlerta;
    private LocalDateTime fechaHora;
    private NivelAlerta nivel;
    private TipoEventoClimatico tipoEvento;
    private String descripcion;
    private EquipoRespuesta equipoRespuesta;
    private ZonaGeografica zonaGeografica;
    private boolean atendida;

    /**
     * Crea una alerta vacia con ID autogenerado.
     */
    public Alerta() {
        this.idAlerta = ++generadorId;
    }

    /**
     * Crea una alerta no atendida. Si se indica un equipo y el nivel lo requiere, se le asigna.
     *
     * @param fechaHora       fecha y hora de emision
     * @param nivel           nivel de severidad
     * @param tipoEvento      tipo de evento climatico
     * @param descripcion     descripcion de la alerta
     * @param equipoRespuesta equipo a asignar, o null si todavia no tiene
     * @param zonaGeografica  zona que emite la alerta
     */
    public Alerta(LocalDateTime fechaHora, NivelAlerta nivel,
            TipoEventoClimatico tipoEvento, String descripcion, EquipoRespuesta equipoRespuesta,
            ZonaGeografica zonaGeografica) {
        if (fechaHora.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha de la alerta no puede ser posterior a la fecha actual.");
        }
        this.idAlerta = ++generadorId;
        this.fechaHora = fechaHora;
        this.nivel = nivel;
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.atendida = false;
        this.zonaGeografica = zonaGeografica;
        if (equipoRespuesta != null && requiereEquipoRespuesta()) {
            asignarEquipo(equipoRespuesta);
        }
    }

    public int getIdAlerta() {
        return idAlerta;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public NivelAlerta getNivel() {
        return nivel;
    }

    public void setNivel(NivelAlerta nivel) {
        this.nivel = nivel;
    }

    public TipoEventoClimatico getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(TipoEventoClimatico tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EquipoRespuesta getEquipoRespuesta() {
        return equipoRespuesta;
    }

    public void setEquipoRespuesta(EquipoRespuesta equipoRespuesta) {
        this.equipoRespuesta = equipoRespuesta;
    }

    public ZonaGeografica getZonaGeografica() {
        return zonaGeografica;
    }

    public void setZonaGeografica(ZonaGeografica zonaGeografica) {
        this.zonaGeografica = zonaGeografica;
    }

    public boolean isAtendida() {
        return atendida;
    }

    /**
     * Asigna un equipo de respuesta a la alerta y lo deja no disponible.
     *
     * @param e equipo a asignar
     * @throws IllegalStateException         si la alerta no requiere equipo o el equipo no esta disponible
     * @throws IllegalArgumentException      si el equipo es null
     * @throws EquipoSinIntegrantesException si el equipo no tiene integrantes
     */
    public void asignarEquipo(EquipoRespuesta e) throws IllegalStateException, EquipoSinIntegrantesException {
        if (!requiereEquipoRespuesta()) {
            throw new IllegalStateException("La alerta no requiere de un equipo de respuesta");
        }
        if (e == null) {
            throw new IllegalArgumentException("Debe indicarse un equipo de respuesta");
        }
        if (!e.isDisponible()) {
            throw new IllegalStateException("El equipo " + e.getNombre() + " no esta disponible");
        }
        if (!e.tieneIntegrantes()) {
            throw new EquipoSinIntegrantesException("El equipo de respuesta esta vacio");
        }
        this.equipoRespuesta = e;
        e.agregarAlerta(this);
        e.setDisponible(false);
    }

    /**
     * Indica si la alerta requiere un equipo y todavia no tiene uno asignado.
     *
     * @return true si esta pendiente de equipo
     */
    public boolean estaPendienteDeEquipo() {
        return requiereEquipoRespuesta() && equipoRespuesta == null;
    }

    /**
     * Marca la alerta como atendida y libera a su equipo de respuesta, si tenia.
     *
     * @throws AlertaYaAtendidaException si la alerta ya estaba atendida
     * @throws IllegalStateException     si es NARANJA o ROJA y no tiene equipo asignado
     */
    public void marcarAtendida() throws AlertaYaAtendidaException {
        if (atendida) {
            throw new AlertaYaAtendidaException("La alerta ya fue atendida");
        }
        if (requiereEquipoRespuesta() && equipoRespuesta == null) {
            throw new IllegalStateException(
                    "La alerta requiere un equipo de respuesta asignado antes de marcarla como atendida.");
        }
        atendida = true;
        if (this.equipoRespuesta != null) {
            equipoRespuesta.setDisponible(true);
        }
    }

    /**
     * Indica si el nivel de la alerta exige un equipo de respuesta (NARANJA o ROJA).
     *
     * @return true si requiere equipo
     */
    public boolean requiereEquipoRespuesta() {
        return this.nivel == NivelAlerta.ROJA || this.nivel == NivelAlerta.NARANJA;
    }

    public String toString() {
        return "Alerta [idAlerta=" + idAlerta + ", fechaHora=" + fechaHora + ", nivel=" + nivel + ", tipoEvento="
                + tipoEvento + ", descripcion=" + descripcion + ", atendida=" + atendida
                + ", equipoRespuesta=" + (equipoRespuesta != null ? equipoRespuesta.getNombre() : "sin equipo")
                + ", zonaGeografica=" + (zonaGeografica != null ? zonaGeografica.getNombre() : "null") + "]";
    }
}

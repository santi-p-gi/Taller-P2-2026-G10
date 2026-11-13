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

    /** {@return el identificador de la alerta} */
    public int getIdAlerta() {
        return idAlerta;
    }

    /** {@return la fecha y hora de la alerta} */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Modifica la fecha y hora de la alerta.
     *
     * @param fechaHora el nuevo valor
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /** {@return el nivel de la alerta} */
    public NivelAlerta getNivel() {
        return nivel;
    }

    /**
     * Modifica el nivel de la alerta.
     *
     * @param nivel el nuevo valor
     */
    public void setNivel(NivelAlerta nivel) {
        this.nivel = nivel;
    }

    /** {@return el tipo de evento climatico de la alerta} */
    public TipoEventoClimatico getTipoEvento() {
        return tipoEvento;
    }

    /**
     * Modifica el tipo de evento climatico de la alerta.
     *
     * @param tipoEvento el nuevo valor
     */
    public void setTipoEvento(TipoEventoClimatico tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    /** {@return la descripcion de la alerta} */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Modifica la descripcion de la alerta.
     *
     * @param descripcion el nuevo valor
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /** {@return el equipo asignado a la alerta} */
    public EquipoRespuesta getEquipoRespuesta() {
        return equipoRespuesta;
    }

    /**
     * Modifica el equipo asignado a la alerta.
     *
     * @param equipoRespuesta el nuevo valor
     */
    public void setEquipoRespuesta(EquipoRespuesta equipoRespuesta) {
        this.equipoRespuesta = equipoRespuesta;
    }

    /** {@return la zona geografica de la alerta} */
    public ZonaGeografica getZonaGeografica() {
        return zonaGeografica;
    }

    /**
     * Modifica la zona geografica de la alerta.
     *
     * @param zonaGeografica el nuevo valor
     */
    public void setZonaGeografica(ZonaGeografica zonaGeografica) {
        this.zonaGeografica = zonaGeografica;
    }

    /** {@return true si la alerta fue atendida} */
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

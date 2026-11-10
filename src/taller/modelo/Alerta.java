package taller.modelo;

import java.time.LocalDateTime;

import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.TipoEventoClimatico;
import taller.excepciones.AlertaYaAtendidaException;
import taller.excepciones.EquipoSinIntegrantesException;

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

    public Alerta() {
        this.idAlerta = generadorId++;
    }

    public Alerta(LocalDateTime fechaHora, NivelAlerta nivel,
            TipoEventoClimatico tipoEvento, String descripcion, EquipoRespuesta equipoRespuesta,
            ZonaGeografica zonaGeografica) {
        this.idAlerta = generadorId++;
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

    public boolean estaPendienteDeEquipo() {
        return requiereEquipoRespuesta() && equipoRespuesta == null;
    }

    public void marcarAtendida() throws AlertaYaAtendidaException {
        if (atendida) {
            throw new AlertaYaAtendidaException("La alerta ya fue atendida");
        }
        atendida = true;
        if (this.equipoRespuesta != null) {
            equipoRespuesta.setDisponible(true);
        }
    }

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

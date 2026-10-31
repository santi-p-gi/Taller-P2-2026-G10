package taller.modelo;

import java.time.LocalDateTime;

import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.TipoEventoClimatico;

public class Alerta {
    private static int generadorId;
    private final int idAlerta;
    private LocalDateTime fechaHora;
    private NivelAlerta nivel;
    private TipoEventoClimatico tipoEvento;
    private String descripcion;
    private EquipoRespuesta equipoRespuesta;
    private ZonaGeografica zonaGeografica;

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
        this.equipoRespuesta = equipoRespuesta;
        this.zonaGeografica = zonaGeografica;
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

    public void asignarEquipoRespuesta(EquipoRespuesta equipo) {
        if (this.requiereEquipoRespuesta()) {
            this.equipoRespuesta = equipo;
        } else {
            System.out.println("No se puede asignar un equipo de respuesta a una alerta de este nivel.");
        }
    }

    public boolean requiereEquipoRespuesta() {
        return this.nivel == NivelAlerta.ROJA || this.nivel == NivelAlerta.NARANJA;
    }

}

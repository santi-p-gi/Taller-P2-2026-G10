package taller.modelo;

import java.time.LocalDateTime;

import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.TipoEventoClimatico;

public class Alerta {
    private String idAlerta;
    private LocalDateTime fechaHora;
    private NivelAlerta nivel;
    private TipoEventoClimatico tipoEvento;
    private String descripcion;
    private EquipoRespuesta equipoRespuesta;
    private ZonaGeografica zonaGeografica;

    public Alerta() {
    }

    public Alerta(String idAlerta, LocalDateTime fechaHora, NivelAlerta nivel,
            TipoEventoClimatico tipoEvento, String descripcion, EquipoRespuesta equipoRespuesta,
            ZonaGeografica zonaGeografica) {
        this.idAlerta = idAlerta;
        this.fechaHora = fechaHora;
        this.nivel = nivel;
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.equipoRespuesta = equipoRespuesta;
        this.zonaGeografica = zonaGeografica;
    }

    public String getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(String idAlerta) {
        this.idAlerta = idAlerta;
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
}

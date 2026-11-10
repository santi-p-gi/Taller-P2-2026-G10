package taller.gestion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.TipoEventoClimatico;
import taller.modelo.Alerta;
import taller.modelo.EquipoRespuesta;
import taller.modelo.ZonaGeografica;

public class GestorAlertas {
    private RegistroDatos registro;

    public GestorAlertas(RegistroDatos registro) {
        this.registro = registro;
    }

    // ---------- Umbrales ----------

    public void configurarUmbrales(int idZona, double amarilla, double naranja, double roja) {
        ZonaGeografica zona = buscarZona(idZona);
        zona.configurarUmbrales(amarilla, naranja, roja);
    }

    // ---------- Registro de alertas ----------

    public Alerta registrarAlertaOperador(int idZona, TipoEventoClimatico tipo, NivelAlerta nivel, String descripcion,
            LocalDateTime fechaHora, Integer idEquipo) {
        if (tipo == TipoEventoClimatico.LLUVIA_COPIOSA) {
            throw new IllegalArgumentException(
                    "Las alertas de LLUVIA_COPIOSA son automaticas, no las registra un operador");
        }

        ZonaGeografica zona = buscarZona(idZona);

        if ((nivel == NivelAlerta.NARANJA || nivel == NivelAlerta.ROJA) && idEquipo == null) {
            throw new IllegalArgumentException("La asignacion es obligatoria para este tipo de alertas");
        }
        EquipoRespuesta equipo = null;

        if (idEquipo != null) {
            equipo = buscarEquipo(idEquipo);
        }

        Alerta nuevaAlerta = zona.emitirAlerta(tipo, nivel, fechaHora, descripcion, equipo);
        return nuevaAlerta;
    }

    // ---------- Gestion de alertas ----------

    public void asignarEquipo(int idAlerta, int idEquipo) {
        Alerta alerta = buscarAlerta(idAlerta);
        EquipoRespuesta equipo = buscarEquipo(idEquipo);
        alerta.asignarEquipo(equipo);
    }

    public void marcarAtendida(int idAlerta) {
        Alerta alerta = buscarAlerta(idAlerta);
        alerta.marcarAtendida();
    }

    // ---------- Consultas ----------

    public Alerta buscarAlerta(int idAlerta) {
        List<ZonaGeografica> zonas = registro.getZonasGeograficas();
        for (ZonaGeografica z : zonas) {
            for (Alerta a : z.getAlertas()) {
                if (a.getIdAlerta() == idAlerta) {
                    return a;
                }
            }
        }
        throw new IllegalArgumentException("No se encontró la alerta con ID: " + idAlerta);
    }

    public List<Alerta> listarAlertas() {
        List<Alerta> todasLasAlertas = new ArrayList<>();
        List<ZonaGeografica> todasLasZonas = registro.getZonasGeograficas();

        for (ZonaGeografica zona : todasLasZonas) {
            todasLasAlertas.addAll(zona.getAlertas());
        }
        return todasLasAlertas;
    }

    public List<Alerta> listarPendientesDeEquipo() {
        List<Alerta> pendientes = new ArrayList<>();

        for (Alerta alerta : listarAlertas()) {
            if (alerta.estaPendienteDeEquipo()) {
                pendientes.add(alerta);
            }
        }

        return pendientes;
    }

    public List<Alerta> listarNoAtendidas() {
        List<Alerta> noAtendidas = new ArrayList<>();

        for (Alerta alerta : listarAlertas()) {
            if (!alerta.isAtendida()) {
                noAtendidas.add(alerta);
            }
        }
        return noAtendidas;
    }

    // ---------- Auxiliares ----------

    private ZonaGeografica buscarZona(int idZona) {
        ZonaGeografica zona = registro.buscarZonaPorId(idZona);
        if (zona == null) {
            throw new IllegalArgumentException("No existe una zona con ID " + idZona);
        }
        return zona;
    }

    private EquipoRespuesta buscarEquipo(int idEquipo) {
        EquipoRespuesta equipo = registro.buscarEquipoPorId(idEquipo);
        if (equipo == null) {
            throw new IllegalArgumentException("No existe un equipo con ID " + idEquipo);
        }
        return equipo;
    }
}
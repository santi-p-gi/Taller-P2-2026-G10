package taller.gestion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.TipoEventoClimatico;
import taller.modelo.Alerta;
import taller.modelo.EquipoRespuesta;
import taller.modelo.ZonaGeografica;

/**
 * Sistema de alerta temprana: umbrales por zona, alertas de operador y asignacion de equipos de respuesta.
 */
public class GestorAlertas {
    private RegistroDatos registro;

    /**
     * Crea el gestor sobre el registro de datos del sistema.
     *
     * @param registro registro de datos compartido
     */
    public GestorAlertas(RegistroDatos registro) {
        this.registro = registro;
    }

    // ---------- Umbrales ----------

    /**
     * Configura los umbrales de lluvia en 24 horas de una zona.
     *
     * @param idZona  ID de la zona
     * @param amarilla umbral del nivel AMARILLA, en mm
     * @param naranja  umbral del nivel NARANJA, en mm
     * @param roja     umbral del nivel ROJA, en mm
     */
    public void configurarUmbrales(int idZona, double amarilla, double naranja, double roja) {
        ZonaGeografica zona = buscarZona(idZona);
        zona.configurarUmbrales(amarilla, naranja, roja);
    }

    // ---------- Registro de alertas ----------

    /**
     * Registra una alerta de CRECIDA_RIO o TORMENTA_SEVERA cargada por un operador.
     *
     * @param idZona      ID de la zona afectada
     * @param tipo        tipo de evento
     * @param nivel       nivel de severidad
     * @param descripcion descripcion de la alerta
     * @param fechaHora   fecha y hora de emision
     * @param idEquipo    ID del equipo a asignar, o null (obligatorio si el nivel es NARANJA o ROJA)
     * @return la alerta registrada
     * @throws IllegalArgumentException si el tipo es LLUVIA_COPIOSA o falta el equipo requerido
     */
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

    /**
     * Asigna un equipo de respuesta a una alerta.
     *
     * @param idAlerta ID de la alerta
     * @param idEquipo ID del equipo
     */
    public void asignarEquipo(int idAlerta, int idEquipo) {
        Alerta alerta = buscarAlerta(idAlerta);
        EquipoRespuesta equipo = buscarEquipo(idEquipo);
        alerta.asignarEquipo(equipo);
    }

    /**
     * Marca una alerta como atendida y libera a su equipo.
     *
     * @param idAlerta ID de la alerta
     */
    public void marcarAtendida(int idAlerta) {
        Alerta alerta = buscarAlerta(idAlerta);
        alerta.marcarAtendida();
    }

    // ---------- Consultas ----------

    /**
     * Busca una alerta por su ID entre las alertas de todas las zonas.
     *
     * @param idAlerta ID de la alerta
     * @return la alerta
     * @throws IllegalArgumentException si no existe
     */
    public Alerta buscarAlerta(int idAlerta) {
        List<ZonaGeografica> zonas = registro.getZonasGeograficas();
        for (ZonaGeografica z : zonas) {
            for (Alerta a : z.getAlertas()) {
                if (a.getIdAlerta() == idAlerta) {
                    return a;
                }
            }
        }
        throw new IllegalArgumentException("No se encontro la alerta con ID: " + idAlerta);
    }

    /**
     * Devuelve las alertas de todas las zonas.
     *
     * @return lista de alertas
     */
    public List<Alerta> listarAlertas() {
        List<Alerta> todasLasAlertas = new ArrayList<>();
        List<ZonaGeografica> todasLasZonas = registro.getZonasGeograficas();

        for (ZonaGeografica zona : todasLasZonas) {
            todasLasAlertas.addAll(zona.getAlertas());
        }
        return todasLasAlertas;
    }

    /**
     * Devuelve las alertas NARANJA o ROJA que todavia no tienen equipo asignado.
     *
     * @return alertas pendientes de equipo
     */
    public List<Alerta> listarPendientesDeEquipo() {
        List<Alerta> pendientes = new ArrayList<>();

        for (Alerta alerta : listarAlertas()) {
            if (alerta.estaPendienteDeEquipo()) {
                pendientes.add(alerta);
            }
        }

        return pendientes;
    }

    /**
     * Devuelve las alertas que todavia no fueron atendidas.
     *
     * @return alertas no atendidas
     */
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

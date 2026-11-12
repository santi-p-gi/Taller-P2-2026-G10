package taller.gestion;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import taller.enumeraciones.EstadoOperativo;
import taller.enumeraciones.NivelAlerta;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.ZonaGeografica;
import taller.modelo.ValorHistorico;
import taller.modelo.Alerta;
import taller.modelo.Sensor;

/**
 * Informes y reportes estadisticos y operativos del sistema. Solo consulta datos, no los modifica.
 * Todos los rangos de fechas incluyen sus dos extremos.
 */
public class GestorReportes {
    private RegistroDatos registro;

    /**
     * Crea el gestor sobre el registro de datos del sistema.
     *
     * @param registro registro de datos compartido
     */
    public GestorReportes(RegistroDatos registro) {
        this.registro = registro;
    }

    // ---------- 1. Historico de precipitaciones por estacion o zona ----------

    /**
     * Milimetros acumulados por una estacion en un rango de fechas.
     *
     * @param idEstacion ID de la estacion
     * @param inicio     inicio del rango
     * @param fin        fin del rango
     * @return milimetros acumulados
     */
    public double acumuladoEstacion(int idEstacion, LocalDateTime inicio, LocalDateTime fin) {
        EstacionMeteorologica estacion = buscarEstacion(idEstacion);
        return estacion.acumuladoEntreSensores(inicio, fin);
    }

    /**
     * Milimetros acumulados por una estacion en un dia.
     *
     * @param idEstacion ID de la estacion
     * @param fecha      dia a consultar (se ignora la hora)
     * @return milimetros acumulados en el dia
     */
    public double acumuladoDiarioPorEstacion(int idEstacion, LocalDateTime fecha) {
        return acumuladoEstacion(idEstacion, fecha.toLocalDate().atStartOfDay(),
                fecha.toLocalDate().plusDays(1).atStartOfDay().minusNanos(1));
    }

    /**
     * Milimetros acumulados por una estacion en los 7 dias desde una fecha.
     *
     * @param idEstacion   ID de la estacion
     * @param inicioSemana primer dia de la semana (se ignora la hora)
     * @return milimetros acumulados en la semana
     */
    public double acumuladoSemanalPorEstacion(int idEstacion, LocalDateTime inicioSemana) {
        return acumuladoEstacion(idEstacion, inicioSemana.toLocalDate().atStartOfDay(),
                inicioSemana.toLocalDate().atStartOfDay().plusDays(7).minusNanos(1));
    }

    /**
     * Milimetros acumulados por una estacion en un mes.
     *
     * @param idEstacion ID de la estacion
     * @param anio       anio
     * @param mes        mes (1 a 12)
     * @return milimetros acumulados en el mes
     */
    public double acumuladoMensualPorEstacion(int idEstacion, int anio, int mes) {
        LocalDateTime inicioMes = LocalDateTime.of(anio, mes, 1, 0, 0);
        return acumuladoEstacion(idEstacion, inicioMes, inicioMes.plusMonths(1).minusNanos(1));
    }

    /**
     * Milimetros acumulados por todas las estaciones de una zona en un rango de fechas.
     *
     * @param idZona ID de la zona
     * @param inicio inicio del rango
     * @param fin    fin del rango
     * @return milimetros acumulados
     */
    public double acumuladoZona(int idZona, LocalDateTime inicio, LocalDateTime fin) {
        ZonaGeografica zona = buscarZona(idZona);
        double acumuladoTotal = 0.0;
        for (EstacionMeteorologica estacion : zona.getEstacionesMeteorologicas()) {
            acumuladoTotal += estacion.acumuladoEntreSensores(inicio, fin);
        }
        return acumuladoTotal;
    }

    /**
     * Milimetros acumulados por una zona en un dia.
     *
     * @param idZona ID de la zona
     * @param fecha  dia a consultar (se ignora la hora)
     * @return milimetros acumulados en el dia
     */
    public double acumuladoDiarioPorZona(int idZona, LocalDateTime fecha) {
        return acumuladoZona(idZona, fecha.toLocalDate().atStartOfDay(),
                fecha.toLocalDate().plusDays(1).atStartOfDay().minusNanos(1));
    }

    /**
     * Milimetros acumulados por una zona en los 7 dias desde una fecha.
     *
     * @param idZona       ID de la zona
     * @param inicioSemana primer dia de la semana (se ignora la hora)
     * @return milimetros acumulados en la semana
     */
    public double acumuladoSemanalPorZona(int idZona, LocalDateTime inicioSemana) {
        return acumuladoZona(idZona, inicioSemana.toLocalDate().atStartOfDay(),
                inicioSemana.toLocalDate().atStartOfDay().plusDays(7).minusNanos(1));
    }

    /**
     * Milimetros acumulados por una zona en un mes.
     *
     * @param idZona ID de la zona
     * @param anio   anio
     * @param mes    mes (1 a 12)
     * @return milimetros acumulados en el mes
     */
    public double acumuladoMensualPorZona(int idZona, int anio, int mes) {
        LocalDateTime inicioMes = LocalDateTime.of(anio, mes, 1, 0, 0);
        return acumuladoZona(idZona, inicioMes, inicioMes.plusMonths(1).minusNanos(1));
    }

    /**
     * Media historica de un mes para una zona, para comparar con el acumulado mensual.
     *
     * @param idZona ID de la zona
     * @param mes    mes (1 a 12)
     * @return acumulado medio historico, en mm
     * @throws IllegalStateException si la zona no tiene valor historico para ese mes
     */
    public double mediaHistoricaMensualPorZona(int idZona, int mes) {
        ZonaGeografica zona = buscarZona(idZona);
        ValorHistorico historico = zona.obtenerValorHistorico(mes);
        if (historico == null) {
            throw new IllegalStateException(
                    "No existe un valor historico para el mes " + mes + " en la zona con ID " + idZona);
        }
        return historico.getAcumuladoMedio();
    }

    // ---------- 2. Ranking de estaciones por volumetria ----------

    /**
     * Estaciones ordenadas de mayor a menor acumulado en un rango de fechas.
     *
     * @param inicio inicio del rango
     * @param fin    fin del rango
     * @return nueva lista ordenada (no modifica el orden del registro)
     */
    public List<EstacionMeteorologica> rankingEstaciones(LocalDateTime inicio, LocalDateTime fin) {
        List<EstacionMeteorologica> estaciones = new ArrayList<>(registro.getEstacionesMeteorologicas());

        estaciones.sort((e1, e2) -> Double.compare(e2.acumuladoEntreSensores(inicio, fin),
                e1.acumuladoEntreSensores(inicio, fin)));
        return estaciones;
    }

    // ---------- 3. Historial de alertas emitidas ----------

    /**
     * Alertas emitidas que cumplen todos los filtros indicados. Un filtro en null no se aplica.
     *
     * @param nivel  nivel de severidad, o null para todos
     * @param desde  fecha minima, o null
     * @param hasta  fecha maxima, o null
     * @param idZona ID de la zona, o null para todas
     * @return alertas que cumplen los filtros
     */
    public List<Alerta> historialAlertas(NivelAlerta nivel, LocalDateTime desde, LocalDateTime hasta, Integer idZona) {
        List<ZonaGeografica> zonas = new ArrayList<>();
        if (idZona == null) {
            zonas.addAll(registro.getZonasGeograficas());
        } else {
            zonas.add(buscarZona(idZona));
        }

        List<Alerta> resultado = new ArrayList<>();
        for (ZonaGeografica zona : zonas) {
            for (Alerta alerta : zona.getAlertas()) {
                if (nivel != null && alerta.getNivel() != nivel) {
                    continue;
                }
                if (desde != null && alerta.getFechaHora().isBefore(desde)) {
                    continue;
                }
                if (hasta != null && alerta.getFechaHora().isAfter(hasta)) {
                    continue;
                }
                resultado.add(alerta);
            }
        }
        return resultado;
    }

    // ---------- 4. Ficha tecnica operativa ----------

    /**
     * Estacion con los datos de su ficha tecnica: estado, sensores con sus lecturas y responsable a cargo.
     *
     * @param idEstacion ID de la estacion
     * @return la estacion
     */
    public EstacionMeteorologica fichaTecnica(int idEstacion) {
        EstacionMeteorologica estacion = buscarEstacion(idEstacion);
        return estacion;
    }

    // ---------- 5. Informe de fallas y mantenimiento ----------

    /**
     * Sensores fuera de servicio o en mantenimiento.
     *
     * @return sensores no operativos
     */
    public List<Sensor> sensoresNoOperativos() {
        List<Sensor> sensores = new ArrayList<>();
        for (Sensor s : registro.getSensores()) {
            if (s.getEstado() != EstadoOperativo.OPERATIVA) {
                sensores.add(s);
            }
        }
        return sensores;
    }

    /**
     * Sensores con al menos una lectura anomala.
     *
     * @return sensores con lecturas anomalas
     */
    public List<Sensor> sensoresConLecturasAnomalas() {
        List<Sensor> sensores = new ArrayList<>();
        for (Sensor s : registro.getSensores()) {
            if (!s.obtenerMedicionesAnomalas().isEmpty()) {
                sensores.add(s);
            }
        }
        return sensores;
    }

    /**
     * Sensores cuya ultima calibracion fue hace mas de 12 meses.
     *
     * @return sensores que requieren calibracion
     */
    public List<Sensor> sensoresQueRequierenCalibracion() {
        List<Sensor> sensores = new ArrayList<>();
        for (Sensor s : registro.getSensores()) {
            if (s.requiereCalibracion()) {
                sensores.add(s);
            }
        }
        return sensores;
    }

    // ---------- 6. Estadisticas de anomalia climatica ----------

    /**
     * Porcentaje de desviacion del acumulado mensual de una zona respecto de su valor historico.
     *
     * @param idZona ID de la zona
     * @param anio   anio
     * @param mes    mes (1 a 12)
     * @return desviacion en porcentaje
     * @throws IllegalStateException si la zona no tiene valor historico para ese mes
     */
    public double desviacionMensual(int idZona, int anio, int mes) {
        ZonaGeografica zona = buscarZona(idZona);
        return zona.desviacionMensual(anio, mes);
    }

    // ---------- Auxiliares ----------

    private ZonaGeografica buscarZona(int idZona) {
        ZonaGeografica zona = registro.buscarZonaPorId(idZona);
        if (zona == null) {
            throw new IllegalArgumentException("No existe una zona con ID " + idZona);
        }
        return zona;
    }

    private EstacionMeteorologica buscarEstacion(int idEstacion) {
        EstacionMeteorologica estacion = registro.buscarEstacionPorId(idEstacion);
        if (estacion == null) {
            throw new IllegalArgumentException("No existe una estacion con ID " + idEstacion);
        }
        return estacion;
    }
}

package taller.ihc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import taller.enumeraciones.NivelAlerta;
import taller.gestion.GestorInfraestructura;
import taller.gestion.GestorReportes;
import taller.modelo.Alerta;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.Medicion;
import taller.modelo.ResponsableTecnico;
import taller.modelo.Sensor;
import taller.modelo.ZonaGeografica;

/**
 * Submenu de informes y reportes. Solo muestra informacion, no modifica datos.
 */
public class MenuReportes {
    private static final int CANTIDAD_ULTIMAS_LECTURAS = 3;

    private Consola consola;
    private GestorReportes gestorReportes;
    private GestorInfraestructura gestorInfraestructura;

    /**
     * Crea el submenu de reportes.
     *
     * @param consola               consola compartida para leer datos
     * @param gestorReportes        gestor de reportes
     * @param gestorInfraestructura gestor de infraestructura, para elegir zonas y estaciones
     */
    public MenuReportes(Consola consola, GestorReportes gestorReportes, GestorInfraestructura gestorInfraestructura) {
        this.consola = consola;
        this.gestorReportes = gestorReportes;
        this.gestorInfraestructura = gestorInfraestructura;
    }

    /**
     * Muestra el submenu en un bucle hasta que el usuario elige volver.
     */
    public void iniciar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = consola.leerEntero("Seleccione una opcion: ");
            try {
                switch (opcion) {
                    case 1:
                        historicoPrecipitaciones();
                        break;
                    case 2:
                        rankingEstaciones();
                        break;
                    case 3:
                        historialAlertas();
                        break;
                    case 4:
                        fichaTecnica();
                        break;
                    case 5:
                        informeFallas();
                        break;
                    case 6:
                        anomaliaClimatica();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("Opcion invalida. Intente nuevamente.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    // ---------- 1. Historico de precipitaciones ----------

    private void historicoPrecipitaciones() {
        String[] ambitos = { "Estacion", "Zona" };
        boolean porEstacion = consola.leerOpcion("Consultar por:", ambitos).equals("Estacion");

        int id;
        int idZona;
        String nombre;
        if (porEstacion) {
            mostrarEstaciones();
            id = consola.leerEntero("ID de la estacion: ");
            EstacionMeteorologica estacion = gestorInfraestructura.buscarEstacionPorId(id);
            nombre = estacion.getNombre();
            idZona = estacion.getZonaGeografica().getIdZona();
        } else {
            mostrarZonas();
            id = consola.leerEntero("ID de la zona: ");
            nombre = gestorInfraestructura.buscarZonaPorId(id).getNombre();
            idZona = id;
        }

        String[] periodos = { "Diario", "Semanal", "Mensual" };
        String periodo = consola.leerOpcion("Periodo:", periodos);

        if (periodo.equals("Diario")) {
            LocalDateTime dia = consola.leerFecha("Dia").atStartOfDay();
            double acumulado;
            if (porEstacion) {
                acumulado = gestorReportes.acumuladoDiarioPorEstacion(id, dia);
            } else {
                acumulado = gestorReportes.acumuladoDiarioPorZona(id, dia);
            }
            System.out.println("Acumulado diario de " + nombre + ": " + mm(acumulado));

        } else if (periodo.equals("Semanal")) {
            LocalDateTime inicio = consola.leerFecha("Primer dia de la semana").atStartOfDay();
            double acumulado;
            if (porEstacion) {
                acumulado = gestorReportes.acumuladoSemanalPorEstacion(id, inicio);
            } else {
                acumulado = gestorReportes.acumuladoSemanalPorZona(id, inicio);
            }
            System.out.println("Acumulado semanal de " + nombre + " (7 dias desde el "
                    + consola.formatearFecha(inicio) + "): " + mm(acumulado));

        } else {
            int anio = consola.leerEntero("Anio: ");
            int mes = leerMes();
            double acumulado;
            if (porEstacion) {
                acumulado = gestorReportes.acumuladoMensualPorEstacion(id, anio, mes);
            } else {
                acumulado = gestorReportes.acumuladoMensualPorZona(id, anio, mes);
            }
            System.out.println("Acumulado mensual de " + nombre + " (" + mes + "/" + anio + "): " + mm(acumulado));
            compararConMediaHistorica(idZona, mes, acumulado);
        }
    }

    private void compararConMediaHistorica(int idZona, int mes, double acumulado) {
        try {
            double media = gestorReportes.mediaHistoricaMensualPorZona(idZona, mes);
            System.out.println("Media historica mensual de la zona: " + mm(media));
            double diferencia = acumulado - media;
            if (diferencia > 0) {
                System.out.println("Supera la media historica en " + mm(diferencia));
            } else if (diferencia < 0) {
                System.out.println("Esta por debajo de la media historica en " + mm(-diferencia));
            } else {
                System.out.println("Coincide con la media historica.");
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    // ---------- 2. Ranking de estaciones ----------

    private void rankingEstaciones() {
        LocalDate desde = consola.leerFecha("Desde");
        LocalDate hasta = consola.leerFecha("Hasta");
        if (hasta.isBefore(desde)) {
            throw new IllegalArgumentException("La fecha hasta no puede ser anterior a la fecha desde");
        }
        LocalDateTime inicio = desde.atStartOfDay();
        LocalDateTime fin = finDelDia(hasta);

        List<EstacionMeteorologica> ranking = gestorReportes.rankingEstaciones(inicio, fin);
        System.out.println("Ranking de estaciones por acumulado de precipitacion:");
        int posicion = 1;
        for (EstacionMeteorologica e : ranking) {
            System.out.println("  " + posicion + ". " + e.getNombre() + " - "
                    + mm(gestorReportes.acumuladoEstacion(e.getIdEstacion(), inicio, fin)));
            posicion++;
        }
    }

    // ---------- 3. Historial de alertas ----------

    private void historialAlertas() {
        NivelAlerta nivel = null;
        if (consola.leerSiNo("Filtrar por nivel de severidad?")) {
            nivel = consola.leerOpcion("Nivel:", NivelAlerta.values());
        }
        LocalDate desde = consola.leerFechaOpcional("Desde");
        LocalDate hasta = consola.leerFechaOpcional("Hasta");
        mostrarZonas();
        Integer idZona = consola.leerEnteroOpcional("ID de la zona (vacio para todas): ");

        LocalDateTime inicio = null;
        if (desde != null) {
            inicio = desde.atStartOfDay();
        }
        LocalDateTime fin = null;
        if (hasta != null) {
            fin = finDelDia(hasta);
        }

        List<Alerta> alertas = gestorReportes.historialAlertas(nivel, inicio, fin, idZona);
        if (alertas.isEmpty()) {
            System.out.println("No hay alertas que cumplan los filtros.");
            return;
        }
        System.out.println("Alertas emitidas:");
        for (Alerta a : alertas) {
            String equipo = "sin equipo";
            if (a.getEquipoRespuesta() != null) {
                equipo = "equipo " + a.getEquipoRespuesta().getNombre();
            }
            String estado = "no atendida";
            if (a.isAtendida()) {
                estado = "atendida";
            }
            System.out.println("  ID " + a.getIdAlerta() + " - " + consola.formatear(a.getFechaHora()) + " - "
                    + a.getNivel() + " - " + a.getTipoEvento() + " - zona " + a.getZonaGeografica().getNombre()
                    + " - " + equipo + " - " + estado);
            System.out.println("      " + a.getDescripcion());
        }
    }

    // ---------- 4. Ficha tecnica operativa ----------

    private void fichaTecnica() {
        mostrarEstaciones();
        int idEstacion = consola.leerEntero("ID de la estacion: ");
        EstacionMeteorologica estacion = gestorReportes.fichaTecnica(idEstacion);

        System.out.println("===== FICHA TECNICA: " + estacion.getNombre() + " =====");
        System.out.println("Tipo: " + estacion.getTipo());
        System.out.println("Estado: " + estacion.getEstado());
        System.out.println("Zona: " + estacion.getZonaGeografica().getNombre());
        ResponsableTecnico responsable = estacion.getResponsableTecnico();
        if (responsable != null) {
            System.out.println("Responsable a cargo: " + responsable.getNombreCompleto() + " (DNI "
                    + responsable.getDni() + ")");
        } else {
            System.out.println("Responsable a cargo: sin asignar");
        }

        System.out.println("Sensores:");
        if (estacion.getSensores().isEmpty()) {
            System.out.println("  La estacion no tiene sensores instalados.");
        }
        for (Sensor s : estacion.getSensores()) {
            System.out.println("  ID " + s.getIdSensor() + " - " + s.getMarca() + " " + s.getModelo() + " - "
                    + s.getEstado());
            List<Medicion> ultimas = s.obtenerUltimasMediciones(CANTIDAD_ULTIMAS_LECTURAS);
            if (ultimas.isEmpty()) {
                System.out.println("      sin lecturas");
            }
            for (Medicion m : ultimas) {
                System.out.println("      " + consola.formatear(m.getFechaHora()) + " - " + mm(m.getMilimetros())
                        + (m.isAnomala() ? " (anomala)" : ""));
            }
        }
    }

    // ---------- 5. Informe de fallas y mantenimiento ----------

    private void informeFallas() {
        System.out.println("Sensores fuera de servicio o en mantenimiento:");
        List<Sensor> noOperativos = gestorReportes.sensoresNoOperativos();
        if (noOperativos.isEmpty()) {
            System.out.println("  Ninguno.");
        }
        for (Sensor s : noOperativos) {
            System.out.println("  ID " + s.getIdSensor() + " - " + s.getMarca() + " " + s.getModelo() + " - "
                    + s.getEstado() + " - " + ubicacion(s));
        }

        System.out.println("Sensores con lecturas anomalas:");
        List<Sensor> conAnomalias = gestorReportes.sensoresConLecturasAnomalas();
        if (conAnomalias.isEmpty()) {
            System.out.println("  Ninguno.");
        }
        for (Sensor s : conAnomalias) {
            System.out.println("  ID " + s.getIdSensor() + " - " + s.getMarca() + " " + s.getModelo() + " - "
                    + ubicacion(s));
            for (Medicion m : s.obtenerMedicionesAnomalas()) {
                System.out.println("      " + consola.formatear(m.getFechaHora()) + " - " + mm(m.getMilimetros()));
            }
        }

        System.out.println("Sensores que requieren calibracion (mas de 12 meses):");
        List<Sensor> aCalibrar = gestorReportes.sensoresQueRequierenCalibracion();
        if (aCalibrar.isEmpty()) {
            System.out.println("  Ninguno.");
        }
        for (Sensor s : aCalibrar) {
            String calibracion = "nunca";
            if (s.getUltimaCalibracion() != null) {
                calibracion = consola.formatearFecha(s.getUltimaCalibracion());
            }
            System.out.println("  ID " + s.getIdSensor() + " - " + s.getMarca() + " " + s.getModelo() + " - "
                    + ubicacion(s) + " - ultima calibracion: " + calibracion);
        }
    }

    // ---------- 6. Estadisticas de anomalia climatica ----------

    private void anomaliaClimatica() {
        mostrarZonas();
        int idZona = consola.leerEntero("ID de la zona: ");
        ZonaGeografica zona = gestorInfraestructura.buscarZonaPorId(idZona);
        int anio = consola.leerEntero("Anio: ");
        int mes = leerMes();

        double desviacion = gestorReportes.desviacionMensual(idZona, anio, mes);
        double acumulado = gestorReportes.acumuladoMensualPorZona(idZona, anio, mes);
        double media = gestorReportes.mediaHistoricaMensualPorZona(idZona, mes);

        System.out.println("Zona " + zona.getNombre() + " - " + mes + "/" + anio);
        System.out.println("Acumulado mensual: " + mm(acumulado));
        System.out.println("Valor historico: " + mm(media));
        System.out.println("Desviacion: " + String.format("%+.1f", desviacion) + " %");
    }

    // ---------- Auxiliares ----------

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("===== INFORMES Y REPORTES =====");
        System.out.println("1. Historico de precipitaciones por estacion o zona");
        System.out.println("2. Ranking de estaciones por volumetria");
        System.out.println("3. Historial de alertas emitidas");
        System.out.println("4. Ficha tecnica operativa de una estacion");
        System.out.println("5. Informe de fallas y mantenimiento");
        System.out.println("6. Estadisticas de anomalia climatica");
        System.out.println("0. Volver");
    }

    private int leerMes() {
        int mes = consola.leerEntero("Mes (1 a 12): ");
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12");
        }
        return mes;
    }

    private LocalDateTime finDelDia(LocalDate dia) {
        return dia.plusDays(1).atStartOfDay().minusNanos(1);
    }

    private String mm(double valor) {
        return String.format("%.1f mm", valor);
    }

    private String ubicacion(Sensor s) {
        if (s.getEstacionMeteorologica() == null) {
            return "sin instalar";
        }
        return "estacion " + s.getEstacionMeteorologica().getNombre();
    }

    private void mostrarZonas() {
        System.out.println("Zonas disponibles:");
        for (ZonaGeografica z : gestorInfraestructura.listarZonas()) {
            System.out.println("  ID " + z.getIdZona() + " - " + z.getNombre());
        }
    }

    private void mostrarEstaciones() {
        System.out.println("Estaciones disponibles:");
        for (EstacionMeteorologica e : gestorInfraestructura.listarEstaciones()) {
            System.out.println("  ID " + e.getIdEstacion() + " - " + e.getNombre() + " - zona "
                    + e.getZonaGeografica().getNombre());
        }
    }
}

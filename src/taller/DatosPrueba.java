package taller;

import java.time.LocalDateTime;

import taller.enumeraciones.EstadoOperativo;
import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.NivelVulnerabilidad;
import taller.enumeraciones.TipoEstacion;
import taller.enumeraciones.TipoEventoClimatico;
import taller.enumeraciones.TipoMantenimiento;
import taller.gestion.GestorAlertas;
import taller.gestion.GestorInfraestructura;
import taller.gestion.GestorMediciones;
import taller.gestion.GestorPersonal;
import taller.modelo.Alerta;
import taller.modelo.EquipoRespuesta;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.ResponsableTecnico;
import taller.modelo.Sensor;
import taller.modelo.ZonaGeografica;

/**
 * Carga el juego de datos de prueba precargado para la defensa del taller.
 * <p>
 * Todos los datos se registran a traves de los gestores, por lo que respetan las
 * mismas reglas de negocio que los datos cargados por el usuario. Las fechas son
 * fijas (octubre y noviembre de 2026) para que los reportes den siempre el mismo
 * resultado.
 */
public class DatosPrueba {

    /**
     * Carga zonas, personal, estaciones, sensores, lecturas, alertas y mantenimientos.
     *
     * @param infra      gestor de infraestructura meteorologica
     * @param personal   gestor de personal tecnico
     * @param mediciones gestor de mediciones
     * @param alertas    gestor de alertas
     */
    public static void cargar(GestorInfraestructura infra, GestorPersonal personal,
            GestorMediciones mediciones, GestorAlertas alertas) {

        // ---------- Zonas geograficas ----------
        ZonaGeografica salado = infra.registrarZona("Cuenca del Rio Salado", NivelVulnerabilidad.MUY_ALTA);
        ZonaGeografica delta = infra.registrarZona("Delta del Parana", NivelVulnerabilidad.ALTA);
        ZonaGeografica pampeana = infra.registrarZona("Llanura Pampeana Norte", NivelVulnerabilidad.MEDIA);
        ZonaGeografica sierras = infra.registrarZona("Sierras del Sur", NivelVulnerabilidad.BAJA);

        // La zona mas vulnerable usa umbrales mas exigentes; las demas quedan con 50/100/150
        alertas.configurarUmbrales(salado.getIdZona(), 40, 80, 120);

        // ---------- Valores historicos (enero a diciembre) ----------
        cargarHistoricos(infra, salado.getIdZona(),
                new double[] { 110, 105, 120, 90, 60, 40, 35, 40, 55, 95, 105, 115 });
        cargarHistoricos(infra, delta.getIdZona(),
                new double[] { 120, 115, 125, 100, 70, 50, 45, 50, 65, 100, 110, 120 });
        cargarHistoricos(infra, pampeana.getIdZona(),
                new double[] { 100, 95, 110, 85, 50, 30, 25, 30, 45, 85, 95, 105 });
        cargarHistoricos(infra, sierras.getIdZona(),
                new double[] { 85, 80, 90, 65, 40, 25, 25, 25, 40, 60, 75, 85 });

        // ---------- Responsables tecnicos ----------
        ResponsableTecnico gomez = personal.registrarResponsableTecnico("30111222", "Laura", "Gomez",
                "laura.gomez@ejemplo.com", "11-5555-0101", "Hidrologia", "LIC-1001");
        ResponsableTecnico pereyra = personal.registrarResponsableTecnico("31222333", "Martin", "Pereyra",
                "martin.pereyra@ejemplo.com", "11-5555-0102", "Instrumental meteorologico", "LIC-1002");
        ResponsableTecnico rios = personal.registrarResponsableTecnico("32333444", "Sofia", "Rios",
                "sofia.rios@ejemplo.com", "11-5555-0103", "Electronica", "LIC-1003");
        ResponsableTecnico acosta = personal.registrarResponsableTecnico("33444555", "Diego", "Acosta",
                "diego.acosta@ejemplo.com", "11-5555-0104", "Telecomunicaciones", "LIC-1004");
        ResponsableTecnico molina = personal.registrarResponsableTecnico("34555666", "Valeria", "Molina",
                "valeria.molina@ejemplo.com", "11-5555-0105", "Hidrologia", "LIC-1005");
        ResponsableTecnico herrera = personal.registrarResponsableTecnico("35666777", "Pablo", "Herrera",
                "pablo.herrera@ejemplo.com", "11-5555-0106", "Instrumental meteorologico", "LIC-1006");

        // ---------- Equipos de respuesta ----------
        EquipoRespuesta norte = personal.registrarEquipoRespuesta("Brigada Norte", true);
        EquipoRespuesta sur = personal.registrarEquipoRespuesta("Brigada Sur", true);
        EquipoRespuesta brigadaDelta = personal.registrarEquipoRespuesta("Brigada Delta", true);
        // Sin integrantes: no se le pueden asignar alertas (EquipoSinIntegrantesException)
        personal.registrarEquipoRespuesta("Brigada Reserva", true);

        personal.agregarIntegranteAEquipo(norte.getIdEquipo(), gomez.getDni());
        personal.agregarIntegranteAEquipo(norte.getIdEquipo(), pereyra.getDni());
        personal.agregarIntegranteAEquipo(sur.getIdEquipo(), rios.getDni());
        personal.agregarIntegranteAEquipo(sur.getIdEquipo(), acosta.getDni());
        personal.agregarIntegranteAEquipo(brigadaDelta.getIdEquipo(), molina.getDni());

        // ---------- Estaciones meteorologicas ----------
        EstacionMeteorologica saladoNorte = infra.registrarEstacionMeteorologica("Ruta 5 km 112",
                "Estacion Salado Norte", -35.02, -59.85, 25, TipoEstacion.AUTOMATICA,
                salado.getIdZona(), gomez.getDni());
        EstacionMeteorologica saladoSur = infra.registrarEstacionMeteorologica("Camino Rural 41",
                "Estacion Salado Sur", -35.75, -58.50, 15, TipoEstacion.CONVENCIONAL,
                salado.getIdZona(), pereyra.getDni());
        EstacionMeteorologica tigre = infra.registrarEstacionMeteorologica("Av. Liniers 1200",
                "Estacion Delta Tigre", -34.42, -58.58, 4, TipoEstacion.AUTOMATICA,
                delta.getIdZona(), rios.getDni());
        EstacionMeteorologica campana = infra.registrarEstacionMeteorologica("Costanera 300",
                "Estacion Delta Campana", -34.16, -58.96, 6, TipoEstacion.CONVENCIONAL,
                delta.getIdZona(), molina.getDni());
        EstacionMeteorologica pergamino = infra.registrarEstacionMeteorologica("Ruta 8 km 225",
                "Estacion Pergamino", -33.89, -60.57, 65, TipoEstacion.AUTOMATICA,
                pampeana.getIdZona(), acosta.getDni());
        EstacionMeteorologica tandil = infra.registrarEstacionMeteorologica("Av. Don Bosco 2500",
                "Estacion Tandil", -37.32, -59.13, 180, TipoEstacion.CONVENCIONAL,
                sierras.getIdZona(), herrera.getDni());

        // ---------- Sensores ----------
        Sensor s1 = infra.registrarSensor("Davis", "Rain Collector II", 0.2, fecha(2026, 3, 10, 9));
        Sensor s2 = infra.registrarSensor("Davis", "Rain Collector II", 0.2, fecha(2026, 3, 10, 10));
        Sensor s3 = infra.registrarSensor("Casella", "Tipo Hellmann", 0.1, fecha(2026, 2, 1, 9));
        Sensor s4 = infra.registrarSensor("Campbell Scientific", "TE525MM", 0.1, fecha(2025, 8, 10, 9));
        Sensor s5 = infra.registrarSensor("Casella", "Tipo Hellmann", 0.1, fecha(2026, 1, 15, 9));
        // Ultima calibracion hace mas de 12 meses: requiere calibracion
        Sensor s6 = infra.registrarSensor("Lambrecht", "Joss-Tognini", 0.1, fecha(2025, 5, 20, 9));
        Sensor s7 = infra.registrarSensor("Campbell Scientific", "TE525MM", 0.1, fecha(2026, 4, 5, 9));
        Sensor s8 = infra.registrarSensor("Davis", "Rain Collector II", 0.2, fecha(2026, 6, 12, 9));
        // Queda sin instalar para poder mostrar la instalacion y el traslado
        infra.registrarSensor("Lambrecht", "Joss-Tognini", 0.1, fecha(2026, 9, 1, 9));

        infra.instalarSensor(s1.getIdSensor(), saladoNorte.getIdEstacion());
        infra.instalarSensor(s2.getIdSensor(), saladoNorte.getIdEstacion());
        infra.instalarSensor(s3.getIdSensor(), saladoSur.getIdEstacion());
        infra.instalarSensor(s4.getIdSensor(), tigre.getIdEstacion());
        infra.instalarSensor(s5.getIdSensor(), campana.getIdEstacion());
        infra.instalarSensor(s6.getIdSensor(), pergamino.getIdEstacion());
        infra.instalarSensor(s7.getIdSensor(), pergamino.getIdEstacion());
        infra.instalarSensor(s8.getIdSensor(), tandil.getIdEstacion());

        // ---------- Lecturas de octubre (sin superar umbrales) ----------
        int[] dias = { 3, 8, 12, 17, 22, 27 };
        double[] mmS1 = { 8, 12, 10, 14, 6, 10 };
        double[] mmS2 = { 7, 11, 9, 12, 6, 9 };
        double[] mmS3 = { 3, 5, 4, 6, 2, 4 };
        double[] mmS4 = { 8, 12, 10, 14, 6, 10 };
        double[] mmS5 = { 5, 7, 6, 8, 4, 6 };
        double[] mmS6 = { 5, 7, 6, 8, 4, 6 };
        double[] mmS7 = { 3, 5, 4, 5, 3, 4 };
        double[] mmS8 = { 4, 6, 5, 7, 3, 5 };
        for (int i = 0; i < dias.length; i++) {
            mediciones.registrarLectura(s1.getIdSensor(), mmS1[i], fecha(2026, 10, dias[i], 8));
            mediciones.registrarLectura(s2.getIdSensor(), mmS2[i], fecha(2026, 10, dias[i], 9));
            mediciones.registrarLectura(s3.getIdSensor(), mmS3[i], fecha(2026, 10, dias[i], 10));
            mediciones.registrarLectura(s4.getIdSensor(), mmS4[i], fecha(2026, 10, dias[i], 11));
            mediciones.registrarLectura(s5.getIdSensor(), mmS5[i], fecha(2026, 10, dias[i], 12));
            mediciones.registrarLectura(s6.getIdSensor(), mmS6[i], fecha(2026, 10, dias[i], 13));
            mediciones.registrarLectura(s7.getIdSensor(), mmS7[i], fecha(2026, 10, dias[i], 14));
            mediciones.registrarLectura(s8.getIdSensor(), mmS8[i], fecha(2026, 10, dias[i], 15));
        }

        // ---------- Lecturas de noviembre ----------
        mediciones.registrarLectura(s6.getIdSensor(), 6, fecha(2026, 11, 4, 9));
        mediciones.registrarLectura(s8.getIdSensor(), 4, fecha(2026, 11, 4, 10));

        // Temporal en la Cuenca del Salado: la zona sube de VERDE a ROJA en el dia
        mediciones.registrarLectura(s2.getIdSensor(), 20, fecha(2026, 11, 5, 6));
        mediciones.registrarLectura(s3.getIdSensor(), 25, fecha(2026, 11, 5, 9)); // 45 mm -> AMARILLA
        Alerta alertaNaranja = mediciones.registrarLectura(s2.getIdSensor(), 40, fecha(2026, 11, 5, 12)); // 85 mm
        // 115 mm contra 10 mm de la lectura anterior de s1: queda marcada como anomala
        Alerta alertaRoja = mediciones.registrarLectura(s1.getIdSensor(), 115, fecha(2026, 11, 5, 15)); // 200 mm

        // Lluvias en el Delta: la zona llega a AMARILLA
        mediciones.registrarLectura(s4.getIdSensor(), 30, fecha(2026, 11, 6, 10));
        mediciones.registrarLectura(s5.getIdSensor(), 25, fecha(2026, 11, 6, 14)); // 55 mm -> AMARILLA

        // ---------- Alertas ----------
        // Las alertas NARANJA y ROJA automaticas nacen sin equipo y se les asigna uno
        alertas.asignarEquipo(alertaNaranja.getIdAlerta(), norte.getIdEquipo());
        alertas.marcarAtendida(alertaNaranja.getIdAlerta()); // la Brigada Norte vuelve a quedar disponible
        alertas.asignarEquipo(alertaRoja.getIdAlerta(), sur.getIdEquipo());

        alertas.registrarAlertaOperador(delta.getIdZona(), TipoEventoClimatico.CRECIDA_RIO, NivelAlerta.ROJA,
                "Desborde del rio Parana de las Palmas", fecha(2026, 11, 7, 8), brigadaDelta.getIdEquipo());
        alertas.registrarAlertaOperador(sierras.getIdZona(), TipoEventoClimatico.TORMENTA_SEVERA,
                NivelAlerta.AMARILLA, "Tormenta con caida de granizo", fecha(2026, 11, 8, 18), null);

        // ---------- Mantenimientos ----------
        personal.registrarMantenimiento(saladoNorte.getIdEstacion(), null, gomez.getDni(),
                TipoMantenimiento.PREVENTIVO, fecha(2026, 9, 15, 10), "Limpieza general y revision de la estacion");
        personal.registrarMantenimiento(tigre.getIdEstacion(), s4.getIdSensor(), rios.getDni(),
                TipoMantenimiento.CALIBRACION, fecha(2026, 10, 1, 10), "Calibracion anual del pluviometro");
        personal.registrarMantenimiento(pergamino.getIdEstacion(), s7.getIdSensor(), acosta.getDni(),
                TipoMantenimiento.CORRECTIVO, fecha(2026, 11, 2, 11), "Falla en el registrador de datos");
        personal.registrarMantenimiento(tandil.getIdEstacion(), s8.getIdSensor(), herrera.getDni(),
                TipoMantenimiento.CORRECTIVO, fecha(2026, 11, 9, 9), "Obstruccion del embudo colector");
        personal.registrarMantenimiento(campana.getIdEstacion(), null, molina.getDni(),
                TipoMantenimiento.PREVENTIVO, fecha(2026, 11, 9, 14), "Mantenimiento preventivo programado");

        // ---------- Estados (al final, para no bloquear las lecturas historicas) ----------
        infra.cambiarEstadoSensor(s7.getIdSensor(), EstadoOperativo.FUERA_DE_SERVICIO);
        infra.cambiarEstadoSensor(s8.getIdSensor(), EstadoOperativo.EN_MANTENIMIENTO);
        infra.cambiarEstadoEstacion(campana.getIdEstacion(), EstadoOperativo.EN_MANTENIMIENTO);
    }

    // ---------- Auxiliares ----------

    private static void cargarHistoricos(GestorInfraestructura infra, int idZona, double[] acumulados) {
        for (int mes = 1; mes <= 12; mes++) {
            infra.cargarValorHistorico(idZona, mes, acumulados[mes - 1]);
        }
    }

    private static LocalDateTime fecha(int anio, int mes, int dia, int hora) {
        return LocalDateTime.of(anio, mes, dia, hora, 0);
    }
}

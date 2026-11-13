package taller.ihc;

import java.time.LocalDateTime;
import java.util.List;

import taller.enumeraciones.NivelAlerta;
import taller.enumeraciones.TipoEventoClimatico;
import taller.gestion.GestorAlertas;
import taller.gestion.GestorInfraestructura;
import taller.gestion.GestorPersonal;
import taller.modelo.Alerta;
import taller.modelo.EquipoRespuesta;
import taller.modelo.ZonaGeografica;

/**
 * Submenu del sistema de alerta temprana: umbrales por zona, alertas de operador y gestion de las
 * alertas que requieren equipo de respuesta.
 */
public class MenuAlertas {
    private Consola consola;
    private GestorAlertas gestorAlertas;
    private GestorInfraestructura gestorInfraestructura;
    private GestorPersonal gestorPersonal;

    /**
     * Crea el submenu de alertas.
     *
     * @param consola               consola compartida para leer datos
     * @param gestorAlertas         gestor de alertas
     * @param gestorInfraestructura gestor de infraestructura, para elegir la zona
     * @param gestorPersonal        gestor de personal, para listar los equipos disponibles
     */
    public MenuAlertas(Consola consola, GestorAlertas gestorAlertas, GestorInfraestructura gestorInfraestructura,
            GestorPersonal gestorPersonal) {
        this.consola = consola;
        this.gestorAlertas = gestorAlertas;
        this.gestorInfraestructura = gestorInfraestructura;
        this.gestorPersonal = gestorPersonal;
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
                        configurarUmbrales();
                        break;
                    case 2:
                        registrarAlertaOperador();
                        break;
                    case 3:
                        asignarEquipoAPendiente();
                        break;
                    case 4:
                        marcarAtendida();
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

    // ---------- Opciones ----------

    private void configurarUmbrales() {
        mostrarZonasConUmbrales();
        int idZona = consola.leerEntero("ID de la zona: ");
        gestorInfraestructura.buscarZonaPorId(idZona);
        System.out.println("Escala de referencia: VERDE < 50 mm, AMARILLA < 100 mm, NARANJA < 150 mm, ROJA >= 150 mm.");
        double amarilla = consola.leerDecimal("Umbral AMARILLA (mm en 24 hs): ");
        double naranja = consola.leerDecimal("Umbral NARANJA (mm en 24 hs): ");
        double roja = consola.leerDecimal("Umbral ROJA (mm en 24 hs): ");

        gestorAlertas.configurarUmbrales(idZona, amarilla, naranja, roja);
        System.out.println("Umbrales configurados.");
    }

    private void registrarAlertaOperador() {
        mostrarZonasConUmbrales();
        int idZona = consola.leerEntero("ID de la zona afectada: ");
        gestorInfraestructura.buscarZonaPorId(idZona);
        TipoEventoClimatico[] tiposOperador = { TipoEventoClimatico.CRECIDA_RIO,
                TipoEventoClimatico.TORMENTA_SEVERA };
        TipoEventoClimatico tipo = consola.leerOpcion("Tipo de evento:", tiposOperador);
        NivelAlerta nivel = consola.leerOpcion("Nivel de la alerta:", NivelAlerta.values());
        String descripcion = consola.leerTexto("Descripcion: ");
        LocalDateTime fecha = consola.leerFechaHora("Fecha y hora de emision");

        Integer idEquipo = null;
        if (nivel == NivelAlerta.NARANJA || nivel == NivelAlerta.ROJA) {
            System.out.println("Las alertas NARANJA y ROJA requieren asignar un equipo de respuesta.");
            idEquipo = elegirEquipoDisponible();
            if (idEquipo == null) {
                System.out.println("No hay equipos disponibles. No se puede registrar la alerta.");
                return;
            }
        }

        Alerta alerta = gestorAlertas.registrarAlertaOperador(idZona, tipo, nivel, descripcion, fecha, idEquipo);
        System.out.println("Alerta registrada con ID " + alerta.getIdAlerta() + ".");
    }

    private void asignarEquipoAPendiente() {
        List<Alerta> pendientes = gestorAlertas.listarPendientesDeEquipo();
        if (pendientes.isEmpty()) {
            System.out.println("No hay alertas pendientes de equipo.");
            return;
        }
        System.out.println("Alertas pendientes de equipo:");
        mostrarAlertas(pendientes);
        int idAlerta = consola.leerEntero("ID de la alerta: ");
        Alerta alerta = gestorAlertas.buscarAlerta(idAlerta);
        if (!alerta.estaPendienteDeEquipo()) {
            System.out.println("La alerta " + idAlerta + " no esta pendiente de equipo.");
            return;
        }

        Integer idEquipo = elegirEquipoDisponible();
        if (idEquipo == null) {
            System.out.println("No hay equipos disponibles en este momento.");
            return;
        }
        gestorAlertas.asignarEquipo(idAlerta, idEquipo);
        System.out.println("Equipo asignado a la alerta.");
    }

    private void marcarAtendida() {
        List<Alerta> noAtendidas = gestorAlertas.listarNoAtendidas();
        if (noAtendidas.isEmpty()) {
            System.out.println("No hay alertas sin atender.");
            return;
        }
        System.out.println("Alertas sin atender:");
        mostrarAlertas(noAtendidas);
        int idAlerta = consola.leerEntero("ID de la alerta: ");

        gestorAlertas.marcarAtendida(idAlerta);
        System.out.println("Alerta marcada como atendida.");
        Alerta alerta = gestorAlertas.buscarAlerta(idAlerta);
        if (alerta.getEquipoRespuesta() != null) {
            System.out.println("El equipo " + alerta.getEquipoRespuesta().getNombre() + " vuelve a estar disponible.");
        }
    }

    // ---------- Auxiliares ----------

    /**
     * Muestra los equipos disponibles y pide uno hasta que se elija un ID de la lista.
     * Devuelve null si no hay equipos disponibles.
     */
    private Integer elegirEquipoDisponible() {
        List<EquipoRespuesta> equipos = gestorPersonal.getEquiposDisponibles();
        if (equipos.isEmpty()) {
            return null;
        }
        System.out.println("Equipos disponibles:");
        for (EquipoRespuesta e : equipos) {
            System.out.println("  ID " + e.getIdEquipo() + " - " + e.getNombre());
        }
        while (true) {
            int idEquipo = consola.leerEntero("ID del equipo: ");
            for (EquipoRespuesta e : equipos) {
                if (e.getIdEquipo() == idEquipo) {
                    return idEquipo;
                }
            }
            System.out.println("El equipo " + idEquipo + " no esta entre los disponibles.");
        }
    }

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("===== SISTEMA DE ALERTA TEMPRANA =====");
        System.out.println("1. Configurar umbrales de una zona");
        System.out.println("2. Registrar alerta de operador (crecida de rio o tormenta severa)");
        System.out.println("3. Asignar equipo a una alerta pendiente");
        System.out.println("4. Marcar alerta como atendida");
        System.out.println("0. Volver");
    }

    private void mostrarZonasConUmbrales() {
        System.out.println("Zonas disponibles:");
        for (ZonaGeografica z : gestorInfraestructura.listarZonas()) {
            System.out.println("  ID " + z.getIdZona() + " - " + z.getNombre() + " - alerta " + z.getNivelAlertaActual()
                    + " - umbrales " + z.getUmbralAmarilla() + " / " + z.getUmbralNaranja() + " / "
                    + z.getUmbralRoja() + " mm");
        }
    }

    private void mostrarAlertas(List<Alerta> alertas) {
        for (Alerta a : alertas) {
            String equipo = "sin equipo";
            if (a.getEquipoRespuesta() != null) {
                equipo = "equipo " + a.getEquipoRespuesta().getNombre();
            }
            System.out.println("  ID " + a.getIdAlerta() + " - " + consola.formatear(a.getFechaHora()) + " - "
                    + a.getTipoEvento() + " - " + a.getNivel() + " - zona " + a.getZonaGeografica().getNombre()
                    + " - " + equipo);
        }
    }
}

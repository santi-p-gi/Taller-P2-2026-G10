package taller.ihc;

import java.time.LocalDateTime;
import java.util.List;

import taller.gestion.GestorAlertas;
import taller.gestion.GestorInfraestructura;
import taller.gestion.GestorMediciones;
import taller.gestion.GestorPersonal;
import taller.modelo.Alerta;
import taller.modelo.EquipoRespuesta;
import taller.modelo.Medicion;
import taller.modelo.Sensor;

/**
 * Submenu de registro de mediciones. Al registrar una lectura muestra si fue anomala y, si se
 * emitio una alerta NARANJA o ROJA, pide asignarle un equipo de respuesta.
 */
public class MenuMediciones {
    private Consola consola;
    private GestorMediciones gestorMediciones;
    private GestorInfraestructura gestorInfraestructura;
    private GestorAlertas gestorAlertas;
    private GestorPersonal gestorPersonal;

    /**
     * Crea el submenu de mediciones.
     *
     * @param consola               consola compartida para leer datos
     * @param gestorMediciones      gestor de mediciones
     * @param gestorInfraestructura gestor de infraestructura, para elegir el sensor
     * @param gestorAlertas         gestor de alertas, para asignar equipo a la alerta emitida
     * @param gestorPersonal        gestor de personal, para listar los equipos disponibles
     */
    public MenuMediciones(Consola consola, GestorMediciones gestorMediciones,
            GestorInfraestructura gestorInfraestructura, GestorAlertas gestorAlertas, GestorPersonal gestorPersonal) {
        this.consola = consola;
        this.gestorMediciones = gestorMediciones;
        this.gestorInfraestructura = gestorInfraestructura;
        this.gestorAlertas = gestorAlertas;
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
                        registrarLectura();
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

    private void registrarLectura() {
        mostrarSensoresInstalados();
        int idSensor = consola.leerEntero("ID del sensor: ");
        gestorInfraestructura.buscarSensorPorId(idSensor);
        double milimetros = consola.leerDecimal("Milimetros registrados (0 a 500): ");
        LocalDateTime fecha = consola.leerFechaHora("Fecha y hora de la lectura");

        Alerta alerta = gestorMediciones.registrarLectura(idSensor, milimetros, fecha);
        System.out.println("Lectura registrada.");

        Medicion medicion = gestorMediciones.obtenerUltimaMedicion(idSensor);
        if (medicion.isAnomala()) {
            System.out.println("ATENCION: lectura anomala, difiere en mas de 100 mm de la lectura anterior.");
        }

        if (alerta != null) {
            System.out.println("Se emitio una alerta " + alerta.getTipoEvento() + " de nivel " + alerta.getNivel()
                    + " en la zona " + alerta.getZonaGeografica().getNombre() + ".");
            if (alerta.requiereEquipoRespuesta()) {
                asignarEquipoObligatorio(alerta);
            }
        }
    }

    // ---------- Auxiliares ----------

    private void asignarEquipoObligatorio(Alerta alerta) {
        List<EquipoRespuesta> equipos = gestorPersonal.getEquiposDisponibles();
        if (equipos.isEmpty()) {
            System.out.println("No hay equipos disponibles. La alerta queda pendiente de equipo y se debe asignar"
                    + " desde el sistema de alerta temprana.");
            return;
        }
        System.out.println("La alerta requiere asignar un equipo de respuesta.");
        System.out.println("Equipos disponibles:");
        for (EquipoRespuesta e : equipos) {
            System.out.println("  ID " + e.getIdEquipo() + " - " + e.getNombre());
        }
        boolean asignado = false;
        while (!asignado) {
            int idEquipo = consola.leerEntero("ID del equipo: ");
            try {
                gestorAlertas.asignarEquipo(alerta.getIdAlerta(), idEquipo);
                asignado = true;
                System.out.println("Equipo asignado a la alerta.");
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("===== REGISTRO DE MEDICIONES =====");
        System.out.println("1. Registrar lectura de precipitacion");
        System.out.println("0. Volver");
    }

    private void mostrarSensoresInstalados() {
        System.out.println("Sensores instalados:");
        for (Sensor s : gestorInfraestructura.listarSensores()) {
            if (s.getEstacionMeteorologica() != null) {
                System.out.println("  ID " + s.getIdSensor() + " - " + s.getMarca() + " " + s.getModelo() + " - "
                        + s.getEstado() + " - estacion " + s.getEstacionMeteorologica().getNombre() + " ("
                        + s.getEstacionMeteorologica().getEstado() + ")");
            }
        }
    }
}

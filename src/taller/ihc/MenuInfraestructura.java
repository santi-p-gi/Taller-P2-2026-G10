package taller.ihc;

import java.time.LocalDateTime;
import java.util.List;

import taller.enumeraciones.EstadoOperativo;
import taller.enumeraciones.NivelVulnerabilidad;
import taller.enumeraciones.TipoEstacion;
import taller.gestion.GestorInfraestructura;
import taller.gestion.GestorPersonal;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.ResponsableTecnico;
import taller.modelo.Sensor;
import taller.modelo.ZonaGeografica;

/**
 * Submenu de gestion de infraestructura: zonas, estaciones y sensores.
 */
public class MenuInfraestructura {
    private Consola consola;
    private GestorInfraestructura gestorInfraestructura;
    private GestorPersonal gestorPersonal;

    /**
     * Crea el submenu de infraestructura.
     *
     * @param consola               consola compartida para leer datos
     * @param gestorInfraestructura gestor de infraestructura
     * @param gestorPersonal        gestor de personal, para elegir responsables
     */
    public MenuInfraestructura(Consola consola, GestorInfraestructura gestorInfraestructura,
            GestorPersonal gestorPersonal) {
        this.consola = consola;
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
                        registrarZona();
                        break;
                    case 2:
                        cargarValorHistorico();
                        break;
                    case 3:
                        registrarEstacion();
                        break;
                    case 4:
                        cambiarEstadoEstacion();
                        break;
                    case 5:
                        registrarSensor();
                        break;
                    case 6:
                        instalarSensor();
                        break;
                    case 7:
                        retirarSensor();
                        break;
                    case 8:
                        trasladarSensor();
                        break;
                    case 9:
                        cambiarEstadoSensor();
                        break;
                    case 10:
                        mostrarZonas();
                        break;
                    case 11:
                        listarEstaciones();
                        break;
                    case 12:
                        listarSensores();
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

    // ---------- Zonas ----------

    private void registrarZona() {
        String nombre = consola.leerTexto("Nombre de la zona: ");
        NivelVulnerabilidad nivel = consola.leerOpcion("Nivel de vulnerabilidad:", NivelVulnerabilidad.values());

        ZonaGeografica zona = gestorInfraestructura.registrarZona(nombre, nivel);
        System.out.println("Zona registrada con ID " + zona.getIdZona() + ".");
    }

    private void cargarValorHistorico() {
        mostrarZonas();
        int idZona = consola.leerEntero("ID de la zona: ");
        gestorInfraestructura.buscarZonaPorId(idZona);
        int mes = consola.leerEntero("Mes (1 a 12): ");
        double acumulado = consola.leerDecimal("Acumulado medio historico (mm): ");

        gestorInfraestructura.cargarValorHistorico(idZona, mes, acumulado);
        System.out.println("Valor historico cargado.");
    }

    // ---------- Estaciones ----------

    private void registrarEstacion() {
        String nombre = consola.leerTexto("Nombre de la estacion: ");
        String direccion = consola.leerTexto("Direccion: ");
        double latitud = consola.leerDecimal("Latitud: ");
        double longitud = consola.leerDecimal("Longitud: ");
        double altitud = consola.leerDecimal("Altitud (m): ");
        TipoEstacion tipo = consola.leerOpcion("Tipo de estacion:", TipoEstacion.values());
        mostrarZonas();
        int idZona = consola.leerEntero("ID de la zona a la que pertenece la estacion: ");
        gestorInfraestructura.buscarZonaPorId(idZona);
        mostrarResponsables();
        String dni = consola.leerTexto("DNI del responsable tecnico a cargo: ");

        EstacionMeteorologica estacion = gestorInfraestructura.registrarEstacionMeteorologica(direccion, nombre,
                latitud, longitud, altitud, tipo, idZona, dni);
        System.out.println("Estacion registrada con ID " + estacion.getIdEstacion() + ".");
    }

    private void cambiarEstadoEstacion() {
        mostrarEstaciones(gestorInfraestructura.listarEstaciones());
        int idEstacion = consola.leerEntero("ID de la estacion: ");
        gestorInfraestructura.buscarEstacionPorId(idEstacion);
        EstadoOperativo estado = consola.leerOpcion("Nuevo estado:", EstadoOperativo.values());

        gestorInfraestructura.cambiarEstadoEstacion(idEstacion, estado);
        System.out.println("Estado actualizado.");
    }

    private void listarEstaciones() {
        if (consola.leerSiNo("Filtrar por zona?")) {
            mostrarZonas();
            int idZona = consola.leerEntero("ID de la zona: ");
            mostrarEstaciones(gestorInfraestructura.listarEstacionesPorZona(idZona));
        } else {
            mostrarEstaciones(gestorInfraestructura.listarEstaciones());
        }
    }

    // ---------- Sensores ----------

    private void registrarSensor() {
        String marca = consola.leerTexto("Marca: ");
        String modelo = consola.leerTexto("Modelo: ");
        double precision = consola.leerDecimal("Precision (mm): ");
        LocalDateTime calibracion = consola.leerFechaHora("Fecha de ultima calibracion");

        Sensor sensor = gestorInfraestructura.registrarSensor(marca, modelo, precision, calibracion);
        System.out.println("Sensor registrado con ID " + sensor.getIdSensor() + " (sin instalar).");
    }

    private void instalarSensor() {
        mostrarSensores(gestorInfraestructura.listarSensoresSinInstalar());
        int idSensor = consola.leerEntero("ID del sensor: ");
        gestorInfraestructura.buscarSensorPorId(idSensor);
        mostrarEstaciones(gestorInfraestructura.listarEstaciones());
        int idEstacion = consola.leerEntero("ID de la estacion: ");

        gestorInfraestructura.instalarSensor(idSensor, idEstacion);
        System.out.println("Sensor instalado.");
    }

    private void retirarSensor() {
        mostrarSensores(gestorInfraestructura.listarSensores());
        int idSensor = consola.leerEntero("ID del sensor: ");
        gestorInfraestructura.buscarSensorPorId(idSensor);
        int idEstacion = consola.leerEntero("ID de la estacion donde esta instalado: ");

        gestorInfraestructura.retirarSensor(idSensor, idEstacion);
        System.out.println("Sensor retirado.");
    }

    private void trasladarSensor() {
        mostrarSensores(gestorInfraestructura.listarSensores());
        int idSensor = consola.leerEntero("ID del sensor: ");
        gestorInfraestructura.buscarSensorPorId(idSensor);
        mostrarEstaciones(gestorInfraestructura.listarEstaciones());
        int idDestino = consola.leerEntero("ID de la estacion destino: ");

        gestorInfraestructura.trasladarSensor(idSensor, idDestino);
        System.out.println("Sensor trasladado.");
    }

    private void cambiarEstadoSensor() {
        mostrarSensores(gestorInfraestructura.listarSensores());
        int idSensor = consola.leerEntero("ID del sensor: ");
        gestorInfraestructura.buscarSensorPorId(idSensor);
        EstadoOperativo estado = consola.leerOpcion("Nuevo estado:", EstadoOperativo.values());

        gestorInfraestructura.cambiarEstadoSensor(idSensor, estado);
        System.out.println("Estado actualizado.");
    }

    private void listarSensores() {
        if (consola.leerSiNo("Mostrar solo los sensores sin instalar?")) {
            mostrarSensores(gestorInfraestructura.listarSensoresSinInstalar());
        } else {
            mostrarSensores(gestorInfraestructura.listarSensores());
        }
    }

    // ---------- Auxiliares ----------

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("===== GESTION DE INFRAESTRUCTURA =====");
        System.out.println("1. Registrar zona");
        System.out.println("2. Cargar valor historico de una zona");
        System.out.println("3. Registrar estacion");
        System.out.println("4. Cambiar estado de una estacion");
        System.out.println("5. Registrar sensor");
        System.out.println("6. Instalar sensor");
        System.out.println("7. Retirar sensor");
        System.out.println("8. Trasladar sensor");
        System.out.println("9. Cambiar estado de un sensor");
        System.out.println("10. Listar zonas");
        System.out.println("11. Listar estaciones");
        System.out.println("12. Listar sensores");
        System.out.println("0. Volver");
    }

    private void mostrarZonas() {
        List<ZonaGeografica> zonas = gestorInfraestructura.listarZonas();
        System.out.println("Zonas disponibles:");
        if (zonas.isEmpty()) {
            System.out.println("No hay zonas registradas.");
        }
        for (ZonaGeografica z : zonas) {
            System.out.println("  ID " + z.getIdZona() + " - " + z.getNombre() + " - vulnerabilidad "
                    + z.getNivelVulnerabilidad() + " - alerta " + z.getNivelAlertaActual());
        }
    }

    private void mostrarEstaciones(List<EstacionMeteorologica> estaciones) {
        System.out.println("Estaciones disponibles:");
        if (estaciones.isEmpty()) {
            System.out.println("No hay estaciones para mostrar.");
        }
        for (EstacionMeteorologica e : estaciones) {
            System.out.println("  ID " + e.getIdEstacion() + " - " + e.getNombre() + " - " + e.getTipo() + " - "
                    + e.getEstado() + " - zona " + e.getZonaGeografica().getNombre());
        }
    }

    private void mostrarSensores(List<Sensor> sensores) {
        System.out.println("Sensores disponibles:");
        if (sensores.isEmpty()) {
            System.out.println("No hay sensores para mostrar.");
        }
        for (Sensor s : sensores) {
            String ubicacion = "sin instalar";
            if (s.getEstacionMeteorologica() != null) {
                ubicacion = "estacion " + s.getEstacionMeteorologica().getIdEstacion() + " ("
                        + s.getEstacionMeteorologica().getNombre() + ")";
            }
            System.out.println("  ID " + s.getIdSensor() + " - " + s.getMarca() + " " + s.getModelo() + " - "
                    + s.getEstado() + " - " + ubicacion);
        }
    }

    private void mostrarResponsables() {
        System.out.println("Responsables disponibles:");
        for (ResponsableTecnico r : gestorPersonal.getResponsablesTecnicos()) {
            System.out.println("  DNI " + r.getDni() + " - " + r.getNombreCompleto());
        }
    }
}

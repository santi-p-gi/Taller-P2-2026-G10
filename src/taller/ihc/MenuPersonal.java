package taller.ihc;

import java.time.LocalDateTime;
import java.util.List;

import taller.enumeraciones.TipoMantenimiento;
import taller.gestion.GestorInfraestructura;
import taller.gestion.GestorPersonal;
import taller.modelo.EquipoRespuesta;
import taller.modelo.EstacionMeteorologica;
import taller.modelo.Mantenimiento;
import taller.modelo.ResponsableTecnico;
import taller.modelo.Sensor;

/**
 * Submenu de administracion del personal tecnico: responsables, equipos de respuesta y mantenimientos.
 */
public class MenuPersonal {
    private Consola consola;
    private GestorPersonal gestorPersonal;
    private GestorInfraestructura gestorInfraestructura;

    /**
     * Crea el submenu de personal.
     *
     * @param consola               consola compartida para leer datos
     * @param gestorPersonal        gestor de personal
     * @param gestorInfraestructura gestor de infraestructura, para elegir estaciones y sensores
     */
    public MenuPersonal(Consola consola, GestorPersonal gestorPersonal,
            GestorInfraestructura gestorInfraestructura) {
        this.consola = consola;
        this.gestorPersonal = gestorPersonal;
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
                        registrarResponsable();
                        break;
                    case 2:
                        mostrarResponsables();
                        break;
                    case 3:
                        registrarEquipo();
                        break;
                    case 4:
                        mostrarEquipos();
                        break;
                    case 5:
                        agregarIntegrante();
                        break;
                    case 6:
                        registrarMantenimiento();
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

    // ---------- Responsables tecnicos ----------

    private void registrarResponsable() {
        String dni = consola.leerTexto("DNI: ");
        String nombre = consola.leerTexto("Nombre: ");
        String apellido = consola.leerTexto("Apellido: ");
        String correo = consola.leerTexto("Correo electronico: ");
        String celular = consola.leerTexto("Celular: ");
        String especialidad = consola.leerTexto("Especialidad: ");
        String licencia = consola.leerTexto("Numero de licencia: ");

        ResponsableTecnico responsable = gestorPersonal.registrarResponsableTecnico(dni, nombre, apellido, correo,
                celular, especialidad, licencia);
        System.out.println("Responsable " + responsable.getNombreCompleto() + " registrado.");
    }

    // ---------- Equipos de respuesta ----------

    private void registrarEquipo() {
        String nombre = consola.leerTexto("Nombre del equipo: ");
        boolean disponible = consola.leerSiNo("El equipo esta disponible?");

        EquipoRespuesta equipo = gestorPersonal.registrarEquipoRespuesta(nombre, disponible);
        System.out.println("Equipo registrado con ID " + equipo.getIdEquipo() + " (sin integrantes).");
    }

    private void agregarIntegrante() {
        mostrarEquipos();
        int idEquipo = consola.leerEntero("ID del equipo: ");
        gestorPersonal.buscarEquipoPorId(idEquipo);
        mostrarResponsables();
        String dni = consola.leerTexto("DNI del responsable a agregar: ");

        gestorPersonal.agregarIntegranteAEquipo(idEquipo, dni);
        System.out.println("Integrante agregado.");
    }

    // ---------- Mantenimientos ----------

    private void registrarMantenimiento() {
        mostrarEstaciones();
        int idEstacion = consola.leerEntero("ID de la estacion: ");
        EstacionMeteorologica estacion = gestorInfraestructura.buscarEstacionPorId(idEstacion);

        System.out.println("Sensores de la estacion:");
        for (Sensor s : estacion.getSensores()) {
            System.out.println("  ID " + s.getIdSensor() + " - " + s.getMarca() + " " + s.getModelo() + " - "
                    + s.getEstado());
        }
        Integer idSensor = consola.leerEnteroOpcional(
                "ID del sensor intervenido (vacio si es sobre la estacion, obligatorio para CALIBRACION): ");
        if (idSensor != null) {
            Sensor sensor = gestorInfraestructura.buscarSensorPorId(idSensor);
            if (sensor.getEstacionMeteorologica() != estacion) {
                throw new IllegalArgumentException(
                        "El sensor " + idSensor + " no esta instalado en la estacion " + idEstacion);
            }
        }

        mostrarResponsables();
        String dni = consola.leerTexto("DNI del responsable que lo realizo: ");
        gestorPersonal.buscarResponsablePorDni(dni);
        TipoMantenimiento tipo = consola.leerOpcion("Tipo de mantenimiento:", TipoMantenimiento.values());
        LocalDateTime fecha = consola.leerFechaHora("Fecha del mantenimiento");
        String descripcion = consola.leerTexto("Descripcion: ");

        Mantenimiento mantenimiento = gestorPersonal.registrarMantenimiento(idEstacion, idSensor, dni, tipo, fecha,
                descripcion);
        System.out.println("Mantenimiento registrado con ID " + mantenimiento.getIdMantenimiento() + ".");
        if (tipo == TipoMantenimiento.CALIBRACION) {
            System.out.println("Se actualizo la fecha de calibracion del sensor " + idSensor + ".");
        }
    }

    // ---------- Auxiliares ----------

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("===== ADMINISTRACION DE PERSONAL TECNICO =====");
        System.out.println("1. Registrar responsable tecnico");
        System.out.println("2. Listar responsables tecnicos");
        System.out.println("3. Registrar equipo de respuesta");
        System.out.println("4. Listar equipos de respuesta");
        System.out.println("5. Agregar integrante a un equipo");
        System.out.println("6. Registrar mantenimiento");
        System.out.println("0. Volver");
    }

    private void mostrarResponsables() {
        List<ResponsableTecnico> responsables = gestorPersonal.getResponsablesTecnicos();
        System.out.println("Responsables disponibles:");
        if (responsables.isEmpty()) {
            System.out.println("No hay responsables registrados.");
        }
        for (ResponsableTecnico r : responsables) {
            String equipo = "sin equipo";
            if (r.getEquipoRespuesta() != null) {
                equipo = "equipo " + r.getEquipoRespuesta().getNombre();
            }
            System.out.println("  DNI " + r.getDni() + " - " + r.getNombreCompleto() + " - " + r.getEspecialidad()
                    + " - " + equipo);
        }
    }

    private void mostrarEquipos() {
        List<EquipoRespuesta> equipos = gestorPersonal.getEquiposRespuesta();
        System.out.println("Equipos disponibles:");
        if (equipos.isEmpty()) {
            System.out.println("No hay equipos registrados.");
        }
        for (EquipoRespuesta e : equipos) {
            String estado = "no disponible";
            if (e.isDisponible()) {
                estado = "disponible";
            }
            System.out.println("  ID " + e.getIdEquipo() + " - " + e.getNombre() + " - " + estado);
            if (!e.tieneIntegrantes()) {
                System.out.println("      sin integrantes");
            }
            for (ResponsableTecnico r : e.getResponsablesAsignados()) {
                System.out.println("      DNI " + r.getDni() + " - " + r.getNombreCompleto());
            }
        }
    }

    private void mostrarEstaciones() {
        System.out.println("Estaciones disponibles:");
        for (EstacionMeteorologica e : gestorInfraestructura.listarEstaciones()) {
            String responsable = "sin responsable";
            if (e.getResponsableTecnico() != null) {
                responsable = e.getResponsableTecnico().getNombreCompleto();
            }
            System.out.println("  ID " + e.getIdEstacion() + " - " + e.getNombre() + " - " + e.getEstado()
                    + " - a cargo de " + responsable);
        }
    }
}

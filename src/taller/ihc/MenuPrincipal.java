package taller.ihc;

import java.util.Scanner;

import taller.gestion.GestorAlertas;
import taller.gestion.GestorInfraestructura;
import taller.gestion.GestorMediciones;
import taller.gestion.GestorPersonal;

/**
 * Menu principal del sistema. Muestra los modulos disponibles y deriva a cada submenu.
 */
public class MenuPrincipal {
    private GestorInfraestructura gestorInfraestructura;
    private GestorPersonal gestorPersonal;
    private GestorMediciones gestorMediciones;
    private GestorAlertas gestorAlertas;
    private Scanner scanner;

    /**
     * Crea el menu principal con los gestores del sistema.
     *
     * @param gestorInfraestructura gestor de infraestructura meteorologica
     * @param gestorPersonal        gestor de personal tecnico
     * @param gestorMediciones      gestor de mediciones
     * @param gestorAlertas         gestor de alertas
     */
    public MenuPrincipal(GestorInfraestructura gestorInfraestructura, GestorPersonal gestorPersonal,
            GestorMediciones gestorMediciones, GestorAlertas gestorAlertas) {
        this.gestorInfraestructura = gestorInfraestructura;
        this.gestorPersonal = gestorPersonal;
        this.gestorMediciones = gestorMediciones;
        this.gestorAlertas = gestorAlertas;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Muestra el menu en un bucle hasta que el usuario elige salir.
     */
    public void iniciar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = leerOpcion();
            switch (opcion) {
                case 1:
                    System.out.println("Gestion de infraestructura: en construccion.");
                    break;
                case 2:
                    System.out.println("Administracion de personal tecnico: en construccion.");
                    break;
                case 3:
                    System.out.println("Registro de mediciones: en construccion.");
                    break;
                case 4:
                    System.out.println("Sistema de alerta temprana: en construccion.");
                    break;
                case 5:
                    System.out.println("Informes y reportes: en construccion.");
                    break;
                case 0:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
            }
        } while (opcion != 0);
    }

    // ---------- Auxiliares ----------

    private void mostrarOpciones() {
        System.out.println();
        System.out.println("===== SISTEMA DE ALERTA TEMPRANA - EL NINO =====");
        System.out.println("1. Gestion de infraestructura meteorologica");
        System.out.println("2. Administracion de personal tecnico");
        System.out.println("3. Registro de mediciones");
        System.out.println("4. Sistema de alerta temprana");
        System.out.println("5. Informes y reportes");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
    }

    private int leerOpcion() {
        String linea = scanner.nextLine();
        try {
            return Integer.parseInt(linea.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

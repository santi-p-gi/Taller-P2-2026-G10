package taller.ihc;

import taller.gestion.GestorAlertas;
import taller.gestion.GestorInfraestructura;
import taller.gestion.GestorMediciones;
import taller.gestion.GestorPersonal;
import taller.gestion.GestorReportes;

/**
 * Menu principal del sistema. Muestra los modulos disponibles y deriva a cada
 * submenu.
 */
public class MenuPrincipal {
    private GestorInfraestructura gestorInfraestructura;
    private GestorPersonal gestorPersonal;
    private GestorMediciones gestorMediciones;
    private GestorAlertas gestorAlertas;
    private GestorReportes gestorReportes;
    private Consola consola;

    /**
     * Crea el menu principal con los gestores del sistema.
     *
     * @param gestorInfraestructura gestor de infraestructura meteorologica
     * @param gestorPersonal        gestor de personal tecnico
     * @param gestorMediciones      gestor de mediciones
     * @param gestorAlertas         gestor de alertas
     * @param gestorReportes        gestor de reportes
     */
    public MenuPrincipal(GestorInfraestructura gestorInfraestructura, GestorPersonal gestorPersonal,
            GestorMediciones gestorMediciones, GestorAlertas gestorAlertas, GestorReportes gestorReportes) {
        this.gestorInfraestructura = gestorInfraestructura;
        this.gestorPersonal = gestorPersonal;
        this.gestorMediciones = gestorMediciones;
        this.gestorAlertas = gestorAlertas;
        this.gestorReportes = gestorReportes;
        this.consola = new Consola();
    }

    /**
     * Muestra el menu en un bucle hasta que el usuario elige salir.
     */
    public void iniciar() {
        int opcion;
        do {
            mostrarOpciones();
            opcion = consola.leerEntero("Seleccione una opcion: ");
            switch (opcion) {
                case 1:
                    new MenuInfraestructura(consola, gestorInfraestructura, gestorPersonal).iniciar();
                    break;
                case 2:
                    new MenuPersonal(consola, gestorPersonal, gestorInfraestructura).iniciar();
                    break;
                case 3:
                    new MenuMediciones(consola, gestorMediciones, gestorInfraestructura, gestorAlertas,
                            gestorPersonal).iniciar();
                    break;
                case 4:
                    new MenuAlertas(consola, gestorAlertas, gestorInfraestructura, gestorPersonal).iniciar();
                    break;
                case 5:
                    new MenuReportes(consola, gestorReportes, gestorInfraestructura).iniciar();
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
    }
}

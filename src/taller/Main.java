package taller;

import taller.gestion.GestorAlertas;
import taller.gestion.GestorInfraestructura;
import taller.gestion.GestorMediciones;
import taller.gestion.GestorPersonal;
import taller.gestion.GestorReportes;
import taller.gestion.RegistroDatos;
import taller.ihc.MenuPrincipal;

/**
 * Punto de entrada del sistema de gestion de estaciones meteorologicas y
 * alertas por El Nino.
 */
public class Main {

    /**
     * Crea el registro de datos y los gestores, carga los datos de prueba e inicia
     * el menu principal.
     *
     * @param args argumentos de linea de comandos (no se usan)
     */
    public static void main(String[] args) {
        RegistroDatos registro = new RegistroDatos();
        GestorInfraestructura gestorInfraestructura = new GestorInfraestructura(registro);
        GestorPersonal gestorPersonal = new GestorPersonal(registro);
        GestorMediciones gestorMediciones = new GestorMediciones(registro);
        GestorAlertas gestorAlertas = new GestorAlertas(registro);
        GestorReportes gestorReportes = new GestorReportes(registro);

        DatosPrueba.cargar(gestorInfraestructura, gestorPersonal, gestorMediciones, gestorAlertas);

        MenuPrincipal menu = new MenuPrincipal(gestorInfraestructura, gestorPersonal, gestorMediciones,
                gestorAlertas, gestorReportes);
        menu.iniciar();
    }
}

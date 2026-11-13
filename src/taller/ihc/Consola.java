package taller.ihc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

/**
 * Lectura de datos por consola compartida por todos los menus. Cada metodo repite la pregunta
 * hasta que el usuario ingresa un dato valido.
 */
public class Consola {
    // STRICT rechaza fechas imposibles como 31/02 en lugar de ajustarlas
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter FORMATO_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    private Scanner scanner;

    /**
     * Crea la consola con un unico Scanner sobre la entrada estandar.
     */
    public Consola() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Lee un texto no vacio.
     *
     * @param mensaje texto a mostrar
     * @return el texto ingresado
     */
    public String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            if (!linea.isEmpty()) {
                return linea;
            }
            System.out.println("El dato no puede estar vacio.");
        }
    }

    /**
     * Lee un numero entero.
     *
     * @param mensaje texto a mostrar
     * @return el numero ingresado
     */
    public int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero.");
            }
        }
    }

    /**
     * Lee un numero entero que se puede omitir dejando la linea vacia.
     *
     * @param mensaje texto a mostrar
     * @return el numero ingresado, o null si se dejo vacio
     */
    public Integer leerEnteroOpcional(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                return null;
            }
            try {
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero o dejarlo vacio.");
            }
        }
    }

    /**
     * Lee un numero decimal. Acepta punto o coma como separador.
     *
     * @param mensaje texto a mostrar
     * @return el numero ingresado
     */
    public double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = scanner.nextLine().trim().replace(',', '.');
            try {
                double valor = Double.parseDouble(linea);
                if (!Double.isNaN(valor) && !Double.isInfinite(valor)) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // se informa abajo
            }
            System.out.println("Debe ingresar un numero.");
        }
    }

    /**
     * Lee una respuesta por si o por no.
     *
     * @param mensaje texto a mostrar
     * @return true si la respuesta es S, false si es N
     */
    public boolean leerSiNo(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (S/N): ");
            String linea = scanner.nextLine().trim().toUpperCase();
            if (linea.equals("S")) {
                return true;
            }
            if (linea.equals("N")) {
                return false;
            }
            System.out.println("Debe responder S o N.");
        }
    }

    /**
     * Lee una fecha con formato dd/MM/yyyy.
     *
     * @param mensaje texto a mostrar
     * @return la fecha ingresada
     */
    public LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (dd/MM/yyyy): ");
            String linea = scanner.nextLine().trim();
            try {
                return LocalDate.parse(linea, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha invalida.");
            }
        }
    }

    /**
     * Lee una fecha con formato dd/MM/yyyy que se puede omitir dejando la linea vacia.
     *
     * @param mensaje texto a mostrar
     * @return la fecha ingresada, o null si se dejo vacio
     */
    public LocalDate leerFechaOpcional(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (dd/MM/yyyy, vacio para omitir): ");
            String linea = scanner.nextLine().trim();
            if (linea.isEmpty()) {
                return null;
            }
            try {
                return LocalDate.parse(linea, FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha invalida.");
            }
        }
    }

    /**
     * Lee una fecha y hora con formato dd/MM/yyyy HH:mm.
     *
     * @param mensaje texto a mostrar
     * @return la fecha y hora ingresada
     */
    public LocalDateTime leerFechaHora(String mensaje) {
        while (true) {
            System.out.print(mensaje + " (dd/MM/yyyy HH:mm): ");
            String linea = scanner.nextLine().trim();
            try {
                return LocalDateTime.parse(linea, FORMATO_FECHA_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha y hora invalidas.");
            }
        }
    }

    /**
     * Muestra una lista numerada de opciones y devuelve la elegida. Sirve para cualquier enum,
     * pasando sus valores, por ejemplo {@code NivelAlerta.values()}.
     *
     * @param <T>     tipo de las opciones
     * @param mensaje texto a mostrar
     * @param valores opciones disponibles
     * @return la opcion elegida
     */
    public <T> T leerOpcion(String mensaje, T[] valores) {
        System.out.println(mensaje);
        for (int i = 0; i < valores.length; i++) {
            System.out.println("  " + (i + 1) + ". " + valores[i]);
        }
        while (true) {
            int opcion = leerEntero("Opcion: ");
            if (opcion >= 1 && opcion <= valores.length) {
                return valores[opcion - 1];
            }
            System.out.println("Opcion fuera de rango.");
        }
    }

    /**
     * Formatea una fecha y hora para mostrarla por consola.
     *
     * @param fechaHora fecha y hora
     * @return texto con formato dd/MM/yyyy HH:mm
     */
    public String formatear(LocalDateTime fechaHora) {
        return fechaHora.format(FORMATO_FECHA_HORA);
    }

    /**
     * Formatea solo la fecha de una fecha y hora para mostrarla por consola.
     *
     * @param fechaHora fecha y hora
     * @return texto con formato dd/MM/yyyy
     */
    public String formatearFecha(LocalDateTime fechaHora) {
        return fechaHora.format(FORMATO_FECHA);
    }
}

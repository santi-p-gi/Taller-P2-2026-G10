package taller.modelo;

/**
 * Acumulado medio de precipitacion (mm) de una zona para un mes del anio, usado como referencia historica.
 */
public class ValorHistorico {
    private int mes;
    private double acumuladoMedio;

    /**
     * Crea un valor historico vacio.
     */
    public ValorHistorico() {
    }

    /**
     * Crea un valor historico para un mes.
     *
     * @param mes            mes del anio (1 a 12)
     * @param acumuladoMedio acumulado medio de precipitacion del mes, en mm
     * @throws IllegalArgumentException si el mes no esta entre 1 y 12
     */
    public ValorHistorico(int mes, double acumuladoMedio) throws IllegalArgumentException {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }
        this.mes = mes;
        setAcumuladoMedio(acumuladoMedio);
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public double getAcumuladoMedio() {
        return acumuladoMedio;
    }

    public void setAcumuladoMedio(double acumuladoMedio) {
        if (!(acumuladoMedio >= 0)) {
            throw new IllegalArgumentException("El acumulado medio no puede ser negativo.");
        }
        this.acumuladoMedio = acumuladoMedio;
    }

    public String toString() {
        return "ValorHistorico [mes=" + mes + ", acumuladoMedio=" + acumuladoMedio + "]";
    }
}
package taller.modelo;

public class ValorHistorico {
    private int mes;
    private double acumuladoMedio;

    public ValorHistorico() {
    }

    public ValorHistorico(int mes, double acumuladoMedio) throws IllegalArgumentException {
        if (mes < 1 || mes > 12) {
            throw new IllegalArgumentException("El mes debe estar entre 1 y 12.");
        }
        this.mes = mes;
        this.acumuladoMedio = acumuladoMedio;
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
        this.acumuladoMedio = acumuladoMedio;
    }

    public String toString() {
        return "ValorHistorico [mes=" + mes + ", acumuladoMedio=" + acumuladoMedio + "]";
    }
}
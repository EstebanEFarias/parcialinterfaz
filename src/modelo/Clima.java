package modelo;

public enum Clima {

    SOLEADO(1.5, 5.0, 0.0, 0.0),
    LLUVIOSO(2.0, 3.0, -5.0, 0.0),
    SEQUIA(0.5, -5.0, 0.0, 0.0),
    INVIERNO(0.0, -8.0, 0.0, 0.20);

    private final double factorReproduccionPlanta;
    private final double efectoEnergiaConejo;
    private final double efectoEnergiaLobo;
    private final double bonusCazaLobo;

    Clima(double factorReproduccionPlanta, double efectoEnergiaConejo,
            double efectoEnergiaLobo, double bonusCazaLobo) {
        this.factorReproduccionPlanta = factorReproduccionPlanta;
        this.efectoEnergiaConejo = efectoEnergiaConejo;
        this.efectoEnergiaLobo = efectoEnergiaLobo;
        this.bonusCazaLobo = bonusCazaLobo;
    }

    public double getFactorReproduccionPlanta() {
        return factorReproduccionPlanta;
    }

    public double getEfectoEnergiaConejo() {
        return efectoEnergiaConejo;
    }

    public double getEfectoEnergiaLobo() {
        return efectoEnergiaLobo;
    }

    public double getBonusCazaLobo() {
        return bonusCazaLobo;
    }
}

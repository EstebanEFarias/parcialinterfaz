package modelo;

import java.util.Random;

public class Conejo extends Animal implements Reproducible {
    private static final double ENERGIA_MINIMA_REPRODUCCION = 60.0;
    private static final double ENERGIA_PERDIDA_SIN_COMIDA = 15.0;
    private static final double ENERGIA_PELIGRO = 20.0;
    private static final double COSTO_ENERGIA_CRIA = 20.0;
    private static final Random RANDOM = new Random();

    public Conejo(String nombre, double energia) {
        super(nombre, energia, 8, 2.5);
    }

    @Override
    public void actuar(Ecosistema eco) {
        comer(eco);
        if (isViva()) {
            intentarReproduccion(eco);
        }
    }

    @Override
    public void comer(Ecosistema eco) {
        Planta planta = eco.buscarPlantaViva();
        if (planta == null) {
            setEnergia(getEnergia() - ENERGIA_PERDIDA_SIN_COMIDA);
            eco.registrarEvento("Conejo '" + getNombre() + "' no encontró comida (-"
                    + (int) ENERGIA_PERDIDA_SIN_COMIDA + " energía)");
            return;
        }

        double valorNutritivo = planta.serComida();
        eco.sumarMuertePlanta();
        setEnergia(getEnergia() + valorNutritivo);

        if (valorNutritivo < 0) {
            eco.registrarEvento("Conejo '" + getNombre() + "' comió '" + planta.getNombre()
                    + "' y era VENENOSA (" + (int) valorNutritivo + " energía)");
        } else {
            eco.registrarEvento("Conejo '" + getNombre() + "' comió '" + planta.getNombre()
                    + "' (+" + (int) valorNutritivo + " energía)");
        }
    }

    @Override
    public boolean puedeReproducirse() {
        return isViva() && getEnergia() > ENERGIA_MINIMA_REPRODUCCION;
    }

    @Override
    public void reproducirse(Ecosistema eco) {
        if (eco.contarConejosVivos() < 2) {
            return;
        }
        Conejo cria = eco.crearConejo(30 + RANDOM.nextInt(15));
        eco.registrarNuevoConejo(cria);
        setEnergia(getEnergia() - COSTO_ENERGIA_CRIA);

        eco.registrarEvento("Conejo '" + getNombre() + "' tuvo una cría: '" + cria.getNombre() + "'");
    }

    @Override
    public void morir() {
        setViva(false);
    }

    @Override
    public String getTipo() {
        return "Conejo";
    }

    @Override
    public String describirEstado() {
        return getEnergia() < ENERGIA_PELIGRO ? "En peligro (poca energía)" : "Bien";
    }

    public void aplicarEfectoClima(Clima clima) {
        setEnergia(getEnergia() + clima.getEfectoEnergiaConejo());
    }
}
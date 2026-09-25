package ecosistema;

import java.util.Random;

public class Conejo extends Animal implements Reproducible {

    private static final double ENERGIA_MINIMA_REPRODUCCION = 60.0;
    private static final double ENERGIA_PERDIDA_SIN_COMIDA = 15.0;
    private static final double ENERGIA_PELIGRO = 20.0;
    private static final double COSTO_ENERGIA_CRIA = 20.0;
    private static final Random RANDOM = new Random();
    private static int contador = 0;

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
            eco.contarEvento();
            System.out.println("Conejo '" + getNombre() + "' no encontró comida (-"
                    + (int) ENERGIA_PERDIDA_SIN_COMIDA + " energia)" + etiquetaPeligro());
            return;
        }

        double valorNutritivo = planta.serComida();
        setEnergia(getEnergia() + valorNutritivo);
        eco.contarEvento();

        if (valorNutritivo < 0) {
            System.out.println("Conejo '" + getNombre() + "' comió '" + planta.getNombre()
                    + "' y resultó ser venenosa (" + (int) valorNutritivo + " energia)" + etiquetaPeligro());
        } else {
            System.out.println("Conejo '" + getNombre() + "' comió '" + planta.getNombre()
                    + "' (+" + (int) valorNutritivo + " energia)");
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
        contador++;
        double energiaCria = 30 + RANDOM.nextInt(15);
        Conejo cria = new Conejo("Cria-" + contador, energiaCria);

        eco.registrarNuevoConejo(cria);
        eco.contarEvento();
        setEnergia(getEnergia() - COSTO_ENERGIA_CRIA);

        System.out.println("Conejo '" + getNombre() + "' tuvo una cría: '" + cria.getNombre() + "'");
    }

    @Override
    public void morir() {
        setViva(false);
        System.out.println("Conejo '" + getNombre() + "' murió de inanición.");
    }

    @Override
    public void mostrarEstado() {
        System.out.println("Conejo '" + getNombre() + "' | energia: " + (int) getEnergia()
                + etiquetaPeligro());
    }

    private String etiquetaPeligro() {
        return getEnergia() < ENERGIA_PELIGRO ? " [PELIGRO: energia=" + (int) getEnergia() + "]" : "";
    }

    public void aplicarEfectoClima(Clima clima) {
        setEnergia(getEnergia() + clima.getEfectoEnergiaConejo());
    }
}

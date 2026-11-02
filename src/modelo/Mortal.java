package modelo;

public interface Mortal {

    boolean estaVivo();

    void morir();

    default void verificarMuerte() {
        if (!estaVivo()) {
            morir();
        }
    }
}

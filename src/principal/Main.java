package principal;

import controlador.SimulacionController;
import dao.SimulacionDAO;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        SimulacionDAO dao = new SimulacionDAO();
        VentanaPrincipal vista = new VentanaPrincipal();

        SimulacionController controlador = new SimulacionController(vista, dao);
        vista.setControlador(controlador);

        vista.setLocationRelativeTo(null);
        vista.setVisible(true);

        controlador.iniciar();
    }
}
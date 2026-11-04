package controlador;

import dao.SimulacionDAO;
import java.util.ArrayList;
import java.util.List;
import modelo.Clima;
import modelo.Ecosistema;
import modelo.Entidad;
import modelo.Lobo;
import vista.VentanaPrincipal;

public class SimulacionController {
    public static final String PANTALLA_CONFIG = "config";
    public static final String PANTALLA_SIMULACION = "simulacion";
    public static final String PANTALLA_REPORTE = "reporte";

    private Ecosistema modelo;
    private final VentanaPrincipal vista;
    private final SimulacionDAO dao;

    public SimulacionController(VentanaPrincipal vista, SimulacionDAO dao) {
        this.vista = vista;
        this.dao = dao;
        this.modelo = null;
    }

    public void iniciar() {
        vista.cargarClimas(nombresDeClimas());

        if (!dao.existeGuardado()) {
            mostrarSinSimulacion();
            vista.mostrarMensaje("No hay simulaciones guardadas. Configurá una nueva para empezar.");
            return;
        }

        Ecosistema cargado = dao.cargar();
        if (cargado == null) {
            mostrarSinSimulacion();
            vista.mostrarError("El archivo guardado tiene datos inválidos y no se pudo cargar.\n"
                    + "Vas a empezar una simulación nueva.");
            return;
        }

        modelo = cargado;
        refrescarVista();
        if (modelo.estaTerminada()) {
            mostrarReporte();
            vista.mostrarMensaje("Se cargó una simulación que ya había terminado.");
        } else {
            vista.mostrarPantalla(PANTALLA_SIMULACION);
            vista.mostrarMensaje("Se retomó la simulación guardada (turno "
                    + modelo.getTurnoActual() + " de " + modelo.getTurnosTotales() + ").");
        }
    }

    public void iniciarSimulacion(String txtPlantas, String txtConejos, String txtLobos,
            String nombreClima, String txtTurnos) {
        Integer plantas = validarEntero(txtPlantas, "plantas", Ecosistema.MIN_PLANTAS, Ecosistema.MAX_PLANTAS);
        if (plantas == null) {
            return;
        }
        Integer conejos = validarEntero(txtConejos, "conejos", Ecosistema.MIN_CONEJOS, Ecosistema.MAX_CONEJOS);
        if (conejos == null) {
            return;
        }
        Integer lobos = validarEntero(txtLobos, "lobos", Ecosistema.MIN_LOBOS, Ecosistema.MAX_LOBOS);
        if (lobos == null) {
            return;
        }
        Clima clima = convertirClima(nombreClima);
        if (clima == null) {
            vista.mostrarError("Elegí un clima inicial.");
            return;
        }
        Integer turnos = validarEntero(txtTurnos, "turnos", Ecosistema.MIN_TURNOS, Ecosistema.MAX_TURNOS);
        if (turnos == null) {
            return;
        }

        modelo = new Ecosistema(clima, turnos);
        modelo.poblarInicial(plantas, conejos, lobos);

        guardarCambios();
        refrescarVista();
        vista.mostrarPantalla(PANTALLA_SIMULACION);
        vista.mostrarMensaje("Simulación creada. Tocá \"Avanzar turno\" para empezar.");
    }

    public void avanzarTurno() {
        if (modelo == null) {
            vista.mostrarError("Todavía no hay una simulación en curso.");
            return;
        }
        if (modelo.estaTerminada()) {
            vista.mostrarError("La simulación ya terminó. Podés ver el reporte o empezar una nueva.");
            return;
        }

        modelo.procesarTurno();
        guardarCambios();
        refrescarVista();

        if (modelo.estaTerminada()) {
            vista.mostrarInformacion("La simulación terminó.\n" + modelo.getCausaFin());
            mostrarReporte();
            vista.mostrarMensaje("Simulación terminada. " + modelo.getCausaFin());
        } else if (modelo.puedeIntervenir()) {
            vista.mostrarMensaje("Turno " + modelo.getTurnoActual()
                    + ": podés intervenir o seguir avanzando.");
        } else {
            vista.mostrarMensaje("Turno " + modelo.getTurnoActual() + " procesado.");
        }
    }

    public void cambiarClima(String nombreClima) {
        if (!sePuedeIntervenir()) {
            return;
        }
        Clima nuevo = convertirClima(nombreClima);
        if (nuevo == null) {
            vista.mostrarError("Elegí un clima.");
            return;
        }
        if (nuevo == modelo.getClimaActual()) {
            vista.mostrarError("El clima ya es " + nuevo + ". Elegí otro.");
            return;
        }
        modelo.cambiarClima(nuevo);
        guardarCambios();
        refrescarVista();
        vista.mostrarMensaje("El clima ahora es " + nuevo + ".");
    }

    public void agregarEntidad(String tipo) {
        if (!sePuedeIntervenir()) {
            return;
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            vista.mostrarError("Elegí qué entidad agregar.");
            return;
        }
        if (tipo.equalsIgnoreCase("lobo") && !modelo.puedeAgregarLobo()) {
            vista.mostrarError("No se puede agregar: ya hubo " + Ecosistema.MAX_LOBOS_SIMULACION
                    + " lobos en esta simulación (es el máximo).");
            return;
        }
        if (!modelo.agregarEntidad(tipo)) {
            vista.mostrarError("No se pudo agregar la entidad.");
            return;
        }
        guardarCambios();
        refrescarVista();
        vista.mostrarMensaje("Entidad agregada: " + tipo.toLowerCase() + ".");
    }

    public void nuevaSimulacion() {
        if (modelo != null && !modelo.estaTerminada()) {
            boolean seguro = vista.confirmar("Hay una simulación en curso (turno "
                    + modelo.getTurnoActual() + "). Si empezás una nueva se pierde.\n¿Querés continuar?");
            if (!seguro) {
                return;
            }
        }
        if (!dao.borrar()) {
            vista.mostrarError("No se pudo borrar la simulación guardada.");
        }
        modelo = null;
        mostrarSinSimulacion();
        vista.mostrarMensaje("Configurá la nueva simulación.");
    }

    public void verReporte() {
        if (modelo == null) {
            vista.mostrarError("Todavía no hay ninguna simulación.");
            return;
        }
        if (!modelo.estaTerminada()) {
            vista.mostrarError("El reporte está disponible cuando termina la simulación.\n"
                    + "Van " + modelo.getTurnoActual() + " de " + modelo.getTurnosTotales() + " turnos.");
            return;
        }
        mostrarReporte();
    }

    public void volverASimulacion() {
        if (modelo == null) {
            vista.mostrarPantalla(PANTALLA_CONFIG);
        } else {
            vista.mostrarPantalla(PANTALLA_SIMULACION);
        }
    }

    private void refrescarVista() {
        vista.actualizarContadores(modelo.getTurnoActual(), modelo.getTurnosTotales(),
                modelo.getClimaActual().name(), modelo.contarPlantasVivas(),
                modelo.contarConejosVivos(), modelo.contarLobosVivos());
        vista.actualizarTabla(filasEntidades());
        vista.actualizarEventos(modelo.getEventosTurno());
        vista.habilitarAvanzar(!modelo.estaTerminada());
        vista.habilitarVerReporte(modelo.estaTerminada());

        if (modelo.estaTerminada()) {
            vista.habilitarIntervencion(false, "La simulación terminó.");
        } else if (modelo.puedeIntervenir()) {
            vista.habilitarIntervencion(true, "¡Podés intervenir! Cambiá el clima o agregá una entidad.");
        } else if (modelo.isIntervinoEsteTurno()) {
            vista.habilitarIntervencion(false, "Ya interviniste. Próxima intervención: turno "
                    + modelo.getProximoTurnoIntervencion() + ".");
        } else {
            vista.habilitarIntervencion(false, "Próxima intervención: turno "
                    + modelo.getProximoTurnoIntervencion() + ".");
        }
    }

    private void mostrarSinSimulacion() {
        vista.limpiarFormulario();
        vista.limpiarSimulacion();
        vista.habilitarAvanzar(false);
        vista.habilitarVerReporte(false);
        vista.habilitarIntervencion(false, "No hay simulación en curso.");
        vista.mostrarPantalla(PANTALLA_CONFIG);
    }

    private List<Object[]> filasEntidades() {
        List<Object[]> filas = new ArrayList<>();
        for (Entidad e : modelo.getEntidadesVivas()) {
            filas.add(new Object[]{
                e.getTipo(), e.getNombre(), (int) e.getEnergia(), e.getEdad(), e.describirEstado()
            });
        }
        return filas;
    }

    private void mostrarReporte() {
        List<Object[]> resumen = new ArrayList<>();
        resumen.add(new Object[]{"Causa de fin", modelo.getCausaFin()});
        resumen.add(new Object[]{"Turnos jugados", modelo.getTurnoActual() + " de " + modelo.getTurnosTotales()});
        resumen.add(new Object[]{"Turno de mayor actividad", "Turno " + modelo.getTurnoMasActivo()
                + " (" + modelo.getMaxEventosRegistrados() + " eventos)"});
        resumen.add(new Object[]{"Planta más longeva", describirLongeva("planta")});
        resumen.add(new Object[]{"Conejo más longevo", describirLongeva("conejo")});
        resumen.add(new Object[]{"Lobo más longevo", describirLongeva("lobo")});
        Lobo cazador = modelo.getMejorCazador();
        resumen.add(new Object[]{"Lobo con más cacerías", cazador == null ? "No hubo lobos"
                : cazador.getNombre() + " (" + cazador.getExitosCaza() + " cacerías)"});

        List<Object[]> porTipo = new ArrayList<>();
        porTipo.add(filaTipo("Plantas", modelo.getNacimientosPlantas(), modelo.getMuertesPlantas(), 1));
        porTipo.add(filaTipo("Conejos", modelo.getNacimientosConejos(), modelo.getMuertesConejos(), 2));
        porTipo.add(filaTipo("Lobos", 0, modelo.getMuertesLobos(), 3));

        List<Object[]> historial = new ArrayList<>();
        for (int[] r : modelo.getHistorialPoblacion()) {
            historial.add(new Object[]{r[0], r[1], r[2], r[3]});
        }

        vista.mostrarReporte(resumen, porTipo, historial);
        vista.mostrarPantalla(PANTALLA_REPORTE);
    }

    private Object[] filaTipo(String tipo, int nacimientos, int muertes, int columna) {
        int[] mm = modelo.getMaximoYMinimo(columna);
        return new Object[]{tipo, nacimientos, muertes,
            mm[0] + " (turno " + mm[1] + ")", mm[2] + " (turno " + mm[3] + ")"};
    }

    private String describirLongeva(String tipo) {
        Entidad e = modelo.getMasLongeva(tipo);
        if (e == null) {
            return "No hubo";
        }
        return e.getNombre() + " (" + e.getEdad() + " turnos)";
    }

    private boolean sePuedeIntervenir() {
        if (modelo == null) {
            vista.mostrarError("Todavía no hay una simulación en curso.");
            return false;
        }
        if (!modelo.puedeIntervenir()) {
            vista.mostrarError("Ahora no se puede intervenir. Se puede cada "
                    + Ecosistema.CADA_CUANTOS_TURNOS_INTERVENIR + " turnos (una vez).");
            return false;
        }
        return true;
    }

    private void guardarCambios() {
        boolean salioBien = dao.guardar(modelo);
        if (!salioBien) {
            vista.mostrarError("No se pudo guardar la simulación en el archivo.");
        }
    }

    private Integer validarEntero(String texto, String campo, int min, int max) {
        if (texto == null || texto.trim().isEmpty()) {
            vista.mostrarError("Completá la cantidad de " + campo + ".");
            return null;
        }
        int valor;
        try {
            valor = Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            vista.mostrarError("La cantidad de " + campo + " tiene que ser un número entero.");
            return null;
        }
        if (valor < min || valor > max) {
            vista.mostrarError("La cantidad de " + campo + " tiene que estar entre " + min + " y " + max + ".");
            return null;
        }
        return valor;
    }

    private Clima convertirClima(String nombre) {
        if (nombre == null) {
            return null;
        }
        try {
            return Clima.valueOf(nombre);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String[] nombresDeClimas() {
        Clima[] climas = Clima.values();
        String[] nombres = new String[climas.length];
        for (int i = 0; i < climas.length; i++) {
            nombres[i] = climas[i].name();
        }
        return nombres;
    }
}
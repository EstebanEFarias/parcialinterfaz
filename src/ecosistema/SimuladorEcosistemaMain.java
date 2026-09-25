package ecosistema;

import java.util.Scanner;
import java.util.Random;

public class SimuladorEcosistemaMain {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("==================================================");
        System.out.println("      SIMULADOR DE ECOSISTEMA - por turnos");
        System.out.println("==================================================");

        Configuracion config = pedirConfiguracion(sc);
        Ecosistema eco = crearEcosistema(config);

        boolean terminado = false;
        String causaFin = "";

        for (int t = 1; t <= config.cantidadTurnos && !terminado; t++) {
            eco.procesarTurno();

            if (eco.ecosistemaColapsado()) {
                causaFin = "colapso del ecosistema (se extinguieron los " + eco.poblacionExtinta() + ")";
                terminado = true;
                break;
            }

            System.out.print("\n>>> Presione Enter para continuar...");
            sc.nextLine();

            if (eco.getTurnoActual() % 3 == 0) {
                menuIntervencion(sc, eco);
            }
        }

        if (!terminado) {
            causaFin = "se completaron los " + config.cantidadTurnos + " turnos configurados.";
        }

        eco.generarReporteFinal(causaFin);
        sc.close();
    }

    private static Configuracion pedirConfiguracion(Scanner sc) {
        Configuracion config;
        boolean confirmado = false;
        config = new Configuracion();

        do {
            System.out.println("\n-- Configuración inicial --");
            config.cantidadPlantas = leerEntero(sc, "Cantidad inicial de plantas (5-30): ", 5, 30);
            config.cantidadConejos = leerEntero(sc, "Cantidad inicial de conejos (2-15): ", 2, 15);
            config.cantidadLobos = leerEntero(sc, "Cantidad inicial de lobos (1-5): ", 1, 5);
            config.climaInicial = leerClima(sc);
            config.cantidadTurnos = leerEntero(sc, "Cantidad de turnos de la simulación (10-50): ", 10, 50);

            System.out.println("\n-- Resumen de la configuración --");
            System.out.println("Plantas: " + config.cantidadPlantas
                    + " | Conejos: " + config.cantidadConejos
                    + " | Lobos: " + config.cantidadLobos);
            System.out.println("Clima inicial: " + config.climaInicial
                    + " | Turnos: " + config.cantidadTurnos);

            confirmado = leerConfirmacion(sc, "¿Confirmar esta configuración? (s/n): ");
        } while (!confirmado);

        return config;
    }

    private static Clima leerClima(Scanner sc) {
        Clima[] opciones = Clima.values();
        while (true) {
            System.out.println("Clima inicial:");
            for (int i = 0; i < opciones.length; i++) {
                System.out.println((i + 1) + ". " + opciones[i]);
            }
            System.out.print("Opción: ");
            String entrada = sc.nextLine();
            try {
                int opcion = Integer.parseInt(entrada);
                if (opcion >= 1 && opcion <= opciones.length) {
                    return opciones[opcion - 1];
                }
                System.out.println("Opción fuera de rango.");
            } catch (NumberFormatException e) {
                System.out.println("Eso no es un número válido. Intenta de nuevo.");
            }
        }
    }

    private static Ecosistema crearEcosistema(Configuracion config) {
        Ecosistema eco = new Ecosistema(config.climaInicial);

        for (int i = 1; i <= config.cantidadPlantas; i++) {
            eco.agregarEntidad("planta", 20 + RANDOM.nextInt(20));
        }
        for (int i = 1; i <= config.cantidadConejos; i++) {
            eco.agregarEntidad("conejo", 40 + RANDOM.nextInt(20));
        }
        for (int i = 1; i <= config.cantidadLobos; i++) {
            eco.agregarEntidad("lobo", 50 + RANDOM.nextInt(20));
        }

        System.out.println("\nEcosistema creado. ¡Comienza la simulación!");
        return eco;
    }

    private static void menuIntervencion(Scanner sc, Ecosistema eco) {
        System.out.println("\n=== INTERVENCIÓN (cada 3 turnos) ===");
        System.out.println("1. Cambiar clima (actual: " + eco.getClimaActual() + ")");
        System.out.println("2. Agregar entidad");
        System.out.println("3. Solo avanzar");
        int opcion = leerEntero(sc, "Opción: ", 1, 3);

        switch (opcion) {
            case 1:
                Clima nuevoClima = leerClima(sc);
                if (leerConfirmacion(sc, "¿Confirmar cambio de clima a " + nuevoClima + "? (s/n): ")) {
                    eco.cambiarClima(nuevoClima);
                } else {
                    System.out.println("Cambio de clima cancelado.");
                }
                break;

            case 2:
                String tipo = leerTipoEntidad(sc);
                if (leerConfirmacion(sc, "¿Confirmar agregar un/a " + tipo + "? (s/n): ")) {
                    eco.agregarEntidad(tipo);
                } else {
                    System.out.println("Se canceló agregar la entidad.");
                }
                break;

            default:
                System.out.println("Se avanza sin intervenir.");
        }
    }

    private static String leerTipoEntidad(Scanner sc) {
        while (true) {
            System.out.print("¿Qué entidad agregar? (planta/conejo/lobo): ");
            String entrada = sc.nextLine().trim().toLowerCase();
            if (entrada.equals("planta") || entrada.equals("conejo") || entrada.equals("lobo")) {
                return entrada;
            }
            System.out.println("Opción inválida. Escribí planta, conejo o lobo.");
        }
    }

    private static int leerEntero(Scanner sc, String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine();
            try {
                int valor = Integer.parseInt(entrada);
                if (valor < min || valor > max) {
                    System.out.println("El valor debe estar entre " + min + " y " + max + ".");
                    continue;
                }
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Eso no es un número válido. Intenta de nuevo.");
            }
        }
    }

    private static boolean leerConfirmacion(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim().toLowerCase();
            if (entrada.equals("s") || entrada.equals("si")) {
                return true;
            }
            if (entrada.equals("n") || entrada.equals("no")) {
                return false;
            }
            System.out.println("Respondé con 's' o 'n'.");
        }
    }

    private static class Configuracion {
        int cantidadPlantas;
        int cantidadConejos;
        int cantidadLobos;
        Clima climaInicial;
        int cantidadTurnos;
    }
}

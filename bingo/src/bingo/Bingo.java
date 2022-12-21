package bingo;

import java.util.Scanner;

/**
 *
 * @author Adriano Díaz Benítez <Adriano.Díaz>
 */
public class Bingo {

    static int obtenerEntero(int min, String mensaje, String mensajeError) {
        int num;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.print(mensaje);
            num = sc.nextInt();
            if (num < min) {
                System.out.print(mensajeError);
            }
        } while (num < min);
        return (num);
    }

    static int generarNumAleatorio(int min, int max) {
        return ((int) ((Math.random() * (max + 1 - min) + min)));
    }

    static void mostrarArray(int[][] array, int inicio, int fin,
            String mensaje, boolean enumerar) {

        /* la variable contadorFilas la usaré para ordenar los valores de cinco 
        en cinco */
        int contFilas = 0;

        for (int indice = inicio; indice < fin; indice++) {

            System.out.println();

            /* en caso de que haya que mostrar el array enumerado, se usa la 
            primera condición */
            if (enumerar) {
                System.out.println("------------- " + mensaje
                        + (indice + 1) + " -------------\n");
            } else {
                System.out.println("- " + mensaje
                        + "\n");
            }

            for (int pos = 0; (pos < array[indice].length)
                    && (array[indice][pos] != 0); pos++) {
                if (array[indice][pos] < 10) {
                    System.out.print(" | 0" + array[indice][pos] + " | ");
                } else {
                    System.out.print(" | " + array[indice][pos] + " | ");
                }
                contFilas++;
                /* cada cinco posiciones se imprime un salto de línea */
                if (contFilas % 5 == 0) {
                    System.out.println();
                }
            }

            System.out.println("\n---------------------------------------");
        }
    }

    static void rellenarCartones(int[][] cartones) {
        int num;
        for (int jugador = 0; jugador < cartones.length; jugador++) {
            for (int pos = 0; pos < cartones[jugador].length; pos++) {
                do {
                    num = generarNumAleatorio(1, 90);
                } while (estaRepetido(cartones, jugador, pos, num));
                cartones[jugador][pos] = num;
            }
        }
    }

    static boolean estaRepetido(int[][] cartones, int jugador, int indice,
            int clave) {
        boolean repetido = false;
        for (int i = 0; (i < cartones[jugador].length) && !repetido
                && (i < indice); i++) {
            repetido = cartones[jugador][i] == clave;
        }
        return (repetido);
    }

    static int jugarBingo(int[][] combGanadora, int[][] jugadores) {
        int num, totalNum = 0, ganador = 0;
        boolean bingo = false, linea = false;
        while (!bingo) {

            /* este do-while es el equivalente a "sacar un número del bombo" */
            do {
                num = generarNumAleatorio(1, 90);
            } while (estaRepetido(combGanadora, 0, totalNum,
                    num));
            combGanadora[0][totalNum++] = num;

            /* Empezamos a comprobar los cartones a partir de los cinco primeros
            números, porque antes es imposible cantar línea */
            if (totalNum >= 5) {

                int contador;

                for (int jugador = 0; jugador < jugadores.length; jugador++) {
                    contador = 0;
                    for (int pos = 0; pos < jugadores[jugador].length; pos++) {
                        for (int numPremiado = 0; (numPremiado < combGanadora[0].length) && (numPremiado <= totalNum); numPremiado++) {
                            if (jugadores[jugador][pos] == combGanadora[0][numPremiado]) {
                                contador++;
                            }
                            if ((contador >= 5) && comprobarLineas(combGanadora, jugadores, totalNum, jugador) && !linea) {
                                linea = true;
                                System.out.println("\n¡El jugador "
                                        + (jugador + 1)
                                        + " ha cantado línea!\n");
                            }
                        }
                    }
                    if (contador == 15) {
                        bingo = true;
                        ganador = jugador;
                    }
                }
            }
        }
        return (ganador);
    }

    static boolean comprobarLineas(int[][] combGanadora, int[][] jugadores, int indiceGanador, int jugador) {
        boolean cantarLinea = false;
        int recorrido = 0, limite = 4;
        /* este while hace que se comprueben las posiciones de cinco en cinco */
        while (!cantarLinea && (limite < 15)) {
            /* este otro while comprueba cada una de las posiciones dentro de
            una línea con la totalidad de los números de la 
            combinación ganadora */
            while ((recorrido <= limite) && (recorrido <= indiceGanador)) {
                cantarLinea = estaRepetido(combGanadora, 0, indiceGanador, jugadores[jugador][recorrido]);
                recorrido++;
            }
            limite += 5;
        }
        return (cantarLinea);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int numJugadores = obtenerEntero(4,
                "Introduzca el número de jugadores (mínimo cuatro): ",
                "ERROR: el número mínimo de jugadores es cuatro. ");

        int[][] cartones = new int[numJugadores][15];
        rellenarCartones(cartones);
        mostrarArray(cartones, 0, cartones.length,
                "JUGADOR: ", true);
        int[][] combGanadora = new int[1][90];
        System.out.println("GANADOR: el jugador " + (jugarBingo(combGanadora, cartones) + 1));
        mostrarArray(combGanadora, 0, combGanadora.length,
                "Números extraidos del bombo: ", false);
    }

}

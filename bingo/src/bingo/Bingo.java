package bingo;

import java.util.Scanner;

/**
 * Este programa es un bingo desarrollado para la asignatura de Programación de
 * 1º de Desarrollo de Aplicaciones Multiplataforma
 *
 * @author Adriano Díaz Benítez <Adriano.Díaz>
 */
public class Bingo {

    /**
     * Esta función sirve para obtener un número entero a partir de un mínimo
     *
     * @param min
     * @param mensaje mensaje para informar al usuario
     * @param mensajeError en caso de que el usuario introduzca un número
     * inferior al mínimo
     * @return
     */
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

    /**
     * Esta función genera un número aleatorio a partir de un máximo y un mínimo
     *
     * @param min
     * @param max
     * @return
     */
    static int generarNumAleatorio(int min, int max) {
        return ((int) ((Math.random() * (max + 1 - min) + min)));
    }

    /**
     * Esta función muestra un array bidimensional por el terminal
     *
     * @param array el array a mostrar
     * @param inicio desde dónde se muestra el array
     * @param fin hasta dónde se muestra el array
     * @param mensaje un mensaje para mostrar sobre el array
     * @param enumerar si es necesario enumerar los arrays a mostrar
     */
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

    /**
     * Esta función rellena los cartones con números aleatorios del '1' al '90'
     *
     * @param cartones
     */
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

    /**
     * Esta función comprueba si un número está repetido en un array
     * bidimensional
     *
     * @param cartones el array a comprobar
     * @param jugador la posición del array a comprobar
     * @param indice hasta dónde comprobar
     * @param clave que número hay que buscar
     * @return
     */
    static boolean estaRepetido(int[][] cartones, int jugador, int indice,
            int clave) {
        boolean repetido = false;
        for (int i = 0; (i < cartones[jugador].length) && !repetido
                && (i < indice); i++) {
            repetido = cartones[jugador][i] == clave;
        }
        return (repetido);
    }

    /**
     * Esta función juega al bingo, generando una serie de números aleatorios en
     * la combGanadora, luego comprueba qué jugador ha hecho bingo y muestra por
     * pantalla aquel jugador que ha hecho línea
     *
     * @param combGanadora el array que contendrá la combinación ganadora
     * @param jugadores el array con los cartones de los jugadores
     * @return el jugador ganador
     */
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
            if (totalNum >= 4) {

                int contador;

                /* recorro cada uno de los jugadores */
                for (int jugador = 0; jugador < jugadores.length; jugador++) {
                    contador = 0;
                    /* recorro cada una de las posiciones dentro de cada 
                    jugador */
                    for (int pos = 0; pos < jugadores[jugador].length; pos++) {
                        /* ahora compruebo esa posición con la lista de números 
                        premiados */
                        for (int numPremiado = 0;
                                (numPremiado < combGanadora[0].length)
                                && (numPremiado < totalNum); numPremiado++) {

                            /* sumaré en un contador los aciertos, el que llegue
                            a 15 será el ganador */
                            if (jugadores[jugador][pos] == combGanadora[0][numPremiado]) {
                                contador++;
                            }

                            /* esta condición comprueba si un jugador con cinco 
                            aciertos ha hecho línea */
                            if (!linea && (contador >= 5)
                                    && comprobarLineas(combGanadora, jugadores,
                                            totalNum, jugador)) {
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

    /**
     * Esta función comprueba si un jugador ha hecho línea
     *
     * @param combGanadora la lista de números extraidos
     * @param jugadores los cartones de los jugadores
     * @param indiceGanador el índice de la lista de números ganadores hasta
     * donde debe comprobar
     * @param jugador el jugador que ha de comprobar
     * @return
     */
    static boolean comprobarLineas(int[][] combGanadora, int[][] jugadores,
            int indiceGanador, int jugador) {
        boolean cantarLinea = false;
        int recorrido = 0, limite = 4, contador;
        /* este while hace que se comprueben las posiciones de cinco en cinco */
        while (!cantarLinea && (limite < 15)) {
            contador = 0;
            /* este otro while comprueba cada una de las posiciones dentro de
            una línea con la totalidad de los números de la 
            combinación ganadora */
            while ((recorrido <= limite) && (recorrido < indiceGanador)) {
                if (estaRepetido(combGanadora, 0,
                        indiceGanador, jugadores[jugador][recorrido])) {
                    contador++;
                }
                recorrido++;
            }
            limite += 5;
            cantarLinea = contador == 5;
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
        System.out.println("GANADOR: jugador "
                + (jugarBingo(combGanadora, cartones) + 1));
        mostrarArray(combGanadora, 0, combGanadora.length,
                "Números extraidos del bombo: ", false);
    }

}

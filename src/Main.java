import java.util.ArrayList;

public class Main {
    static String tablero[][] = new String[8][8];
    static char cols[] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    static int movs[][] = {{2, -1}, {2, 1}, {1, -2}, {1, 2}, {-1, -2}, {-1, 2}, {-2, -1}, {-2, 1}};
    static String casillas[] = new String[64];
    static boolean visited[][] = new boolean[8][8]; // Matriz para rastrear las posiciones visitadas

    public static void main(String[] args) {
        int posi = (int) (Math.random() * 64); // Generar un número aleatorio entre 0 y 63
        int fila = 8 - (posi / 8); // Calcular la fila inicial
        char columna = cols[posi % 8]; // Calcular la columna inicial
        String posIni = "" + columna + fila; // Concatenar la columna y la fila
        System.out.println("Posición Inicial del caballo: " + posIni);
        casillas[posi] = "♘";

        int posf = (int) (Math.random() * 64); // Generar un número aleatorio entre 0 y 63
        fila = 8 - (posf / 8); // Calcular la fila inicial
        columna = cols[posf % 8]; // Calcular la columna inicial
        String posFin = "" + columna + fila; // Concatenar la columna y la fila
        System.out.println("Posición de Destino del caballo: " + posFin);
        casillas[posf] = "♔";

        int mina1 = (int) (Math.random() * 64); // Generar un número aleatorio entre 0 y 63
        casillas[mina1] = "® ";
        int mina2 = (int) (Math.random() * 64); // Generar un número aleatorio entre 0 y 63
        casillas[mina2] = "® ";
        int mina3 = (int) (Math.random() * 64); // Generar un número aleatorio entre 0 y 63
        casillas[mina3] = "® ";
        System.out.println();
        printTableroInicial();
        System.out.println();

        // Inicializar la matriz visited
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                visited[i][j] = false;
            }
        }

        ArrayList<String> ruta = rutaSegura(posIni, posFin);
        System.out.println("Ruta segura del caballo:");
        for (String movimiento : ruta) {
            System.out.print(movimiento + ",");
        }
    }

    static void printTableroInicial() {
        int index = 0;
        int numfila = 8;
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < tablero[i].length; j++) {
                System.out.print("|" + cols[j] + "" + (numfila));
                tablero[i][j] = (cols[j] + "" + (numfila));
                casillas[index] = (casillas[index] == null ? tablero[i][j] : casillas[index]);//lenamos el vector
                index++;
            }
            numfila--;

            System.out.println();

        }
        System.out.println();
       //for (String casilla : casillas)
            //System.out.print(casilla);
        System.out.println();

        //imprimimos de nuevo el tablero con las minas y las posiciónes
        numfila = 8;
        index = 0;
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < tablero[i].length; j++) {
                tablero[i][j] = (casillas[index]);
                System.out.print(tablero[i][j] + "|");
                index++;
            }
            numfila--;

            System.out.println();
        }
    }

    static boolean verifiCoords(int fila, int columna) {
        return fila >= 0 && fila < 8 && columna >= 0 && columna < 8;
    }

    static ArrayList<String> rutaSegura(String posIni, String posFin) {
        ArrayList<String> ruta = new ArrayList<>();
        int filaIni = 8 - (Character.getNumericValue(posIni.charAt(1))); // Obtener fila inicial
        int columnaIni = getIndex(cols, posIni.charAt(0)); // Obtener columna inicial
        int filaFin = 8 - (Character.getNumericValue(posFin.charAt(1))); // Obtener fila final
        int columnaFin = getIndex(cols, posFin.charAt(0)); // Obtener columna final

        // Añadir posición inicial a la ruta
        ruta.add(posIni);

        // Realizar movimientos hasta llegar a la posición final
        dfs(filaIni, columnaIni, filaFin, columnaFin, ruta);

        return ruta;
    }

    static void dfs(int filaIni, int columnaIni, int filaFin, int columnaFin, ArrayList<String> ruta) {
        // Si hemos llegado a la posición final, detener la búsqueda
        if (filaIni == filaFin && columnaIni == columnaFin) {
            return;
        }

        // Marcar la celda actual como visitada
        visited[filaIni][columnaIni] = true;

        // Explorar todas las posibles direcciones de movimiento
        for (int[] mov : movs) {
            int newFila = filaIni + mov[0];
            int newColumna = columnaIni + mov[1];

            // Si la nueva posición es válida, no es una mina y no ha sido visitada, moverse a ella y continuar la búsqueda
            if (verifiCoords(newFila, newColumna) && !esMina(newFila, newColumna) && !visited[newFila][newColumna]) {
                ruta.add("" + cols[newColumna] + (8 - newFila));
                dfs(newFila, newColumna, filaFin, columnaFin, ruta);

                // Si hemos llegado a la posición final, detener la búsqueda
                if (ruta.get(ruta.size() - 1).equals("" + cols[columnaFin] + (8 - filaFin))) {
                    return;
                }

                // Si la ruta actual no lleva a la posición final, retroceder y explorar otras direcciones
                ruta.remove(ruta.size() - 1);
            }
        }

        // Desmarcar la celda actual como visitada antes de retroceder
        visited[filaIni][columnaIni] = false;
    }

    static boolean esMina(int fila, int columna) {
        int index = (8 - fila) * 8 + getColumnIndex(cols[columna]);
        return index >= 0 && index < 64 && casillas[index].equals("® ");
    }

    static int getIndex(char[] arr, char target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    static int getColumnIndex(char columna) {
        for (int i = 0; i < cols.length; i++) {
            if (cols[i] == columna) {
                return i;
            }
        }
        return -1;
    }
}

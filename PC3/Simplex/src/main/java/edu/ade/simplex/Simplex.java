package edu.ade.simplex;
/**
 *
 * @author jose
 */
public class Simplex {
    private final int nroFilas;
    private final int nroColumnas;
    private final int nroVariables;
    private final int nroRestricciones;
    private final int[] values;
    private final double[] funcionObjetivo;
    private final double[][] tabla;
    private final double[][] tablaSimplex;

    public Simplex(int nroVariables, int nroRestricciones, double[] funcionObjetivo, double[][] tabla) {
        this.nroVariables = nroVariables;
        this.nroRestricciones = nroRestricciones;
        this.tabla = tabla;
        this.funcionObjetivo = funcionObjetivo;
        this.nroFilas = nroRestricciones + 1;
        this.nroColumnas = nroVariables + nroRestricciones + 2;
        this.tablaSimplex = new double[nroFilas][nroColumnas];
        this.values = new int[nroRestricciones];
        initTabla();
    }
    // Forma estandar
    private void initTabla() {
        // Columna para reemplazar las variables basicas
        for (int i = 0; i < nroRestricciones; ++i) {
            values[i] = -1;
        }
        // Inicializamos la tabla
        for (int i = 0; i < nroFilas; ++i) {
            for (int j = 0; j < nroColumnas; ++j) {
                tablaSimplex[i][j] = 0;
            }
        }
        // la fila Z: existencia 
        tablaSimplex[nroFilas - 1][0] = 1;
        
        // funcion objetivo
        for (int i = 0; i < nroVariables; i++) {
            tablaSimplex[nroFilas - 1][i + 1] = (-1) * funcionObjetivo[i];
        }
        // restricciones y variables no basicas
        for (int i = 0; i < nroRestricciones; i++) {
            for (int j = 0; j < nroVariables; j++) {
                tablaSimplex[i][j + 1] = tabla[i][j];
            }
            tablaSimplex[i][nroColumnas - 1] = tabla[i][nroVariables];
        }
        //variables basicas s1, s2, ...
        for (int i = 0; i < nroVariables; i++) {
            tablaSimplex[i][nroVariables + 1 + i] = 1;
        }
    }

    public int indiceColumnPivot() {
        int index = -1;
        int row = nroFilas;
        double choose = 1;
        for (int i = 1; i <= nroVariables; i++) {
            if (tablaSimplex[row - 1][i] >= 0) continue;
            if (choose > Math.abs(tablaSimplex[row - 1][i])) {
                choose = Math.abs(tablaSimplex[row - 1][i]);
                index = i;
            }
        }
        return index;
    }

    public int indiceRowPivot() {
        int ColumnPiv = indiceColumPivot();
        if (ColumnPiv == -1) return -1;
        int row = nroFilas;
        int column = nroColumnas;
        boolean found = false;  
        double choose = -1;
        int index = -1;
        for (int i = 0; i < row - 1; i++) {
            if (tablaSimplex[i][ColumnPiv] != 0 && tablaSimplex[i][column - 1] * tablaSimplex[i][ColumnPiv] > 0) {
                if (found) {
                    if (choose > (tablaSimplex[i][column - 1] / tablaSimplex[i][ColumnPiv])) {
                        choose = tablaSimplex[i][ColumnPiv];
                        index = i;
                    }
                }
                else {    
                    found = true;
                    choose = (tablaSimplex[i][column - 1] / tablaSimplex[i][ColumnPiv]);
                    index = i;
                }
            }
        }
        return index;
    }

    public void AllOne() {
        int col = indiceColumPivot();
        int row = indiceRowPivot();
        System.out.println("Escogemos como pivote a: fila -> " + row + ", columna -> " + col +", valor -> " + tablaSimplex[row][col]);
        if (row != -1 && col != -1) {
            values[row] = col;
            double factor = tablaSimplex[row][col];
            for (int i = 0; i < nroColumnas; i++) {
                tablaSimplex[row][i] = tablaSimplex[row][i] / factor;
            }
            ElementalOperation(col, row);
        }
    }

    public boolean NoNegative() {
        int row = nroFilas;
        int column = nroColumnas;
        int cnt = column - 1;
        for (int i = 1; i < column; i++) {
            if (tablaSimplex[row - 1][i] >= 0) cnt--;
        }
        return cnt == 0;
    }

    public void simplexMaximizar() {
        while (!NoNegative()) {
            printTabla();
            System.out.println("==============================================================");
            AllOne();
        }
        printTabla();
    }

    public void ElementalOperation(int column, int row) {
        int element = (int) tablaSimplex[row][column];
        for (int i = 0; i < nroFilas; i++) {
            if (i != row) {
                double r = (-1) * (tablaSimplex[i][column] / element);
                for (int j = 0; j < nroColumnas; j++) {
                    tablaSimplex[i][j] = tablaSimplex[i][j] + (r * tablaSimplex[row][j]);
                }
            }
        }
    }

    public void printTabla() {
        for (int i = 0; i < nroFilas; i++) {
            for (int j = 0; j < nroColumnas; j++) {
                System.out.print(tablaSimplex[i][j] + " \t");
            }
            System.out.println();
        }
    }
    public void printValues() {
        double[] exist_value = new double[1000];
        for (int i = 0; i < nroRestricciones; ++i) {
            exist_value[i] = 0;
        }
        for (int i = 0; i < nroRestricciones; ++i) {
            if (values[i] != -1) {
                exist_value[values[i]] = tablaSimplex[i][nroColumnas - 1];
            }
        }
        for (int i = 1; i <= nroVariables; ++i) {
            System.out.println("X" + i + " = " + exist_value[i]);
        }
        System.out.println("La solucion es: " + tablaSimplex[nroFilas - 1][nroColumnas - 1]);
    }
    public static void main(String[] args) {
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public int indiceColumPivot() {
        int index = -1;
        int row = nroFilas;
        double choose = 1;
        for (int i = 0; i < nroColumnas; i++) {
            if (choose > tablaSimplex[row - 1][i]) {
                choose = tablaSimplex[row - 1][i];
                index = i;
            }
        }
        return index;
    }

    
}


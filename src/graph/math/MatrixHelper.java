package graph.math;

import java.util.Arrays;

public class MatrixHelper {

    public static int[][] pow(int[][] matrix, int power) {
        int[][] result = matrix;
        for (int i = 2; i <= power; i++)
            result = multiply(result, matrix);

        return result;
    }

    public static int[][] multiply(int[][] a, int[][] b) {
        int len = a.length;
        int[][] result = new int[len][len];
        int accumulator = 0;

        for (int y = 0; y < len; y++) {
            for (int x = 0; x < len; x++) {
                for (int z = 0; z < len; z++)
                    accumulator += a[y][z] * b[z][x];

                result[y][x] = accumulator;
                accumulator = 0;
            }
        }

        return result;
    }

    public static int[][] union(int[][] a, int[][] b) {
        int len = a.length;
        int[][] result = new int[len][len];

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++)
                result[i][j] = a[i][j] + b[i][j];
        }

        return booleanTransform(result);
    }

    public static int[][] booleanTransform(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i][j] = matrix[i][j] >= 1 ? 1 : 0;
            }
        }

        return newMatrix;
    }

    public static int[][] booleanTransform(double[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                newMatrix[i][j] = matrix[i][j] >= 1 ? 1 : 0;
            }
        }

        return newMatrix;
    }

    public static int[][] transitiveClosure(int[][] relationMatrix) {
        int len = relationMatrix.length;
        int[][] closure = new int[len][len];

        for (int k = 0; k < len; k++) {
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < len; j++) {
                    closure[i][j] = relationMatrix[i][j] | (relationMatrix[i][k] & relationMatrix[k][j]);
                }
            }
        }

        return closure;
    }

    public static int[][] nonEfficientTransitiveClosure(int[][] relationMatrix) {
        int len = relationMatrix.length;
        int[][] transitiveClosure = identityMatrix(len);
        int[][] powMatrix = new int[len][];

        for (int i = 0; i < len; i++)
            powMatrix[i] = Arrays.copyOf(relationMatrix[i], len);
        transitiveClosure = union(transitiveClosure, powMatrix);

        for (int i = 2; i < len; i++) {
            powMatrix = multiply(powMatrix, relationMatrix);
            transitiveClosure = union(transitiveClosure, powMatrix);
        }

        return transitiveClosure;
    }

    public static int[][] identityMatrix(int size) {
        int[][] identityMatrix = new int[size][size];
        for (int i = 0; i < size; i++)
            identityMatrix[i][i] = 1;

        return identityMatrix;
    }

    public static int[][] upperTriangularMatrix(int size) {
        int[][] tr = new int[size][size];
        for (int i = 0; i < tr.length; i++) {
            for (int j = 0; j < tr.length; j++)
                tr[i][j] = i < j ? 1 : 0;
        }

        return tr;
    }

    public static int[][] transpose(int[][] matrix) {
        int len = matrix.length;
        int[][] transposed = new int[len][len];

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++)
                transposed[i][j] = matrix[j][i];
        }

        return transposed;
    }

}

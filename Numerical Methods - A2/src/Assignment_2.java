import java.util.*;
import java.io.*;

public class Assignment_2 {
	private static double[][] A = { { -4, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, -4, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, -4, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 1, 0, 0, -4, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 1, 0, 1, -4, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 1, 0, 1, -4, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, -4, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 1, 0, 1, -4, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 1, 0, 1, -4, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, -4, 2, 0, 0, 0, 1, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, -4, 1, 0, 0, 0, 1, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, -4, 1, 0, 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, -4, 1, 0, 0, 0, 1, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, -4, 0, 0, 0, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, -4, 2, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, -4, 1, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, -4, 1, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, -4, 1 },
			{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, -4 } };
	private static double[][] B = { { -15 }, { 0 }, { 0 }, { -15 }, { 0 }, { 0 }, { -15 }, { 0 }, { 0 }, { -15 },
			{ -15 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 }, { 0 } };

	public static void main(String[] args) {
		String part = "B";

		if (part.equals("A")) {
			System.out.println("==========================================");
			System.out.println(" QUESTION - 3: PART A");
			System.out.println("==========================================");
			System.out.println(" Solving By CHOLESKI DECOMPOSITION Method");
			System.out.println("==========================================");
			// Solving by Choleski Decomposition
			printMatrix(A);
			printMatrix(B);
			printMatrix(scalarMultiplication(4, B));
			printMatrix(solveByCD(A, B));
		} else if (part.equals("B")) {
			System.out.println("==========================================");
			System.out.println(" QUESTION - 3: PART B");
			System.out.println("==========================================");
			// System.out.println(" Solving By CHOLESKI DECOMPOSITION Method");
			// System.out.println("==========================================");
			// Solving the modified the equation by Choleski Decomposition
			double[][] x = new double[A.length][1];

			double[][] AT = matrixTransposition(A);
			double[][] newA = matrixMultiplication(AT, A);
			double[][] newB = matrixMultiplication(AT, B);
			x = solveByCD(newA, newB);
			// printMatrix(x);
			System.out.println(" Solving By CONJUGATE GRADIENT Method");
			System.out.println("==========================================");
			// Solving by Conjugate Gradient Method
			x = CGMethod(newA, newB, 19);
			// printMatrix(x);
		}
	}

	// Conjugate Gradient Method
	public static double[][] CGMethod(double[][] A, double[][] b, double eigenValues) {
		double alpha = 0;
		double beta = 0;
		double[][] numerator = { { 0.0 } };
		double[][] denominator = { { 0.0 } };
		double[][] x = new double[A.length][1];
		double[][] r = matrixSubtraction(b, matrixMultiplication(A, x));
		double[][] p = copyMatrix(r);
		double infNorm = 0;
		double twoNorm = 0;
		double absValue = 0;

		for (int k = 0; k < eigenValues; k++) {
			numerator = matrixMultiplication(matrixTransposition(p), r);
			denominator = matrixMultiplication(matrixMultiplication(matrixTransposition(p), A), p);
			alpha = numerator[0][0] / denominator[0][0];
			x = matrixAddition(x, scalarMultiplication(alpha, p));
			r = matrixSubtraction(b, matrixMultiplication(A, x));
			numerator = matrixMultiplication(matrixMultiplication(matrixTransposition(p), A), r);
			beta = -numerator[0][0] / denominator[0][0];
			p = matrixAddition(r, scalarMultiplication(beta, p));
			// Calculating the Infinity-Norm and 2-Norm
			for (int i = 0; i < eigenValues; i++) {
				absValue = Math.abs(r[i][0]);
				// System.out.println(k + ", " + i + ", " +absValue);
				if (absValue > infNorm) {
					infNorm = absValue;
				}
				twoNorm += (r[i][0] * r[i][0]);
			}
			twoNorm = Math.sqrt(twoNorm);
			// System.out.println("Iterations: " + k + ",\t Inifinity Norm: " + infNorm +
			// ",\t 2-Norm: " + twoNorm);

			// Resets the Norms for the next iteration
			infNorm = 0;
			twoNorm = 0;
		}
		return x;
	}

	// Choleski Decomposition
	public static double[][] choleskiDecomposition(double[][] matrixA) {
		int rowsA = matrixA.length;
		int colsA = matrixA[0].length;
		double[][] matrixL = new double[rowsA][colsA];

		// Checking the Eligibility for Choleski Decomposition:
		// Checking if the Matrix is Symmetric or not
		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < colsA; j++) {
				if (matrixA[i][j] != matrixA[j][i]) {
					throw new IllegalArgumentException("A is not Symmetric! Cannot carryout Choleski Decomposition!");
				}
			}
		}

		// Checking if the Matrix is a Square Matrix or not
		if (rowsA != colsA) {
			throw new IllegalArgumentException(
					"Number of rows of Matrix A: " + rowsA + " doesnot match to the number of columns of A " + colsA
							+ "! Hence, A is not a Square Matrix! Cannot carryout Choleski Decomposition!");
		}

		// Copying the lower part of Matrix A to Matrix L
		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < i + 1; j++) {
				matrixL[i][j] = matrixA[i][j];
			}
		}

		final long startTime = System.currentTimeMillis();

		// Choleski Decomposition
		for (int j = 0; j < colsA; j++) {
			matrixL[j][j] = Math.sqrt(matrixL[j][j]);
			for (int i = j + 1; i < rowsA; i++) {
				matrixL[i][j] = matrixL[i][j] / matrixL[j][j];
				for (int k = j + 1; k < i + 1; k++) {
					matrixL[i][k] = matrixL[i][k] - (matrixL[i][j] * matrixL[k][j]);
				}
			}
		}

		final long endTime = System.currentTimeMillis();
		// System.out.println("Time Taken" + ((endTime - startTime) * 1000) + "
		// microseconds");
		return matrixL;
	}

	// Solves Ax=b using Choleski Decomposition
	public static double[][] solveByCD(double[][] matrixA, double[][] b) {
		int rowsA = matrixA.length;
		int colsA = matrixA[0].length;
		int rowsB = b.length;
		int colsB = b[0].length;
		double[][] matrixL = new double[rowsA][colsA];
		double[][] matrixLT = new double[rowsA][colsA];
		double[][] x = new double[colsA][colsB];
		double[][] y = new double[rowsA][colsB];

		// Verifying the Inputs
		if (rowsA != rowsB) {
			throw new IllegalArgumentException(
					"The Size of Matrix A: " + rowsA + " does not match to that of b: " + rowsB);
		}

		// Gets the Lower Matrix "L" from A by Choleski Decomposition
		matrixL = choleskiDecomposition(matrixA);
		// Gets the Transpose of Matrix L
		matrixLT = matrixTransposition(matrixL);

		// Solves Ly=b
		for (int i = 0; i < rowsA; i++) {
			double sum = 0.0;
			for (int j = 0; j < i; j++) {
				sum += matrixL[i][j] * y[j][0];
			}
			y[i][0] = (b[i][0] - sum) / matrixL[i][i];
		}

		// Solves (L^T)x=y
		for (int i = rowsA - 1; i > -1; i--) {
			double sum = 0.0;
			for (int j = i + 1; j < rowsA; j++) {
				sum += matrixLT[i][j] * x[j][0];
			}
			x[i][0] = (y[i][0] - sum) / matrixLT[i][i];
		}
		return x;
	}

	// Prints a Matrix
	public static void printMatrix(double[][] matrixA) {
		int rowsA = matrixA.length;
		int colsA = matrixA[0].length;

		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < colsA; j++) {
				if (matrixA[i][j] < -10.0) {
					System.out.print(matrixA[i][j] + " ");
				} else if (matrixA[i][j] < 0.0) {
					System.out.print(" " + matrixA[i][j] + " ");
				} else if (matrixA[i][j] < 10.0) {
					System.out.print(" " + matrixA[i][j] + " ");

				} else if (matrixA[i][j] > 99.0) {
					System.out.print(" " + matrixA[i][j]);
				} else {
					System.out.print(" " + matrixA[i][j] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}

	// Copies a matrix
	private static double[][] copyMatrix(double[][] inputMatrix) {
		int rows = inputMatrix.length;
		int cols = inputMatrix[0].length;
		double[][] outputMatrix = new double[rows][cols];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				outputMatrix[i][j] = inputMatrix[i][j];
			}
		}
		return outputMatrix;
	}

	// Transposes a Matrix
	public static double[][] matrixTransposition(double[][] matrixA) {
		int rowsA = matrixA.length;
		int colsA = matrixA[0].length;
		double[][] transposedMatrix = new double[colsA][rowsA];

		for (int i = 0; i < colsA; i++) {
			for (int j = 0; j < rowsA; j++) {
				transposedMatrix[i][j] = matrixA[j][i];
			}
		}
		return transposedMatrix;
	}

	// Adds 2 Matrices
	public static double[][] matrixAddition(double[][] matrixA, double[][] matrixB) {
		int rowsA = matrixA.length;
		int colsA = matrixA[0].length;
		int rowsB = matrixB.length;
		int colsB = matrixB[0].length;

		// Checking the Eligibility for Addition
		if (rowsA != rowsB) {
			throw new IllegalArgumentException("Number of rows of Matrix A: " + rowsA
					+ " doesnot match to that of Matrix B " + rowsB + "! Addition not possible!");
		} else if (colsA != colsB) {

			throw new IllegalArgumentException("Number of columns of Matrix A: " + colsA
					+ " doesnot match to that of Matrix B " + colsB + "! Addition not possible!");
		}

		double[][] matrixSum = new double[rowsA][colsA];

		// Adding the 2 Matrices
		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < colsA; j++) {
				matrixSum[i][j] = matrixA[i][j] + matrixB[i][j];
			}
		}
		return matrixSum;
	}

	// Subtracts one Matrix from another
	public static double[][] matrixSubtraction(double[][] matrixA, double[][] matrixB) {
		int rowsA = matrixA.length;
		int colsA = matrixA[0].length;
		int rowsB = matrixB.length;
		int colsB = matrixB[0].length;

		// Checking the Eligibility for Subtraction
		if (rowsA != rowsB) {
			throw new IllegalArgumentException("Number of rows of Matrix A: " + rowsA
					+ " doesnot match to that of Matrix B " + rowsB + "! Subtraction not possible!");
		} else if (colsA != colsB) {
			throw new IllegalArgumentException("Number of columns of Matrix A: " + colsA
					+ " doesnot match to that of Matrix B " + colsB + "! Subtraction not possible!");
		}

		double[][] matrixSum = new double[rowsA][colsA];

		// Subtracting the Matrix B from A
		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < colsA; j++) {
				matrixSum[i][j] = matrixA[i][j] - matrixB[i][j];
			}
		}
		return matrixSum;
	}

	// Multiplies 2 Matrices
	public static double[][] matrixMultiplication(double[][] matrixA, double[][] matrixB) {
		int rowsA = matrixA.length;
		int colsA = matrixA[0].length;
		int rowsB = matrixB.length;

		int colsB = matrixB[0].length;

		// Checking the Eligibility for Multiplication
		if (colsA != rowsB) {
			throw new IllegalArgumentException("Number of columns of Matrix A: " + colsA
					+ " doesnot match to the number of rows of Matrix B " + rowsB + "! Multiplication not possible!");
		}

		double[][] matrixProduct = new double[rowsA][colsB];

		// Multiplying the 2 Matrices
		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < colsB; j++) {
				for (int k = 0; k < colsA; k++) {
					matrixProduct[i][j] += matrixA[i][k] * matrixB[k][j];
				}
			}
		}
		return matrixProduct;
	}

	// Multiplies a Scalar with a Matrix
	public static double[][] scalarMultiplication(double scalarA, double[][] matrixB) {
		int rowsB = matrixB.length;
		int colsB = matrixB[0].length;

		double[][] matrixProduct = new double[rowsB][colsB];

		// Multiplying the Scalar with the Matrix
		for (int i = 0; i < rowsB; i++) {
			for (int j = 0; j < colsB; j++) {
				matrixProduct[i][j] = scalarA * matrixB[i][j];
			}
		}
		return matrixProduct;
	}
}
import java.util.*;
import java.io.*;

public class Assignment_1 {
	public static String pathTestCircuit = "C:\\Users\\MohammadMushfiqur\\Desktop\\Test_Circuits.xlsx";

	public static void main(String[] args) {

		// double[][] A = {{49,56,63,35}, {56,145,90,112}, {63,90,94,64},
		// {35,112,64,115}};
		double[][] X = { { 10 }, { 20 }, { 30 }, { 40 } };
		// double[][] B = new double[4][1];
		double[][] C = new double[3][3];
		double[][] D = new double[4][1];
		int questionNo = 3;

		if (questionNo == 1) {
			// QUESTION - 1
			String part = "D";

			if (part.equals("C")) {
				System.out.println("==========================================");
				System.out.println(" QUESTION - 1: PART C");
				System.out.println("==========================================");
				// n = 2
				System.out.println("X1 = ");
				double[][] matrixB1 = new double[Test_Matrices.x1.length][Test_Matrices.x1[0].length];
				matrixB1 = matrixMultiplication(Test_Matrices.test1, Test_Matrices.x1);
				printMatrix(solveByCD(Test_Matrices.test1, matrixB1));
				// n = 3
				System.out.println("X2 =");
				double[][] matrixB2 = new double[Test_Matrices.x2.length][Test_Matrices.x2[0].length];
				matrixB2 = matrixMultiplication(Test_Matrices.test2, Test_Matrices.x2);
				printMatrix(solveByCD(Test_Matrices.test2, matrixB2));
				// n= 4
				System.out.println("X3 = ");
				double[][] matrixB3 = new double[Test_Matrices.x3.length][Test_Matrices.x3[0].length];
				matrixB3 = matrixMultiplication(Test_Matrices.test3, Test_Matrices.x3);
				printMatrix(solveByCD(Test_Matrices.test3, matrixB3));

			} else if (part.equals("D")) {
				System.out.println("==========================================");
				System.out.println(" QUESTION - 1: PART D");
				System.out.println("==========================================");
				ImportExcel workbook = new ImportExcel(pathTestCircuit);
				int totalTestCircuits = 5;
				int matrixA = 0;
				int matrixJ = 1;
				int matrixR = 2;
				int matrixE = 3;

				for (int i = 1; i <= totalTestCircuits; i++) {
					double[][] A = workbook.importMatrix(matrixA, i);
					double[][] J = workbook.importMatrix(matrixJ, i);
					double[][] R = workbook.importMatrix(matrixR, i);
					double[][] E = workbook.importMatrix(matrixE, i);
					double[][] AT = matrixTransposition(A);
					double[][] Y = new double[A[0].length][A[0].length];
					for (int j = 0; j < Y.length; j++) {
						for (int k = 0; k < Y[0].length; k++) {
							if (j == k) {
								Y[j][k] = 1.0 / R[k][0];
							} else {
								Y[j][k] = 0;
							}
						}
					} // Solving the Equation (AYA^T)V = A(J - YE)
					double[][] YAT = matrixMultiplication(Y, AT);
					double[][] RSPD = matrixMultiplication(A, YAT);
					double[][] JMinusYE = matrixSubtraction(J, matrixMultiplication(Y, E));
					double[][] B = matrixMultiplication(A, JMinusYE);
					double[][] V = solveByCD(RSPD, B);
					System.out.println("The Voltage(s) for the Test Circuit " + i);
					printMatrix(V);
					System.out.println("==========================================");
				}
			}
		} else if (questionNo == 2) {
			// QUESTION - 2
			String part = "C";
			System.out.println("==========================================");
			System.out.println(" QUESTION - 2: PART " + part);
			System.out.println("==========================================");

			if (part.equals("A") || part.equals("B")) {
				System.out.println("Solving by Choleski Decomposition");
			} else if (part.equals("C")) {
				System.out.println("Solving by Modified Choleski Decomposition for Sparse Matrices");
			}
			System.out.println("======================================================================");

			for (int N = 2; N < 11; N++) {
				// Generates N by 2N Mesh Network
				MeshGenerator mesh = new MeshGenerator(N);
				// Extracting Matrices A, J, R & E
				double[][] tempA = mesh.getA();
				double[][] Jk = mesh.getJ();
				double[][] Rk = mesh.getR();
				double[][] Ek = mesh.getE();
				// Getting A with the last node grounded
				double[][] A = new double[tempA.length - 1][tempA[0].length];

				for (int i = 0; i < tempA.length - 1; i++) {
					for (int j = 0; j < tempA[0].length; j++) {
						A[i][j] = tempA[i][j];
					}
				}

				double[][] AT = matrixTransposition(A);
				// Generates Matrix Y from Rk
				double[][] Y = new double[A[0].length][A[0].length];

				for (int j = 0; j < Y.length; j++) {
					for (int k = 0; k < Y[0].length; k++) {
						if (j == k) {
							Y[j][k] = 1.0 / Rk[k][0];
						} else {
							Y[j][k] = 0.0;
						}
					}
				}

				// Doing the Calculations for each part of the Question Separately
				if (part.equals("A")) {
					// Solving the Equation (AYA^T)V = A(J - YE)
					double[][] YAT = matrixMultiplication(Y, AT);
					double[][] RSPD = matrixMultiplication(A, YAT);
					double[][] JMinusYE = matrixSubtraction(Jk, matrixMultiplication(Y, Ek));
					double[][] B = matrixMultiplication(A, JMinusYE);
					double[][] V = new double[AT[0].length][B[0].length];

					// Solve by Choleski Decomposition
					V = solveByCD(RSPD, B);
					double R = (1.0f / ((Ek[Ek.length - 1][0] / V[0][0]) - 1));

					// Prints the Equivalent Resistance
					System.out.println("The equivalent resistance R for N: " + N + ", i.e " + N + " by " + 2 * N
							+ " mesh network is " + R + " KOhms");
				} else if (part.equals("B")) {
					// Timing the program
					final long startTime = System.nanoTime();
					// Solving the Equation (AYA^T)V = A(J - YE)
					double[][] YAT = matrixMultiplication(Y, AT);
					double[][] RSPD = matrixMultiplication(A, YAT);
					double[][] JMinusYE = matrixSubtraction(Jk, matrixMultiplication(Y, Ek));
					double[][] B = matrixMultiplication(A, JMinusYE);
					double[][] V = new double[AT[0].length][B[0].length];

					// Solve by Choleski Decomposition
					V = solveByCD(RSPD, B);
					double R = (1.0f / ((Ek[Ek.length - 1][0] / V[0][0]) - 1));
					final long endTime = System.nanoTime();

					// Prints the Time taken to solve
					System.out.println("The time taken to solve for N:" + N + ", i.e " + N + " by " + 2 * N
							+ " mesh network is " + ((endTime - startTime) / 1000) + " milliseconds");
				} else if (part.equals("C")) {
					// Timing the program
					final long startTime = System.nanoTime();
					// Solving the Equation (AYA^T)V = A(J - YE)
					double[][] YAT = matrixMultiplication(Y, AT);
					double[][] RSPD = matrixMultiplication(A, YAT);
					double[][] JMinusYE = matrixSubtraction(Jk, matrixMultiplication(Y, Ek));
					double[][] B = matrixMultiplication(A, JMinusYE);
					double[][] V = new double[AT[0].length][B[0].length];

					// Solve by Modified Choleski Decomposition
					V = solveByCDMod(RSPD, B);
					double R = (1.0f / ((Ek[Ek.length - 1][0] / V[0][0]) - 1));
					final long endTime = System.nanoTime();

					// Prints the Time taken to solve
					System.out.println("R = " + R + ", for N:" + N + ", i.e " + N + " by " + 2 * N
							+ " mesh network, with the time taken: " + ((endTime - startTime) / 1000)
							+ " milliseconds");
				}
			}
		} else if (questionNo == 3) {
			// QUESTION - 3
			String part = "D";

			if (part.equals("B")) {
				// PART B
				System.out.println("==========================================");
				System.out.println(" QUESTION - 3: PART B");
				System.out.println("==========================================");
				System.out.println(" Solving By SOR Method");
				System.out.println("==========================================");

				for (double w = 1.0; w < 2.0; w += 0.1) {
					// Generates Mesh with h = 0.02
					FiniteDifferenceMethod FDM1 = new FiniteDifferenceMethod(0.02);
					double[][] test = FDM1.getInitialMesh();
					// Finds the no. of Iterations using SOR Method
					int iterations = FDM1.solveBySOR(w);
					// Gets the Potential at (x, y) = (0.06, 0.04)
					double potential = FDM1.getNewPotentialAt(0.06, 0.04);
					// Prints the Result
					System.out.println("For w = " + w + ", iterations = " + iterations
							+ ", and potential at (0.06, 0.04) = " + potential + "\n");
				}
			} else if (part.equals("C")) {
				// PART C
				double w = 1.30;
				System.out.println("==========================================");
				System.out.println(" QUESTION - 3: PART C");
				System.out.println("==========================================");
				System.out.println(" Solving By SOR Method");
				System.out.println("==========================================");

				for (double h = 0.02; h >= (double) (1.0 / 1600.0); h /= 2) {
					// Generates Mesh with varying h
					FiniteDifferenceMethod FDM2 = new FiniteDifferenceMethod(h);
					// Finds the no. of Iterations using SOR Method
					int iterations = FDM2.solveBySOR(w);
					// Gets the Potential at (x, y) = (0.06, 0.04)
					double potential = FDM2.getNewPotentialAt(0.06, 0.04);
					// Prints the Result
					System.out.println("For h = " + h + ", i.e for 1/h = " + 1 / h + ", iterations = " + iterations
							+ " and potential at (0.06, 0.04) = " + potential);
				}
			} else if (part.equals("D")) {
				// PART D
				System.out.println("==========================================");
				System.out.println(" QUESTION - 3: PART D");
				System.out.println("==========================================");
				System.out.println(" Solving By JACOBI Method");
				System.out.println("==========================================");

				for (double h = 0.02; h >= (double) (1.0 / 1600.0); h /= 2) {

					// Generates Mesh with varying h
					FiniteDifferenceMethod FDM3 = new FiniteDifferenceMethod(h);
					// Finds the no. of Iterations using JACOBI Method
					int iterations = FDM3.solveByJacobi();
					// Gets the Potential at (x, y) = (0.06, 0.04)
					double potential = FDM3.getNewPotentialAt(0.06, 0.04);
					// Prints the Result
					System.out.println("For h = " + h + ", i.e for 1/h = " + 1 / h + ", iterations = " + iterations
							+ " and potential at (0.06, 0.04) = " + potential);
				}
			} else if (part.equals("E")) {
				// PART E
				System.out.println("==========================================");
				System.out.println(" QUESTION - 3: PART E");
				System.out.println("==========================================");
				System.out.println(" Solving By SOR Method");
				System.out.println("==========================================");
				double w = 1.30;
				double hX = 0.01;
				double hY = 0.01;
				// Orientation of Alpha and Beta are opposite to the coordinates in the Question
				// diagram
				double[] alpha = { 2, 1.5, 0.25, 0.25, 0.25, 0.25, 0.50, 1.5, 1.5, 2 };
				double[] beta = { 2, 2, 1, 0.5, 0.25, 0.25, 0.25, 0.25, 1.5, 2 };
				ModifiedFiniteDifferenceMethod FDM4 = new ModifiedFiniteDifferenceMethod(hX, hY, alpha, beta);
				int iterations = FDM4.solveBySOR(w);
				double potential = FDM4.getNewPotentialAt(0.06, 0.04);
				System.out.println("Iterations: " + iterations + ", Potential = " + potential);
			}
		}
	}

	// Generating a Real, Symmetric and Positive-Definite (RSPD) Matrix
	public static double[][] generateRSPDMatrix(int matrixSize) {
		double[][] matrixM = new double[matrixSize][matrixSize];
		double[][] matrixMT = new double[matrixSize][matrixSize];
		double[][] RSPDMatrix = new double[matrixSize][matrixSize];

		// Generating a random Real, Positive, Square, Lower Triangular Matrix M of size
		// "matrixSize"
		for (int i = 0; i < matrixSize; i++) {
			for (int j = 0; j < i + 1; j++) {
				Random rand = new Random();
				matrixM[i][j] = rand.nextInt(9) + 1.0;

			}
		}
		// Generating the Transpose of Matrix M
		matrixMT = matrixTransposition(matrixM);
		// Constructing a symmetric matrix
		RSPDMatrix = matrixMultiplication(matrixM, matrixMT);

		return RSPDMatrix;
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

		return matrixL;
	}

	// Modified Choleski Decomposition for Sparse Matrix
	public static double[][] choleskiDecompositionMod(double[][] matrixA) {
		int rowsA = matrixA.length;
		int colsA = matrixA[0].length;
		double[][] matrixL = new double[rowsA][colsA];
		int maxBandwidth = 0;
		int bandwidth = 0;

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
		// printMatrix(matrixL);
		final long startTime = System.currentTimeMillis();

		// Computes the Half-bandwidth b for the Matrix L
		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < i + 1; j++) {
				if (matrixL[i][j] != 0) {
					bandwidth = i + 1 - j;
					j = colsA - 1;
				}
			}
			if (bandwidth > maxBandwidth) {
				maxBandwidth = bandwidth;
			}
		}

		System.out.println("Half-bandwidth = " + maxBandwidth);
		int bandwidthLimiter;

		// Modified Choleski Decomposition
		for (int j = 0; j < colsA; j++) {
			matrixL[j][j] = Math.sqrt(matrixL[j][j]);
			if (j + maxBandwidth < rowsA) {
				bandwidthLimiter = j + maxBandwidth;
			} else {
				bandwidthLimiter = rowsA;
			}
			for (int i = j + 1; i < bandwidthLimiter; i++) {
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

	// Solves Ax=b using Modified Choleski Decomposition
	public static double[][] solveByCDMod(double[][] matrixA, double[][] b) {
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

		// Gets the Lower Matrix "L" from A by Modified Choleski Decomposition
		matrixL = choleskiDecompositionMod(matrixA);
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
				if (matrixA[i][j] < 0.0) {

					System.out.print(matrixA[i][j] + " ");
				} else if (matrixA[i][j] < 10.0) {
					System.out.print(matrixA[i][j] + " ");
				} else if (matrixA[i][j] > 99.0) {
					System.out.print(matrixA[i][j] + " ");
				} else {
					System.out.print(matrixA[i][j] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
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
}
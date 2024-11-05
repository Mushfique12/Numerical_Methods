import java.math.*;

public class ModifiedFiniteDifferenceMethod {
	private final static double CABLE_WIDTH = 0.1;
	private final static double CABLE_HEIGHT = 0.1;
	private final static double CORE_WIDTH = 0.04;
	private final static double CORE_HEIGHT = 0.02;
	private final static double CORE_POTENTIAL = 15;
	private final static double MAXIMUM_RESIDUAL = 0.00001;
	public double[][] mesh;
	public double[][] updatedMesh;
	private double hY, hX;
	private double[] alpha, beta, nodesX, nodesY;
	private int totalNodesX;
	private int totalNodesY;

	public ModifiedFiniteDifferenceMethod(double hY, double hX, double[] alpha, double[] beta) {
		this.hY = hY;
		this.hX = hX;
		this.alpha = alpha;
		this.beta = beta;
		this.totalNodesX = alpha.length + 1;
		this.totalNodesY = beta.length + 1;
		this.nodesX = new double[totalNodesX];
		this.nodesY = new double[totalNodesY];
		checkAlphaBeta();
		generateNodes(hY, hX, alpha, beta);
		generateMesh();
	}

	// Checks the Values of Alpha and Beta
	public void checkAlphaBeta() {
		double sum = 0.0;
		// Checks Alpha
		for (int i = 0; i < alpha.length; i++) {
			sum += alpha[i];
		}
		if (sum != round(CABLE_WIDTH / hX)) {
			System.out.println("Values of Apha not Correct!");
		}
		sum = 0.0;
		// Checks Beta
		for (int i = 0; i < beta.length; i++) {
			sum += beta[i];
		}
		if (sum != round(CABLE_HEIGHT / hY)) {
			System.out.println("Values of Beta not Correct!");
		}

	}

	// Generates Node Array
	public void generateNodes(double hY, double hX, double[] alpha, double[] beta) {
		double sum = 0.0;
		// Generates Nodes on X Axis
		for (int i = 0; i < alpha.length; i++) {
			sum += hX * alpha[i];
			this.nodesX[i + 1] = round(sum);
			// System.out.println(nodesX[i]);
		}
		// Clears the Sum
		sum = 0.0;
		// Generates Nodes on Y Axis
		for (int i = 0; i < beta.length; i++) {
			sum += hY * beta[i];
			this.nodesY[i + 1] = round(sum);
			// System.out.println(nodesY[i]);
		}
	}

	// Generates the Mesh Matrix
	public void generateMesh() {
		mesh = new double[totalNodesY][totalNodesX];
		for (int y = 0; y < totalNodesY; y++) {
			for (int x = 0; x < totalNodesX; x++) {
				if ((nodesX[x] <= CORE_WIDTH) && (nodesY[y] <= CORE_HEIGHT)) {
					mesh[y][x] = CORE_POTENTIAL;
				} else {
					mesh[y][x] = 0;
				}
			}
		}
	}

	// Solving using SOR Method
	public int solveBySOR(double w) {
		updatedMesh = copyMatrix(mesh);
		int iterations = 0;
		while (maximumResidual() > MAXIMUM_RESIDUAL) {
			SORMethod(w);
			iterations++;
		}
		// printMatrix(updatedMesh);
		return iterations;
	}

	// SOR Method
	private void SORMethod(double w) {
		for (int y = 0; y < totalNodesY - 1; y++) {
			for (int x = 0; x < totalNodesX - 1; x++) {

				if ((nodesY[y] > CORE_HEIGHT) || (nodesX[x] > CORE_WIDTH)) {
					if (x == 0) {
						double op1 = 2 / (hX * hX * alpha[x] * (alpha[x] + alpha[x]));
						double op2 = 2 / (hX * hX * alpha[x] * (alpha[x] + alpha[x]));
						double op3 = 2 / (hY * hY * beta[y - 1] * (beta[y - 1] + beta[y]));
						double op4 = 2 / (hY * hY * beta[y] * (beta[y - 1] + beta[y]));
						updatedMesh[y][x] = (1 / (op1 + op2 + op3 + op4))
								* ((op1 * updatedMesh[y][x + 1]) + (op2 * updatedMesh[y][x + 1])
										+ (op3 * updatedMesh[y - 1][x]) + (op4 * updatedMesh[y + 1][x]));
					} else if (y == 0) {
						double op1 = 2 / (hX * hX * alpha[x - 1] * (alpha[x - 1] + alpha[x]));
						double op2 = 2 / (hX * hX * alpha[x] * (alpha[x - 1] + alpha[x]));
						double op3 = 2 / (hY * hY * beta[y] * (beta[y] + beta[y]));
						double op4 = 2 / (hY * hY * beta[y] * (beta[y] + beta[y]));
						updatedMesh[y][x] = (1 / (op1 + op2 + op3 + op4))
								* ((op1 * updatedMesh[y][x - 1]) + (op2 * updatedMesh[y][x + 1])
										+ (op3 * updatedMesh[y + 1][x]) + (op4 * updatedMesh[y + 1][x]));
					} else {
						double op1 = 2 / (hX * hX * alpha[x - 1] * (alpha[x - 1] + alpha[x]));
						double op2 = 2 / (hX * hX * alpha[x] * (alpha[x - 1] + alpha[x]));
						double op3 = 2 / (hY * hY * beta[y - 1] * (beta[y - 1] + beta[y]));
						double op4 = 2 / (hY * hY * beta[y] * (beta[y - 1] + beta[y]));
						updatedMesh[y][x] = (1 / (op1 + op2 + op3 + op4))
								* ((op1 * updatedMesh[y][x - 1]) + (op2 * updatedMesh[y][x + 1])
										+ (op3 * updatedMesh[y - 1][x]) + (op4 * updatedMesh[y + 1][x]));
					}
				}
			}
		}

	}

	// Gets the Maximum Residual in the Mesh
	private double maximumResidual() {
		double maxResidual = 0.0;
		double residual = 0.0;
		for (int y = 0; y < totalNodesY - 1; y++) {
			for (int x = 0; x < totalNodesX - 1; x++) {
				if ((nodesY[y] > CORE_HEIGHT) || (nodesX[x] > CORE_WIDTH)) {
					if (x == 0) {
						double op1 = 2 / (hX * hX * alpha[x] * (alpha[x] + alpha[x]));
						double op2 = 2 / (hX * hX * alpha[x] * (alpha[x] + alpha[x]));
						double op3 = 2 / (hY * hY * beta[y - 1] * (beta[y - 1] + beta[y]));
						double op4 = 2 / (hY * hY * beta[y] * (beta[y - 1] + beta[y]));
						residual = ((op1 * updatedMesh[y][x + 1]) + (op2 * updatedMesh[y][x + 1])
								+ (op3 * updatedMesh[y - 1][x]) + (op4 * updatedMesh[y + 1][x]))
								- ((op1 + op2 + op3 + op4) * updatedMesh[y][x]);
					} else if (y == 0) {
						double op1 = 2 / (hX * hX * alpha[x - 1] * (alpha[x - 1] + alpha[x]));
						double op2 = 2 / (hX * hX * alpha[x] * (alpha[x - 1] + alpha[x]));
						double op3 = 2 / (hY * hY * beta[y] * (beta[y] + beta[y]));
						double op4 = 2 / (hY * hY * beta[y] * (beta[y] + beta[y]));
						residual = ((op1 * updatedMesh[y][x - 1]) + (op2 * updatedMesh[y][x + 1])
								+ (op3 * updatedMesh[y + 1][x]) + (op4 * updatedMesh[y + 1][x]))
								- ((op1 + op2 + op3 + op4) * updatedMesh[y][x]);
					} else {
						double op1 = 2 / (hX * hX * alpha[x - 1] * (alpha[x - 1] + alpha[x]));
						double op2 = 2 / (hX * hX * alpha[x] * (alpha[x - 1] + alpha[x]));
						double op3 = 2 / (hY * hY * beta[y - 1] * (beta[y - 1] + beta[y]));
						double op4 = 2 / (hY * hY * beta[y] * (beta[y - 1] + beta[y]));
						residual = ((op1 * updatedMesh[y][x - 1]) + (op2 * updatedMesh[y][x + 1])

								+ (op3 * updatedMesh[y - 1][x]) + (op4 * updatedMesh[y + 1][x]))
								- ((op1 + op2 + op3 + op4) * updatedMesh[y][x]);
					}
					residual = Math.abs(residual);
					if (residual > maxResidual) {
						maxResidual = residual;
					}
				}
			}
		}
		return maxResidual;
	}

	// Copies a matrix
	private double[][] copyMatrix(double[][] inputMatrix) {
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

	// Returns the Mesh Generated Initially
	public double[][] getInitialMesh() {
		return this.mesh;
	}

	// Returns the Mesh Generated
	public double[][] getUpdatedMesh() {
		return this.updatedMesh;
	}

	public double getNewPotentialAt(double x, double y) {
		int potAtX = 0;
		int potAtY = 0;
		for (int i = 0; i < nodesX.length; i++) {
			if (nodesX[i] == round(CABLE_WIDTH - x)) {
				potAtX = i;
			}
			// System.out.println(round(CABLE_WIDTH - x));
		}
		for (int i = 0; i < nodesY.length; i++) {
			if (nodesY[i] == round(CABLE_HEIGHT - y)) {
				potAtY = i;
			}
		}

		return updatedMesh[potAtY][potAtX];
	}

	// Round to 2 decimal places
	public static double round(double input) {
		return Math.floor(input * 100000) / 100000;
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
}
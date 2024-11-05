import java.math.*;

public class FiniteDifferenceMethod {
	private final static double CABLE_WIDTH = 0.1;
	private final static double CABLE_HEIGHT = 0.1;
	private final static double CORE_WIDTH = 0.04;
	private final static double CORE_HEIGHT = 0.02;
	private final static double CORE_POTENTIAL = 15;
	private final static double MAXIMUM_RESIDUAL = 0.00001;
	private double h;
	private int nodesOnXAxis;
	private int nodesOnYAxis;
	public double[][] mesh;
	public double[][] updatedMesh;

	public FiniteDifferenceMethod(double h) {
		this.h = h;
		this.nodesOnXAxis = (int) (CABLE_WIDTH / h) + 1;
		this.nodesOnYAxis = (int) (CABLE_HEIGHT / h) + 1;
		generateMesh();
	}

	// Generates the Mesh Matrix
	public void generateMesh() {
		mesh = new double[nodesOnYAxis][nodesOnXAxis];
		for (int y = 0; y < mesh.length; y++) {
			for (int x = 0; x < mesh[0].length; x++) {
				if ((x <= (int) (CORE_WIDTH / h)) && (y <= (int) (CORE_HEIGHT / h))) {
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
		return iterations;
	}

	// SOR Method
	private void SORMethod(double w) {
		for (int y = 0; y < nodesOnYAxis - 1; y++) {
			for (int x = 0; x < nodesOnXAxis - 1; x++) {

				if ((y > (CORE_HEIGHT / h)) || (x > (CORE_WIDTH / h))) {
					if (x == 0) {
						updatedMesh[y][x] = (1.0 - w) * updatedMesh[y][x] + (w / 4.0) * (updatedMesh[y][x + 1]
								+ updatedMesh[y][x + 1] + updatedMesh[y - 1][x] + updatedMesh[y + 1][x]);
					} else if (y == 0) {
						updatedMesh[y][x] = (1.0 - w) * updatedMesh[y][x] + (w / 4.0) * (updatedMesh[y][x - 1]
								+ updatedMesh[y][x + 1] + updatedMesh[y + 1][x] + updatedMesh[y + 1][x]);
					} else {
						updatedMesh[y][x] = (1.0 - w) * updatedMesh[y][x] + (w / 4.0) * (updatedMesh[y][x - 1]
								+ updatedMesh[y][x + 1] + updatedMesh[y - 1][x] + updatedMesh[y + 1][x]);
					}
				}
			}
		}
	}

	// Solving using Jacobi Method
	public int solveByJacobi() {
		updatedMesh = copyMatrix(mesh);
		int iterations = 0;
		while (maximumResidual() > MAXIMUM_RESIDUAL) {
			JacobiMethod();
			iterations++;
		}
		return iterations;
	}

	// Jacobi Method
	private void JacobiMethod() {
		generateMesh();
		for (int y = 0; y < this.nodesOnYAxis - 1; y++) {
			for (int x = 0; x < this.nodesOnXAxis - 1; x++) {
				if ((x > (CORE_WIDTH / h)) || (y > (CORE_HEIGHT / h))) {
					if (x == 0) {
						updatedMesh[y][x] = (1.0 / 4.0) * (updatedMesh[y][x + 1] + updatedMesh[y][x + 1]
								+ updatedMesh[y - 1][x] + updatedMesh[y + 1][x]);
					} else if (y == 0) {
						updatedMesh[y][x] = (1.0 / 4.0) * (updatedMesh[y][x - 1] + updatedMesh[y][x + 1]
								+ updatedMesh[y + 1][x] + updatedMesh[y + 1][x]);
					} else {
						updatedMesh[y][x] = (1.0 / 4.0) * (updatedMesh[y][x - 1] + updatedMesh[y][x + 1]
								+ updatedMesh[y - 1][x] + updatedMesh[y + 1][x]);
					}

				}
			}
		}
	}

	// Gets the Maximum Residual in the Mesh
	private double maximumResidual() {
		double maxResidual = 0.0;
		double residual = 0.0;
		for (int y = 0; y < nodesOnYAxis - 1; y++) {
			for (int x = 0; x < nodesOnXAxis - 1; x++) {
				if ((x > (CORE_WIDTH / h)) || (y > (CORE_HEIGHT / h))) {
					if (x == 0) {
						residual = Math.abs(updatedMesh[y][x + 1] + updatedMesh[y][x + 1] + updatedMesh[y + 1][x]
								+ updatedMesh[y - 1][x] - (4 * updatedMesh[y][x]));
					} else if (y == 0) {
						residual = Math.abs(updatedMesh[y][x + 1] + updatedMesh[y][x - 1] + updatedMesh[y + 1][x]
								+ updatedMesh[y + 1][x] - (4 * updatedMesh[y][x]));
					} else {
						residual = Math.abs(updatedMesh[y][x + 1] + updatedMesh[y][x - 1] + updatedMesh[y + 1][x]
								+ updatedMesh[y - 1][x] - (4 * updatedMesh[y][x]));
					}
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

	// Returns the Potential at a given point of the Initial Mesh
	public double getPotentialAt(double x, double y) {
		int totalNodeX = (int) (CABLE_WIDTH / h + 1);
		int totalNodeY = (int) (CABLE_HEIGHT / h + 1);
		int potAtX = (int) (totalNodeX - (x / h) - 1);
		int potAtY = (int) (totalNodeY - (y / h) - 1);
		return mesh[potAtY][potAtX];
	}

	// Returns the Potential at a given point of the Updated Mesh
	public double getNewPotentialAt(double x, double y) {
		int totalNodeX = (int) (CABLE_WIDTH / h + 1);
		int totalNodeY = (int) (CABLE_HEIGHT / h + 1);
		int potAtX = (int) (totalNodeX - (x / h) - 1);
		int potAtY = (int) (totalNodeY - (y / h) - 1);
		return updatedMesh[potAtY][potAtX];
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
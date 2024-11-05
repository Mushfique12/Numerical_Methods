import java.util.*;
import java.io.*;

public class MeshGenerator {
	public double[][] matrixA, matrixE, matrixJ, matrixR;
	private int N, totalNodes, totalBranches;

	public MeshGenerator(int N) {
		this.N = N;
		this.totalNodes = N * 2 * N;
		this.totalBranches = N * (4 * N - 3);
		this.matrixA = new double[totalNodes][totalBranches + 1];
		this.matrixE = new double[totalBranches + 1][1];
		this.matrixJ = new double[totalBranches + 1][1];
		this.matrixR = new double[totalBranches + 1][1];
		// Generates all the Matrices
		generateMatrixA();
		generateMatrixJ();
		generateMatrixR();
		generateMatrixE();
	}

	private void generateMatrixA() {
		int node, branchAbove, branchBelow, branchLeft, branchRight;
		for (int i = 1; i < this.N + 1; i++) {
			for (int j = 1; j < 2 * (this.N) + 1; j++) {
				node = N * (j - 1) + i;
				branchAbove = node + N * (j - 1) - (j - 1);
				branchBelow = branchAbove - 1;
				branchLeft = branchAbove - N;
				branchRight = branchAbove + (N - 1);
				matrixA[0][totalBranches] = -1;
				matrixA[totalNodes - 1][totalBranches] = 1;
				// For the Left Most Nodes - No Left Branch
				if (j == 1) {
					matrixA[node - 1][branchRight - 1] = 1;
					if (i == 1) {
						matrixA[node - 1][branchAbove - 1] = 1;
					} else if (i == N) {
						matrixA[node - 1][branchBelow - 1] = -1;
					} else {
						matrixA[node - 1][branchAbove - 1] = 1;
						matrixA[node - 1][branchBelow - 1] = -1;
					}
				}
				// For the Right Most Nodes - No Right Branch
				else if (j == 2 * N) {
					matrixA[node - 1][branchLeft - 1] = -1;
					if (i == 1) {
						matrixA[node - 1][branchAbove - 1] = 1;
					} else if (i == N) {

						matrixA[node - 1][branchBelow - 1] = -1;
					} else {
						matrixA[node - 1][branchAbove - 1] = 1;
						matrixA[node - 1][branchBelow - 1] = -1;
					}
				}
				// For the Rest
				else {
					matrixA[node - 1][branchLeft - 1] = -1;
					matrixA[node - 1][branchRight - 1] = 1;
					if (i == 1) {
						matrixA[node - 1][branchAbove - 1] = 1;
					} else if (i == N) {
						matrixA[node - 1][branchBelow - 1] = -1;
					} else {
						matrixA[node - 1][branchAbove - 1] = 1;
						matrixA[node - 1][branchBelow - 1] = -1;
					}
				}
			}
		}
	}

	// Generates Matrix J
	private void generateMatrixJ() {
		for (int i = 0; i < matrixJ.length; i++) {
			for (int j = 0; j < matrixJ[0].length; j++) {
				matrixJ[i][j] = 0;
			}
		}
	}

	// Generates Matrix R
	private void generateMatrixR() {
		for (int i = 0; i < matrixR.length; i++) {
			for (int j = 0; j < matrixR[0].length; j++) {
				matrixR[i][j] = 1;
			}
		}
	}

	// Generates Matrix E
	private void generateMatrixE() {
		for (int i = 0; i < matrixE.length; i++) {
			for (int j = 0; j < matrixE[0].length; j++) {
				if (i == totalBranches) {
					matrixE[totalBranches][0] = 1;
				} else {
					matrixE[i][j] = 0;
				}
			}
		}
	}

	// Returns Matrix A
	public double[][] getA() {
		return this.matrixA;
	}

	// Returns Matrix E
	public double[][] getE() {
		return this.matrixE;
	}

	// Returns Matrix J
	public double[][] getJ() {
		return this.matrixJ;
	}

	// Returns Matrix R
	public double[][] getR() {
		return this.matrixR;
	}
}
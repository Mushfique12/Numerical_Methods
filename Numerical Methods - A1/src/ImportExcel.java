import java.io.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class ImportExcel {
	private String path;
	private FileInputStream fis;
	private XSSFWorkbook workbook;
	private FormulaEvaluator evaluator;

	// Constructor
	public ImportExcel(String filePath) {
		this.path = filePath;
		try {
			this.fis = new FileInputStream(new File(this.path));
			this.workbook = new XSSFWorkbook(fis);
			this.evaluator = workbook.getCreationHelper().createFormulaEvaluator();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Returns the Excel Path
	public String getPath() {
		return this.path;
	}

	// Returns the Excel Workbook
	public XSSFWorkbook getWorkbook() {
		return this.workbook;
	}

	// Returns the Excel Worksheet
	public XSSFSheet getWorksheet(int sheetNumber) {
		return getWorkbook().getSheetAt(sheetNumber);
	}

	// Returns the Excel Cell
	// public XSSFCell getCell(int sheetNumber, int rowNumber, int colNumber) {
	// return getWorksheet(sheetNumber).getRow(rowNumber).getCell(colNumber);
	// }
	// Closes the Excel File
	private void endExcel() {
		try {
			this.fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Import the required Matrices from the Worksheets
	public double[][] importMatrix(int sheetNumber, int testCircuit) {
		// XSSFRow row = worksheet.getRow(0);
		int startRow = 0;
		int endRow = 0;
		int rowCounter = 0;
		// Finds the required Sheet
		// For Incidence Matrix A
		if (sheetNumber == 0) {
			XSSFSheet worksheet = this.workbook.getSheetAt(sheetNumber);
			int lastRowNo = worksheet.getLastRowNum() + 1;
			// Finds the required Test Circuit and Matrix Dimension
			for (int i = 1; i < lastRowNo; i++) {
				XSSFRow row = worksheet.getRow(i);
				XSSFCell cell = row.getCell(0);
				double temp = cell.getNumericCellValue();
				int currentTestCicuit = (int) temp;
				if (currentTestCicuit == testCircuit) {
					endRow = i;
					rowCounter++;
				}
			}
			startRow = endRow - rowCounter + 1;
			int colCounter = worksheet.getRow(startRow).getLastCellNum();
			double[][] matrix = new double[rowCounter][colCounter - 1];
			// Stores the matrix from Excel
			for (int j = 0; j < rowCounter; j++) {
				for (int k = 0; k < colCounter - 1; k++) {
					XSSFRow row = worksheet.getRow(startRow);
					XSSFCell cell = row.getCell(k + 1);
					matrix[j][k] = cell.getNumericCellValue();
				}
				startRow++;
			}
			return matrix;
		}
		// For Matrices J, R & E
		else {
			XSSFSheet worksheet = this.workbook.getSheetAt(sheetNumber);
			int lastRowNo = worksheet.getLastRowNum() + 1;
			// Finds the required Test Circuit and Matrix Dimension
			for (int i = 1; i < lastRowNo; i++) {
				XSSFRow row = worksheet.getRow(i);
				XSSFCell cell = row.getCell(0);
				double temp = cell.getNumericCellValue();
				int currentTestCicuit = (int) temp;
				if (currentTestCicuit == testCircuit) {
					endRow = i;
					rowCounter++;
				}
			}
			startRow = endRow - rowCounter + 1;
			int colCounter = worksheet.getRow(startRow).getLastCellNum();

			double[][] matrix = new double[colCounter - 1][rowCounter];
			// Stores the matrix from Excel
			XSSFRow row = worksheet.getRow(startRow);
			for (int j = 0; j < rowCounter; j++) {
				for (int k = 0; k < colCounter - 1; k++) {
					XSSFCell cell = row.getCell(k + 1);
					matrix[k][j] = cell.getNumericCellValue();
				}
			}
			return matrix;
		}
	}
}
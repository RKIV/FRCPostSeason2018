package org.usfirst.frc.team5530.robot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuilder;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CSVOut {
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private int columnLength;
	FileWriter fileWriter = null;
	StringBuilder sb = new StringBuilder();
	
	
	/**
	 *A Class to log and export a CSV file
	 * @param columns Name each column that will be used
	 */
	public CSVOut(String fileName, String ... columns) {
		
		columnLength = columns.length;
		
		try {
			fileWriter = new FileWriter(fileName);

			//Write the CSV file header
			fileWriter.append(columns.toString());
			
			//Add a new line separator after the header
			fileWriter.append(NEW_LINE_SEPARATOR);
			
		} catch (Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		}
	}
	/**
	 * Run every loop to update CSV values
	 * @param values Each value as an argument in the same order that the CSV was contructed
	 * Will throw error if the number of values is different from the number of columns
	 */
	public void updateCSV(double ... values  ) {
		if (values.length != columnLength) {
			throw new RuntimeException("Column number not equal to data number");
		}
		for (double value : values) {
			sb.append(Double.toString(value));
			sb.append(Double.toString(value));
			sb.append(COMMA_DELIMITER);
		}
		sb.append(NEW_LINE_SEPARATOR);

	}
	/**
	 * Call this at the very end of the program to output the CSV
	 * @param fileName
	 */
	public void writeCsvFile(String fileName) {
		try {
			fileWriter.append(sb.toString());
			System.out.println("CSV Created");
		}catch(Exception e) {
			System.out.println("Error in CsvFileWriter !!!");
			e.printStackTrace();
		}
	}
}

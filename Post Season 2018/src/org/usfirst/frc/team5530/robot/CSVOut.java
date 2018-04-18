package org.usfirst.frc.team5530.robot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CSVOut {
	
	private static final String COMMA_DELIMITER = ",";
	private static final String NEW_LINE_SEPARATOR = "\n";
	private int columnLength;
	FileWriter fileWriter = null;
	
	
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
	
	public void update(double ... values  ) {
		if (values.length != columnLength) {
			throw new RuntimeException("Column number not equal to data number");
		}
		for (double value : values) {
			try {
				fileWriter.append(Double.toString(value));
				fileWriter.append(COMMA_DELIMITER);
			} catch (IOException e) {
				System.out.println("Error in CsvFileWriter !!!");
				e.printStackTrace();
			}
		}
		
		try {
			fileWriter.append(NEW_LINE_SEPARATOR);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void writeCsvFile(String fileName) {
		
	}
}

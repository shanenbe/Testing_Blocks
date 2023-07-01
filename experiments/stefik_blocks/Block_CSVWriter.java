package experiments.stefik_blocks;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class Block_CSVWriter {

	final String filename;
	final PrintWriter writer;
	final List<String> columns;

	public Block_CSVWriter(String filename, List<String> columns) throws Exception {
		super();
		this.filename = filename;
		this.columns = columns;
		File f = new File(filename + ".csv");
		boolean fileExists = f.exists();
		FileWriter fileWriter = new FileWriter(f, true);
		writer = new PrintWriter(fileWriter);
		if (!fileExists)
			writeCSVHeader();
	}

	public void writeCSVHeader() throws Exception {
		try {

			writer.print("timeStamp;");
			for (String columnName : columns) {
				writer.print(columnName + ";");
			}

			writer.print("response;");
			writer.print("isCorrect;");
			writer.println("time");
			writer.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Cannot write to file: " + filename + ".csv");
			System.err.println("fix this first");
			System.exit(-1);
		}
	}

	public void print(String string) {
		writer.print(string);
	}

	public void flush() {
		writer.flush();
	}

	public void println(String string) {
		writer.println(string);
	}

	public void writeResults(Block_Task task, Block_Measurement measurement, Object response) {

		writer.print(Long.toString(measurement.timestamp) + ";");

		for (String column : this.columns) {
			writer.print(task.treatmentCombination.get(column).toString() + ";");
		}

		// response
		writer.print(response.toString() + ";");
		
		
		Object correct = task.treatmentCombination.get("correctAnswer");

		Object expected_answer = task.treatmentCombination.get("correctAnswer");
		Object given_answer = response;


		boolean wasCorrect = 
				expected_answer.toString().equals(
						String.valueOf(response.toString()));
		
		writer.print((wasCorrect?"1":0)+";");
		// last column is the last time
		writer.println(Long.toString(measurement.finish));
		writer.flush();

	}
	
	public void writeResultTemplate(Block_Task task) {

		writer.print("0;");

		for (String column : this.columns) {
			writer.print(task.treatmentCombination.get(column).toString() + ";");
		}

		// response
		writer.print("0;");
		
		// last column is the last time
		writer.println("0");
		writer.flush();

	}	

}

/**
 * Sep 13, 2011
 */
package pl.nitroit.saintsday.db.seed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


/**
 * @author kbl
 *
 */
class CsvParser {

	private static final String NAMES_DELIMITER = " ";
	private static final int DOT_INDEX = 2;
	private static final int COMA_INDEX = 5;

	private BufferedReader reader;

	public static class Record {
		public Record(Integer day, Integer month, String[] names) {
			this.day = day;
			this.month = month;
			this.names = names;
		}

		int day;
		int month;
		String[] names;
	}

	public CsvParser(String filePath) {
		try {
			reader = new BufferedReader(new FileReader(filePath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Record readRecord() {
		String line;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if(line == null) {
			return null;
		}
		return instantiateRecord(line);
	}

	private Record instantiateRecord(String line) {
		String day = line.substring(0, DOT_INDEX);
		String month = line.substring(DOT_INDEX + 1, COMA_INDEX);
		String[] names = line.substring(COMA_INDEX + 1).split(NAMES_DELIMITER);

		return new Record(Integer.valueOf(day), Integer.valueOf(month), names);
	}

}

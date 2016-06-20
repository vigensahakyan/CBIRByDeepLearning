package com.helper.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class CSVReader {

	String filePath = "";
	int colNum = 0;
	int rowNum = 0;

	public Long doInBackground(double[][] Xinp, double[] Yinp) throws IOException {

		long lineNumber = 0;
		InputStreamReader inputStreamReader;
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(filePath));
			Scanner inputStream = new Scanner(inputStreamReader);
			inputStream.nextLine(); // Ignores the first line

			int k = 0;
			while (inputStream.hasNext()) {
				lineNumber++;
				String data = inputStream.nextLine(); // Gets a whole line
				String[] line = data.split(","); // Splits the line up into a
													// string array

				if (line.length > 1) {
					for (int xi = 0; xi < line.length; ++xi) {
						double v = Double.parseDouble(line[xi]);
						if (xi == (colNum - 1)) {
							Yinp[k] = v;
						} else {
							Xinp[k][xi] = v;
						}
					}
				}
				// Do stuff, e.g:
				k++;
			}

			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineNumber;
	}

	public void generateCsvFile(double[][] Xout, double[] Yout) {
		try {
			File file = new File(filePath);
			file.createNewFile();

			FileWriter writer = new FileWriter(file);
			for (int i = 0; i < Xout.length; ++i) {
				for (int j = 0; j <= Xout[i].length; ++j) {
					if (j == (Xout[i].length)) {
						writer.write(Double.toString(Yout[i]));
						writer.write('\n');
					} else {
						writer.write(Double.toString(Xout[i][j]));
						writer.write(',');
					}

				}
			}

			// generate whatever data you want

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}

	public static int countCol(String filename) throws IOException {
		int colNumber = 0;
		InputStreamReader inputStreamReader;
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(filename));
			Scanner inputStream = new Scanner(inputStreamReader);
			inputStream.nextLine(); // Ignores the first line

			int k = 0;
			if (inputStream.hasNext()) {
				String data = inputStream.nextLine(); // Gets a whole line
				String[] line = data.split(","); // Splits the line up into a
													// string array

				colNumber = line.length;
				// Do stuff, e.g:
			}

			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return colNumber;
	}

	public int getRowNum() {
		return rowNum;
	}

	public int getColNum() {
		return colNum;
	}

	public CSVReader(String path, int res) throws IOException {
		filePath = path;
		if (res == 0) {
			rowNum = countLines(filePath) - 1;
			colNum = countCol(filePath);
		}
	}

}

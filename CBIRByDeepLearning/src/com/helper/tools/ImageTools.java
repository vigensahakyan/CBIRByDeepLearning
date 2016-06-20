package com.helper.tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.javafx.image.PixelAccessor;

public class ImageTools {

	public static BufferedImage ResizeImage(int newWidth, int newHeight, BufferedImage originalImage, int saveImage) {
		// public static BufferedImage ResizeImage(int newWidth, int newHeight,
		// String pathToImage, int saveImage ){
		BufferedImage resizedImage = null;
		try {
			// BufferedImage originalImage = ImageIO.read(new
			// File(pathToImage));
			// BufferedImage originalImage = ImageIO.read(new
			// File(pathToImage));

			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();

			resizedImage = new BufferedImage(newWidth, newHeight, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
			g.dispose();

			if (saveImage != 0) {
				ImageIO.write(resizedImage, "jpg", new File("C:\\Users\\V.Sahakyan\\Pictures\\imgTestResized.jpg"));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resizedImage;

	}

	// Read image and convert it to grayscale, then obtain
	// and return pixel values array from grayscale image
	// public static int [] ReadImageConvertToGrayScale(String pathToImage){
	public static int[] ReadImageConvertToGrayScale(BufferedImage originalImageFile) {
		// File originalImageFile = new File(pathToImage);
		BufferedImage originalImage = null;
		int[] pixelArray = null;
		try {
			// originalImage = ImageIO.read(originalImageFile);
			originalImage = originalImageFile;
			BufferedImage grayscaleImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			pixelArray = new int[originalImage.getWidth() * originalImage.getHeight()];
			for (int i = 0; i < originalImage.getWidth(); i++) {
				for (int j = 0; j < originalImage.getHeight(); j++) {
					// Get RGB color
					Color col = new Color(originalImage.getRGB(i, j));
					int red = col.getRed();
					int green = col.getGreen();
					int blue = col.getBlue();
					int alpha = col.getAlpha();
					double gsd = 0.3 * red + 0.59 * green + 0.11 * blue;

					int gs = gsd >= 0 ? (int) Math.floor(gsd) : (int) Math.ceil(gsd);
					// int number = matrix[i][j];
					pixelArray[i * originalImage.getWidth() + j] = gs;
					Color grscol = new Color(gs, gs, gs, col.getAlpha());
					grayscaleImage.setRGB(i, j, grscol.getRGB());

				}
			}
			ImageIO.write(grayscaleImage, "jpg", new File("C:\\Users\\V.Sahakyan\\Pictures\\imgTestGrayScale.jpg"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return pixelArray;
	}

	// Normalize array within [0,1]
	public static double[] NormalizeIntArray(int[] pixelArray) {
		double[] normalizedPixelArray = new double[pixelArray.length];

		for (int i = 0; i < pixelArray.length; i++) {
			normalizedPixelArray[i] = (pixelArray[i]) / (255.0);
		}
		return normalizedPixelArray;
	}

	// Generate image from pixel array and save it to pathToSaveImage
	public static void GenerateImageFromPixelArray(int imgWidth, int imgHeight, int[] pixelArray,
			String pathToSaveImage) throws IOException {
		BufferedImage grayscaleImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
		if (imgWidth * imgHeight == pixelArray.length) {
			int row = 0;
			int col = 0;
			for (int i = 0; i < pixelArray.length; i++) {

				if (row <= (imgWidth - 1)) {
					if (col < (imgHeight - 1)) {
						int gs = pixelArray[i];
						;
						Color grscol = new Color(gs, gs, gs);
						grayscaleImage.setRGB(row, col, grscol.getRGB());
						// matrix[row][col]=pixelArray[i];
						col++;
					} else if (col == (imgHeight - 1)) {
						int gs = pixelArray[i];
						;
						Color grscol = new Color(gs, gs, gs);
						grayscaleImage.setRGB(row, col, grscol.getRGB());
						// matrix[row][col]=pixelArray[i];
						row++;
						col = 0;
					}
				}
			}
			ImageIO.write(grayscaleImage, "jpg", new File(pathToSaveImage));
		} else {
			System.out.println("Wrong dimension");
		}

	}

	// Generate gray scale images to outDir from CSV file with row Images in
	// every row
	public static void GenerateImageFromCSVDataSet(int imgWidth, int imgHeight, String csvPath, String outDir)
			throws IOException {
		CSVReader csvr = new CSVReader(csvPath, 0);
		double[][] XInput = new double[csvr.getRowNum()][csvr.getColNum() - 1];
		double[] YInput = new double[csvr.getRowNum()];
		csvr.doInBackground(XInput, YInput);

		for (int i = 0; i < XInput.length; i++) {
			int[] XIntInp = new int[XInput[i].length];
			for (int j = 0; j < XInput[i].length; j++) {
				XIntInp[j] = (int) XInput[i][j];
			}
			StringBuilder path = new StringBuilder(outDir);
			path.append(Integer.toString(i));
			path.append(".jpg");
			GenerateImageFromPixelArray(imgWidth, imgHeight, XIntInp, new String(path));
		}
	}

	// Helper Functions
	private int[] convertMatrixToArray(int[][] matrix) {
		int newArray[] = new int[matrix.length * matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			int[] row = matrix[i];
			for (int j = 0; j < row.length; j++) {
				int number = matrix[i][j];
				newArray[i * row.length + j] = number;
			}
		}

		return newArray;
	}

	private int[][] ConvertArrayToSquareMatrix(int[] array, int dim) {
		int[][] matrix = null;
		if (dim * dim == array.length) {
			matrix = new int[dim][dim];
			int row = 0;
			int col = 0;
			for (int i = 0; i < array.length; i++) {

				if (row <= dim) {
					if (col < dim) {
						matrix[row][col] = array[i];
						col++;
					} else if (col == dim) {
						matrix[row][col] = array[i];
						row++;
						col = 0;
					}
				}
			}
		} else {
			System.out.println("Wrong dimension");
		}

		return matrix;
	}

}

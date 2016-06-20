package com.predictive.algorithms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.helper.tools.CSVReader;


public class AutoEncoder extends NeuralNetworkBase {
	
	protected double [][] Xoutput;
	
	public AutoEncoder(){
		super();
	}
	public AutoEncoder(int Lsize, int[] LvecOfsize){
		super(Lsize,LvecOfsize);
	}
	public void InitializeTrainingData(String pathtoCSV) throws IOException{ 
		super.InitializeTrainingData(pathtoCSV);
		YInput=XInput;
	}
	@Override
	protected double calcSparsityDerivitive(int Li, int I) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static double [] DeNormalizeIntArray(double [] pixelArray){
		double [] normalizedPixelArray = new double[pixelArray.length];
		
		for(int i = 0; i< pixelArray.length; i++){
			int res = (int)((pixelArray[i])*(255));
			if(res>255){
				res = 255;
			}
			if(res<0){
				res = 0;
			}
			normalizedPixelArray[i]= res;
		}
		return normalizedPixelArray;
	}
	
	public void writeResult(String pathtoCSV) throws IOException{
		CSVReader csvr = new CSVReader(pathtoCSV,1); 
		csvr.generateCsvFile(Xoutput, YInpRaw);
	}
	
	public void writeFeatureResult(String pathtoCSV) throws IOException{
		CSVReader csvr = new CSVReader(pathtoCSV,1); 
		//double [] [] XEncode = W[0];
		double [][] XEncode = new double[W[0].length][];
		for(int i = 0; i < W[0].length; i++){
			XEncode = W[0].clone();
		}
		
		for(int i = 0; i<XEncode.length; i++){
			XEncode[i]= DeNormalizeIntArray(XEncode[i]);
		}
		csvr.generateCsvFile(XEncode, YInpRaw);
	}
	
	private double [] copyArray(double [] arr) {
		double [] resarr = new double[arr.length];
		for(int i = 0; i<arr.length;++i){
			resarr[i]=arr[i];
		}
		return resarr;
	}
	
	public void encode(){
		Xoutput = new double[XInput.length][];
		for(int i=0; i<XInput.length;++i){
			FeedForward(i);
			Xoutput[i]=copyArray(AUnit[1]);
		}

	}
	
	public double [] predict(double [] XInputfeet){
		FeedForward(XInputfeet);
		return copyArray(AUnit[1]);
	}

	
//	 public static void main(String[] args) throws IOException {
//		 int[] la = new int[3];
//		 la[0]=784; la[1]=200; la[2]=784;
//		 AutoEncoder nn = new AutoEncoder(3,la);
//		 nn.InitializeTrainingData("C:/Users/V.Sahakyan/Documents/R-Workspace/mnistnorm.csv");
//		 nn.firstWeightActivation();
//		 nn.learnNNByStohasticGradientDescent(45);
//		 try
//	      {
//
//				File file = new File("C:/Users/V.Sahakyan/Desktop/anasun/AutoEncoder.ser");
//				file.createNewFile();
//				
//			   // FileWriter writer = new FileWriter(file);
//	         FileOutputStream fileOut =
//	         new FileOutputStream(file);
//	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
//	         out.writeObject(nn);
//	         out.close();
//	         fileOut.close();
//	         System.out.println("Serialized data is saved in C:/Users/V.Sahakyan/Desktop/anasun/autoencoder.ser");
//	      }catch(IOException i)
//	      {
//	          i.printStackTrace();
//	      }
//	
//		 //nn.FeedForward(0);
//		 //nn.predict();
//
//		 System.out.println("write feature result");
//		 nn.writeFeatureResult("C:/Users/V.Sahakyan/Documents/R-Workspace/featureResult.csv");
//		 nn.encode();
//		 System.out.println("write to file");
//		 nn.writeResult("C:/Users/V.Sahakyan/Documents/R-Workspace/mnistencode.csv");
//		 nn.InitializeTrainingData("C:/Users/V.Sahakyan/Documents/R-Workspace/mnist_testnorm.csv");
//		 nn.encode();
//		 System.out.println("write to file");
//		 nn.writeResult("C:/Users/V.Sahakyan/Documents/R-Workspace/mnistTestencode.csv");
//		 
////		 Random randomno = new Random();
////		 double x = randomno.nextGaussian()*0.0001;
////		 System.out.println();
//	 }
//	
	
}

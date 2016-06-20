package com.helper.tools;

import java.beans.FeatureDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.SortedMap;
import java.util.TreeMap;

import com.predictive.algorithms.*;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMessages_pt_BR;

public class TestNN {
	NeuralNetwork nn;
	String csvpath;
	double [] YInp;
	double [] Yres;
	SortedMap<Integer, Integer> erMap = new TreeMap<Integer,Integer>();
	public TestNN(String path){
		csvpath=path;
	}
	
	public void Run(String path) throws IOException{
		int[] la = new int[3];
		 la[0]=200; la[1]=40; la[2]=10;
		 nn = new NeuralNetwork(3,la);
		 nn.InitializeTrainingData(path);
		 nn.firstWeightActivation();
		 nn.learnNNByStohasticGradientDescent(500);
		 
		 try
	      {

				File file = new File("C:/Users/V.Sahakyan/Desktop/anasun/neuralnet.ser");
				file.createNewFile();
				
			   // FileWriter writer = new FileWriter(file);
	         FileOutputStream fileOut =
	         new FileOutputStream(file);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(nn);
	         out.close();
	         fileOut.close();
	         System.out.println("Serialized data is saved in C:/Users/V.Sahakyan/Desktop/anasun/neuralnet.ser");
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }
	}
	
	public void RestoreFromDump(String path) throws IOException{
		try
	      {
	         FileInputStream fileIn = new FileInputStream("C:/Users/V.Sahakyan/Desktop/anasun/neuralnet.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         nn = (NeuralNetwork) in.readObject();
	         in.close();
	         fileIn.close();
	         System.out.println("Serialized data is saved in C:/Users/V.Sahakyan/Desktop/anasun/neuralnet.ser");
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Employee class not found");
	         c.printStackTrace();
	         return;
	      }
	}
	
	public int Result() throws IOException{
		int res=0;
		nn.InitializeTrainingData(csvpath);
		YInp=nn.getInputY();
		Yres= new double[YInp.length];
		for(int i=0; i<YInp.length;++i){
			nn.FeedForward(i);
			Yres[i]=nn.getResultClass();
			if((YInp[i]-Yres[i])==0){
				++res;
			}else{
				if(erMap.get((int)YInp[i])==null){
					erMap.put((int) YInp[i], 1);
				}else{
					Integer val = erMap.get((int)YInp[i]);
					erMap.put((int) YInp[i], val+1);
				}
			}
		}
		return res;
		
		
		
	}
	
	public static void main(String[] args) throws IOException {
		TestNN tnn = new TestNN("C:/Users/V.Sahakyan/Documents/R-Workspace/mnistTestencode.csv");	
		//tnn.Run("C:/Users/V.Sahakyan/Documents/R-Workspace/mnistencode.csv");
		tnn.RestoreFromDump("foo");
		System.out.println(tnn.Result());
		System.out.println(tnn.erMap.toString());
		 
	 }
}
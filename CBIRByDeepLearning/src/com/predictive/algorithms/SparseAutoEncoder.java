package com.predictive.algorithms;

import java.io.IOException;
import java.util.Random;

import com.helper.tools.CSVReader;


public class SparseAutoEncoder extends AutoEncoder {

	private double [][] MeanUnit; // Mean value for activations units.
	private double beta=0.2;   // beta sparsity influence parameter.
	private double sparsityParam=0.15; // sparsity parameters.
	
	public SparseAutoEncoder(int Lsize, int[] LvecOfsize) {
		super(Lsize, LvecOfsize);
		MeanUnit = new double[L][];
		for(int count = 0; count<(Lvec.length);++count){
			MeanUnit[count]= new double[Lvec[count]];	
		}
	}
	
	public void InitializeTrainingData(String pathtoCSV) throws IOException{ 
		super.InitializeTrainingData(pathtoCSV);
		YInput=XInput;
	}

	public void firstWeightActivation(){
		
		super.firstWeightActivation();
		
		Random randomno = new Random();				
		for(int Lin = 0; Lin<MeanUnit.length;++Lin){
			for(int Iin = 0; Iin<MeanUnit[Lin].length;++Iin){
					double x = randomno.nextGaussian()*0.001;
					MeanUnit[Lin][Iin]=x;
			}
		}	
	}
	
	// compute Ro hat for all activation units with respect to whole training set.
	void computeMeanForActivationUnits(){
		for(int i = 0; i<MeanUnit.length;++i){
				for(int j=0; j<MeanUnit[i].length;++j){
				MeanUnit[i][j]= (0.999*MeanUnit[i][j] + 0.001*AUnit[i][j]);
			}
		}
	} 
		
	protected double calcSparsityDerivitive(int Li, int I){
		double res = -(sparsityParam/MeanUnit[Li][I])+((1-sparsityParam)/(1-MeanUnit[Li][I]));
		return res;
	}
	
	
	public void updateParams(){
		
		for(int Lin = 0; Lin<W.length;++Lin){
			for(int Iin = 0; Iin<W[Lin].length;++Iin){
				if(Lin==0){
					bWeight[Lin][Iin]= bWeight[Lin][Iin] - alpha*((beta*(MeanUnit[Lin+1][Iin]-sparsityParam))+ bWeightParDer[Lin][Iin]);
				}else{
					bWeight[Lin][Iin]= bWeight[Lin][Iin] - alpha*(bWeightParDer[Lin][Iin]);
				}
				for(int Jin = 0; Jin<W[Lin][Iin].length;++Jin){
					W[Lin][Iin][Jin] = W[Lin][Iin][Jin]- alpha*WParDer[Lin][Iin][Jin];
				}
			}
		}
		
	}
	
	
}

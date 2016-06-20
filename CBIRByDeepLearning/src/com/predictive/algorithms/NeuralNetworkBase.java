package com.predictive.algorithms;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

import com.helper.tools.*;;

public abstract class NeuralNetworkBase implements Serializable {
	
	protected int L; //number of layers
	protected int [] Lvec; //area with sizes of each layer.E.g: Lvec(4,5,5,2) 4 layer NN  
	protected double [][] AUnit; // active units
	private double [][] ZUnit; // z units - the linear combination of W(L)*X
	private double [][] deltaUnit; // delta values for each node in each layer
	private double [] bUnit; //bias unit
	protected double [][][] W; //weight w[L][NextLsize][PreviewLsize]
	protected double [][] bWeight; //bias units weight
	
	// partial derivatives by every parameter
	protected double [][][] WParDer; // partial derivatives for all W
	protected double [][] bWeightParDer; // partial derivatives for all bWeight
	
	protected double[][] XInput; // input training set X with m-rows.
	protected double[][] YInput; // input training label set Y with m-rows.
	protected double[] YInpRaw; // input as numbers m*1 column vector.
	
	private double lambda=0.0001; // lambda value for regularization
	protected double alpha=0.02; // alpha value for learning rate. 
	
	// Constructor
	
	public NeuralNetworkBase(){};
	
	public NeuralNetworkBase(int Lsize, int[] LvecOfsize){
		L=Lsize;
		Lvec=LvecOfsize;
		AUnit = new double[L][];
		ZUnit = new double[L][];
		bUnit = new double[L-1];
		deltaUnit = new double[L][];
		W= new double[L-1][][];
		WParDer= new double[L-1][][];
		bWeight = new double[L-1][];
		bWeightParDer = new double[L-1][];
		for(int count = 0; count<(Lvec.length-1);++count){
			int Lplus1=count+1;
			// PROBLEMATIC DEBUG
			W[count]= new double[Lvec[Lplus1]][Lvec[count]]; // layer I[L] and J[L+1]
			WParDer[count]= new double[Lvec[Lplus1]][Lvec[count]]; 
			bWeight[count] = new double[Lvec[Lplus1]]; 
			bWeightParDer[count] = new double[Lvec[Lplus1]]; 
			
		}
		for (double myInt: bUnit)
		      myInt = 1;
		for(int count = 0; count<(Lvec.length);++count){ 
			AUnit[count]= new double[Lvec[count]]; 
			ZUnit[count]= new double[Lvec[count]]; 
			deltaUnit[count]= new double[Lvec[count]]; 
			
		}
		
		
	}
	
	
	
	// Helper functions
	
	public double [] getInputY(){
		return YInpRaw;
	}
	
	private double [] substract(double [] x, double[] y){
		if(x.length == y.length){
			double [] res = new double[x.length];
			for(int i = 0; i<x.length; ++i){
				res[i]=x[i]-y[i];
		}
		return res;
		}else{
			return new double[1];
		}
	}
	private double vectorNorm(double[] x){
		double normRes=0;
		for(int i = 0; i<x.length; ++i){
			normRes += x[i]*x[i];
		}
		return normRes;
	}
	
	private static double sigmoid(double x)
	{
		double y=0;
		if( x < -10 )
		    y = 0;
		else if( x > 10 )
		    y = 1;
		else if (x == 0)
			y=0.5;
		else
		    y = 1 / (1 + Math.exp(-x));
		return y;
	    
	}
	
	
	public void InitializeTrainingData(String pathtoCSV) throws IOException{ 
		CSVReader csvr = new CSVReader(pathtoCSV,0);
		int numRow = csvr.getRowNum();
		XInput = new double[numRow][csvr.getColNum()-1]; 
        YInpRaw = new double[numRow]; 
		csvr.doInBackground(XInput,YInpRaw);
		
		//TO DO in derived classes
	}
	
	private void computeSingleZUnit(int Ls,int Is){
		// PROBLEMATIC DEBUG
		double zval=0;
		for(int cn = 0; cn<W[Ls-1][Is].length; ++cn){
			zval+=W[Ls-1][Is][cn]*AUnit[Ls-1][cn];	
			// rewrit to 0
		}
		zval+=bUnit[Ls-1]*bWeight[Ls-1][Is];
		ZUnit[Ls][Is]=zval;
	}
	
	private void computeAUnitByZUnit(int Ls, int Is){
		AUnit[Ls][Is]=sigmoid(ZUnit[Ls][Is]);
	}
	
	private double costFunctionPerExample(int k){
		
		FeedForward(k);
		double costF = (1/2)*vectorNorm(substract(AUnit[AUnit.length-1],YInput[k] ));
		//TO DO
		return costF;
		
	}
	
	private double costFunction(){
		double overallError=0;
		double costError=0;
		double costRegularization=0;
		
		for(int i = 0; i<XInput.length; ++i){
			costError+= costFunctionPerExample(i);
		}
		for(int Lin = 0; Lin<W.length;++Lin){
			for(int Iin = 0; Iin<W[Lin].length;++Iin){
				for(int Jin = 0; Jin<W[Lin][Iin].length;++Jin){
					costRegularization+=W[Lin][Iin][Jin]*W[Lin][Iin][Jin];
				}
			}
		}
		overallError = (1/XInput.length)*costError + (lambda/2)*costRegularization;
		
		// TO DO
		return overallError;
	}
	
	private double calcSigmDerivitive(int Li, int I){
		double Ai = AUnit[Li][I];
		return (Ai*(1-Ai));
	}
	private double calcDeltSum(int Li,int I){
		
		double delval=0; // FIX PROBLEMS
		for(int j=0; j<Lvec[Li];++j){
			delval+=(W[Li-1][j][I]*deltaUnit[Li][j]);
 			WParDer[Li-1][j][I]=(AUnit[Li-1][I]*deltaUnit[Li][j] + lambda*W[Li-1][j][I]);
		}
		return delval;
	}
	
	protected abstract double calcSparsityDerivitive(int Li, int I);
	
	public void firstWeightActivation(){
		Random randomno = new Random();
				
		for(int Lin = 0; Lin<W.length;++Lin){
			for(int Iin = 0; Iin<W[Lin].length;++Iin){
				for(int Jin = 0; Jin<W[Lin][Iin].length;++Jin){
					double x = randomno.nextGaussian()*0.001;
					W[Lin][Iin][Jin]=x;
				}
			}
		}
		for(int Lin = 0; Lin<bWeight.length;++Lin){
			for(int Iin = 0; Iin<bWeight[Lin].length;++Iin){
				double x = randomno.nextGaussian()*0.001;
				bWeight[Lin][Iin]=x;
			}
		}
				
	}

	public void FeedForward(int k ){
		// PROBLEMATIC DEBUG
		if(AUnit[0].length == XInput[k].length){
			for(int i = 0; i<AUnit[0].length; ++i){
			AUnit[0][i]=XInput[k][i];
			}
			
			for(int Li = 1; Li<(L);++Li){
				for(int Ii = 0; Ii<( Lvec[Li] );++Ii){
					computeSingleZUnit(Li,Ii);
					computeAUnitByZUnit(Li,Ii);
					
				}
			}
			
		}else{
			System.out.println("Inconsistent size of Xinput and First layer of neural network");
		}
	}
	
	public void FeedForward(double [] XInputfeet ){
		// PROBLEMATIC DEBUG
		if(AUnit[0].length == XInputfeet.length){
			for(int i = 0; i<AUnit[0].length; ++i){
			AUnit[0][i]=XInputfeet[i];
			}
			
			for(int Li = 1; Li<(L);++Li){
				for(int Ii = 0; Ii<( Lvec[Li] );++Ii){
					computeSingleZUnit(Li,Ii);
					computeAUnitByZUnit(Li,Ii);
					
				}
			}
			
		}else{
			System.out.println("Inconsistent size of Xinput and First layer of neural network");
		}
	}
	
	public void backPropogation(int k){
		//calculate error estimation between last unit and labels TODO . 
		if((AUnit[L-1].length == deltaUnit[L-1].length) && (AUnit[L-1].length == YInput[k].length)){
			
			for(int i=0; i<AUnit[L-1].length;++i){
				double Ai = AUnit[L-1][i];
				deltaUnit[L-1][i]=(-(YInput[k][i] - Ai)*calcSigmDerivitive(L-1, i));
			}
		}else{
			System.out.println("Inconsistent between activation and delta unists.");
		}
		
		for(int Lit=L-2; Lit>=0;--Lit){
			for(int i=0;i<bWeightParDer[Lit].length;++i){
				bWeightParDer[Lit][i]= deltaUnit[Lit+1][i];
			}
			for(int i=0; i<deltaUnit[Lit].length; ++i){
				deltaUnit[Lit][i]=calcDeltSum(Lit+1, i)*calcSigmDerivitive(Lit, i);
//				if(i<bWeightParDer[Lit].length){
//					bWeightParDer[Lit][i]=deltaUnit[Lit+1][i];
//				}
			}
		}	
		
	}
	
	public void updateParams(){
		
		for(int Lin = 0; Lin<W.length;++Lin){
			for(int Iin = 0; Iin<W[Lin].length;++Iin){
				bWeight[Lin][Iin]= bWeight[Lin][Iin] - alpha*bWeightParDer[Lin][Iin];
				for(int Jin = 0; Jin<W[Lin][Iin].length;++Jin){
					W[Lin][Iin][Jin] = W[Lin][Iin][Jin]- alpha*WParDer[Lin][Iin][Jin];
				}
			}
		}
		
	}
	
	public void learnNNByStohasticGradientDescent(int numberOfRepetition){ 
		
		for(int i=0; i<numberOfRepetition;++i){
			System.out.println(i+1);
			for(int k = 0; k<XInput.length; ++k){
				//System.out.print('.');;
				FeedForward(k);
				backPropogation(k);
				updateParams();
				
			}
		}
		
	}
	
	
}

package com.predictive.algorithms;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;

import com.helper.tools.CSVReader;

public class NeuralNetwork extends NeuralNetworkBase {

	public NeuralNetwork() {
		super();
	}

	public NeuralNetwork(int Lsize, int[] LvecOfsize) {
		super(Lsize, LvecOfsize);
	}

	public void InitializeTrainingData(String pathtoCSV) throws IOException {
		super.InitializeTrainingData(pathtoCSV);

		CSVReader csvr = new CSVReader(pathtoCSV, 0);
		int numRow = csvr.getRowNum();
		Set<Double> uniqueNumbers = new HashSet<Double>(Arrays.asList(ArrayUtils.toObject(YInpRaw)));
		double s = Collections.max(uniqueNumbers);
		int classCount = (int) s + 1;
		YInput = new double[numRow][classCount];
		for (int k = 0; k < numRow; ++k) {
			double[] tmpY = new double[classCount];

			tmpY[(int) YInpRaw[k]] = 1;
			YInput[k] = tmpY;
		}

	}

	@Override
	protected double calcSparsityDerivitive(int Li, int I) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getResultClass() {
		double maxv = 0;
		int maxind = -1;
		for (int i = 0; i < AUnit[L - 1].length; ++i) {
			if (AUnit[L - 1][i] > maxv) {
				maxv = AUnit[L - 1][i];
				maxind = i;
			}
		}
		// if(maxv >1){
		return maxind;
		// }else{
		// return 9999;
		// }
	}

	public int predict(double[] XInputfeet) {
		FeedForward(XInputfeet);
		return getResultClass();
	}

}

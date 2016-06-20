package com.prediction.services;

import com.predictive.algorithms.NeuralNetwork;

public class NeuralNetworModelSingleton {
	private static NeuralNetworModelSingleton NNMSInstance=null;
	private NeuralNetwork NN;
	
	private NeuralNetworModelSingleton(){
		
	}
	// Lazy Initialization (If required then only)
	public static NeuralNetworModelSingleton getInstance() {
		if (NNMSInstance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (NeuralNetworModelSingleton.class) {
				if (NNMSInstance == null) {
					NNMSInstance = new NeuralNetworModelSingleton();
				}
			}
		}
		return NNMSInstance;
	}
	
	public void setNeuralNetwork(NeuralNetwork nn){
		NN=nn;
	}
	public NeuralNetwork getNeuralNetwork(){
		return NN;
	}
}

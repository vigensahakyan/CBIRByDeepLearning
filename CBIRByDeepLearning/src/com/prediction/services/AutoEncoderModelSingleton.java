package com.prediction.services;

import com.predictive.algorithms.AutoEncoder;

public class AutoEncoderModelSingleton {
	private static AutoEncoderModelSingleton AEMSInstance=null;
	private AutoEncoder AE;
	
	private AutoEncoderModelSingleton(){
		
	}
	// Lazy Initialization (If required then only)
	public static AutoEncoderModelSingleton getInstance() {
		if (AEMSInstance == null) {
			// Thread Safe. Might be costly operation in some case
			synchronized (AutoEncoderModelSingleton.class) {
				if (AEMSInstance == null) {
					AEMSInstance = new AutoEncoderModelSingleton();
				}
			}
		}
		return AEMSInstance;
	}
	
	public void setAutoEncoder(AutoEncoder ae){
		AE=ae;
	}
	public AutoEncoder getAutoEncoder(){
		return AE;
	}
}
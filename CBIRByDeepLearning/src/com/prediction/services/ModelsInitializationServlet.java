package com.prediction.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.predictive.algorithms.*;

@Path("/InitializeModels")
public class ModelsInitializationServlet {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getInitializationInfo(@QueryParam("NNPATH") String nnPath, @QueryParam("AEPATH") String aePath) {
		StringBuilder returnValue = new StringBuilder("Initialization service ");
		returnValue.append("<br/>");
		if (!nnPath.equals("")) {
			returnValue.append("1. Get argument for NeuralNetwork model path ");
			NeuralNetwork nn;
			try {
				FileInputStream fileIn = new FileInputStream(nnPath);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				nn = (NeuralNetwork) in.readObject();
				in.close();
				fileIn.close();

				NeuralNetworModelSingleton.getInstance().setNeuralNetwork(nn);
				returnValue.append("And succesfully initialize Model,");
				System.out.println("Serialized data is saved path/to/neuralnet.ser");
			} catch (IOException i) {
				returnValue.append(" but didn't initialize model because of wrong argument ");
				i.printStackTrace();

			} catch (ClassNotFoundException c) {
				System.out.println("Employee class not found");
				c.printStackTrace();
			}
		} else {
			returnValue.append("1. Didn't receive argument for NeuralNetwork model path");
		}
		returnValue.append("<br/>");
		if (!aePath.equals("")) {
			returnValue.append("2. Get argument for AutoEncoder model path,");
			AutoEncoder ae;
			try {
				FileInputStream fileIn = new FileInputStream(aePath);
				ObjectInputStream in = new ObjectInputStream(fileIn);
				ae = (AutoEncoder) in.readObject();
				in.close();
				fileIn.close();

				AutoEncoderModelSingleton.getInstance().setAutoEncoder(ae);
				returnValue.append("And succesfully initialize Model");
				System.out.println("Serialized data is saved in path/to/neuralnet.ser");
			} catch (IOException i) {
				returnValue.append(" but didn't initialize model because of wrong argument ");
				i.printStackTrace();

			} catch (ClassNotFoundException c) {
				System.out.println("Employee class not found");
				c.printStackTrace();
			}
		} else {
			returnValue.append("2. Didn't receive argument for AutoEncoder model path");
		}

		return "<html>" + "<titles" + "University Information" + "</titles>" + "<body><h1>" + returnValue
				+ "</h1></body>" + "</html>";
	}
}

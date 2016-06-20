package com.prediction.services;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import com.predictive.algorithms.*;

 
/**
 * @author Crunchify.com
 */
 
@SuppressWarnings("serial")
public class PredictionModelSingleton extends HttpServlet
{
	
    public void init() throws ServletException
    {
    	  NeuralNetworModelSingleton.getInstance().setNeuralNetwork(new NeuralNetwork());
    	  AutoEncoderModelSingleton.getInstance().setAutoEncoder(new AutoEncoder());
    	  //DBModelConfiguration.getInstance().setConfiguration("192.168.0.5", "1433", "PACManagerDB", "Sa", "Sa123456789", DBServerType.MSSQL);
          System.out.println("----------");
          System.out.println("---------- CrunchifyExample Servlet Initialized successfully ----------");
          System.out.println("----------");
    }
}
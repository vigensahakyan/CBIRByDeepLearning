package com.prediction.services;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.prism.Image;
import com.helper.tools.*;

@Path("/RecognizeImage")
public class ImagePredictionService {
	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String predictImageAndReturnClassValue(
			@FormDataParam("my_file") InputStream fileInputStream,
			@FormDataParam("my_file") FormDataContentDisposition fileFormDataContentDisposition
            ) throws IOException{
		// local variables
        String fileName = null;
        String uploadFilePath = null;
        int classID;
        try {
        	ImageTools IT = new ImageTools();
        	int [] arr = IT.ReadImageConvertToGrayScale(IT.ResizeImage(28, 28, ImageIO.read(fileInputStream), 0));
        	//IT.GenerateImageFromPixelArray(28,28,arr,"C:\\Users\\V.Sahakyan\\Documents\\NPATH.jpg");
        	double [] AEInput = IT.NormalizeIntArray(arr);
        	double [] NNInput = AutoEncoderModelSingleton.getInstance().getAutoEncoder().predict(AEInput);
        	classID = NeuralNetworModelSingleton.getInstance().getNeuralNetwork().predict(NNInput);
            //fileName = fileFormDataContentDisposition.getFileName();
            //String uploadedFileLocation = "C:\\Users\\V.Sahakyan\\Documents\\"+fileName;
            //writeToFile(fileInputStream, uploadedFileLocation);
        }
        finally{
            // release resources, if any
        }
        //return Response.ok("File uploaded successfully at " + uploadFilePath).build();
        return Integer.toString(classID);
		//return "<html>"+"<titles"+"University Information"+"</titles>"+ "<body><h1>" + classID +"</h1></body>"+"</html>";
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public String predictionServerCheker() {
		 
		return "<html>"+"<titles"+"University Information"+"</titles>"+ "<body><h1>"+ "Your prediction server is Up and Run. Please initialize your NeuralNetwork and AutoEncoder models." +"</h1></body>"+"</html>";
	}
	
	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

			try {
				FileOutputStream out = new FileOutputStream(new File(
						uploadedFileLocation));
				int read = 0;
				byte[] bytes = new byte[1024];

				out = new FileOutputStream(new File(uploadedFileLocation));
				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				//out.flush();
				if(out!=null){
					out.close();
					out = null;
					System.gc();
				}
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
}
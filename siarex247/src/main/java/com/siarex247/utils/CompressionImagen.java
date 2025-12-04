package com.siarex247.utils;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.stream.*;

public class CompressionImagen {

  public static void main(String[] args) throws IOException {

    File input = new File("C:/FACTURAS/logoSiarex.png");
    BufferedImage image = ImageIO.read(input);

    File compressedImageFile = new File("C:/FACTURAS/compressed_image.jpg");
    OutputStream os = new FileOutputStream(compressedImageFile);

    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
    ImageWriter writer = (ImageWriter) writers.next();

    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
    writer.setOutput(ios);

    ImageWriteParam param = writer.getDefaultWriteParam();

    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
    //param.setCompressionQuality(0.05f);  // Change the quality value you prefer
    writer.write(null, new IIOImage(image, null, null), param);

    os.close();
    ios.close();
    writer.dispose();
  }
  
  
  public static void comprimirArchivo(String pathOrigen, String pathDestino) {
	  try {
		    File input = new File(pathOrigen);
		    BufferedImage image = ImageIO.read(input);

		    File compressedImageFile = new File(pathDestino);
		   // File compressedImageFile = new File(pathDestino);
		   // if (!compressedImageFile.exists()) {
		   // 	compressedImageFile.createNewFile();
		   // }
		    File parent = new File(compressedImageFile.getParent());
		    if (!parent.exists()) {
				parent.mkdirs();
			}
		    OutputStream os = new FileOutputStream(compressedImageFile);

		    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
		    ImageWriter writer = (ImageWriter) writers.next();

		    ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		    writer.setOutput(ios);

		    ImageWriteParam param = writer.getDefaultWriteParam();

		    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		    //param.setCompressionQuality(0.05f);  // Change the quality value you prefer
		    writer.write(null, new IIOImage(image, null, null), param);

		    os.close();
		    ios.close();
		    writer.dispose();
		  
	  }catch(Exception e) {
		  Utils.imprimeLog("", e);
	  }
  }
}
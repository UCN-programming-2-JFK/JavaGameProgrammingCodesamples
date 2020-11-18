package jfk.tools;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;


public class Toolbox {

	
	 public BufferedImage loadcImage(String imagePathOrUrl)
	    {
		 BufferedImage image = null;
		 try {
			 image = ImageIO.read(this.getClass().getResource(imagePathOrUrl));
//			 image = ImageIO.read(new File(imagePathOrUrl));
			} catch (IOException e) {System.out.println(e.getMessage());}
		 return image;
	    }
	
}

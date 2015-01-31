/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdrelements01;

/**
 *
 * @author David Karlsson
 */
import java.io.File;
import com.drew.metadata.*;
import com.drew.imaging.jpeg.*;
import java.util.Iterator;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Color;

public class HDRElemJPEG 
{
    private float exposureTime;        
    private BufferedImage image;
    private String imageName;
    
    public HDRElemJPEG(File fp)
    {
	Metadata metadata = null;	
	
	try
	{
	    File jpegFile = new File(fp, fp.getName());
//            File jpegFile = fp;
	    metadata = JpegMetadataReader.readMetadata(fp);	    	    
	} catch (JpegProcessingException e)	
	{
	    System.out.println("File not found.");
	}
	
	this.exposureTime = getExposureTime(metadata);
	this.imageName = fp.getName();
        
	try
	{
	    this.image = ImageIO.read(fp);
	} catch (IOException e) {}				
    }
    
    public float getExposureTime()
    {
	return this.exposureTime;
    }
    
    public int getWidth()
    {
	return this.image.getWidth();
    }
    
    public int getHeight()
    {
	return this.image.getHeight();
    }
    
    public String getName()
    {
	return this.imageName;
    }
    
    public int[] getRGB(int x, int y)
    {
	 Color color = new Color(this.image.getRGB(x, y));
	 
	 int[] components = new int[3];
	 
	 components[0] = color.getRed();
	 components[1] = color.getGreen();
	 components[2] = color.getBlue();
	 
	 return components;
    }
    
    private float getExposureTime(Metadata metadata)
    {
	Iterator directories = metadata.getDirectoryIterator();
	
	float expTime = 0.0f;
	
	while (directories.hasNext()) 
	{
	    Directory directory = (Directory)directories.next();
	
	    Iterator tags = directory.getTagIterator();
	    
	    while (tags.hasNext()) 
	    {
		Tag tag = (Tag)tags.next();	
		String tagname = (tag.getTagName()).toLowerCase();
	
		try
		{		    		    
		    if (tagname.indexOf("exposure") != -1 && tagname.indexOf("time") != -1)
		    {
			String value = tag.getDescription();		   		       
									
			if (value.indexOf("/") != -1)
			{
			    String[] wordlist = (value.trim()).split(" ");
			    String[] partofnumber = wordlist[0].split("/");

			    expTime = (new Float(partofnumber[0])).floatValue() / (new Float(partofnumber[1])).floatValue();
			}
			else
			{
			    String[] wordlist = (value.trim()).split(" ");
			    expTime = (new Float(wordlist[0])).floatValue();			    			    
			}											
		    }		    		    		    
		} catch (MetadataException e) {}
	
	    }
	}
		
	return expTime;
    }
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdrelements01;

/**
 *
 * @author David Karlsson
 */
import java.io.*;

public class HDRElemPFM 
{
    private int width;
    private int height;
    private float pixelRatio;
    private boolean  blackandwhite;
    private float[] imageData;
    
    public static void main(String[] args)
    {	
	HDRElemPFM testPFM = new HDRElemPFM(new File("c:\\temp\\testphotoshop.pfm"));
			
	for (int n = 0; n < 100; n++)
	    System.out.println(testPFM.imageData[n*3]);
	
	testPFM.writePFM(new File("c:\\temp\\testphotoshop2.pfm"));
    }	    
    
    public HDRElemPFM(File fp)
    {
	readPFM(fp);
    }
    
    public void readPFM(File fp)
    {
	try
	{
	    FileInputStream fis = new FileInputStream(fp);
	    BufferedInputStream bis = new BufferedInputStream(fis);
	    DataInputStream dis = new DataInputStream(bis);	    	    
	    
	    String header = getHeader(dis);
	    	    	    
	    if (header.indexOf("PF") != -1)
	    {
		System.out.println("Found color PFM file.");
		this.blackandwhite = false;
	    }
	    else if (header.indexOf("Pf") != -1)
	    {
		System.out.println("Found black and white PFM file.");
		this.blackandwhite = true;
	    }
	    else
	    {
		System.out.println("PFM header not found.");
		return;
	    }

	    this.width = getNextInt(dis);
	    this.height = getNextInt(dis);
	    this.pixelRatio = getNextFloat(dis);	    
	    
	    System.out.println("Width: " + this.width);
	    System.out.println("Height: " + this.height);
	    System.out.println("Ratio: " + this.pixelRatio);
	    
	    int dataSize = this.width*this.height;
	    
	    if (this.blackandwhite == false)
		dataSize = dataSize * 3;
	    
	    this.imageData = new float[dataSize];
	    	    	    
	    for (int n = 0; n < dataSize; n++)
	    {
		this.imageData[n] = dis.readFloat();

		if (this.pixelRatio < 0.0f)
		{
		    int tmp = Float.floatToIntBits(this.imageData[n]);		    
		    this.imageData[n] = Float.intBitsToFloat(reverseInt(tmp));
		}
	    }
	
	    if (this.pixelRatio < 0.0f)
		this.pixelRatio = -this.pixelRatio;
	    
	    dis.close();
	    bis.close();
	    fis.close();
	}
	catch (FileNotFoundException e) 
	{
	    System.out.println("File not found.");
	}
	catch (IOException e) 
	{
	    System.out.println("Error reading PFM file.");
	}
	
    }
    
    public void writePFM(File fp)
    {
	writePFM(fp, this.width, this.height, this.pixelRatio, this.blackandwhite, this.imageData);
    }
    
    public static void writePFM(File fp, int width, int height, float pixelRatio, boolean blackandwhite, float[] data)
    {	 	
	try
	{
	    FileOutputStream fos = new FileOutputStream(fp);	
	    DataOutputStream ds = new DataOutputStream(fos);

	    if (blackandwhite == false)
		ds.writeBytes("PF\n");
	    else
		ds.writeBytes("Pf\n");
	    
	    ds.writeBytes(width + " " + height + "\n");
	    ds.writeBytes(pixelRatio + "\n");

	    for (int h = 0; h < height; h++)
		for (int w = 0; w < width; w++)	    
		{
		    int index = w + (height-1-h)*width;
		    
		    if (blackandwhite == false)		    
		    {
			ds.writeFloat(data[index*3]);
			ds.writeFloat(data[index*3+1]);
			ds.writeFloat(data[index*3+2]);
		    }
		    else
			ds.writeFloat(data[index]);
		}
	    
	    ds.close();
	    fos.close();
	}
	catch (IOException e) {
            System.out.println("Error writing file to disk...");
        }	
    }
    
    private String getHeader(DataInputStream dis)
    {
	try
	{
	    byte[] tmp = new byte[3];

	    for (int n = 0; n < 3; n++)
		tmp[n] = dis.readByte();							    

	    return new String(tmp);
	}
	catch (IOException e)
	{
	    System.out.println("Error reading header.");
	    return "";
	}
    }
    
    private int getNextInt(DataInputStream dis)
    {
	try
	{
	    String buffer = "";
	    	    
	    byte[] currentChar = new byte[1];
	    
	    currentChar[0] = dis.readByte();
	    
	    while(currentChar[0] < 48 || currentChar[0] > 57)
		currentChar[0] = dis.readByte();
	    
	    while (currentChar[0] >= 48 && currentChar[0] <= 57)
	    {		
		buffer += new String(currentChar);
		currentChar[0] = dis.readByte();
	    }
	    
	    return Integer.parseInt(buffer);
	}
	catch (IOException e)
	{
	    System.out.println("Error reading integer.");
	    return 0;
	}
    }
    
    private float getNextFloat(DataInputStream dis)
    {
	try
	{
	    String buffer = "";	    	    
	    byte[] currentChar = new byte[1];
	    
	    currentChar[0] = dis.readByte();
	    
	    while((currentChar[0] < 48 || currentChar[0] > 57) && currentChar[0] != 45)
		currentChar[0] = dis.readByte();	    
	    
	    while ((currentChar[0] >= 48 && currentChar[0] <= 57) || currentChar[0] == 46 || currentChar[0] == 45)
	    {		
		buffer += new String(currentChar);
		currentChar[0] = dis.readByte();
	    }
	    
	    return Float.parseFloat(buffer);
	}
	catch (IOException e)
	{
	    System.out.println("Error reading float.");
	    return 0.0f;
	}
    }
    
    private static int reverseInt(int data)
    {
	return (data >>> 24) | (data << 24) | ((data << 8) & 0x00FF0000) | ((data >> 8) & 0x0000FF00);
    }
}


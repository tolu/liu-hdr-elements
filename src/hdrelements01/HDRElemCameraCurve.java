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
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import java.io.*;

public class HDRElemCameraCurve
{
    private Matrix[] respCurves;

//    public static void main(String[] args)
//    {
//	HDRElemJPEG[] images = new HDRElemJPEG[7];
//
//	images[0] = new HDRElemJPEG(new File("IMG_5172.JPG"));
//	images[1] = new HDRElemJPEG(new File("IMG_5173.JPG"));
//	images[2] = new HDRElemJPEG(new File("IMG_5174.JPG"));
//	images[3] = new HDRElemJPEG(new File("IMG_5175.JPG"));
//	images[4] = new HDRElemJPEG(new File("IMG_5176.JPG"));
//	images[5] = new HDRElemJPEG(new File("IMG_5177.JPG"));
//	images[6] = new HDRElemJPEG(new File("IMG_5178.JPG"));
//
//	new HDRElemCameraCurve(images, 100);
//
//    }

    public HDRElemCameraCurve()
    {
	this.respCurves = new Matrix[3];

	for (int channel = 0; channel < 3; channel ++)
	{
	    this.respCurves[channel] = new Matrix(256, 1);

	    for (int n = 0; n < 256; n++)
		this.respCurves[channel].set(n, 0, (double)n/256.0);
	}
    }

    public HDRElemCameraCurve(HDRElemJPEG[] imageList, int numSamples)
    {
	System.out.println("Using:");

	for (int n = 0; n < imageList.length; n++)
	    System.out.println(imageList[n].getName() + "   factor: " + imageList[n].getExposureTime());

	int imageWidth = imageList[0].getWidth();
	int imageHeight = imageList[0].getHeight();

	int[] x = new int[numSamples];
	int[] y = new int[numSamples];

	for (int n = 0; n < numSamples; n++)
	{
	    x[n] = (int)(Math.random()*imageWidth);
	    y[n] = (int)(Math.random()*imageHeight);
	}

	int numChannels = 3;

	this.respCurves = new Matrix[numChannels];

	for (int channel = 0; channel < numChannels; channel ++)
	{
	    Matrix A = new Matrix(numSamples*imageList.length + 256 + 1, 256 + numSamples);
	    Matrix b = new Matrix(A.getRowDimension(), 1);

	    int k = 0;
	    for (int i = 0; i < numSamples; i++)
	    {
		for (int j = 0; j < imageList.length; j++)
		{
		    int[] clr = imageList[j].getRGB(x[i], y[i]);
		    int intensity = clr[channel];

		    float wij = getWeight(intensity);

		    A.set(k, intensity, wij);
		    A.set(k, 256 + i, -wij);
		    b.set(k, 0, wij*Math.log(imageList[j].getExposureTime()));

		    k ++;
		}
	    }

	    A.set(k, 128, 1.0);
	    k ++;

	    float lamdba = 20.0f;

	    for (int i = 0; i < 256-2; i++)
	    {
		A.set(k, i, lamdba*getWeight(i+1));
		A.set(k, i+1, -2.0f*lamdba*getWeight(i+1));
		A.set(k, i+2, lamdba*getWeight(i+1));

		k ++;
	    }

	    SingularValueDecomposition svd = new SingularValueDecomposition(A);

	    this.respCurves[channel] = (((svd.getV()).times((svd.getS()).inverse())).times((svd.getU()).transpose())).times(b);
	}
/*
	try
	{
	    BufferedWriter out = new BufferedWriter(new FileWriter("c:\\temp\\tcurve.m"));

	    for (int channel = 0; channel < numChannels; channel ++)
	    {
		out.write("mycurve" + channel + " = [");

		for (int n = 0; n < 256; n++)
		{
		    out.write((new Double(respCurves[channel].get(n, 0))).toString());

		    if (n != 255)
			out.write(";");
		}

		out.write("];");
	    }
	    out.close();

	} catch (IOException e) {}
*/
    }

    public float[] getResponse(int[] color)
    {
	float[] response = new float[3];

	for (int n = 0; n < 3; n++){
            double index = this.respCurves[n].get(color[n], 0);//ändrade index till noll här
	    response[n] = (float) Math.pow(2.0, (index));
        }
	return response;
    }

    private float getWeight(int index)
    {
	if (index < 128)
	    return (float)index/128.0f;
	else
	    return (float)(256-index)/128.0f;
    }
}


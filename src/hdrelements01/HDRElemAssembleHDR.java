/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdrelements01;

import Jama.Matrix;
import java.io.File;

/**
 *
 * @author Tobias
 */
public class HDRElemAssembleHDR {
    private float exposure;
    private float weight;
    private float[] hdr;
    private float[] den;
    private float[] nom;
    private final int R = 0;
    private final int G = 1;
    private final int B = 2;
    private java.util.Calendar cal1, cal2;
    private int t1, t2;
    
    
    public HDRElemAssembleHDR(HDRElemJPEG[] imageList, HDRElemCameraCurve curve){
        int imWidth = imageList[0].getWidth();
        int imHeight = imageList[0].getHeight();
        
        //skapa matris att lägga bilderna i
        hdr = new float[3*imWidth*imHeight];
        
        den = new float[3*imWidth*imHeight];
        
        nom = new float[3*imWidth*imHeight];

        
        //skapa viktfunktion = [0 255]
        float[] vikt = createWeights();
        
        for (int n = 0; n < imageList.length; n++){
           exposure = imageList[n].getExposureTime();
            
           //räkna tiden det tar att räkna på varje bild
           cal1 = java.util.Calendar.getInstance();
           t1 = cal1.get(java.util.Calendar.MINUTE);
           t1 = 60*t1 + cal1.get(java.util.Calendar.SECOND);
            
            //kalibrera bilden mha camera curve  = calibrated
            for(int x = 0; x < imWidth; x++){
                for(int y = 0; y < imHeight; y++){
                    //hämta färgvärdena i pixelpositionen
                    int[] color = imageList[n].getRGB(x, y);
                    
                    //hämta det kalibrerade värdet för den färgen
                    float[] response =  new float[3];
                    response = curve.getResponse(color);
                    
                    //hämta nytt viktat värde och addera till matrisen 'den' (denominator)
                    den[(x+y*imWidth)*3]   +=  vikt[color[R]];
                    den[(x+y*imWidth)*3+1] +=  vikt[color[G]];
                    den[(x+y*imWidth)*3+2] +=  vikt[color[B]];
                    
                    //addera det kalibrerade värdet, dividerat med slutartiden och addera med 'nom' (nominator)
                    nom[(x+y*imWidth)*3]   +=  vikt[color[R]] * response[R] / exposure;
                    nom[(x+y*imWidth)*3+1] +=  vikt[color[G]] * response[G] / exposure;
                    nom[(x+y*imWidth)*3+2] +=  vikt[color[B]] * response[B] / exposure;
                    
                }
            }
            cal2 = java.util.Calendar.getInstance();
            t2 = cal2.get(java.util.Calendar.MINUTE);
            t2 = 60*t2 + cal2.get(java.util.Calendar.SECOND);
            
            System.out.println("tid för uträkning= " + (t2-t1));
            System.out.println((n+1) + " bilder färdigräknade");
        }
        
        //dividera nominator med denominator = hdr
        for(int i = 0; i< 3*imWidth*imHeight; i++){
            hdr[i] = nom[i] / den[i];
        }
        System.out.println("skriver till fil");
        HDRElemPFM.writePFM(new File("C:\\Users\\Tobias\\hdr.pfm"), imWidth, imHeight, 1.0f, false, hdr);
    }
    
    private float[] createWeights(){
                
        float[] vikt = new float[256];
        vikt[0] = 0; //initialt värde
        for(int a = 1; a<20 ; a++){
            vikt[a] = (float) (vikt[a - 1] + (1.0 / 20.0));
        }
        for(int a = 20; a < (236); a++){
            vikt[a] = 1;
        }
        
        for(int a = 236; a < 256; a++){
            vikt[a] = (float) (vikt[a - 1] - (1.0 / 20.0));
        }
        vikt[255] = (float) 0.0;
        return vikt;
    }
    
}

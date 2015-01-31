/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdrelements01;

/**
 *
 * @author Tobias
 */
public class PFM {
    
    //VARIABELDEKLARATION
    float data; //pekare i c++
    int width;  //unsigned i c++
    int height; //unsigned i c++
    
    //KONSTRUKTORER
    //behöver ingen
    
    
        // public:
        /* Contructor */
        //PFMImage() : data_(0), width_(0), height_(0){}
        /* Destructor */
        //~PFMImage() { if(data_) delete [] data_;}

  
  
        /* Initialize the image buffer to a given size (resX, resY). 
        * This creates an empty image.
        * If the last (optional) parameteter is set to a non-zero
        * value the image will be initialized to zero.
        */
        //inline void Initialize(unsigned int resX, unsigned int resY, int reset = 0){
        //  if(resX * resY != width_ * height_){
        //    if(data_) delete [] data_;
        //    data_ = new float[resX*resY*3];
        //  }
        //  width_ = resX;
        //  height_ = resY;
        //}


        /* Sets the RGB values at pixel location (x,y) to the (R,G,B) pointed to by col*/
        //inline void Set(unsigned int x, unsigned int y, float *col){
        // data_[3*(y*width_ + x)]   = col[0];
        //  data_[3*(y*width_ + x)+1] = col[1];
        //  data_[3*(y*width_ + x)+2] = col[2];
        //}

        /* Sets the RGB values at pixel location (x,y) to (R,G,B) */
        //inline void Set(unsigned int x, unsigned int y, 
        //              float R, float G, float B){
        //      data_[3*(y*width_ + x)]   = R;
        //      data_[3*(y*width_ + x)+1] = G;
        //      data_[3*(y*width_ + x)+2] = B;
        //}

        /* Returns a pointer to the RGB values at pixel location (x,y)*/
        //inline float * Get(unsigned int x, unsigned int y){
        // return &(data_[3*(y*width_ + x)]);
        //}
  
        /* returns a pointer to the stored image data */
        // inline float* GetDataBuffer() {return data_;}


         /* Type cast operator that returns the image data.
          * Usage: float *p = (float*)myPFMImage;
          * p will now point to the data_ buffer
          */
          //inline operator float* () {return data_;}

         /* Read the PFM file specified by fname */
         // int Read(const char *fname);

          /* Write the image to disk using the filename fname */
          //int Write(const char *fname);

           //inline unsigned int Width()  const {return width_;}
           //inline unsigned int Height() const {return height_;}

         //private:
          //float *data_;
          //unsigned int width_;
          //unsigned int height_;

//}; 

/*----------------------------------------------------------------------------*
 * Read the PFM file specified by fname 
 *----------------------------------------------------------------------------*/
//int PFMImage::Read(const char *fname){
 // FILE *fp = fopen(fname, "rb");
 // if(!fp) return -1;
  
 // char p = fgetc(fp);
 // char f = fgetc(fp);
 // char i = fgetc(fp);
 // if (p != 'P' || (f != 'F' && f != 'f')) 
 //   return - 1;
 // int color = (f == 'F') ? 1 : 0;
 // if(!color){
 //   fclose(fp);
 //   return -1;
 // }
 // int xRes, yRes;
 // fscanf(fp, "%d %d%c", &xRes, &yRes, &i);
 // if((xRes <= 0) || (yRes <= 0)){
 //   fclose(fp);
 //     return -1;
 // }
 // Initialize(xRes, yRes);  
 // float scale;
//  fscanf(fp, "%f%c", &scale, &i);
  
 // if(scale > 0.f) {
 //   printf("Error :: reverse byte order not supported\n");
 //   fclose(fp);
 //   return -1;
  //}
    
  //for(int ii = 0; ii < height_; ii++){
  //  fread((void*)(data_ + 3*ii*width_), sizeof(float), 3 * width_, fp);
  //}
 // fclose(fp);
 // return 0;
//}

/*----------------------------------------------------------------------------*
 * Write the PFM file specified by fname 
 *----------------------------------------------------------------------------*/
//int PFMImage::Write(const char *fname){
 // FILE *fp = fopen(fname, "wb");
  //if(!fp) return -1;
  //Magic header
 // fputc('P', fp);
 // fputc('F', fp);
 // fputc(0x0a, fp);
  
 // fprintf(fp, "%d %d", width_, height_);
 // fputc(0x0a, fp);
  
 // fprintf(fp, "%f", -1.0f);
 // fputc(0x0a, fp);
  
  //Write Data
 // for(unsigned int j = 0; j <width_ * height_; j++){
  //  if((fwrite((const void*) &(data_[3*j]), sizeof(float), 3, fp)) < 3){
   //   printf("Error writePFM( %s ):: Write to disk failiure\n", fname);
    //  fclose(fp);
     // return -1;
   // }
  //} 
 // fclose(fp);
  //return 0;
//}
}



import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class MatrixDebayerTest {

	public int convolute(int[][] matrix,BufferedImage image, int xP, int yP){
		return convolute(matrix, image, xP, yP, 0);
	}
	
	public int convolute(int[][] matrix,BufferedImage image, int xP, int yP, int col){
		int sum = 0;
		int divide = 0;
		int offset = -matrix.length/2;
		for (int x=0;x<matrix.length;x++){
			for (int y=0;y<matrix[x].length;y++){
				if (matrix[x][y]!=0){
					divide += (matrix[x][y]);
					switch(col){
					case 0:
						sum += matrix[x][y] * new Color(image.getRGB(x+xP+offset, y+yP+offset)).getRed();
						break;
					case 1:
						sum += matrix[x][y] * new Color(image.getRGB(x+xP+offset, y+yP+offset)).getGreen();
						break;
					case 2:
						sum += matrix[x][y] * new Color(image.getRGB(x+xP+offset, y+yP+offset)).getBlue();
						break;
					}
				}	
			}
		}
		int v = sum/(divide != 0 ? divide : 1);
		return Math.max(Math.min(v, 255),0);
	}

	public void sharpen(String path) throws IOException{
		int[][] sharpen = {{-1,-1,-1},
				   		   {-1, 9,-1},
				   		   {-1,-1,-1}};
		
		int[][] blur = {{-1,-1,-1,-1,-1},
		   		   		{-1,1,1,1,-1},
		   		   		{-1,1,9,1,-1},
		   		   		{-1,1,1,1,-1},
		   		   		{-1,-1,-1,-1,-1}};
		
		BufferedImage srcImg = ImageIO.read(new File(path));
		BufferedImage procImg = new BufferedImage(srcImg.getWidth(),srcImg.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
		
		for(int x=2;x<srcImg.getWidth()-2;x++){
			for(int y=2;y<srcImg.getHeight()-2;y++){
				int r = convolute(blur, srcImg, x, y,0);
				int g = convolute(blur, srcImg, x, y,1);
				int b = convolute(blur, srcImg, x, y,2);
				procImg.setRGB(x, y, new Color(r,g,b).getRGB());
			}
		}
		ImageIO.write(procImg, "png", new File("sharp.png"));


	}
	
	public void createPic(String path, String out) throws IOException{
		BufferedImage srcImg = ImageIO.read(new File(path));
		BufferedImage procImg = new BufferedImage(srcImg.getWidth(),srcImg.getHeight(),BufferedImage.TYPE_INT_RGB);

		int[][] diag = {{1,0,1},
					    {0,0,0},
				        {1,0,1}};

		int[][] cross = {{0,1,0},
					     {1,0,1},
					     {0,1,0}};
		
		int[][] gvert = {{0,1,0},
			     		 {0,0,0},
			     		 {0,1,0}};
		
		int[][] ghorz = {{0,0,0},
	     		 		 {1,0,1},
	     		 		 {0,0,0}};
		
		
		
		int r=0, g=0, b=0;

		for(int x=2;x<srcImg.getWidth()-2;x++){
			for(int y=2;y<srcImg.getHeight()-2;y++){
				int col = x%2;
				int row = y%2;
				//0,0 B    1,1 R    0,1 1,0 G
				if (col+row == 0){
					// B
					r = convolute(diag, srcImg, x, y);
					g = convolute(cross, srcImg, x, y);
					b = brightness(srcImg.getRGB(x, y));
				} else if (col+row == 1){
					// G
					if (row==0){
						r=convolute(ghorz, srcImg, x, y);
						g=brightness(srcImg.getRGB(x, y));
						b=convolute(gvert, srcImg, x, y);
					} else {
						r=convolute(gvert, srcImg, x, y);
						g=brightness(srcImg.getRGB(x, y));
						b=convolute(ghorz, srcImg, x, y);
					}
				} else {
					// R
					r = brightness(srcImg.getRGB(x, y));
					g = convolute(cross, srcImg, x, y);
					b = convolute(diag, srcImg, x, y);
				}
				procImg.setRGB(x, y, new Color(r,g,b).getRGB());
			}	
		}

		ImageIO.write(procImg, "png", new File("out.png"));
	}

	private int brightness(int rgb) {
		return rgb & 0x0000FF;
	}



	public static void main(String[] args) throws IOException {
		MatrixDebayerTest h = new MatrixDebayerTest();
//		System.out.println(h.brightness(new Color(128,234,128,255).getRGB()));
		//h.createPic("/Users/andreasrettig/Documents/TFH/Paper/Auswahl/big_1235_DW8_3.jpg","d.jpg");
		h.sharpen("/Users/andreasrettig/workspace/Debayer/out_.png");
	}
}

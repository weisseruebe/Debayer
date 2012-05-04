

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class DebayerTest {

	LinkedList<Point> allPoints = new LinkedList<Point>();

	public void createPic(String path, String out) throws IOException{
		BufferedImage srcImg = ImageIO.read(new File(path));
		BufferedImage procImg = new BufferedImage(srcImg.getWidth(),srcImg.getHeight(),BufferedImage.TYPE_INT_RGB);

		//		for(int x=0;x<srcImg.getWidth()-1;x+=2){
		//			for(int y=0;y<srcImg.getHeight()-1;y+=2){
		//				//int rgb = srcImg.getRGB(x, y);
		//				int r = brightness(srcImg.getRGB(x+1, y+1));
		//				int g = (brightness(srcImg.getRGB(x+1, y)) + brightness(srcImg.getRGB(x, y+1))) /2;
		//				int b = brightness(srcImg.getRGB(x, y));
		//				//System.out.println(r+" "+g+" "+b);
		//				procImg.setRGB(x/2, y/2, new Color(r,g,b).getRGB());
		////				procImg.setRGB(x+1, y+1, new Color(r,g,b).getRGB());
		////				procImg.setRGB(x, y+1, new Color(r,g,b).getRGB());
		////				procImg.setRGB(x+1, y, new Color(r,g,b).getRGB());
		//			}	
		//		}

		int r=0, g=0, b=0;
		for(int x=2;x<srcImg.getWidth()-2;x++){
			for(int y=2;y<srcImg.getHeight()-2;y++){
				int col = x%2;
				int row = y%2;
				//0,0 B    1,1 R    0,1 1,0 G
				if (col+row == 0){
					// B
					r = (int) (0.25*(brightness(srcImg.getRGB(x+1, y-1))+brightness(srcImg.getRGB(x+1, y+1))+brightness(srcImg.getRGB(x-1, y-1))+brightness(srcImg.getRGB(x-1, y+1))));;
					g = (int) (0.25*(brightness(srcImg.getRGB(x-1, y))+brightness(srcImg.getRGB(x+1, y))+brightness(srcImg.getRGB(x, y-1))+brightness(srcImg.getRGB(x, y+1))));
					b = brightness(srcImg.getRGB(x, y));
				} else if (col+row == 1){
					// G
					if (row==0){
						r=(int) (0.5*(brightness(srcImg.getRGB(x, y-1))+brightness(srcImg.getRGB(x, y+1))));
						g=brightness(srcImg.getRGB(x, y));
						b=(int) (0.5*(brightness(srcImg.getRGB(x-1, y))+brightness(srcImg.getRGB(x+1, y))));
					} else {
						b=(int) (0.5*(brightness(srcImg.getRGB(x, y-1))+brightness(srcImg.getRGB(x, y+1))));
						g=brightness(srcImg.getRGB(x, y));
						r=(int) (0.5*(brightness(srcImg.getRGB(x-1, y))+brightness(srcImg.getRGB(x+1, y))));
					}
				} else {
					// R
					r = brightness(srcImg.getRGB(x, y));
					g = (int) (0.25*(brightness(srcImg.getRGB(x-1, y))+brightness(srcImg.getRGB(x+1, y))+brightness(srcImg.getRGB(x, y-1))+brightness(srcImg.getRGB(x, y+1))));
					b = (int) (0.25*(brightness(srcImg.getRGB(x+1, y-1))+brightness(srcImg.getRGB(x+1, y+1))+brightness(srcImg.getRGB(x-1, y-1))+brightness(srcImg.getRGB(x-1, y+1))));;
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
		DebayerTest h = new DebayerTest();
		h.createPic("/Users/andreasrettig/Documents/TFH/Paper/Auswahl/big_1235_DW8_3.jpg","d.jpg");

	}
}

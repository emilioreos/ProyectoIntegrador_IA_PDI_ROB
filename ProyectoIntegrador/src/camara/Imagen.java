package camara;

import java.awt.image.*;

public class Imagen {
	public static int BUSCAR_META=0;
	public static int BUSCAR_PUERTA=1;
	public static final int NEGRO=0xff000000;
	public static final int BLANCO=0xffffffff;
	
	private BufferedImage roja;
	private BufferedImage amarilla;
	private double[] centroRojo=new double[3],centroAmarillo=new double[3];
	
	public Imagen(BufferedImage imagen) throws Exception{
		int alto=imagen.getHeight(),ancho=imagen.getWidth();
		roja=new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_BGR);
		amarilla=new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_BGR);
		int xRojo=0,yRojo=0,xAmarillo=0,yAmarillo=0,contadorRojo=0,contadorAmarillo=0;
		for(int i=0;i<ancho;i++){
			for(int j=0;j<alto;j++){
				int color=imagen.getRGB(i, j);
				int R,G,B;
				B=color&255;
				G=(color>>8)&255;
				R=(color>>16)&255;
				double[] hsb=RGBaHSB((short)R,(short)G,(short)B);
				if(((hsb[0]>=340&&hsb[0]<=360)||(hsb[0]>=0&&hsb[0]<=20))&&hsb[1]>0.45&&hsb[2]>0.5){
					roja.setRGB(i, j, BLANCO);
					xRojo+=i;
					yRojo+=j;
					contadorRojo++;
				}else if(hsb[0]>=50&&hsb[0]<=70&&hsb[1]>0.5&&hsb[2]>0.60){
					amarilla.setRGB(i, j, BLANCO);
					xAmarillo+=i;
					yAmarillo+=j;
					contadorAmarillo++;
				}else{
					roja.setRGB(i, j, NEGRO);
					amarilla.setRGB(i, j, NEGRO);
				}
			}
		}
		centroRojo[0]=xRojo*1.0/contadorRojo;
		centroAmarillo[0]=xAmarillo*1.0/contadorAmarillo;
		centroRojo[1]=yRojo*1.0/contadorRojo;
		centroAmarillo[1]=yAmarillo*1.0/contadorAmarillo;
		centroRojo[2]=contadorRojo;
		centroAmarillo[2]=contadorAmarillo;
	}
	public BufferedImage getImagen(int tipo){
		if(tipo==BUSCAR_META){
			return roja;
		}else if(tipo==BUSCAR_PUERTA){
			return amarilla;
		}
		return null;
	}
	public double[] RGBaHSB(short R,short G, short B){
		double[] HSB=new double[3];
		short M=(short)Math.max(R, Math.max(G, B)),m=(short)Math.min(R, Math.min(G, B)),C;
		C=(short)(M-m);
		if(C==0){
			HSB[0]=0;
		}else if(M==R){
			HSB[0]=60*(((G-B)/(C*1.0))%6);
		}else if(M==G){
			HSB[0]=60*(((B-R)/(C*1.0))+2);
		}else{
			HSB[0]=60*(((R-G)/(C*1.0))+4);
		}
		HSB[1]=(C==0)?0:(C*1.0)/M;
		HSB[2]=M/255.0;//(0.3*R+0.59*G+0.11*B)/255;
		if(HSB[0]<0){
			HSB[0]=360+HSB[0];
		}
		return HSB;
	}
	public double[] getCentroide(int imagen){
		if(imagen==BUSCAR_META){
			return centroRojo;
		}else{
			return centroAmarillo;
		}
	}
}

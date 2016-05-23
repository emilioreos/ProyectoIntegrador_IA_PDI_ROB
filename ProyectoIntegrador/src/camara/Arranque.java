package camara;

import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import javax.swing.*;

import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public class Arranque {
	
	public static void main(String[] args){
		Controlador n=new Controlador();
		try {
			n.girar(1, 90);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		Cerebro c=new Cerebro();
		try{
			c.buscar();
			System.out.println("Busqueda terminada");
		}catch(Exception e){
			System.out.println("Se ha presentado una excepcion al buscar");
			e.printStackTrace();
		}*/
		/*
		JLabel jl=new JLabel();
		Camara c=new Camara(0);
		
		Controlador con=new Controlador();
		con.adelante(100);
		con.atras(100);
		con.girar(Controlador.DERECHA, 36);
		con.girar(Controlador.IZQUIERDA, 36);
		
		
		for(int i=0;i<100;i++){
			BufferedImage ima=c.getCaptura();
			Imagen imag=new Imagen(ima);
			jl.setIcon(new ImageIcon(imag.getImagen(Imagen.BUSCAR_META)));
			JOptionPane.showMessageDialog(null, jl);
			c.nuevaCptura();
			double temp=imag.getCentroide(Imagen.BUSCAR_META)[0];
			if(temp>180){
				con.girar(Controlador.DERECHA, 50);
			}else if(temp<140){
				con.girar(Controlador.IZQUIERDA, 50);
			}else if(temp>139&&temp<181){
				con.adelante(50);
			}else{
				continue;
			}
			System.out.println(temp);
		}
		
		*/
	}

}

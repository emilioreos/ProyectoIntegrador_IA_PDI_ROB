package camara;

import java.util.ArrayList;

import au.edu.jcu.v4l4j.exceptions.V4L4JException;



public class Cerebro {
	private Camara camara;
	private Controlador controlador;
	private class Contenedor{
		public double[] datos;
		public int angulo;
	}
	public Cerebro(){
		camara=new Camara(0);
		controlador=new Controlador();
	}
	private void buscarMeta() throws Exception{
		camara.nuevaCptura();
		for(;;){
			Imagen imagen=new Imagen(camara.getCaptura());
			if(imagen.getCentroide(Imagen.BUSCAR_META)[0]>130&&imagen.getCentroide(Imagen.BUSCAR_META)[0]<190){
				camara.nuevaCptura();
				break;
			}else if(imagen.getCentroide(Imagen.BUSCAR_META)[0]<160&&imagen.getCentroide(Imagen.BUSCAR_META)[0]!=Double.NaN){
				if(imagen.getCentroide(Imagen.BUSCAR_META)[0]<50){
					controlador.girar(Controlador.IZQUIERDA, 30);
				}else if(imagen.getCentroide(Imagen.BUSCAR_META)[0]<100){
					controlador.girar(Controlador.IZQUIERDA, 20);
				}else{
					controlador.girar(Controlador.IZQUIERDA, 10);
				}
			}else if(imagen.getCentroide(Imagen.BUSCAR_META)[0]>160&&imagen.getCentroide(Imagen.BUSCAR_META)[0]!=Double.NaN){
				if(imagen.getCentroide(Imagen.BUSCAR_META)[0]>270){
					controlador.girar(Controlador.DERECHA, 30);
				}else if(imagen.getCentroide(Imagen.BUSCAR_META)[0]>220){
					controlador.girar(Controlador.DERECHA, 20);
				}else{ 
					controlador.girar(Controlador.DERECHA, 10);
				}
			}else{
				controlador.girar(Controlador.DERECHA, 45);
			}
			camara.nuevaCptura();
		}
	}
	private ArrayList<Contenedor> obtenerVesinos() throws Exception{
		Imagen imagen;
		ArrayList<Contenedor> lista = new ArrayList<Contenedor>();
		for(int i=0;i<360;){
			imagen=new Imagen(camara.getCaptura());
			if(imagen.getCentroide(Imagen.BUSCAR_PUERTA)[0]!=Double.NaN&&imagen.getCentroide(Imagen.BUSCAR_META)[0]>130&&imagen.getCentroide(Imagen.BUSCAR_META)[0]<190){
				Contenedor c=new Contenedor();
				c.datos=imagen.getCentroide(Imagen.BUSCAR_PUERTA);
				c.angulo=i;
				lista.add(c);
				
			}else{
				
			}
			controlador.girar(Controlador.DERECHA, 10);
			camara.nuevaCptura();
		}
		return lista;
	}
	private Contenedor mejorAngulo(ArrayList<Contenedor> lista){
		Contenedor mejor=new Contenedor();
		mejor.angulo=370;
		mejor.datos=null;
		for(int i=0;i<lista.size();i++){
			Contenedor c=lista.get(i);
			if(c.angulo<mejor.angulo){
				mejor.angulo=c.angulo;
				mejor.datos=c.datos;
			}else if(c.angulo>=180){
				if((-(360-c.angulo))<mejor.angulo){
					mejor.angulo=c.angulo;
					mejor.datos=c.datos;
				}
			}
		}
		return mejor;
	}
	private void buscarMeta(int direccion) throws Exception{
		//hay que implementar
		camara.nuevaCptura();
		for(int i=0;i<360;i+=10){
			Imagen imagen=new Imagen(camara.getCaptura());
			if(imagen.getCentroide(Imagen.BUSCAR_META)[0]>100&&imagen.getCentroide(Imagen.BUSCAR_META)[0]<220){
				camara.nuevaCptura();
				break;
			}else{
				controlador.girar(direccion, 10);
			}
			camara.nuevaCptura();
		}
	}
	private void irALugar(int lugar) throws Exception{
		//hay que implementarlo
		System.out.print("Ir a ");
		int margen;
		boolean sentido;
		if(lugar==Imagen.BUSCAR_META){
			margen=150;
			sentido=true;
			System.out.println("Meta");
		}else{
			margen=100;
			sentido=false;
			System.out.println("Puerta");
		}
		camara.nuevaCptura();
		for(;;){
			Imagen imagen=new Imagen(camara.getCaptura());
			if(imagen.getCentroide(lugar)[0]>100&&imagen.getCentroide(lugar)[0]<220){

				System.out.println("Centrado");
				if(sentido&&imagen.getCentroide(lugar)[1]>margen){
					camara.nuevaCptura();
					System.out.println("Adios");
					break;
				}else if(!sentido&&imagen.getCentroide(lugar)[1]<margen){
					camara.nuevaCptura();
					System.out.println("adios");
					break;
				}else{
					System.out.println("casi llegamos");
					controlador.adelante(10);
				}
			}else if(imagen.getCentroide(lugar)[0]>160){
				controlador.girar(Controlador.DERECHA, 10);
			}else{
				controlador.girar(Controlador.IZQUIERDA, 10);
				
			}
			camara.nuevaCptura();
		}
	}
	public void buscar() throws Exception{
		buscarMeta();
		Imagen imagen=new Imagen(camara.getCaptura());
		if(imagen.getCentroide(Imagen.BUSCAR_META)[1]>100){
			//controlador.adelante((int)(320*240/imagen.getCentroide(Imagen.BUSCAR_META)[2]));
			irALugar(Imagen.BUSCAR_META);
		}else{
			camara.nuevaCptura();
			while(true){
				ArrayList<Contenedor> lista=obtenerVesinos();
				Contenedor angulo=mejorAngulo(lista);
				if(angulo.angulo==370){
					return;
				}else if(angulo.angulo<180){
					controlador.girar(Controlador.DERECHA, angulo.angulo);
				}else{
					controlador.girar(Controlador.IZQUIERDA, -(360-angulo.angulo));
				}
				irALugar(Imagen.BUSCAR_PUERTA);
				if(angulo.angulo<180){
					controlador.girar(Controlador.DERECHA, angulo.angulo);
					buscarMeta(Controlador.DERECHA);
				}else{
					controlador.girar(Controlador.IZQUIERDA, -(360-angulo.angulo));
					buscarMeta(Controlador.IZQUIERDA);
				}
				imagen=new Imagen(camara.getCaptura());
				if(imagen.getCentroide(Imagen.BUSCAR_META)[1]>120){
					irALugar(Imagen.BUSCAR_META);
					return;
				}
				camara.nuevaCptura();
			}
		}
		controlador.cerrar();
	}
}

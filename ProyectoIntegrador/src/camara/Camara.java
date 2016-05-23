package camara;
import au.edu.jcu.v4l4j.*;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

import java.awt.image.BufferedImage;

public class Camara implements CaptureCallback{
	private static int ancho=320,alto=240,std=V4L4JConstants.STANDARD_WEBCAM,canal=0;
	private static String dev="/dev/video";
	
	private FrameGrabber frame=null;
	private VideoDevice videoDev=null;
	private volatile BufferedImage captura=null;
	private volatile boolean capturando=false;
	
	public Camara(int dispositivo){
		try {
			videoDev = new VideoDevice(dev+dispositivo);
			frame=videoDev.getRGBFrameGrabber(ancho, alto, canal, std);
			ancho=frame.getWidth();
			alto=frame.getHeight();
			frame.setCaptureCallback(this);
			
		} catch (V4L4JException e) {
			// TODO Auto-generated catch block
			this.exceptionReceived(e);
			
		}
		
	}
	@Override
	public void nextFrame(VideoFrame frame) {
		// TODO Auto-generated method stub
		BufferedImage bfi=frame.getBufferedImage();
		this.setImagen(bfi);
		this.frame.stopCapture();
		this.capturando=false;
	}
	
	@Override
	public void exceptionReceived(V4L4JException e) {
		// TODO Auto-generated method stub
		System.out.println("Se ha obtenido una excepcion con la camara");
		if(frame!=null){
			frame.stopCapture();
			videoDev.releaseFrameGrabber();
			videoDev.release();
		}
	}
	private synchronized void setImagen(BufferedImage im){
		captura=im;
	}
	public void nuevaCptura() throws V4L4JException{
		if(!this.capturando){
			this.capturando=true;
			frame.startCapture();
		}
	}
	public BufferedImage getCaptura() throws InterruptedException{
		BufferedImage ima=null;
		for(;captura==null;){
			Thread.sleep(5);
		}
		ima=captura;
		captura=null;
		return ima;
	}
}

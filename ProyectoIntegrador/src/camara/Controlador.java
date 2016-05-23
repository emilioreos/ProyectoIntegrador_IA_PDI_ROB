package camara;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;


public class Controlador {
	public static final int DERECHA=0;
	public static final int IZQUIERDA=1;
	
	private GpioController gpio=GpioFactory.getInstance();
	private GpioPinDigitalOutput derecha1;
	private GpioPinDigitalOutput derecha2;
	private GpioPinDigitalOutput izquierda1;
	private GpioPinDigitalOutput izquierda2;
	
	public Controlador(){
		derecha1=gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00,PinState.LOW);
		derecha2=gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01,PinState.LOW);
		izquierda1=gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02,PinState.LOW);
		izquierda2=gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03,PinState.LOW);
		derecha1.setShutdownOptions(true,PinState.LOW);
		derecha2.setShutdownOptions(true,PinState.LOW);
		izquierda1.setShutdownOptions(true,PinState.LOW);
		izquierda2.setShutdownOptions(true,PinState.LOW);
	}
	public void cerrar(){
		gpio.shutdown();
	}
	public void adelante(int desplazamiento) throws InterruptedException{
		derecha1.setState(false);
		izquierda1.setState(false);
		derecha2.setState(false);
		izquierda2.setState(false);
		derecha1.high();
		izquierda1.high();
		////////////////////////////////
		Thread.sleep(desplazamiento*60);
		derecha1.low();
		izquierda1.low();
	}
	public void atras(int desplazamiento) throws InterruptedException{
		derecha1.setState(false);
		izquierda1.setState(false);
		derecha2.setState(false);
		izquierda2.setState(false);
		derecha2.high();
		izquierda2.high();
		Thread.sleep(desplazamiento*60);
		derecha2.low();
		izquierda2.low();
	}
	public void girar(int direccion,int angulo) throws InterruptedException{
		derecha1.setState(false);
		izquierda1.setState(false);
		derecha2.setState(false);
		izquierda2.setState(false);
		
		//GpioPinDigitalOutput t1,t2,t3,t4;
		/*
		if(angulo<0){
			t1=derecha1;
			t2=derecha2;
			t3=izquierda1;
			t4=izquierda2;
			angulo=-angulo;
		}else{

			t3=derecha1;
			t4=derecha2;
			t1=izquierda1;
			t2=izquierda2;
		}*/
		if(direccion==DERECHA){
			derecha2.high();
			izquierda1.high();
			Thread.sleep(angulo*9);
			derecha2.low();
			izquierda1.low();
		}else if(direccion==IZQUIERDA){
			derecha1.high();
			izquierda2.high();
			Thread.sleep(angulo*10);
			derecha1.low();
			izquierda2.low();
		}
		
	}
	/*
	public static void main(String[] args) throws InterruptedException {
        
        System.out.println("<--Pi4J--> GPIO Blink Example ... started.");
        
        // create gpio controller
        
        // provision gpio pin #01 & #03 as an output pins and blink

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {
                @Override
                public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                    // when button is pressed, speed up the blink rate on LED #2
                    if(event.getState().isHigh()){
                      led2.blink(200);
                    }                        
                    else{
                      led2.blink(1000);
                    }
                }
            });

        // continuously blink the led every 1/2 second for 15 seconds
        led1.blink(500, 15000);

        // continuously blink the led every 1 second 
        led2.blink(1000);
        
        System.out.println(" ... the LED will continue blinking until the program is terminated.");
        System.out.println(" ... PRESS <CTRL-C> TO STOP THE PROGRAM.");
        
        // keep program running until user aborts (CTRL-C)
        while(true) {
            Thread.sleep(500);
        }
        
        // stop all GPIO activity/threads
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }*/
}

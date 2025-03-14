import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/*
 * AUthor:Julius
 * 
 * estimates pi with one pausable thread
 * 
*/
public class PiEstimator{

static Double pi=0.0;// the estimated value of pi
static int n=0;// the number of trials 

public static class PIThread extends Thread {
	Double pi=0.0;// value of pi estimated by this thread
	int n=100000;// number of points to be generated to calculate pi
	volatile boolean running=true;
	double c=0.0;// c for count
	//its a double so pi is a double not an int when it is calculated
	
	//constructor specifying the number of points to generate
	public PIThread(int n) {
	this.n=n;
	}
	// run method estimating pi and repeating endlessly unless the program is ended 
	//also can be paused and resumed
	public void run() {
		System.out.println("thread started");
		running=true;
		while(true){	
		while (!running) {
			try {
			System.out.println("Paused");
			synchronized(this){
			wait();
			}
			} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("error caught");
				e.printStackTrace();
			}
		}
		while(running){
		for(int i=0;i<=n;i++){
		double x=(Math.random());
		double y=(Math.random());
		//System.out.println("x"+x);
		//System.out.println("y"+y);
		double r=x*x+y*y;
		//System.out.println("r"+r);
		if((r)<=1){
			c++;
		}else{
			r=0.0;
			//do nothing
		}
		}
		pi=(4*c)/n;//estimates pi
		//System.out.println("PI is estimated as"+pi);
		send(pi);
		c=0;
		n+=100000;// more points to make it more accurate over time
		}
	}
		}
		
	}




// updates the global variable pi with a new estimate
//also increments the number of trials to keep the display accurate
public static synchronized void send(Double piestimate){

	PiEstimator.n++;

	if(pi==0){
		pi=piestimate;
	}else{
		pi=(pi+piestimate)/2;
	}
}



//main method
public static void main(String[] args) {  
		// creates display on screen
	    JFrame f=new JFrame("Button Example");  
	    JButton b=new JButton("run/pause button");  
	    JLabel example = new JLabel("Actual value of pi: " + Double.toString(Math.PI));
		JLabel estimate = new JLabel("pi estimate:");
		JLabel trialsDisplay = new JLabel("Nubmer of trials:");

		f.add(trialsDisplay);
		f.add(estimate);
	    f.add(example);
	    f.add(b);  
	    f.setSize(300,300);  
	    f.setLayout(new GridLayout(4, 1));  
	    f.setVisible(true);   

		// creates and starrts thread to estimate pi
		PIThread thread=new PIThread(800000);
		thread.start();
		
//mouseLIstener to make the button pause 
		b.addMouseListener(new MouseListener() {


			public void mouseExited(MouseEvent m){
				// do nothing

			}
			public void mouseReleased(MouseEvent m){
				// do nothing
			 }
			//pauses and unpauses the thread 
			 public void mouseClicked(MouseEvent m){

				//PIThread thread = new PIThread(PiEstimator.n+1000000);
				
				if(thread.running){
					thread.running=false;
					//System.out.println("paused running");
				}else{
					synchronized(thread){
						thread.running=true;
						thread.notify();
						//System.out.println("restarted running");
						//thread.start();
					}
				}
				/* 
				try {
			thread.join();
			} catch (Exception e) {
			System.out.println(e.getStackTrace());
			System.out.println("clicked the button");
		}0
		*/
		
		estimate.setText("pi estimate:"+ pi.toString());
		}// end mouseClicked
			public void mousePressed(MouseEvent m){
			// do nothing
			 }
			 //also updates screen
			public void mouseEntered(MouseEvent m){
				// updates the screen
				estimate.setText("pi estimate:"+ pi.toString());
				trialsDisplay.setText("number of trials: "+n);
			}




		}
		);
		System.out.println("pi: "+pi);

		//constantly updates the screen
		while(true){
			synchronized(thread){
			estimate.setText("pi estimate:"+ pi.toString());
			trialsDisplay.setText("number of trials: "+n);
			}
			f.revalidate();
			f.repaint();
		}

	}  
}

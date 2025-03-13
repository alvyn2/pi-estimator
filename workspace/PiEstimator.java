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
 * estimates pi with threads
 * 
*/
public class PiEstimator{
//the following code is just to jog your memory about how labels and buttons work!
//implement your Pi Estimator as described in the project. You may do it all in main below or you 
//may implement additional functions if you feel it necessary.
static Double pi=0.0;
static int n=0;
int trials;
public static class PIThread extends Thread {
	Double pi=0.0;
	int n;
	volatile boolean running=true;
	double c=0.0;// c for count double so pi is a double not an int when it is calculated
	public PIThread(int n) {
	this.n=n;
	}
	public void run() {
		System.out.println("thread started");
		while (!running) {
			try {
			System.out.println("Paused!");
			wait();
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
		// need to make random actually include one somehow
		if((r)<=1){
			c++;
		}else{
			r=0.0;
			//do nothing
		}
		}
		pi=(4*c)/n;
		System.out.println("PI is estimated as"+pi);
		send(pi);
		c=0;
		n+=1000;
		}
		System.out.println("while loop ended");
		}
		
	}





public static synchronized void send(Double piestimate){

		PiEstimator.n++;

	if(pi==0){
		pi=piestimate;
	}else{
		pi=(pi+piestimate)/2;
	}
}

public static void main(String[] args) {  
	    JFrame f=new JFrame("Button Example");  
	    JButton b=new JButton("run/pause button");  
	    JLabel example = new JLabel(Double.toString(Math.PI));
		JLabel estimate = new JLabel("pi estimate:");
		JLabel trialsDisplay = new JLabel("Nubmer of trials:");

		f.add(trialsDisplay);
		f.add(estimate);
	    f.add(example);
	    f.add(b);  
	    f.setSize(300,300);  
	    f.setLayout(new GridLayout(4, 1));  
	    f.setVisible(true);   

		
		PIThread thread=new PIThread(800000);
		thread.start();
		
//mouseLIstener to make the button work
		b.addMouseListener(new MouseListener() {


			public void mouseExited(MouseEvent m){
				// do nothing

			}
			public void mouseReleased(MouseEvent m){

			 }
			//pauses and unpauses the thread estimating pi
			 public void mouseClicked(MouseEvent m){

				//PIThread thread = new PIThread(PiEstimator.n+1000000);
				
				if(thread.running){
					thread.running=false;
					System.out.println("paused running");
				}else{
					synchronized(thread){
						thread.running=true;
						thread.notify();
						System.out.println("restarted running");
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
				// update
				estimate.setText("pi estimate:"+ pi.toString());
				trialsDisplay.setText("number of trials: "+n);
			}




		}

		);
		System.out.println("pi: "+pi);

		while(true){
			estimate.setText("pi estimate:"+ pi.toString());
			trialsDisplay.setText("number of trials: "+n);
			f.revalidate();
			f.repaint();
		}

	}  
}

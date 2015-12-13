import java.util.concurrent.*;
import java.util.Scanner;
public class SB extends Thread {
	
	public static Semaphore customers = new Semaphore(0) ; 
    public static Semaphore barber = new Semaphore(0) ;
    public static Semaphore accessSeats = new Semaphore(1) ; 
    
    public static final int Chairs = 3 ;
    
    public static int numberOfFreeSeats = Chairs ;
    
 class Customer extends Thread {
	 int id;
	 boolean notCut = true ;
	 
	 
	 public Customer(int i) {
		 id = i ;
	 }
	 public void run() {   
		while (notCut) { 
		   try {
			accessSeats.acquire();  
				if (numberOfFreeSeats > 0) { 
					System.out.println("Customer " + this.id + " just sat down.");
        numberOfFreeSeats--;  
        customers.release();  
        accessSeats.release();  
        try {
	barber.acquire(); 
        notCut = false;  
        this.get_haircut(); 
        } catch (InterruptedException ex) {}
      }   
      else  {  
        System.out.println("There are no free Seats. Customer " + this.id + " has Left the barbershop.");
        accessSeats.release();  
        notCut=false; 
      }
     }
      catch (InterruptedException ex) {}
    }
  }

  /* For hair-cut */
  
  public void get_haircut(){
    System.out.println("Customer " + this.id + " is getting his hair cut");
    try {
    sleep(5050);
    } catch (InterruptedException ex) {}
  }
 }
 
 /* THE BARBER THREAD */


class Barber extends Thread {
  
  public Barber() {}
  
  public void run() {
    while(true) {  
      try {
      customers.acquire(); 
      accessSeats.release(); 
        numberOfFreeSeats++; 
      barber.release();  
      accessSeats.release(); 
      this.cutHair();  
    } catch (InterruptedException ex) {}
    }
  }

    
   
  public void cutHair(){
    System.out.println("The Barber is Cutting hair");
    try {
      sleep(5000);
    } catch (InterruptedException ex){ }
  }
}  



///////////////////////////////////////////////
// Main method start:
public static void main(String arg[]) {
	SB barberShop = new SB();  
	barberShop.start(); //
}

	public void run() {    //
	 Barber bilu = new Barber(); // "bilu" is barber in barber Shop
	 bilu.start();    
	

	
 
	System.out.println("\nEnter the Average Number of Customers on Daily Basis:");
        Scanner in = new Scanner(System.in) ;
        int noOfCustomer = in.nextInt();
		for(int i = 1; i <= noOfCustomer; i++ ) {
			Customer comeingCustomer = new Customer(i) ;
			comeingCustomer.start() ;
			  try {
				sleep(3000) ;
			  }catch( InterruptedException ex ) {} ;
		 }
  System.out.println("\n\nNo Customer Remaining...:It's time to close SHOP:\n\n") ;
  System.exit(0) ;
}
}

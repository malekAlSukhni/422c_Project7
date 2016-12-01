/* <ClientObserver.java>
 * EE422C Project 7 submission by
 * Robert Bolt
 * rob329
 * 16465
 * Malek Al Sukhni
 * mha664
 * 16470
 * Slip days used: 1
 * Fall 2016
 */
package assignment7;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Observable;
import java.util.Observer;

public class ClientObserver extends PrintWriter implements Observer {
	
	public ClientObserver(OutputStream out) {
		super(out);
	}
	@Override
	public void update(Observable o, Object arg) {
		this.println(arg); //writer.println(arg);
		this.flush(); //writer.flush();
	}

}

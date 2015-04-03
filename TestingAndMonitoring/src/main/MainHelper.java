package main;

import java.security.SecureRandom;

import sl.intertrust.annotations.Monitor;
import sl.intertrust.annotations.MonitorOnly;
import sl.intertrust.annotations.Testing;

@Testing(reset="resetMe", ready="isItReady")
public class MainHelper {
	
	SecureRandom rndgen;
	
	MainHelper() {
		rndgen = new SecureRandom();
	}
	
	@MonitorOnly(mask=1)
	public void f() {};
	@Monitor
	public void g() {};
	
	public void resetMe() {}
	
	public Boolean isItReady() {
		return (rndgen.nextFloat() < 1.2); // with 0.2 there are many resets, and occasionally also some maxretry/aborts
	}
	
	
}

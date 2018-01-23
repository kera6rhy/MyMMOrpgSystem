package main;

import apptemplate.Client;

public class ClientAdapter extends Client{
	public static final int PORT = 50020;
	
	public ClientAdapter(String HOST){
		super(HOST,PORT);
		
	}
	@Override 
	public void update(){
		
		
	}
	
	
	@Override
	public void talk()throws Exception{
		
	}
}




package apptemplate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;




public abstract class Server extends Thread{
	ArrayList<SocketData> socketList = new ArrayList<SocketData>(); 
	ArrayList<SocketData> addSocketList = new ArrayList<SocketData>(); 
	ArrayList<SocketData> lostSocketList = new ArrayList<SocketData>(); 
	
	private int myPort;

	public Server(int m){
		myPort = m;
		ServerThread st = new ServerThread();
		st.start();
		this.start();
		
	}
	
	class ServerThread extends Thread{
		@Override public void run(){
			while(true){
				try{
					ServerSocket ss = new ServerSocket(myPort);
					while(true){
						try{
							Socket sc = ss.accept();
							SocketData sd = new SocketData(sc);
							sd.lastConnectTime = System.currentTimeMillis();
							addSocketList.add(sd);  
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}catch(Exception r){
					r.printStackTrace();
				}
				System.out.println("death Server");;
			}
		}
	}
	
	
	public void run(){
		int t = 0;
		while(true){
			t++;
			if(t%3000==0){
				//System.out.println("ok");
			}
			int count = 0;
			long nowTime = System.currentTimeMillis();
			for(SocketData sd: socketList){
			//	System.out.println("a");
				if(nowTime - sd.lastConnectTime >10000){

					lostSocketList.add(sd);
					System.out.println("connecting lost");
				}
				try{
					if(sd.sc.getInputStream().available() >= Integer.SIZE/8){
						talk(sd);
						sd.lastConnectTime = nowTime;
						count++;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}

			//System.out.println("b");
			int tmp = 0;
			for(SocketData sd: lostSocketList){
				System.out.print(socketList.size()+" -> ");
				socketList.remove(sd);
				System.out.println(socketList.size()+" ");
				tmp = 1;
			}
			if(tmp==1){
				lostSocketList.clear();
			}
			tmp=0;
			for(SocketData sd: addSocketList){
				System.out.print(socketList.size()+" -> ");
				socketList.add(sd);
				System.out.println(socketList.size()+" ");
				tmp = 1;
			}
			if(tmp==1){
				addSocketList.clear();
			}
			
			if(count == 0){
				try{
					Thread.sleep(1);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			}
	}
	
	abstract public void talk(SocketData sd)throws Exception;

	public class SocketData{
		private PrintWriter pw;
		private BufferedReader br;
		private Socket sc;
		private InputStream in;
		private OutputStream os;
		long lastConnectTime = 0;
		boolean readHeaderFlag = false;
		public int readState = 0;
		int datasize = 0;
		public int switchNum1;
		public int switchNum2;
		
		public SocketData(Socket sc){
			this.sc = sc;
			
			try{
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sc.getOutputStream())));
				br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
				in = sc.getInputStream();
				os = sc.getOutputStream();
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}

		public void printByte(byte b[]){
/*
			for(int i=0;i<b.length;i++){
				System.out.print(b[i]+" ");
			}
			System.out.println("");*/
			try{
				os.write(b);
			}catch(IOException e){
				e.printStackTrace();
			}
			
		}
		
		public void flushByte(){
			try{
				os.flush();
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		public void println(String str){
			//System.out.println("println : "+str);;
			pw.println(str);
		}
		public void flush(){
			pw.flush();
		}
		/*
		public String readLine() throws Exception{
			String str = null;
			str = br.readLine();
			if(str == null){
				throw new NotConnectingException();
			}
			return str;
		}*/
		public MyByteBuffer readByte()throws Exception{

			if(readHeaderFlag == false){
				if(in.available() < Integer.SIZE/8){
					//System.out.println("1");
					return null;
				}
				readHeaderFlag = true;
				
				byte tmp[] = new byte[Integer.SIZE/8];
	            in.read(tmp, 0, Integer.SIZE/8);
	            datasize =0;
	            int tmpa = 1;
	
	            ByteBuffer buf = ByteBuffer.wrap(tmp);
	            buf.order(ByteOrder.BIG_ENDIAN);
	            datasize = buf.getInt();
	            datasize -= Integer.SIZE/8;
			}
            
            
			if(in.available() < datasize){
				//System.out.println("2");
				return null;
			}
 				
 			MyByteBuffer bb = new MyByteBuffer();
             byte[] data = new byte[datasize];
             in.read(data, 0, datasize);
             bb.setArray(data);
             for(int i=0; i<data.length;i++){
            	//System.out.print(" "+ data[i]);
             }	
           //  System.out.println("");
 			readHeaderFlag = false;
             return bb;
		}
	}

	static class NotConnectingException extends Exception{
		public NotConnectingException(){
			super("My Not Connecting Exception");
		}
	}
}

package apptemplate;

import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.*;



public abstract class Client extends Thread{
	private String host;
	private String myIP;
	private int myPort;
	private BufferedReader br;
	private PrintWriter pw;
	protected OutputStream os;
	private InputStream in;
	private FrameRate fr;
	protected boolean talkFlag;
	private boolean roopFlag;


	public Client(String h, int i ){
		host = h;
		myPort = i;
		roopFlag = true;

		fr = new FrameRate();
		this.start();
	}

	public void println(String str){
		System.out.println("println : "+str);
		pw.println(str);
	}

	public void flush(){
		pw.flush();
	}

	public void printByte(byte b[]){
		try{
			os.write(b);
			/*System.out.print(b.length+"byte ");
			for(int i=0;i<b.length;i++){
				System.out.print(" "+b[i]);
			}
			System.out.println("");*/
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

	public String readLine(){
		String str = null;
		try{
			str = br.readLine();
		}catch(Exception e){
			e.printStackTrace();
		}
		return str;
	}
	public MyByteBuffer readByte()throws Exception{
		MyByteBuffer bb = new MyByteBuffer();
		while(true){
			if(in.available() >= Integer.SIZE/8)break;
			try{
				Thread.sleep(1);
			}finally{
			}
		}
		byte tmp[] = new byte[Integer.SIZE/8];
        in.read(tmp, 0, Integer.SIZE/8);
        int datasize =0;
        int tmpa = 1;

        ByteBuffer buf = ByteBuffer.wrap(tmp);
        buf.order(ByteOrder.BIG_ENDIAN);
        datasize = buf.getInt();

        datasize -= Integer.SIZE/8;
        /*
		for(int i=0;i<Integer.SIZE/8;i++){
			datasize+=(char)(tmp[Integer.SIZE/8-i-1] * tmpa);
			tmpa *= 0x100;
		}*/
		for(int i=0;i<4;i++){
			//System.out.print(tmp[i] + " ");
		}
		//System.out.println("");;
		//System.out.println(""+datasize);;
         byte[] data = new byte[datasize];
         while(true){
				if(in.available() >= datasize)break;
				try{
					Thread.sleep(1);
				}finally{
				}
			}
         in.read(data, 0, datasize);
         bb.setArray(data);
         return bb;
	}

	public void roopEnd(){
		roopFlag = false;
	}

	public void run(){
		Socket sc = null;

		System.out.println("1");
		try{
			sc = new Socket(host, myPort);
			System.out.println("2");
			br = new BufferedReader(new InputStreamReader(sc.getInputStream()));
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sc.getOutputStream())));
			os = sc.getOutputStream();
			in = sc.getInputStream();

			while(roopFlag){
				fr.update();
				update();
				if(talkFlag == false){
					try{
						Thread.sleep(1);
					}catch(Exception e){
						e.printStackTrace();
					}
					continue;
				}
				talk();
				talkFlag = false;//トークは１ループのみにさせる
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				br.close();
				pw.close();
				sc.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		System.out.println("スレッドが終了しました");
	}
	abstract public void update();
	abstract public void talk()throws Exception;
}
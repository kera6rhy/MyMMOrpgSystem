package apptemplate;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

public class MyByteBuffer {
	private ArrayList<DataPart> list = new ArrayList<DataPart>();
	private int datasize = Integer.SIZE/8;
	private int in;
	private ByteBuffer buf;
	private int dataCount = 0;
	private boolean lock = false;
	
	/*
	public void copyFrom(MyByteBuffer a){
		waitLock();
		list.clear();
		datasize = a.datasize;
		in = a.in;
		dataCount = a.dataCount;
		//lock = a.lock;
		lock = true;
		for(DataPart dp :a.list){
			list.add(dp);
		}
		lock = false;
	}*/
	private void waitLock(){
		while(lock){
			try{
				Thread.sleep(1);
			}
			catch(Exception e){
				
			}
		}
	}
	public int getListSize(){
		return list.size();
	}
	public int getDataCount(){
		return dataCount;
	}
	public int getDataSize(){
		return datasize;
	}
	public MyByteBuffer putInt(int i){
		//waitLock();
		list.add(new DataPart(i));
		datasize += Integer.SIZE/8;
		dataCount++;
		return this;
	}
	public MyByteBuffer putDouble(double d){
		//waitLock();
		list.add(new DataPart(d));
		datasize += Double.SIZE/8;
		dataCount++;
		return this;
	}
	public MyByteBuffer putBoolean(boolean b){

		list.add(new DataPart(b));
		datasize += 1;
		dataCount++;
		return this;
	}
	
	public MyByteBuffer putString(String s){
		//waitLock();
		list.add(new DataPart(s));
		datasize += s.length() * Character.SIZE/8;
		datasize += Integer.SIZE/8;
		dataCount++;
		return this;
	}
	
	public void clear(){
		//waitLock();
		list.clear();
		datasize = Integer.SIZE/8;
		dataCount = 0;
	}
	
	public void setArray(byte[] a){
		in = 0;

        buf = ByteBuffer.wrap(a);
        buf.order(ByteOrder.BIG_ENDIAN);
	}
	
	public int getInt(){
		int ret = buf.getInt(in);
		in += Integer.SIZE/8;
		return ret;
	}
	public double getDouble(){
		double ret = buf.getDouble(in);
		in += Double.SIZE/8;
		return ret;
	}
	public String getString(){
		StringBuilder sb = new StringBuilder();
		int num = buf.getInt(in);
		in += Integer.SIZE/8;
		for(int i=0;i<num;i++){
			sb.append(buf.getChar(in));
			in += Character.SIZE/8;
		}
		return sb.toString();
	}
	
	public boolean getBoolean(){
		boolean ret = true;
		if(buf.get(in) == 0){
			ret = false;
		}
		in += 1;
		return ret;
	}
	public byte[] getArray(){
		ByteBuffer buf = ByteBuffer.allocate(datasize);
		//System.out.print(datasize+" ");
		buf.putInt(datasize);
		waitLock();
		lock = true;
		for(DataPart dp: list){
			switch(dp.dt){
			case INT:
				buf.putInt(dp.i);
				break;
			case DOUBLE:
				buf.putDouble(dp.d);
				break;
			case STRING:
				buf.putInt( dp.str.length() );
				for(int i=0; i<dp.str.length();i++){
					buf.putChar(dp.str.charAt(i));
				}
				break;
			case BOOLEAN:
				byte tb= 1;
				if(dp.b == false){
					tb =0;
				}
				buf.put(tb);
				break;
			}
		}
		lock = false;
		return buf.array();
	}
	class DataPart{
		DataType dt = null;
		int i;
		double d;
		String str;
		boolean b;
		
		private DataPart(){
			
		}
		DataPart(int a){
			i = a;
			dt = DataType.INT;
		}
		DataPart(double a){
			d = a;
			dt = DataType.DOUBLE;
		}
		DataPart(String a){
			str = a;
			dt = DataType.STRING;
		}
		DataPart(boolean a){
			b = a;
			dt = DataType.BOOLEAN;
		}
	}
	enum DataType{
		INT,DOUBLE,STRING,BOOLEAN
	}
}

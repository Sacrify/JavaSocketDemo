import java.net.*;
import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

import sun.misc.*;

public class SocketDemo {

	static LinkedBlockingQueue<message> msgQueue = new LinkedBlockingQueue<message>();
	static Socket mySocket = null;
	static OutputStream myWriter = null;
	static InputStream myReader = null;
	static boolean myQuitSignal = false;
	static int myRecvCount = 5120;
	static byte [] myRecv = new byte [myRecvCount];
	static int myRecvIndicator = 0;

	static BASE64Decoder decoder = new BASE64Decoder();
	
	public static void clearBuffer() {
		for (int i = 0; i < myRecvCount; i++){
			myRecv[i] = 0;
		}
		myRecvIndicator = 0;
	}
	
	
	public static int searchForHead(int from){
		for (int i = from; i < myRecvIndicator - 4; i++){
			if (myRecv[i] == '$' && myRecv[i+1] == '@' && 
					myRecv[i + 2] == '$' && myRecv[i + 3] == '@')
				return i;
		}
		
		return -1;
	}
	
	public static int toIntFromByteBigEnding(byte x1, byte x2, byte x3, byte x4){
		return (x4 << 24) & 0xFF000000 | 
				(x3 << 16) & 0x00FF0000 |
				(x2 << 8) & 0x0000FF00 |
				(x1 << 0) & 0x000000FF;
	}
	
	public static int toIntFromByte(byte x1, byte x2, byte x3, byte x4){
		return (x1 << 24) & 0xFF000000 | 
				(x2 << 16) & 0x00FF0000 |
				(x3 << 8) & 0x0000FF00 |
				(x4 << 0) & 0x000000FF;
	}
	
	public static int reorder(int x){
		byte x1 = (byte) ((x >> 0) & 0x000000FF);
		byte x2 = (byte) ((x >> 8) & 0x000000FF);
		byte x3 = (byte) ((x >> 16) & 0x000000FF);
		byte x4 = (byte) ((x >> 24) & 0x000000FF);
		
		return toIntFromByte(x1, x2, x3, x4);
	}
	
	public static void processData() {
		System.out.println("processData");
		int headIndex = -1;
		
		while ((headIndex = searchForHead(0)) != -1){

			System.out.println(headIndex);
			
			//if (headIndex == -1) return;
			if (myRecvIndicator < 6) return;
			
			int msgTotalCount = 0;
			int msgContentCnt = 
					(myRecv[headIndex + 4] << 8) & 0xFF00 |
					(myRecv[headIndex + 5] << 0) & 0x00FF;
			System.out.println(msgContentCnt);
			msgTotalCount = 6 + msgContentCnt;
			
			if (msgTotalCount > myRecvIndicator) return;
			
			int contentIndex = headIndex + 6;
			
			for (int i = 0; i < msgTotalCount; i++){
				System.out.println(String.format("%02x", (myRecv[headIndex + i] & 0xFF)));
			}
			
			// Move Buffer
			System.out.println("myRecvIndicator" + myRecvIndicator);
			System.out.println("msgTotalCount" + msgTotalCount);
			
			
			for (int i = 0; i < myRecvCount - msgTotalCount; i++){
				myRecv[i] = myRecv[msgTotalCount + i];
			}
			
			myRecvIndicator -= msgTotalCount;
			for (int i = myRecvIndicator; i < myRecvCount; i++){
				myRecv[i] = 0;
			}
			
			System.out.println("processData Done. myRecvIndicator: " + myRecvIndicator);
		}
	}
	
	
	public static void main(String[] args) {
		
		try {
			mySocket = new Socket("xxx.xxx.xxx.xxx", 8080);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			mySocket.setTcpNoDelay(true);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		try {
			myWriter = mySocket.getOutputStream();
			myReader = mySocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		clearBuffer();
			
		SendThread sendTh = new SendThread();
		RecvThread recvTh = new RecvThread();
		
		sendTh.start();
		recvTh.start();
		
	}
}

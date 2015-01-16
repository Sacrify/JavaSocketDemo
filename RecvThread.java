import java.io.IOException;


public class RecvThread extends Thread {

	public void run(){
		while (SocketDemo.myQuitSignal == false){
			try {
				int readCount = SocketDemo.myReader.read(
						SocketDemo.myRecv, SocketDemo.myRecvIndicator, 
						SocketDemo.myRecvCount - SocketDemo.myRecvIndicator - 1);
				if (readCount > 0) {
					SocketDemo.myRecvIndicator += readCount;
					SocketDemo.processData();
				}
					
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

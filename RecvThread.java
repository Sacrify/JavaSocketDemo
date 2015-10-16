import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Date;


public class RecvThread extends Thread {
	
	public SocketDemo socketDemo;
	
	public void run(){
		while (this.socketDemo.myQuitSignal == false){
			try {
				int readCount = this.socketDemo.myReader.read(
						this.socketDemo.myRecv, this.socketDemo.myRecvIndicator, 
						this.socketDemo.myRecvCount - this.socketDemo.myRecvIndicator - 1);
				if (readCount > 0) {
					this.socketDemo.myRecvIndicator += readCount;
					this.socketDemo.processData();
				}
            } catch (SocketTimeoutException e) {
                continue;
			} catch (Exception e) {
				e.printStackTrace();
				this.socketDemo.myQuitSignal = true;
				this.socketDemo.myQuitDate = new Date();
			}
		}
	}

}

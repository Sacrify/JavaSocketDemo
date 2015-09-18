import java.io.IOException;
import java.util.Date;


public class SendThread extends Thread {

	public SocketDemo socketDemo;
	
	public void run(){
		while (this.socketDemo.myQuitSignal == false) {
			if (this.socketDemo.msgQueue.isEmpty()) {
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
					this.socketDemo.myQuitSignal = true;
					this.socketDemo.myQuitDate = new Date();
					break;
				}
			}
			else {
				message oneMsg = this.socketDemo.msgQueue.peek();
				try {
					System.out.println("send message");
					this.socketDemo.myWriter.write(oneMsg.GetDetail());
					this.socketDemo.myWriter.flush();
					this.socketDemo.msgQueue.poll();
				} catch (Exception e) {
					e.printStackTrace();
					this.socketDemo.myQuitSignal = true;
					this.socketDemo.myQuitDate = new Date();
				}
			}
		}
	}
}

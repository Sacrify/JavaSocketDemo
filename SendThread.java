import java.io.IOException;


public class SendThread extends Thread {

	public void run(){
		while (SocketDemo.myQuitSignal == false) {
			if (SocketDemo.msgQueue.isEmpty()) {
				try {
					java.lang.Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
			else {
				message oneMsg = SocketDemo.msgQueue.peek();
				try {
					System.out.println("send message");
					SocketDemo.myWriter.write(oneMsg.GetDetail());
					SocketDemo.myWriter.flush();
					SocketDemo.msgQueue.poll();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

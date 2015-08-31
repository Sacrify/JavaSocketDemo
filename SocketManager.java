import java.util.Date;
import java.util.Vector;

public class SocketManager {

public static void main(String[] args) {
    
	Vector<SocketDemo> socketDemos = new Vector<SocketDemo>();
	
	while (true) {
		boolean socketDemoAliveExist = false; 
		for (int i = socketDemos.size() - 1; i >= 0; i--) {
			SocketDemo demo = socketDemos.get(i);
			if (demo == null) {
				socketDemos.remove(i);
				continue;
			}
			
			if (demo.myQuitSignal == false) {
				socketDemoAliveExist = true;
				continue;
			} else {
				// 如果设置了要退出但是没有设置时间，增加时间标记
				if (demo.myQuitDate == null) {
					demo.myQuitDate = new Date();
					continue;
				}
				
				// 30 秒默认超时停止时间
				if (((new Date()).getTime() - demo.myQuitDate.getTime()) / 1000 > 30) {
					if (demo.mySendTh.isAlive()) demo.mySendTh.interrupt();
					if (demo.myRecvTh.isAlive()) demo.myRecvTh.interrupt();
					socketDemos.remove(i);
					continue;
				}
			}
		}
		
		if (socketDemoAliveExist == false) {
			SocketDemo demo = SocketDemo.createSocketDemo("xxx.xxx.xx.xx", 8080);
			if (demo != null) socketDemos.add(demo);
		}
		
		try {
			java.lang.Thread.sleep(30 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
}

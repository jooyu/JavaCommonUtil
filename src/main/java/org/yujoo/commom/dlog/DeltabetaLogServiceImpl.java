package org.yujoo.commom.dlog;




public class DeltabetaLogServiceImpl implements DeltabetaLogService {

//	@Value("${deltabeta.logserver.addr}") 
	private String mAddr;
//	@Value("${deltabeta.logserver.port}")
//	private String mPort;
//	@Value("${deltabeta.logserver.gameappid}")
	private String mGameAppid;
//	@Value("${deltabeta.logserver.gameserverid}")
	private String mGameServerid;
	private Thread mThread;
	private SocketThead socketThead;
	public DeltabetaLogServiceImpl() {
		socketThead = null;
		
	}
	public void Init() {
		
		socketThead = new SocketThead(mAddr);
		mThread = new Thread(socketThead);
		mThread.start();
	}
	public void SendProtocol(String message) {
		if(socketThead == null) {
			socketThead = new SocketThead(mAddr);
			
		}
		socketThead.Send(message);
		StringBuffer buffer;
		
		
	}

	
	
}

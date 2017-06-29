package org.yujoo.commom.dlog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SocketThead implements Runnable {
	private final int MAX_TIME_OUT = 20000;
	private final int MAX_QUENE_SIZE = 200000;
	private final int SOCKET_BUFFER_SIZE = 4096;
	private Vector<String> mSendQuene;
	private String mSendBuffer;

	private DatagramSocket mLogSocket;
	private InetSocketAddress addrServer;
	private InetSocketAddress addrTencentServer;
	Logger logger = LoggerFactory.getLogger(SocketThead.class);
	public SocketThead(String addr) {
		mSendQuene = new Vector<String>();
		String[] servers = addr.split("[|]");
		int len = null != servers ? servers.length : 0;
		for(int i = 0;i < len;i++)
		{
		    if(i == 0)
		    {
		        String[] temp = servers[i].split("[:]");
		        if (temp.length == 2){
		            String hostDlog = temp[0];
	                int portDlog = Integer.parseInt(temp[1]);
	                
	                addrServer = new InetSocketAddress(hostDlog, portDlog);
		        }
		    }
		    else if(i == 1)
		    {
		        String[] temp = servers[i].split("[:]");
		        if (temp.length == 2){
		            String hostTlog = temp[0];
	                int portTlog = Integer.parseInt(temp[1]);
	                
	                addrTencentServer = new InetSocketAddress(hostTlog, portTlog);
		        }
		    }
		}
	}

	public void Send(String message) {
		synchronized (mSendQuene) {
			mSendQuene.add(message);
		}
	}

//	@Override
	public void run() {

		while (true) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
			int queneSize = mSendQuene.size();
			if (queneSize > 0) {
				if (queneSize > MAX_QUENE_SIZE) {
					// protect .
					mSendQuene.removeAllElements();
					continue;
				}

				try {
					mLogSocket = new DatagramSocket();
					mLogSocket.setSoTimeout(MAX_TIME_OUT);
					synchronized (mSendQuene) {
						for (int i = 0; i < mSendQuene.size(); i++) {
							mSendBuffer = mSendQuene.get(i);
							boolean error = ProcessSend();
						}
						mSendQuene.clear();
					}
					mLogSocket.close();
					mLogSocket = null;
				} catch (SocketException e) {
					e.printStackTrace();
					continue;
				}

			}

		}

	}

	private boolean ProcessSend() {
		boolean bError = false;
		int bytesToSend = mSendBuffer.length();
		if (bytesToSend == 0 || bytesToSend > SOCKET_BUFFER_SIZE) {
			return bError;
		}
		byte[] sendDataByte = mSendBuffer.getBytes();
		DatagramPacket dataPacket;
		try {
		    if(null != addrServer)
		    {
		        dataPacket = new DatagramPacket(sendDataByte, sendDataByte.length,
					addrServer);
		        mLogSocket.send(dataPacket);
		    }
			if(null != addrTencentServer)
			{
			    dataPacket = new DatagramPacket(sendDataByte, sendDataByte.length,
			        addrTencentServer);
			    mLogSocket.send(dataPacket);
			}
			//logger.info(mSendBuffer);
		} catch (SocketException e) {
			// e.printStackTrace();
			bError = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			bError = true;
		}

		return bError;
	}
}

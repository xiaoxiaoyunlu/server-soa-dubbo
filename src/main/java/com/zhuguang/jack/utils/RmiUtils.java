package com.zhuguang.jack.utils;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.zhuguang.jack.loadbalance.NodeInfo;
import com.zhuguang.jack.rmi.SoaRmi;
import com.zhuguang.jack.rmi.SoaRmiImpl;

public class RmiUtils {
	
	
	public void startRmiServer(String host,String port,String id){
		try {
			SoaRmi soaRmi=new SoaRmiImpl();
			LocateRegistry.createRegistry(Integer.parseInt(port));
			//rmi://127.0.0.1:1135/fufdasfew
			Naming.bind("rmi://"+host+":"+Integer.parseInt(port)+"/"+id, soaRmi);
			System.out.println("Rmi server started!");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

	public SoaRmi startRmiClient(NodeInfo nodeInfo, String id) {
		String host=nodeInfo.getHost();
		String port=nodeInfo.getPort();
		
		try {
			return (SoaRmi)Naming.lookup("rmi://"+host+":"+port+"/"+id);
		} catch(MalformedURLException e){
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}

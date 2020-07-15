package core.wlwallet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.bouncycastle.util.Arrays;



public class SimpleNode{
	
	public String host;
	public int port;
	public boolean testnet;
	public Socket socket;
	public boolean logging=false;
	public DataInputStream dis;
	public DataOutputStream dos;
	
	public SimpleNode(String host, boolean testnet) {
		this(host, 0, testnet, false);
	}
	
	public SimpleNode(String host, int port, boolean testnet, boolean logging) {
		super();
		if(port==0){
			if(testnet){
				this.port = 18333;
			}else{
				this.port = 8333;
			}
		}
		this.testnet = testnet;
		this.logging = logging;
		try {
			this.socket = new Socket(host, this.port);
			this.dis = new DataInputStream(this.socket.getInputStream());
			this.dos = new DataOutputStream(this.socket.getOutputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(Message message){
		NetworkEnvelope envelope = new NetworkEnvelope(message.getCommand(), message.serialize(), this.testnet);
		if(this.logging){
			System.out.println("sending: "+envelope);
		}
		try {
			dos.write(envelope.serialize());
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public NetworkEnvelope read(){
		NetworkEnvelope envelope = NetworkEnvelope.parse(dis, this.testnet);
		if(this.logging){
			System.out.println("receiving : " + envelope);
		}
		return envelope;
	}
	
	public Message waitFor(List<String> list){
		NetworkEnvelope envelope = null;
		byte[] command = new byte[]{};
		
		HashMap<String, String> commandToClass = new HashMap<String, String>();
		for(String mStr: list){
			
//			String fileName = "c:/Users/xushui/workspace/wlwallet/src/main/java/core/wlwallet/";
			
			URL[] urls;
			try {
				
				
			/*	String packageStr = mStr.substring(mStr.lastIndexOf(".")+1);
				JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
				StandardJavaFileManager fileMgr = compiler.getStandardFileManager(null, null, null);
				Iterable units = fileMgr.getJavaFileObjects(fileName+packageStr+".java");
				
				CompilationTask t = compiler.getTask(null, fileMgr, null, null, null, units);
				t.call();
				fileMgr.close();*/
				
				
				
				urls = new URL[]{new URL("file:/c:/Users/xushui/workspace/wlwallet/target/classes/")};
				URLClassLoader ul = new URLClassLoader(urls);
				Class c = ul.loadClass(mStr);
				Constructor ctr = c.getConstructor();
				Object o = ctr.newInstance();
				
				Message m = (Message) o;
				byte[] comm = m.getCommand();
				commandToClass.put(new String(comm).trim(), mStr);
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		while(!commandToClass.containsKey(new String(command).trim())){
		/*	try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			envelope = this.read();
			command = envelope.command;
			String commandStr = new String(command).trim();
			System.out.println(commandStr);
			if(commandStr.equals("version")){
				this.send(new VerAckMessage());
			}else if(commandStr.equals("pong")){
				this.send(new PongMessage(envelope.payload));
			}
		}
		DataInputStream envelopePayloadStream = envelope.stream();
		
		String messageStr = commandToClass.get(new String(command).trim());
		URL[] urls;
		Message message=null;
		try {
			urls = new URL[]{new URL("file:/" + "c:/Users/xushui/workspace/wlwallet/target/classes")};
			URLClassLoader ul = new URLClassLoader(urls);
			Class c = ul.loadClass(messageStr);
			Constructor ctr = c.getConstructor();
			Object o = ctr.newInstance();
			
			
			Method method = c.getMethod("parse", DataInputStream.class, boolean.class);
			message=(Message) method.invoke(o, envelopePayloadStream, false);
			
		
	//		message=((Message) o).parse(envelopePayloadStream);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}
	
	public Message handShake(){
		ArrayList<String> list = new ArrayList<String>();
		list.add("core.wlwallet.VerAckMessage");
		VersionMessage version = new VersionMessage();
		this.send(version);
		return this.waitFor(list);
	}
	
}

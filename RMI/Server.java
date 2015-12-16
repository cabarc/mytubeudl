import java.net.URLEncoder;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Vector;
import java.io.*;
import java.nio.file.*;

public class Server implements systemFunctions {
	private Manip con;
	private String host;
	private String port;

	public Server() throws RemoteException {
		this.con = new Manip();
	}

	@Override
	public String uploadFile(String filename, String description, byte[] data) throws RemoteException, UnsupportedEncodingException, IOException{
		Arquivo arquivo = new Arquivo();
		arquivo.setKey(filename);
		arquivo.setDescription(description);
		arquivo.setHost(this.host);
		arquivo.setPort(this.port);
		if (con.enviarArquivo(arquivo) == "FIAL") {
			return "File previusly uploaded";
		} else {
			FileOutputStream out = new FileOutputStream("data/"+filename);
			out.write(data);
			out.close();
			return "File correctly uploaded";
		}
	}

	@Override
	public byte[] downloadFile(String filename) throws RemoteException, UnsupportedEncodingException, IOException{
		String output = con.receberArquivo("key", filename);
		if (output == "FIAL") {
			return null;
		} else {
			String[] parts = output.split(";");
			String key = parts[0];
			String desc = parts[1];
			String host = parts[2];
			String port = parts[3];
			if (this.host.equals(host) && this.port.equals(port)) {
				Path path = Paths.get("data/"+key);
				byte[] data = Files.readAllBytes(path);	
				return data;
			} else {
				try{
					Registry registry_tmp = LocateRegistry.getRegistry(host, Integer.parseInt(port));
		    			systemFunctions stub_tmp = (systemFunctions) registry_tmp.lookup("systemFunctions");
					return stub_tmp.downloadFile(key);
				} catch (Exception e) {
					return null;
				}
			}
		}
	}

	@Override
	public String downloadFileDescription(String description) throws RemoteException, UnsupportedEncodingException {
		String output = con.receberArquivo("description", description);
		if (output == "FIAL") {
			return "File not exists";
		} else {
			String[] parts = output.split(";");
			String key = parts[0];
			String desc = parts[1];
			String host = parts[2];
			String port = parts[3];
			
			return key;
		}
	}

	@Override
	public String getAllFiles() throws RemoteException, UnsupportedEncodingException {
		String files = con.receberAll();
		return files;
	}

	private static Registry startRegistry(int RMIPortNum) throws RemoteException {
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(RMIPortNum);
			registry.list();
			System.out.println("RMI registry running at port " + RMIPortNum);
		} catch (RemoteException ex) {
			System.out.println("RMI registry cannot be located at port " + RMIPortNum);
			registry = LocateRegistry.createRegistry(RMIPortNum);
			System.out.println("RMI registry created at port " + RMIPortNum);
		}
		return registry;
	}

	public static void main(String args[]) throws RemoteException {
		try {
			if (args.length != 2) {
				System.out.println(
						"java -Djava.security.policy=rmi.policy Server <host> <port>");
				System.exit(0);
			}
			int port = Integer.parseInt(args[1]);
			String host = args[0];
			System.out.println("Host " + host);
			System.setProperty("java.rmi.server.hostname", host);
			Registry registry = startRegistry(port);
			Server obj = new Server();
			obj.port = port + "";
			obj.host = host;
			systemFunctions stub = (systemFunctions) UnicastRemoteObject.exportObject(obj, 0);

			// Bind the remote object's stub in the registry
			registry.bind("systemFunctions", stub);
			System.err.println("Server ready");
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}

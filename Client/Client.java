import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import java.util.HashMap;
import java.io.*;
import java.nio.file.*;

public class Client {

	private Client() {
	}

	public static void main(String[] args) {

	if (args.length != 2){
		System.out.println("java -Djava.security.policy=rmi.policy Server <localhost> <port>");
		System.exit(0);
	}
	int port = Integer.parseInt(args[1]);
	String host = args[0];

        try {
			Registry registry = LocateRegistry.getRegistry(host, port);
		    	systemFunctions stub = (systemFunctions) registry.lookup("systemFunctions");

			InputStreamReader r=new InputStreamReader(System.in);  
			BufferedReader br=new BufferedReader(r);  
			clearScreen();
			while(true){
				System.out.println("Press number for select the option:");
				System.out.println("1. Upload file");
				System.out.println("2. Download file");
				System.out.println("3. List all files");
				System.out.println("4. Download file from description");
				System.out.println("5. Exit");  
				int option = (int) Integer.parseInt(br.readLine()); 
				String desc;
				String output;
				switch (option) {
					case 1:  
						System.out.println("Enter filepath:");
						String pathfile = br.readLine();
						System.out.println("Enter description file:");
						desc = br.readLine();
						
						Path path = Paths.get(pathfile);
						byte[] data = Files.readAllBytes(path);	
							
						String[] partpath = pathfile.split("/");
						String file = partpath[partpath.length-1];

						output = stub.uploadFile(file, desc, data);
						System.out.println(output);
						break;
					case 2:
						System.out.println("Enter filename");   
						desc = br.readLine();
						byte[] filecontent = stub.downloadFile(desc);
						if (filecontent != null){
							FileOutputStream out = new FileOutputStream("files/"+desc);
							out.write(filecontent);
							out.close();
							System.out.println("File downloaded");
						} else {
							System.out.println("FILE NOT FOUND");
						}
						break;
					case 3:
						System.out.println("Available files:");
						System.out.println("__________________________________________________");
						output = stub.getAllFiles();
						System.out.println(output.replace("_", "\n"));
						break;
					case 4:
						System.out.println("Enter description");   
						desc = br.readLine();
						String filename2 = stub.downloadFileDescription(desc);
						byte[] filecontent2 = stub.downloadFile(filename2);
						if (filecontent2 != null){
							FileOutputStream out = new FileOutputStream("files/"+filename2);
							out.write(filecontent2);
							out.close();
							System.out.println("File downloaded");
						} else {
							System.out.println("FILE NOT FOUND");
						}
						break;
					case 5:
						System.exit(0);
					default:
						System.out.println("Wrong input");
						break;
				}
				System.out.println("_____________________________________________________________________");
				System.out.println("Press enter to clear");    
				br.readLine();
				clearScreen();
				
			}
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

	public static void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
}

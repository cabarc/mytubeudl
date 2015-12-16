import java.io.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface systemFunctions extends Remote {
    String uploadFile(String filename, String description, byte[] data) throws RemoteException, UnsupportedEncodingException, IOException;
    byte[] downloadFile(String description) throws RemoteException, UnsupportedEncodingException, IOException;
    String downloadFileDescription(String description) throws RemoteException, UnsupportedEncodingException;
    String getAllFiles() throws RemoteException, UnsupportedEncodingException;

}

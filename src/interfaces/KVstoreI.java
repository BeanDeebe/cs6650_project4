package interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import common.Result;

public interface KVstoreI extends Remote {
    Result get(String key) throws RemoteException;
    String put(String key, String value) throws RemoteException;
    Result delete(String key) throws RemoteException;
}

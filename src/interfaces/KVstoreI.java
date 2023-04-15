package interfaces;
import java.rmi.RemoteException;
import common.Result;

public interface KVstoreI extends Remote {
    Result get(String key) throws RemoteException;
    Result put(String key, String value) throws RemoteException;
    Result delete(String key) throws RemoteException;
}

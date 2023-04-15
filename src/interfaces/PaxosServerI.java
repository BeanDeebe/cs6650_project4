package interfaces;

import common.Proposal;
import common.Result;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;



public interface PaxosServerI extends KVstoreI, Remote {

    /**
     * Prepares the proposal and then returns the result as the acceptor.
     * This is the first phase of the algo.
     * @param proposal
     * @return the resulting promise.
     * @throws RemoteException
     */
    Proposal promise(Proposal proposal) throws RemoteException;

    /**
     * Accepting the proposal as the acceptor
     * @param proposal
     * @return True if proposal is accepted, False otherwise.
     * @throws RemoteException
     */
    Boolean accept(Proposal proposal) throws RemoteException;

    /**
     * Learning the proposal as a learner object.
     * @param proposal
     * @return the result!
     * @throws RemoteException
     */
    Result learn(Proposal proposal) throws RemoteException;

    /**
     * Tries to recover a copy of the KVstore from another node that resides at the
     * port passed in.
     */
    void recover(int port, String hostname) throws RemoteException;

    /**
     * gets a copy of the KVstore
     * @throws RemoteException
     */
    Map<String, String> copyKVstore() throws RemoteException;

    /**
     * getter for the port number
     */
    int getPort() throws RemoteException;


    /*
     * setters for coordinator / host / port.
     */

    void setCoordinator(CoordinatorI coordinator) throws RemoteException;

    void setHost(String host) throws RemoteException;

    void setPort(int port) throws RemoteException;
}

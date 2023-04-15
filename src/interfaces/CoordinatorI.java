package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import common.Proposal;
import common.Result;

public interface CoordinatorI extends Remote {

    // the different actions you can execute in the system (get put delete).
    Result execute(Proposal proposal) throws RemoteException;

    // keeps track of the acceptors & where they exist.
    void addAcceptor(int port, String hostname) throws RemoteException;

    String receiveRequest(String[] command);
}

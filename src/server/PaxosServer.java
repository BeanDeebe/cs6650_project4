package server;

import common.Proposal;
import common.Result;
import interfaces.CoordinatorI;
import interfaces.PaxosServerI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PaxosServer extends UnicastRemoteObject implements PaxosServerI {
    private static final long serialVersionUTD = 1L;
    private String host;
    private int port;
    private CoordinatorI coordinator;
    private Map<String, String> kvStore;
    private AtomicInteger proposalNumber; // thread safe integer type (online resources recommended to prevent race conditions.)
    private Proposal highestPromisedProposal;
    private Proposal acceptedProposal;

    public PaxosServer(String host, int port) throws RemoteException {
        this.host = host;
        this.port = port;
        this.kvStore = new HashMap<>();
        this.proposalNumber = new AtomicInteger(0);
        this.acceptedProposal = null;
    }
    @Override
    public Result get(String key) throws RemoteException {
        return null;
    }

    @Override
    public String put(String key, String value) throws RemoteException {
        this.kvStore.put(key, value);
        return "Server " + this.port + " acknowledging PUT request for value pair { " + key + " : " + value + " }";
    }


    @Override
    public Result delete(String key) throws RemoteException {
        return null;
    }

    @Override
    public Proposal promise(Proposal proposal) throws RemoteException {
        if (highestPromisedProposal == null || proposal.getProposalNumber() > highestPromisedProposal.getProposalNumber()) {
            highestPromisedProposal = proposal;
            return proposal;
        } else {
            return highestPromisedProposal;
        }
    }

    @Override
    public Boolean accept(Proposal proposal) throws RemoteException {
        if (proposal.getProposalNumber() >= highestPromisedProposal.getProposalNumber()) {
            this.acceptedProposal = proposal;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String learn(Proposal proposal) throws RemoteException {
        this.accept(proposal);
        if (acceptedProposal != null && acceptedProposal.equals(proposal)) {
            this.kvStore.put(proposal.getKey(), proposal.getValue());
            return "System acknowledging PUT request for value pair { " + proposal.getKey() + " : " + proposal.getValue() + " }";
        } else {
            return null;
        }
    }

    @Override
    public void recover(int port, String hostname) throws RemoteException {

    }

    @Override
    public Map<String, String> copyKVstore() throws RemoteException {
        return new HashMap<>(kvStore);
    }

    @Override
    public int getPort() throws RemoteException {
        return port;
    }

    @Override
    public void setCoordinator(CoordinatorI coordinator) throws RemoteException {
        this.coordinator = coordinator;
    }

    @Override
    public void setHost(String host) throws RemoteException {
        this.host = host;
    }

    @Override
    public void setPort(int port) throws RemoteException {
        this.port = port;
    }
}

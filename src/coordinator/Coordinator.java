package coordinator;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import interfaces.CoordinatorI;
import interfaces.PaxosServerI;
import common.*;

public class Coordinator extends UnicastRemoteObject implements CoordinatorI{

    private Set<Map.Entry<String, Integer>> servers;

    protected Coordinator() throws RemoteException {
        this.servers = new HashSet<>();

    }

    @Override
    public Result execute(Proposal proposal) throws RemoteException {
        List<PaxosServerI> acceptors = new ArrayList<>();

        for (Map.Entry<String, Integer> server: this.servers)
        {
            try {
                PaxosServerI acceptor = (PaxosServerI) Naming.lookup("rmi://" + server.getKey() + ":" + server.getValue() + "/keyValueServer");
                acceptors.add(acceptor);
            } catch (Exception e) {
                System.out.println("Port ID: " + server.getValue() + " not responding.");
                continue;
            }
        }

        int halfServerSize = Math.floorDiv(acceptors.size(), 2) + 1;
        int promised = 0;

        // preparing message phase 1
        for (PaxosServerI acceptor: acceptors){
            try {
                Promise promise = acceptor.promise(proposal);
                if (promise == null) {
                    System.out.println("Server not responding at port: " + acceptor.getPort());
                }
                if (promise.getStatus() == Status.PROMISED || promise.getStatus() == Status.ACCEPTED) {
                    promised ++;
                    System.out.println("Server at port " + acceptor.getPort() + " PROMISED proposal");

                }
                else {
                    System.out.println("Server at port " + acceptor.getPort() + " REJECTED proposal");
                }
            } catch (Exception e) {
                System.out.println("Server not responding at port: " + acceptor.getPort());
                continue;
            }
        }

        // phase two, which is sending the accept message
        if (promised < halfServerSize) {
            return Result.setResult(ResultCodeEnum.CONSENSUS_NOT_REACHED);
        }

        // sending accept message.
        int accepted = 0;
        for (PaxosServerI acceptor : acceptors) {
            try {
                Boolean isAccepted = acceptor.accept(proposal);

                if (isAccepted == null) {
                    System.out.println("Server at port " + acceptor.getPort() + " REJECTED proposal");
                }

                if (isAccepted) {
                    accepted ++;
                    System.out.println("Server at port " + acceptor.getPort() + " ACCEPTED proposal");
                }
            } catch(Exception e) {
                continue;
            }
        }

        if (accepted < halfServerSize) {
            return Result.setResult(ResultCodeEnum.CONSENSUS_NOT_REACHED);
        }

        //phase 3 -- learn message
        Result result = null;

        for (PaxosServerI acceptor: acceptors) {
            try{
                result = acceptor.learn(proposal);
            } catch(Exception e){
                continue;
            }
        }

        return result;
    }

    @Override
    public void addAcceptor(int port, String hostname) throws RemoteException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAcceptor'");
    }

    @Override
    public String receiveRequest(String[] command) {
        return null;
    }

}

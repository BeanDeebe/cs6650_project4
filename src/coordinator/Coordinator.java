package coordinator;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import interfaces.CoordinatorI;
import interfaces.PaxosServerI;
import common.*;

import static common.Proposal.Status.ACCEPTED;
import static common.Proposal.Status.PROMISED;

public class Coordinator extends UnicastRemoteObject implements CoordinatorI{

    private Set<Map.Entry<String, Integer>> servers;

    public Coordinator() throws RemoteException {
        this.servers = new HashSet<>();

    }

    @Override
    public String execute(Proposal proposal) throws RemoteException {
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
                Proposal promise = acceptor.promise(proposal);
                if (promise == null) {
                    System.out.println("Server not responding at port: " + acceptor.getPort());
                }
                if (promise.getStatus() == PROMISED || promise.getStatus() == ACCEPTED) {
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

        // phase two, which is sending the accept message - if less than half servers agree, returns false.
        if (promised < halfServerSize) {
            return "Not enough accepted nodes. Aborting.";
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
            return "Not enough accepted nodes. Aborting.";
        }

        //phase 3 -- learn message
        String result = null;

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
        Map.Entry<String, Integer> newServer = new AbstractMap.SimpleEntry<>(hostname,port);
        this.servers.add(newServer);
    }

    @Override
    public String receiveRequest(String[] command) throws RemoteException {
        if (command == null || command.length < 2) {
            System.out.println("Invalid command format.");
            return null;
        }

        String action = command[0].toLowerCase();
        System.out.println(action);
        String key = command[1];
        System.out.println(key);
        String value = null;
        if (action.equals("put") && command.length >= 3) {
            value = command[2];
        }
        System.out.println(value);

        Proposal proposal = new Proposal(key, value, action);
        System.out.println(proposal);

        try {
            return execute(proposal);
        } catch (RemoteException e) {
            System.err.println("Error executing the command: " + e.getMessage());
            return null;
        }
    }

}

package server;

import coordinator.Coordinator;
import interfaces.CoordinatorI;
import interfaces.PaxosServerI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    public static void main(String[] args) {
        try {
            CoordinatorI coordinator = new Coordinator();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("coordinator", coordinator);
            System.out.println("Coordinator on port 1099");

            int[] ports = {1000, 1001, 1002};
            String host = "localhost";

            for (int port : ports){
                PaxosServerI paxosServer = new PaxosServer(host, port);
                registry = LocateRegistry.createRegistry(port);
                registry.rebind("keyValueServer", paxosServer);
                System.out.println("PaxosServer running on port: " + port);

                paxosServer.setCoordinator(coordinator);
                coordinator.addAcceptor(port, host);
            }

            System.out.println("We've completed system setup of servers");
        } catch (RemoteException e) {
            System.err.println("Could not set up system\n\n\n");
            throw new RuntimeException(e);
        }
    }
}

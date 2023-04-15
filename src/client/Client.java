/**
 * Dean Beebe
 * CS6650
 * Project 3
 *
 * This file contains the client code for Project 3. Multiple instances of this code can be run in order to access the
 * server key-value store in parallel.
 */
package client;
import interfaces.CoordinatorI;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 * ClientRMI
 *
 * Client class that connects to the RMI registry, accepts user input, and contacts the coordinator
 */
public class Client {
    // private constructor
    private Client() {}

    public static void main(String[] args) {
        try {
            // grabbing the Coordinator registry on port 1099
            Registry registry = LocateRegistry.getRegistry(1099);

            // stub as a local rep to execute methods on the server
            CoordinatorI stub = (CoordinatorI) registry.lookup("coordinator");
            Scanner scan = new Scanner(System.in);

            /*
             * while loop that accepts user input, then parses the input to get the
             * user's command. Afterward, it correctly calls the remote method relating to
             * the command and prints out the response it receives.
             */
            while (true) {

                System.out.println("Enter a command (put, get, or delete). Alternatively, type 'q' to quit: ");
                String userInput = scan.nextLine();
                String[] command = userInput.split(" ");

                // quit command for user to end client program.
                if(command[0].toLowerCase().equals("q")) {
                    break;
                } else {
                    String request = stub.receiveRequest(command);
                    System.out.println(request);
                }
            }
            scan.close();
        } catch (Exception e) {
            System.out.println("oops!\n" + e);
        }
    }
}
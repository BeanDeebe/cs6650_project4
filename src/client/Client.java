package client;

import interfaces.CoordinatorI;
import common.Result;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    // private constructor
    private Client() {}

    public static void main(String[] args) {
        try {
            // grabbing the Coordinator registry on port 1099
            Registry registry = LocateRegistry.getRegistry(1099);

            // stub as a local rep to execute methods on the server
            CoordinatorI coordinator = (CoordinatorI) registry.lookup("coordinator");
            Scanner scan = new Scanner(System.in);

            while (true) {
                System.out.println("Enter a command (put, get, or delete). Alternatively, type 'q' to quit: ");
                String userInput = scan.nextLine();
                String[] command = userInput.split(" ");

                // quit command for user to end client program.
                if (command[0].toLowerCase().equals("q")) {
                    break;
                } else {
                    String result = coordinator.receiveRequest(command);
                    if (result == null) {
                        System.out.println("Failed to execute command: " + userInput);
                    } else {
                        System.out.println(result);
                    }
                }
            }
            scan.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

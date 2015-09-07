/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;
import java.io.*;
import java.net.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author SangMade
 */
public class Server {
    private static ServerSocket serverSocket = null;
    private static Socket clientSocket = null;
    private static final int maxConnection = 10;
    private static final ServerThread[] thread = new ServerThread[maxConnection];

    public static void main(String[] args) {
        int port = 777;
        if (args.length < 1) {
            System.out.println("You are connect to the " + port);
        } else {
            port = Integer.valueOf(args[0]).intValue();
        }
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("IO Exception - koneksi port");
        }

        while (true) {
            try {
                clientSocket = serverSocket.accept();
                int max = 0;
                for (int i = 0; i < maxConnection; i++) {
                    if (thread[i] == null) {
                        (thread[i] = new ServerThread(clientSocket, thread)).start();
                        break;
                    }
                }
                if (max == maxConnection) {
                    PrintStream out = new PrintStream(clientSocket.getOutputStream());
                    clientSocket.close();
                }

            } catch (IOException ex) {
                System.out.println("IO Exception = connection client");
            }

        }
    }
}

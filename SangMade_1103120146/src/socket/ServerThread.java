/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author SangMade
 */
public class ServerThread extends Thread {
    private DataInputStream inStream = null;
    private PrintStream outStream = null;
    private Socket clientSocket = null;
    private final ServerThread[] threads;
    private final int maxConnection;

    public ServerThread(Socket clientSocket, ServerThread[] threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
        maxConnection = threads.length;
    }

    @Override
    public void run() {
        try {
            int maksimum = this.maxConnection;
            ServerThread[] trit = this.threads;

            inStream = new DataInputStream(clientSocket.getInputStream());
            outStream = new PrintStream(clientSocket.getOutputStream());
                outStream.println("Username: ");
                String id = inStream.readLine().trim();
                outStream.println("----------------------");
                outStream.println("Hello you," + id + "!");
                outStream.println("----------------------");
                outStream.println("If you want to quit, type 'quit' or 'q'");
                outStream.println("----------------------");
            for (int i = 0; i < maksimum; i++) {
                if (trit[i] != null && trit[i] != this) {
                    trit[i].outStream.println("" + id + " joined the chat");
                }
            }
            while (true) {
                String chat = inStream.readLine();
                if (chat.startsWith("quit") || chat.startsWith("q")) {
                    break;
                }
                for (int i = 0; i < maksimum; i++) {
                    if (trit[i] != null) {
                        trit[i].outStream.println("<" + id + ">: " + chat);
                    }
                }
            }
            for (int i = 0; i < maksimum; i++) {
                if (trit[i] != null && trit[i] != this) {
                    trit[i].outStream.println("" + id + " left the chat");
                }
            }
            outStream.println("Bye " + id + "");
            for (int i = 0; i < maksimum; i++) {
                if (trit[i] == this) {
                    trit[i] = null;
                }
            }
            inStream.close();
            outStream.close();
            clientSocket.close();
        } catch (IOException ex) {
            System.out.println("IO Exception - chat");
        }

    }

}

package com.carlosprados.lab.simpleproxy;

import java.net.ServerSocket;
import java.net.Socket;

public class Proxy {

    public static final String usageArgs = " <localport> <host> <port> <timeout_ms>";

    static int clientCount;

    public static synchronized void display(String s) {
        System.err.println(s);
    }

    public static void main(String[] _argv) {
        Proxy proxy = new Proxy();

        if (_argv.length >= 3) {
            int localport = Integer.parseInt(_argv[0]);
            String remoteHost = _argv[1];
            int remotePort = Integer.parseInt(_argv[2]);
            int timeout = 30000;
            try {
                timeout = Integer.parseInt(_argv[3]);
            } catch (Exception e) {
            }
            proxy.run(localport, remoteHost, remotePort, timeout);
        } else {
            System.err.println("usage: java " + proxy.getClass().getName() + usageArgs);
        }
    }

    public static synchronized void print(int _integer) {
        System.out.print((char) _integer);
    }

    public static synchronized void println(String _string) {
        System.out.println(_string);
    }

    public static synchronized void quit(long t) {
        display("...quit after waiting " + t + " ms");
        clientCount--;
    }

    public void run(int _localport, String _host, int _port, long _timeout) {
        try {
            ServerSocket server = new ServerSocket(_localport);
            while (true) {
                Socket socket = null;
                try {
                    display("listening...");
                    socket = server.accept();
                    if (socket != null) {
                        clientCount++;
                        display("accepted as #" + clientCount + ":" + socket);
                        ProxyConnection proxyConnection = new ProxyConnection(socket, _host, _port, _timeout);
                        proxyConnection.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
                /*
                 * try { cSocket.close(); } catch (Exception e) { // fall thru }
                 */
            }
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

}// class


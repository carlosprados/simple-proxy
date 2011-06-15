package com.carlosprados.lab.simpleproxy;

import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class ProxyConnection extends Thread {

    Socket m_fromClientSocket;
    String m_host;
    int m_port;
    long m_timeout;

    public ProxyConnection(Socket _socket, String _host, int _port, long _timeout) {
        m_fromClientSocket = _socket;
        m_host = _host;
        m_port = _port;
        m_timeout = _timeout;
    }

    @Override
    public void run() {
        InputStream clientIn = null;
        OutputStream clientOut = null;
        InputStream serverIn = null;
        OutputStream serverOut = null;
        Socket toServer = null;
        int r0 = -1, r1 = -1, ch = -1, i = -1;
        long time0 = new Date().getTime();
        long time1 = new Date().getTime();
        try {
            toServer = new Socket(m_host, m_port);
            Proxy.display("open connection to:" + toServer + "(timeout=" + m_timeout + " ms)");
            clientIn = m_fromClientSocket.getInputStream();
            clientOut = new BufferedOutputStream(m_fromClientSocket.getOutputStream());
            serverIn = toServer.getInputStream();
            serverOut = new BufferedOutputStream(toServer.getOutputStream());
            while ((r0 != 0) || (r1 != 0) || ((time1 - time0) <= m_timeout)) {
                while ((r0 = clientIn.available()) > 0) {
                    Proxy.println("");
                    Proxy.println("<<<" + r0 + " bytes from client");
                    Proxy.display("");
                    Proxy.display("<<<" + r0 + " bytes from client");
                    for (i = 0; i < r0; i++) {
                        ch = clientIn.read();
                        if (ch != -1) {
                            serverOut.write(ch);
                            Proxy.print(ch);
                        } else {
                            Proxy.display("client stream closed");
                        }
                    }
                    time0 = new Date().getTime();
                    serverOut.flush();
                }
                while ((r1 = serverIn.available()) > 0) {
                    Proxy.println("");
                    Proxy.println(">>>" + r1 + " bytes from server");
                    Proxy.display("");
                    Proxy.display(">>>" + r1 + " bytes from server");
                    for (i = 0; i < r1; i++) {
                        ch = serverIn.read();
                        if (ch != -1) {
                            clientOut.write(ch);
                            Proxy.print(ch);
                        } else {
                            Proxy.display("server stream closed");
                        }
                    }
                    time0 = new Date().getTime();
                    clientOut.flush();
                }
                if ((r0 == 0) && (r1 == 0)) {
                    time1 = new Date().getTime();
                    Thread.sleep(100);
                    // Proxy.display("waiting:"+(time1-time0)+" ms");
                }
            }
        } catch (Throwable t) {
            Proxy.display("i=" + i + " ch=" + ch);
            t.printStackTrace(System.err);
        } finally {
            try {
                clientIn.close();
                clientOut.close();
                serverIn.close();
                serverOut.close();
                m_fromClientSocket.close();
                toServer.close();
                Proxy.quit(time1 - time0);
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.thrift;

import java.net.ServerSocket;
import javax.annotation.PreDestroy;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class DatetimeServer {

    public static DatetimeServiceImpl datetimeServiceImpl;
    public static DatetimeService.Processor processor;
    public static TServerTransport serverTransport;
    public static TServer server;
    public static int port;

    public void start() {
        try {
            datetimeServiceImpl = new DatetimeServiceImpl();
            processor = new DatetimeService.Processor(datetimeServiceImpl);

            Runnable simple = new Runnable() {
                @Override
                public void run() {
                    simple(processor);
                }
            };

            new Thread(simple).start();

        } catch (Exception x) {
            System.err.println(x);
        }
    }

    public void simple(DatetimeService.Processor processor) {
        try {
            //Open socket at available port
            ServerSocket serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            System.out.println(port);
            serverTransport = new TServerSocket(serverSocket);
            server = new TSimpleServer(new Args(serverTransport).processor(processor));
            System.out.println("Starting the Thrift server in Thread " + Thread.currentThread().getId());
            server.serve();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @PreDestroy
    public void StopServer() {
        System.out.println("Closing Thrift server (inside obj)");
        server.stop();
        serverTransport.close();
    }
}

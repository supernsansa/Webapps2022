/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.thrift;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

public class DatetimeClient {

    public String fetchDatetime() {
        try {
            TTransport transport;

            transport = new TSocket("localhost", 9090);
            transport.open();

            TProtocol protocol = new TBinaryProtocol(transport);
            DatetimeService.Client client = new DatetimeService.Client(protocol);
            String datetime = client.currentDatetime();
            System.out.println("Current datetime from server:" + datetime);
            transport.close();
            return datetime;
        } catch (TException x) {
            System.err.println(x);
            return "Error";
        }
    }
}

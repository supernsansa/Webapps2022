/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.thrift;

import java.time.OffsetDateTime;

public class DatetimeServiceImpl implements DatetimeService.Iface {

    @Override
    public String currentDatetime() {
        System.out.println("Fetching current datetime");
        OffsetDateTime currentDateTime = OffsetDateTime.now();
        String stringDateTime = currentDateTime.toString();
        System.out.print(stringDateTime);
        return stringDateTime;
    }
}

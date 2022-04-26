/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webapps2022.restservice;

import com.webapps2022.resources.Currency;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.ejb.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

//Class that handles conversion REST service
@Singleton
@Path("/conversion")
public class Conversion {

    public Conversion() {
    }

    @GET
    @Path("/{currency1}/{currency2}/{amount}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    public Double convert(@PathParam("currency1") String currency1, @PathParam("currency2") String currency2,
            @PathParam("amount") String amount) {
        //Convert args to relevant types
        Currency c1 = Currency.valueOf(currency1);
        Currency c2 = Currency.valueOf(currency2);
        Double a = Double.parseDouble(amount);
        //If both currencies are the same, no conversion is required
        if (c1 == c2) {
            BigDecimal bd = new BigDecimal(a).setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } //Otherwise, make a conversion
        //GBP conversions
        else if (c1 == Currency.GBP) {
            if (c2 == Currency.EUR) {
                Double newAmount = a * 1.18;
                BigDecimal bd = new BigDecimal(newAmount).setScale(2, RoundingMode.HALF_UP);
                return bd.doubleValue();
            } else if (c2 == Currency.USD) {
                Double newAmount = a * 1.26;
                BigDecimal bd = new BigDecimal(newAmount).setScale(2, RoundingMode.HALF_UP);
                return bd.doubleValue();
            }
            //EUR conversions
        } else if (c1 == Currency.EUR) {
            if (c2 == Currency.GBP) {
                Double newAmount = a * 0.85;
                BigDecimal bd = new BigDecimal(newAmount).setScale(2, RoundingMode.HALF_UP);
                return bd.doubleValue();
            } else if (c2 == Currency.USD) {
                Double newAmount = a * 1.07;
                BigDecimal bd = new BigDecimal(newAmount).setScale(2, RoundingMode.HALF_UP);
                return bd.doubleValue();
            }
            //USD conversions
        } else if (c1 == Currency.USD) {
            if (c2 == Currency.GBP) {
                Double newAmount = a * 0.79;
                BigDecimal bd = new BigDecimal(newAmount).setScale(2, RoundingMode.HALF_UP);
                return bd.doubleValue();
            } else if (c2 == Currency.EUR) {
                Double newAmount = a * 0.94;
                BigDecimal bd = new BigDecimal(newAmount).setScale(2, RoundingMode.HALF_UP);
                return bd.doubleValue();
            }
        }
        //If no case matches for some reason, return -1.0
        return -1.0;
    }

}

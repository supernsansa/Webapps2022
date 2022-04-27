package com.webapps2022.restservice;

import com.webapps2022.resources.Currency;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

//Client class for conversion REST service
public class ConversionRestClient {
    public static Double runConversionRestOperation(Currency currency1, Currency currency2, Double amount) {
        System.out.println("ConversionRestClient initialized");
        String webappURL = "http://localhost:10000/webapps2022/";
        String restPath = "restservice/conversion/" + currency1.name() + "/" + currency2.name() + "/" + String.valueOf(amount);
        String restapiURI = webappURL + restPath;

        Client client = ClientBuilder.newClient();
        WebTarget myRestResource = client.target(restapiURI);
        Invocation.Builder builder = myRestResource.request(MediaType.APPLICATION_JSON);
        Double response = builder.get(Double.class);
        client.close();
        return response;
    }
}

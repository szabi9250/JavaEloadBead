package com.example.eloadasbeadando;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import soapclient.*;

@SpringBootApplication
@Controller
public class EloadasBeadandoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EloadasBeadandoApplication.class, args);
    }


@GetMapping("/feladat1")
@ResponseBody
public String kiir1() throws MNBArfolyamServiceSoapGetInfoStringFaultFaultMessage,
        MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage,
        MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage {
    MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
    MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();
    return service.getInfo() + "<br>" + service.getCurrentExchangeRates() + "<br>" +
            service.getExchangeRates("2025-08-14","2025-09-14","EUR");
    }
}
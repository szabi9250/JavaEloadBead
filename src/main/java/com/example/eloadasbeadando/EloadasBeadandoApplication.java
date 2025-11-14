package com.example.eloadasbeadando;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import soapclient.MNBArfolyamServiceSoap;
import soapclient.MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage;
import soapclient.MNBArfolyamServiceSoapImpl;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import java.util.*;

@SpringBootApplication
@Controller
public class EloadasBeadandoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EloadasBeadandoApplication.class, args);
    }



    @PostMapping("/soapfeladat")
    public String soap2(@ModelAttribute MessagePrice messagePrice, Model model) throws Exception {
        MNBArfolyamServiceSoapImpl impl = new MNBArfolyamServiceSoapImpl();
        MNBArfolyamServiceSoap service = impl.getCustomBindingMNBArfolyamServiceSoap();

        String xmlRates = service.getExchangeRates(
                messagePrice.getStartDate(),
                messagePrice.getEndDate(),
                messagePrice.getCurrency()
        );

        // XML
        List<String> dates = new ArrayList<>();
        List<Double> rates = new ArrayList<>();

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new java.io.ByteArrayInputStream(xmlRates.getBytes("UTF-8")));
        NodeList dayNodes = doc.getElementsByTagName("Day");

        for (int i = 0; i < dayNodes.getLength(); i++) {
            Element day = (Element) dayNodes.item(i);
            dates.add(day.getAttribute("date"));

            Element rateEl = (Element) day.getElementsByTagName("Rate").item(0);
            String rateStr = rateEl.getTextContent().replace(",", ".");
            rates.add(Double.parseDouble(rateStr));
        }

        model.addAttribute("dates", dates);
        model.addAttribute("rates", rates);
        model.addAttribute("currency", messagePrice.getCurrency());

        return "soapresult";
    }
}
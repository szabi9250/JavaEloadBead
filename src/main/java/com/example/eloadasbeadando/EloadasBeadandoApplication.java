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
import com.oanda.v20.Context;
import com.oanda.v20.account.AccountSummary;
import com.oanda.v20.pricing.ClientPrice;
import com.oanda.v20.pricing.PricingGetRequest;
import com.oanda.v20.pricing.PricingGetResponse;
import org.springframework.ui.Model;
import com.oanda.v20.instrument.Candlestick;
import com.oanda.v20.instrument.InstrumentCandlesRequest;
import com.oanda.v20.instrument.InstrumentCandlesResponse;
import com.oanda.v20.primitives.InstrumentName;
import static com.oanda.v20.instrument.CandlestickGranularity.H1;



@SpringBootApplication
@Controller
public class EloadasBeadandoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EloadasBeadandoApplication.class, args);
    }


    //SOAP
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



    Context ctx = new Context(Config.URL, Config.TOKEN);
    //Forex account lekérés
    @GetMapping("/account_info")
    @ResponseBody
    public AccountSummary f1() {
        try {
            AccountSummary summary = ctx.account.summary(Config.ACCOUNTID).getAccount();
            return summary;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Forex aktuális árak
    @GetMapping("/actual_prices")
    public String actual_prices(Model model) {
        model.addAttribute("par", new MessageActPrice());
        return "forexact_form";
    }

    @PostMapping("/actual_prices")
    public String actual_prices2(@ModelAttribute MessageActPrice messageActPrice, Model model) {
        String strOut="";
        List<String> instruments = new ArrayList<>( );
        instruments.add(messageActPrice.getInstrument());
        try {
            PricingGetRequest request = new PricingGetRequest(Config.ACCOUNTID, instruments);
            PricingGetResponse resp = ctx.pricing.get(request);
            for (ClientPrice price : resp.getPrices())
                strOut+=price+"<br>";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("instr", messageActPrice.getInstrument());
        model.addAttribute("price", strOut);
        return "forexact_result";
    }

}
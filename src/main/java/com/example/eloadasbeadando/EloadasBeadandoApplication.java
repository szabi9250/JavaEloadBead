package com.example.eloadasbeadando;

import com.oanda.v20.trade.Trade;
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
import static com.oanda.v20.instrument.CandlestickGranularity.*;
import com.oanda.v20.order.MarketOrderRequest;
import com.oanda.v20.order.OrderCreateRequest;
import com.oanda.v20.order.OrderCreateResponse;
import com.oanda.v20.trade.*;

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

    //Forex történeti árak

    @GetMapping("/hist_prices")
    public String hist_prices(Model model) {
        model.addAttribute("param", new MessageHistPrice());
        return "forexhist_form";
    }
    @PostMapping("/hist_prices")
    public String hist_prices2(@ModelAttribute MessageHistPrice messageHistPrice, Model model) {
        String strOut;
        try {
            InstrumentCandlesRequest request = new InstrumentCandlesRequest(new
                    InstrumentName(messageHistPrice.getInstrument()));
            switch (messageHistPrice.getGranularity()) {
                case "M1": request.setGranularity(M1); break;
                case "H1": request.setGranularity(H1); break;
                case "D": request.setGranularity(D); break;
                case "W": request.setGranularity(W); break;
                case "M": request.setGranularity(M); break;
            }
            request.setCount(Long.valueOf(10));
            InstrumentCandlesResponse resp = ctx.instrument.candles(request);
            strOut = "";
            for (Candlestick candle : resp.getCandles())
                strOut += candle.getTime() + "\t" + candle.getMid().getC() + ";";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("instr", messageHistPrice.getInstrument());
        model.addAttribute("granularity", messageHistPrice.getGranularity());
        model.addAttribute("price", strOut);
        return "forexhist_result";
        }

    //Forex nyitás

    @GetMapping("/open_position")
    public String open_position(Model model) {
        model.addAttribute("param", new MessageOpenPosition());
        return "forexopen_form";
    }
    @PostMapping("/open_position")
    public String open_position2(@ModelAttribute MessageOpenPosition messageOpenPosition, Model
            model) {
        String strOut;
        try {
            InstrumentName instrument = new InstrumentName(messageOpenPosition.getInstrument());
            OrderCreateRequest request = new OrderCreateRequest(Config.ACCOUNTID);
            MarketOrderRequest marketorderrequest = new MarketOrderRequest();
            marketorderrequest.setInstrument(instrument);
            marketorderrequest.setUnits(messageOpenPosition.getUnits());
            request.setOrder(marketorderrequest);
            OrderCreateResponse response = ctx.order.create(request);
            strOut="tradeId: "+response.getOrderFillTransaction().getId();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("instr", messageOpenPosition.getInstrument());
        model.addAttribute("units", messageOpenPosition.getUnits());
        model.addAttribute("id", strOut);
        return "forexopen_result";
    }

    //Forex nyitó pozíciók kiírása
    @GetMapping("/forexpoz")
    public String positions(Model model) {
        Context ctx = new Context(Config.URL, Config.TOKEN);
        List<Trade> tradesList = new ArrayList<>();

        try {
            List<Trade> trades = ctx.trade.listOpen(Config.ACCOUNTID).getTrades();
            tradesList.addAll(trades);
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("trades", tradesList);

        return "forexpoz";
    }

    //Forex zárás

    @GetMapping("/close_position")
    public String close_position(Model model) {
        model.addAttribute("param", new MessageClosePosition());
        return "forexclose_form";
    }

    @PostMapping("/close_position")
    public String close_position2(@ModelAttribute MessageClosePosition messageClosePosition, Model model) {
        String tradeId= messageClosePosition.getTradeId()+"";
        String strOut="Closed tradeId= "+tradeId;
        try {
            ctx.trade.close(new TradeCloseRequest(Config.ACCOUNTID, new TradeSpecifier(tradeId)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("tradeId", strOut);
        return "forexclose_result";
    }
}
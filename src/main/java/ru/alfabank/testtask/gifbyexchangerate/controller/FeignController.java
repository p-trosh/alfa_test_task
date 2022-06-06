package ru.alfabank.testtask.gifbyexchangerate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alfabank.testtask.gifbyexchangerate.client.ExchangeRateClient;
import ru.alfabank.testtask.gifbyexchangerate.client.GifClient;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@RestController
@RequestMapping(value = "/gif")
@RequiredArgsConstructor
public class FeignController {

    @Autowired
    private Environment env;

    @Autowired
    private final GifClient gifClient;

    @Autowired
    private final ExchangeRateClient rateClient;

    @GetMapping
    public ResponseEntity readAirlineData (@RequestParam("symbols") String symbols) {
        String gifUrl = (String) gifClient.readRandomGifByTag(
                env.getProperty("app.giphy.id"),
                isRich(symbols) ? "rich" : "broke"
        ).getData().get("embed_url");
        return ResponseEntity.ok().body("<iframe src=\"" + gifUrl + "\"></iframe>");
    }

    private boolean isRich(String symbols) {
        String appId = env.getProperty("app.exchangerates.id");
        String base = env.getProperty("base.currency");

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());

        BigDecimal todayRate = new BigDecimal(rateClient.readExchangeRateRequestBySymbolsAndDate(
                appId,
                nowAsISO,
                base,
                symbols
        ).getRates().get(symbols));

        BigDecimal yesterdayRate = new BigDecimal(rateClient.readExchangeRateRequestBySymbolsAndDate(
                appId,
                nowAsISO,
                base,
                symbols
        ).getRates().get(symbols));
        return todayRate.compareTo(yesterdayRate) != -1;
    }
}
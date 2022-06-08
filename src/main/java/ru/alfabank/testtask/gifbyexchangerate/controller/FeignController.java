package ru.alfabank.testtask.gifbyexchangerate.controller;

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
@RequestMapping(value = "/gifs/by-exchange-rate")
public class FeignController {

    @Autowired
    private Environment env;

    @Autowired
    private GifClient gifClient;

    @Autowired
    private ExchangeRateClient rateClient;

    private String exchangeAppId;

    private String base;

    @GetMapping
    public ResponseEntity getGifByExchangeRate(@RequestParam("symbols") String symbols) {
        String gifUrl = (String) gifClient.readRandomGifByTag(
                env.getProperty("app.giphy.id"),
                isRich(symbols) ? "rich" : "broke"
        ).getData().get("embed_url");
        return ResponseEntity.ok().body("<iframe src=\"" + gifUrl + "\"></iframe>");
    }

    public boolean isRich(String symbols) {
        Long millis = System.currentTimeMillis();
        BigDecimal todayRate = getRate(millis, symbols);
        BigDecimal yesterdayRate = getRate(millis -  86_400_000L, symbols);
        return todayRate.compareTo(yesterdayRate) != -1;
    }

    public BigDecimal getRate(Long millis, String symbols) {
        exchangeAppId = env.getProperty("app.exchangerates.id");
        base = env.getProperty("base.currency");
        return new BigDecimal(rateClient.readExchangeRateBySymbolsAndDate(
                exchangeAppId,
                getFormatDate(millis),
                base,
                symbols
        ).getRates().get(symbols));
    }

    public String getFormatDate(Long millis) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(new Date(millis));
    }
}
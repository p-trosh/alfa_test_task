package ru.alfabank.testtask.gifbyexchangerate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.alfabank.testtask.gifbyexchangerate.config.FeignClientConfig;
import ru.alfabank.testtask.gifbyexchangerate.rest.ExchangeRateResponse;

@FeignClient(
        value = "${app.exchangerates.config.name}",
        url = "${app.exchangerates.config.url}",
        configuration = FeignClientConfig.class
)
public interface ExchangeRateClient {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{date}.json?app_id={app_id}&base={base}&symbols={symbols}"
    )
    ExchangeRateResponse readExchangeRateBySymbolsAndDate(
            @PathVariable("app_id") String appId,
            @PathVariable("date") String date,
            @PathVariable("base") String base,
            @PathVariable("symbols") String symbols
    );
}



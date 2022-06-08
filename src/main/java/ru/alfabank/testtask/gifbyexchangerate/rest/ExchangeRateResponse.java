package ru.alfabank.testtask.gifbyexchangerate.rest;

import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRateResponse {
    private Map<String, String> rates;
}



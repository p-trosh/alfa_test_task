package ru.alfabank.testtask.gifbyexchangerate.rest;

import lombok.Data;

import java.util.Map;

@Data
public class ExchangeRateRequest {
    private Map<String, String> rates;
}



package ru.alfabank.testtask.gifbyexchangerate.rest;

import lombok.Data;

import java.util.Map;

@Data
public class GifResponse {
    private Map<String, Object> data;
}

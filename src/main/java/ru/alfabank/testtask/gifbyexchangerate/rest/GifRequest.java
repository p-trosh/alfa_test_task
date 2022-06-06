package ru.alfabank.testtask.gifbyexchangerate.rest;

import lombok.Data;

import java.util.Map;

@Data
public class GifRequest {
    private Map<String, Object> data;
}

package ru.alfabank.testtask.gifbyexchangerate.config;

import feign.Response;
import feign.codec.ErrorDecoder;


public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (methodKey.contains("ExchangeRate")) {
            switch (response.status()) {
                case 400:
                    return new Exception("Client requested rates for an unsupported base currency");
                case 401:
                    return new Exception("Client did not provide correct App ID");
                case 403:
                    return new Exception("Access restricted");
                case 404:
                    return new Exception("Client requested a non-existent resource/route");
                case 429:
                    return new Exception("Client doesn’t have permission to access requested route/feature");
                default:
                    return new Exception("Common Feign Exception");
            }
        }
        if (methodKey.contains("RandomGif")) {
            switch (response.status()) {
                case 400:
                    return new Exception(
                            "Your request was formatted incorrectly or missing a required parameter(s)."
                    );
                case 403:
                    return new Exception(
                            "You weren't authorized to make your request; " +
                            "most likely this indicates an issue with your API Key."
                    );
                case 404:
                    return new Exception(
                            "The particular GIF or Sticker you are requesting was not found. " +
                            "This occurs, for example, if you request a GIF by using an id that does not exist."
                    );
                case 414:
                    return new Exception(
                            "The length of the search query exceeds 50 characters."
                    );
                case 429:
                    return new Exception(
                            "Your API Key is making too many requests. " +
                            "Read about requesting a Production Key to upgrade your API Key rate limits."
                    );
                default:
                    return new Exception("Common Feign Exception");
            }
        }
        return null;
    }
}

package ru.alfabank.testtask.gifbyexchangerate.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.alfabank.testtask.gifbyexchangerate.rest.GifRequest;

@FeignClient(
        value = "${app.giphy.config.name}",
        url = "${app.giphy.config.url}"
)
public interface GifClient {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "?api_key={api_key}&tag={tag}"
    )
    GifRequest readRandomGifByTag(
            @PathVariable("api_key") String apiKey,
            @PathVariable("tag") String tag
    );
}

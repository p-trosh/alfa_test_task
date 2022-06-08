package ru.alfabank.testtask.gifbyexchangerate;


import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import ru.alfabank.testtask.gifbyexchangerate.client.ExchangeRateClient;
import ru.alfabank.testtask.gifbyexchangerate.client.GifClient;
import ru.alfabank.testtask.gifbyexchangerate.controller.FeignController;
import ru.alfabank.testtask.gifbyexchangerate.rest.ExchangeRateResponse;
import ru.alfabank.testtask.gifbyexchangerate.rest.GifResponse;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
class FeignControllerTests {

	@Mock
	private Environment env;

	@Mock
	private GifClient gifClient;

	@Mock
	private ExchangeRateClient rateClient;

	@InjectMocks
	private FeignController controller;

	@Before
	public void setUp() throws Exception {
		openMocks(this);

	}

	@Test
	void getFormatDateTest() {
		String expected = "1970-01-01";
		String actual = controller.getFormatDate(1L);
		assertEquals(expected, actual);
	}

	@Test
	void getRateTest() {
		Long millis = 1L;
		String symbols = "RUB";
		ExchangeRateResponse response = new ExchangeRateResponse();
		HashMap ratesMap = new HashMap<>();
		ratesMap.put(symbols, "9.9999");
		response.setRates(ratesMap);

		when(env.getProperty("app.exchangerates.id"))
				.thenReturn("id");
		when(env.getProperty("base.currency"))
				.thenReturn("USD");
		when(rateClient.readExchangeRateBySymbolsAndDate(
				"id",
				"1970-01-01",
				"USD",
				symbols
		)).thenReturn(response);
		BigDecimal expected = new BigDecimal("9.9999");
		BigDecimal actual = controller.getRate(millis, symbols);
		assertEquals(0, expected.compareTo(actual));
		verify(env, times(2)).getProperty(any(String.class));
	}

	@Test
	void isRichTest() {
		FeignController feignController = spy(controller);

		doReturn(new BigDecimal("9.999")).when(feignController).getRate(any(Long.class), any(String.class));
		assertEquals(true, feignController.isRich("RUB"));
		verify(feignController, times(2)).getRate(any(Long.class), eq("RUB"));
	}

	@Test
	void getGifByExchangeRateTest() {
		FeignController feignController = spy(controller);
		String symbols = "RUB";

		GifResponse responseRich = new GifResponse();
		GifResponse responseBroke = new GifResponse();

		HashMap dataMapRich = new HashMap<>();
		HashMap dataMapBroke = new HashMap<>();

		dataMapRich.put("embed_url", "richUrl");
		dataMapBroke.put("embed_url", "brokeUrl");

		responseRich.setData(dataMapRich);
		responseBroke.setData(dataMapBroke);

		when(env.getProperty("app.giphy.id")).thenReturn("id");
		when(gifClient.readRandomGifByTag("id", "rich")).thenReturn(responseRich);
		when(gifClient.readRandomGifByTag("id", "broke")).thenReturn(responseBroke);

		doReturn(true).when(feignController).isRich(symbols);
		ResponseEntity responseEntity = feignController.getGifByExchangeRate(symbols);
		String responseEntityBody = (String) responseEntity.getBody();
		assertTrue(responseEntityBody.contains("richUrl"));

		doReturn(false).when(feignController).isRich(symbols);
		responseEntity = feignController.getGifByExchangeRate(symbols);
		responseEntityBody = (String) responseEntity.getBody();
		assertTrue(responseEntityBody.contains("brokeUrl"));

	}
}

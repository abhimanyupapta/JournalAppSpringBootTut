package net.engineeringdigest.journal.app.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journal.app.cache.AppCache;
import net.engineeringdigest.journal.app.constants.PlaceHolders;
import net.engineeringdigest.journal.app.weather.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WeatherService {

    @Value("${weatherAPI.key}")
    private String apiKey;

    @Autowired
    AppCache appCache;

   // @Value("${weatherAPI.url}")
    //private String api;

    @Autowired
    private RestTemplate restTemplate; //handles http requests for us and gives us the response

    @Autowired
    RedisService redisService;

    public WeatherResponse getWeather(String city) {

        log.info("Checking cached weather");
        WeatherResponse weatherResponse = redisService.get("weather_of_"+ city, WeatherResponse.class);
        if(weatherResponse != null) {
            log.info("returning cached value");
            return weatherResponse;
        } else {
            log.info("no cached value getting from api");
            ResponseEntity<WeatherResponse> response;
            try {
                String finalAPI = appCache.getAppCache().get(AppCache.Keys.WEATHER_API.toString()).replace(PlaceHolders.API_KEY, apiKey).replace(PlaceHolders.CITY, city);
                response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
                WeatherResponse body = response.getBody();
                if(body != null) {
                    redisService.set("weather_of_"+ city, body, 300L);
                }
                return body;
            } catch(Exception e) {
                log.error("Exception while fetching weather");
                return null;
            }
        }
    }
}

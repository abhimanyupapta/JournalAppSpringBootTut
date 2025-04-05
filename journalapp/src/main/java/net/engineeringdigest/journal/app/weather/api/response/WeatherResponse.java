package net.engineeringdigest.journal.app.weather.api.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class WeatherResponse{

        private Current current;

        @Getter
        @Setter
        public class Current{

            @JsonProperty("temp_c")
            private double tempC;
            @JsonProperty("temp_f")
            private double tempF;
            @JsonProperty("is_day")
            private int isDay;
            @JsonProperty("feelslike_c")
            private double feelsLikeC;
            @JsonProperty("feelslike_f")
            private double feelsLikeF;
        }

}


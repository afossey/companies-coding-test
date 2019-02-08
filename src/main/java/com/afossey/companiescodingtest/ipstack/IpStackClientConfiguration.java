package com.afossey.companiescodingtest.ipstack;

import feign.Feign;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IpStackClientConfiguration {

  @Bean
  public IpStackClient ipStackClient(IpStackClientProperties props) {

    // Add the api key to every api calls
    // I admit I have a bit over-engineered this part :)
    class AddApiKeyInterceptor implements RequestInterceptor {

      private static final String ACCESS_KEY = "access_key";

      @Override
      public void apply(RequestTemplate template) {
        template.query(ACCESS_KEY, props.getApikey());
      }
    }

    return Feign.builder()
        .encoder(new JacksonEncoder())
        .decoder(new JacksonDecoder())
        .requestInterceptor(new AddApiKeyInterceptor())
        .target(IpStackClient.class, props.getApiurl());
  }
}

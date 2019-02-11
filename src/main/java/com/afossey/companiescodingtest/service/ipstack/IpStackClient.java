package com.afossey.companiescodingtest.service.ipstack;

import io.reactivex.Maybe;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Mono;

@Component
public class IpStackClient {

  private IpStackClientProperties props;
  private WebClient client;

  public IpStackClient(IpStackClientProperties props) {
    this.client = WebClient.create(props.getApiurl());
    this.props = props;
  }

  public Maybe<String> getCountry(String domain) {
    String access_key = this.props.getApikey();
    Mono<String> countryName = this.client.get()
        .uri("/{domain}?access_key={access_key}", domain, access_key).exchange()
        .flatMap(res -> res.bodyToMono(IpStackResponse.class))
        .filter(res -> StringUtils.isNotBlank(res.getCountryName()))
        .map(IpStackResponse::getCountryName);
    return RxJava2Adapter.monoToMaybe(countryName);
  }
}

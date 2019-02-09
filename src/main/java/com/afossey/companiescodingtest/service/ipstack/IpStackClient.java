package com.afossey.companiescodingtest.service.ipstack;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class IpStackClient {

  private IpStackClientProperties props;
  private WebClient client;

  public IpStackClient(IpStackClientProperties props) {
    this.client = WebClient.create(props.getApiurl());
    this.props = props;
  }

  public Mono<IpStackResponse> getCountry(String domain) {
    String access_key = this.props.getApikey();
    return this.client.get().uri("/{domain}?access_key={access_key}", domain, access_key).exchange()
        .flatMap(res -> res.bodyToMono(IpStackResponse.class));
  }
}

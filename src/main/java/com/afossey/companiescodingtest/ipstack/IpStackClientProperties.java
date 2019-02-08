package com.afossey.companiescodingtest.ipstack;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "company.ipstack")
public class IpStackClientProperties {

  private String apiurl;
  private String apikey;
}

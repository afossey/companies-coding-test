package com.afossey.companiescodingtest.ipstack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class IpStackResponse {

  String countryName;

  @JsonCreator
  IpStackResponse(@JsonProperty("country_name") String cn) {
    this.countryName = cn;
  }
}

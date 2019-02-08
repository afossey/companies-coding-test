package com.afossey.companiescodingtest.ipstack;

import feign.Param;
import feign.RequestLine;

public interface IpStackClient {

  @RequestLine("GET /{domain}")
  IpStackResponse getCountry(@Param("domain") String domain);
}

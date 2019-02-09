package com.afossey.companiescodingtest.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class CountryCompanyPair {

  String country;
  ObjectNode company;

  public static CountryCompanyPair of(String _country, ObjectNode _company) {
    return new CountryCompanyPair(_country, _company);
  }
}

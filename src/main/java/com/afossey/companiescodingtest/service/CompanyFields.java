package com.afossey.companiescodingtest.service;

public enum CompanyFields {
  ID("_id"),
  NAME("name"),
  HOMEPAGE_URL("homepage_url"),
  TOTAL_MONEY_RAISED("total_money_raised");

  private String value;

  CompanyFields(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

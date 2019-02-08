package com.afossey.companiescodingtest.api;

public enum LogMessages {

  A_COMPANY_MUST_HAVE_A_NAME("A company must have a name. Malformed company node id => {}"),
  INVALID_FIRST_CHAR(
      "A node could not be processed. Valid first chars for name are [{}], character was {}."),
  NEW_FIELD_ENCOUNTERED("New field encountered : {}"),
  CSV_GENERATED("CSVs have been generated in {}."),
  REPORT_LOG("Country : {} -- Number of companies : {} -- Average funding : {}."),
  UNABLE_TO_PARSE_FUNDING("Unable to parse total money raised from {}. e : {}."),
  UNABLE_TO_PARSE_DOMAIN("Unable to get domain from {}. e : {}.");

  private String value;

  LogMessages(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}

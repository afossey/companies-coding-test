package com.afossey.companiescodingtest.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CountryReportTests {

  @Test
  public void it_should_compute_average_funding() {
    CountryReport countryReport = new CountryReport();
    countryReport.addFunding(5f);
    countryReport.addFunding(2f);

    assertThat(countryReport.getAverageFunding()).isEqualTo(3.5f);
  }

  @Test
  public void it_should_compute_average_zero_if_no_fundings() {
    CountryReport countryReport = new CountryReport();
    assertThat(countryReport.getAverageFunding()).isEqualTo(0f);
  }
}

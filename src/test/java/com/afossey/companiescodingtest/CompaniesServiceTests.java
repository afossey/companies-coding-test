package com.afossey.companiescodingtest;


import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class CompaniesServiceTests {

  @Autowired
  private CompaniesService service;

  @Test
  public void it_should_complete_the_coding_test_light() throws IOException {
    File file = new ClassPathResource("companies_light.json").getFile();
    service.printReport(file, Boolean.TRUE);
  }

  /*
    Ip Stack is disabled by default on the heavy test because of following concerns :
    - The IpStack API rate limit is 10K hits per month.
    - IpStack API seems too slow to process all companies. (took 4O mins to process 3.5K companies)
  */
  @Test
  public void it_should_complete_the_coding_test_heavy_without_ip_stack() throws IOException {
    File file = new ClassPathResource("companies.json").getFile();
    service.printReport(file, Boolean.FALSE);
  }

  @Test
  public void it_should_get_domain() {
    Optional<String> domain = this.service
        .getDomainFrom("http://adventnet.com/test/with/param");
    assertThat(domain).isEqualTo(Optional.of("adventnet.com"));
  }

  @Test
  public void it_should_parse_total_raised_money() {
    Optional<Float> funding = this.service
        .parseTotalMoneyRaised("$4.5M");
    assertThat(funding).isEqualTo(Optional.of(4500000f));

    Optional<Float> fundingTwo = this.service
        .parseTotalMoneyRaised("€4.5K");
    assertThat(fundingTwo).isEqualTo(Optional.of(4500f));

    Optional<Float> fundingThree = this.service
        .parseTotalMoneyRaised("£4.5k");
    assertThat(fundingThree).isEqualTo(Optional.of(4500f));

    Optional<Float> fundingFour = this.service
        .parseTotalMoneyRaised("4.5B");
    assertThat(fundingFour).isEqualTo(Optional.of(4500000000f));
  }

  @Test
  public void it_should_fail_parse_total_raised_money_unknown_currency() {
    Optional<Float> funding = this.service
        .parseTotalMoneyRaised("¥4.5K");
    assertThat(funding).isEqualTo(Optional.empty());
  }
}

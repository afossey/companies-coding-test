package com.afossey.companiescodingtest.ipstack;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class IpStackClientTests {

  @Autowired
  private IpStackClient client;

  @Test
  public void it_should_call_ipstack_and_get_country() {
    IpStackResponse response = this.client.getCountry("google.com");
    assertThat(response).isNotNull();
    assertThat(response.getCountryName()).isNotEmpty();
  }

  @Test
  public void it_should_fail_on_stale_company_data() {
    IpStackResponse response = this.client.getCountry("adventnet.com");
    assertThat(response).isNotNull();
    assertThat(response.getCountryName()).isNull();
  }
}

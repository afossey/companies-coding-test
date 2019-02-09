package com.afossey.companiescodingtest.service.ipstack;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class IpStackClientTests {

  @Autowired
  private IpStackClient client;

  @Autowired
  private IpStackClientProperties props;

  @Before
  public void init() {
    // prevent IpStack integration tests from starting if no API KEY has been set
    Assume.assumeFalse(props.getApikey().equalsIgnoreCase("YOUR_API_KEY"));
  }

  @Test
  public void it_should_call_ipstack_and_get_country() {
    IpStackResponse res = this.client.getCountry("google.com").block();
    assertThat(res).isNotNull();
    assertThat(res.getCountryName()).isNotEmpty();
  }

  @Test
  public void it_should_fail_on_stale_company_data() {
    IpStackResponse response = this.client.getCountry("adventnet.com").block();
    assertThat(response).isNotNull();
    assertThat(response.getCountryName()).isNull();
  }
}

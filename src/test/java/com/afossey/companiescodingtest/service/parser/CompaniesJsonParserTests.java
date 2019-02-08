package com.afossey.companiescodingtest.service.parser;

import com.afossey.companiescodingtest.FileParser;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.reactivex.subscribers.TestSubscriber;
import java.io.File;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CompaniesJsonParserTests {

  private FileParser<ObjectNode> parser;

  @Before
  public void init() {
    this.parser = new CompaniesJsonParser();
  }

  @Test
  public void it_should_parse_companies_from_light_file() throws IOException {
    TestSubscriber<ObjectNode> subscriber = new TestSubscriber<>();
    File file = new ClassPathResource("companies_light.json").getFile();
    parser.parse(file).subscribe(subscriber);
    subscriber.assertComplete();
    subscriber.assertNoErrors();
    subscriber.assertValueCount(4);
  }

  @Test
  public void it_should_parse_companies_from_heavy_file() throws IOException {
    TestSubscriber<ObjectNode> subscriber = new TestSubscriber<>();
    File file = new ClassPathResource("companies.json").getFile();
    parser.parse(file).subscribe(subscriber);
    subscriber.assertComplete();
    subscriber.assertNoErrors();
  }
}

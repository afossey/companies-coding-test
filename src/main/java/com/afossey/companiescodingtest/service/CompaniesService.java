package com.afossey.companiescodingtest.service;

import static com.afossey.companiescodingtest.service.LogMessages.CSV_GENERATED;
import static com.afossey.companiescodingtest.service.LogMessages.REPORT_LOG;
import static com.afossey.companiescodingtest.service.LogMessages.UNABLE_TO_PARSE_DOMAIN;
import static com.afossey.companiescodingtest.service.LogMessages.UNABLE_TO_PARSE_FUNDING;

import com.afossey.companiescodingtest.FileParser;
import com.afossey.companiescodingtest.FileWriter;
import com.afossey.companiescodingtest.service.ipstack.IpStackClient;
import com.afossey.companiescodingtest.service.writer.CompaniesCsvWriterProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.net.URI;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class CompaniesService {

  private final FileParser<ObjectNode> parser;
  private final FileWriter<ObjectNode> writer;
  private final CompaniesCsvWriterProperties writerProps;
  private final IpStackClient client;

  /**
   * Process each company from a file as specified by the coding test
   *
   * @param withIpStack enable IpStack call
   */
  public void printReport(File file, Boolean withIpStack) {
    final SortedMap<String, CountryReport> report = new TreeMap<>();

    this.parser.parse(file).subscribe(
        company -> {
          this.writer.write(company);
          if (Boolean.TRUE.equals(withIpStack)) {
            getDomainFrom(company.get(CompanyFields.HOMEPAGE_URL.getValue()).asText())
                .flatMap(domain -> Optional.ofNullable(this.client.getCountry(domain)))
                .flatMap(ipStackResponse -> Optional.ofNullable(ipStackResponse.getCountryName()))
                .ifPresent(country -> report(report, company, country));
          }
        }
    ).dispose();

    log.info(CSV_GENERATED.getValue(), this.writerProps.getFormattedCsvDirPath());
    if (Boolean.TRUE.equals(withIpStack)) {
      // print report
      report.forEach(
          (key, value) -> log.info(REPORT_LOG.getValue(),
              key, value.getCompanyCount(), value.getAverageFunding()));
    }
  }

  // Add country and company to the report map.
  private void report(SortedMap<String, CountryReport> report, ObjectNode company, String country) {
    String totalMoneyRaisedLitteral = company.get(CompanyFields.TOTAL_MONEY_RAISED.getValue())
        .asText();
    float funding = parseTotalMoneyRaised(totalMoneyRaisedLitteral).orElse(0f);
    if (!report.containsKey(country)) {
      report.put(country, new CountryReport());
    }
    report.get(country).addFunding(funding);
  }

  /**
   * we need to parse the amount from string to a numeric representation.
   * E.g $4.5M -> 4 500 000
   *
   * @return {@link Optional} of {@link Float}
   */
  protected Optional<Float> parseTotalMoneyRaised(String totalMoneyRaisedLitteral) {
    try {

      String valueOnly = totalMoneyRaisedLitteral.replaceAll("[BMKk$€£]", "");
      if (totalMoneyRaisedLitteral.contains("K") || totalMoneyRaisedLitteral.contains("k")) {
        return Optional.of(Float.valueOf(valueOnly) * 1000);
      }
      if (totalMoneyRaisedLitteral.contains("M")) {
        return Optional.of(Float.valueOf(valueOnly) * 1000000);
      }
      if (totalMoneyRaisedLitteral.contains("B")) {
        return Optional.of(Float.valueOf(valueOnly) * 1000000000);
      }
      return Optional.of(Float.valueOf(valueOnly));

    } catch (Exception e) {
      log.warn(UNABLE_TO_PARSE_FUNDING.getValue(), totalMoneyRaisedLitteral,
          e.getMessage());
      return Optional.empty();
    }

  }


  protected Optional<String> getDomainFrom(String url) {
    try {
      return Optional.of(URI.create(url).getHost());
    } catch (Exception e) {
      log.warn(UNABLE_TO_PARSE_DOMAIN.getValue(), url, e.getMessage());
      return Optional.empty();
    }
  }
}

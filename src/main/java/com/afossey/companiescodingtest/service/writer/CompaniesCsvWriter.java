package com.afossey.companiescodingtest.service.writer;

import static com.afossey.companiescodingtest.api.LogMessages.A_COMPANY_MUST_HAVE_A_NAME;
import static com.afossey.companiescodingtest.api.LogMessages.INVALID_FIRST_CHAR;
import static com.afossey.companiescodingtest.api.LogMessages.NEW_FIELD_ENCOUNTERED;
import static java.util.stream.Collectors.joining;

import com.afossey.companiescodingtest.api.CompanyFields;
import com.afossey.companiescodingtest.api.FileWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class CompaniesCsvWriter implements FileWriter<ObjectNode> {

  private static final String CSV_SEP_STR = ";";
  private static final String NEW_LINE_STR = "\n";
  private static final String RESERVED_CHAR_REGEX = "[" + NEW_LINE_STR + CSV_SEP_STR + "]";

  private Map<Character, File> fileHandles;
  private SortedSet<String> columnFields;
  private Set<String> encounteredFields;
  private CompaniesCsvWriterProperties props;

  public CompaniesCsvWriter(CompaniesCsvWriterProperties properties) {
    this.fileHandles = new HashMap<>();
    this.columnFields = new TreeSet<>();
    this.encounteredFields = new HashSet<>();
    this.props = properties;
  }

  // create or clean csv directory
  @PostConstruct
  private void init() throws IOException {
    File file = new File(props.getFormattedCsvDirPath());
    if (!file.mkdir()) {
      FileUtils.cleanDirectory(file);
    }
  }

  /**
   * Main and only public method.
   * Write a company into a CSV file.
   * If not existing, create the file, write the csv header and append the company row.
   * If existing, retrieve the file handle, append the company row.
   */
  @Override
  public void write(ObjectNode node) throws IOException {

    Character firstLetter = getCompanyNameFirstChar(node).orElse(null);
    if (firstLetter == null) {
      return;
    }

    // Retrieve the File handle from the map if it has already been opened.
    if (fileHandles.containsKey(firstLetter)) {
      File file = fileHandles.get(firstLetter);
      writeRow(file, node);
    } else {
      File file = new File(props.getFormattedCsvDirPath() + firstLetter + ".csv");
      if (!file.exists()) {
        file.createNewFile();
      }
      initColumnFieldsIfEmpty(node);
      writeHeader(file);
      writeRow(file, node);
      fileHandles.put(firstLetter, file);
    }
  }

  // Perform some validations on the company name and retrieve its first char.
  protected Optional<Character> getCompanyNameFirstChar(ObjectNode node) {
    final String companyName = node.get(CompanyFields.NAME.getValue()).asText();
    if (StringUtils.isEmpty(companyName)) {
      log.warn(A_COMPANY_MUST_HAVE_A_NAME.getValue(), node.get(CompanyFields.ID.getValue()));
      return Optional.empty();
    }
    Character firstLetter = companyName.toUpperCase().charAt(0);
    if (props.getAlpharange().indexOf(firstLetter) < 0) {
      log.trace(INVALID_FIRST_CHAR.getValue(), props.getAlpharange(), firstLetter);
      return Optional.empty();
    }
    return Optional.of(firstLetter);
  }

  // As discussed by email, we must decide on a CSV columns structure after reading the first company.
  private void initColumnFieldsIfEmpty(ObjectNode node) {
    if (this.columnFields.isEmpty()) {
      Iterator<String> fieldsIterator = node.fieldNames();
      while (fieldsIterator.hasNext()) {
        this.columnFields.add(fieldsIterator.next());
      }
      this.encounteredFields.clear();
      this.encounteredFields.addAll(this.columnFields);
    }
  }

  // As discussed by email, we log new encountered fields not in the predefined columns structure.
  private void checkForNewEncounteredFields(ObjectNode node) {
    Iterator<String> fieldsIterator = node.fieldNames();
    while (fieldsIterator.hasNext()) {
      String fieldName = fieldsIterator.next();
      if (!this.encounteredFields.contains(fieldName)) {
        log.info(NEW_FIELD_ENCOUNTERED.getValue(), fieldName);
        this.encounteredFields.add(fieldName);
      }
    }
  }

  private void writeHeader(File file) throws IOException {
    String header = String.join(CSV_SEP_STR, this.columnFields) + NEW_LINE_STR;
    writeToFile(file, header, false);
  }

  private void writeRow(File file, ObjectNode node) throws IOException {
    checkForNewEncounteredFields(node);
    String companyLine = this.columnFields.stream()
        .map(field -> getCleanedFieldValue(node, field))
        .collect(joining(CSV_SEP_STR));
    companyLine += NEW_LINE_STR;
    writeToFile(file, companyLine, true);
  }

  // Get the field value from which newlines and/or semicolons are removed
  protected String getCleanedFieldValue(ObjectNode node, String field) {
    JsonNode valueNode = node.get(field);
    if (valueNode != null) {
      return valueNode.asText().replaceAll(RESERVED_CHAR_REGEX, Strings.EMPTY);
    }
    return Strings.EMPTY;
  }

  private void writeToFile(File file, String content, boolean append) throws IOException {
    FileUtils.write(file, content, StandardCharsets.UTF_8, append);
  }

}

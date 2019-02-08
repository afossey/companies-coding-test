package com.afossey.companiescodingtest.service.writer;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class CompaniesCsvWriterTests {

  @Autowired
  private CompaniesCsvWriter writer;

  @Autowired
  private CompaniesCsvWriterProperties props;

  @Test
  public void it_should_get_the_first_char_in_valid_company_name() throws IOException {
    ObjectNode node = new ObjectMapper().readValue("{\n"
        + "  \"name\": \"Zoho\",\n"
        + "  \"permalink\": \"abc4\"\n"
        + "}", ObjectNode.class);
    assertThat(this.writer.getCompanyNameFirstChar(node)).isEqualTo(Optional.of('Z'));
  }

  @Test
  public void it_should_not_get_the_first_char_in_invalid_company_name() throws IOException {
    ObjectNode node = new ObjectMapper().readValue("{\n"
        + "  \"name\": \"1test\",\n"
        + "  \"permalink\": \"abc4\"\n"
        + "}", ObjectNode.class);
    assertThat(this.writer.getCompanyNameFirstChar(node)).isEqualTo(Optional.empty());
  }

  @Test
  public void it_should_write_a_node() throws IOException {
    ObjectNode validNode = new ObjectMapper().readValue("{\n"
        + "  \"name\": \"Zoho\",\n"
        + "  \"permalink\": \"abc4\"\n"
        + "}", ObjectNode.class);

    this.writer.write(validNode);
    List<String> lines = readFileCreatedFrom(validNode);
    assertThat(lines).size().isEqualTo(2);
    assertThat(lines).contains("name;permalink", "Zoho;abc4");
  }

  @Test(expected = NoSuchFileException.class)
  public void it_should_ignore_a_node_with_no_name() throws IOException {
    ObjectNode invalidNode = new ObjectMapper().readValue("{\n"
        + "  \"name\": \"\",\n"
        + "  \"permalink\": \"abc4\"\n"
        + "}", ObjectNode.class);

    this.writer.write(invalidNode);
    readFileCreatedFrom(invalidNode); // no file created
  }

  @Test(expected = NoSuchFileException.class)
  public void it_should_ignore_a_node_with_invalid_name() throws IOException {
    ObjectNode invalidNode = new ObjectMapper().readValue("{\n"
        + "  \"name\": \"@\",\n"
        + "  \"permalink\": \"abc4\"\n"
        + "}", ObjectNode.class);

    this.writer.write(invalidNode);
    readFileCreatedFrom(invalidNode); // no file created
  }

  @Test
  public void it_should_get_fields_value_without_reserved_chars() throws IOException {
    ObjectNode validNode = new ObjectMapper().readValue("{\n"
        + "  \"name\": \"Zoho\",\n"
        + "  \"permalink\": \"abc4;\\n\"\n"
        + "}", ObjectNode.class);

    String cleanedPermalink = this.writer.getCleanedFieldValue(validNode, "permalink");
    assertThat(cleanedPermalink).isEqualTo("abc4");
  }

  private List<String> readFileCreatedFrom(ObjectNode validNode) throws IOException {
    return Files.readAllLines(
        Paths.get(this.props.getFormattedCsvDirPath() +
            this.writer.getCompanyNameFirstChar(validNode).orElse(null) + ".csv"));
  }
}

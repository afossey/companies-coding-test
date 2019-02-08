package com.afossey.companiescodingtest.service.parser;

import com.afossey.companiescodingtest.FileParser;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import java.io.File;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class CompaniesJsonParser implements FileParser<ObjectNode> {

  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
  }

  /**
   * This approach combines streaming, tree model processing and reactive data flow.
   * It reads the input stream looking for the next JSON object, map it into an {@link ObjectNode}
   * then emits its value into a reactive flow that can be observed.
   *
   * @return {@link Flowable} of {@link ObjectNode}
   */
  @Override
  public Flowable<ObjectNode> parse(File file) {
    return Flowable.generate(() -> parser(file), this::getNextOrComplete, JsonParser::close);
  }

  private void getNextOrComplete(JsonParser parser, Emitter<ObjectNode> emitter)
      throws IOException {
    JsonToken current = parser.nextToken();
    // read next JSON object
    if (current == JsonToken.START_OBJECT) {
      emitter.onNext(objectMapper.readTree(parser));
    }
    // end of file
    else if (current == null) {
      emitter.onComplete();
    }
  }

  private JsonParser parser(File file) throws IOException {
    return new JsonFactory().createParser(file);
  }

}

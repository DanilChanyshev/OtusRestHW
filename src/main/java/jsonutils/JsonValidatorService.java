package jsonutils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JsonValidatorService {

  private static ObjectMapper mapper = new ObjectMapper();
  private static JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

  public static void validateJsonSchema(final String json, final String schemaPath) {
    try {
      JsonNode data = mapper.readTree(json);
      InputStream schema = JsonValidatorService.class
              .getClassLoader()
              .getResourceAsStream(schemaPath);
      if (schema == null) {
        throw new IllegalArgumentException("Схема не найдена по пути: %s".formatted(schemaPath));
      }
      JsonNode schemaNode = JsonLoader.fromReader(new InputStreamReader(schema, StandardCharsets.UTF_8));
      JsonSchema schemaValidation = factory.getJsonSchema(schemaNode);
      ProcessingReport report = schemaValidation.validate(data);
      if (!report.isSuccess()) {
        throw new AssertionError("Json валидация провалилась");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}

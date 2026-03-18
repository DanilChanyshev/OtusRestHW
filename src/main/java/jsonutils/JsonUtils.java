package jsonutils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonUtils {

  public static String loadJson(final String path) {
    try (InputStream stream = JsonUtils.class.getClassLoader().getResourceAsStream(path)) {
      if (stream == null) {
        throw new IllegalArgumentException("JSON файл по пути '%s' не найден.".formatted(path));
      }
      return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Не удалось загрузить файл по пути: '%s'".formatted(path), e);
    }
  }
}

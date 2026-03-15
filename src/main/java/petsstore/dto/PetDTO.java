package petsstore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PetDTO {

  private CategoryDTO category;
  private Long id;
  private String name;
  @Singular
  private List<String> photoUrls;
  private String status;
  @Singular
  private List<TagDTO> tags;

  public List<String> getPhotoUrls() {
    return photoUrls != null ? new ArrayList<>(photoUrls) : new ArrayList<>();
  }

  public List<TagDTO> getTags() {
    return tags != null ? new ArrayList<>(tags) : new ArrayList<>();
  }

  public void setPhotoUrls(List<String> photoUrls) {
    this.photoUrls = photoUrls != null ? new ArrayList<>(photoUrls) : new ArrayList<>();
  }

  public void setTags(List<TagDTO> tags) {
    this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
  }
}

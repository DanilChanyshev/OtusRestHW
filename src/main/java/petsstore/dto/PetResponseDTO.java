package petsstore.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class PetResponseDTO {

  private CategoryDTO category;
  private Long id;
  private String name;
  @Singular
  private List<String> photoUrls;
  private String status;
  @Singular
  private List<TagDTO> tags;

  public PetResponseDTO(CategoryDTO category, Long id, String name,
                        List<String> photoUrls, String status, List<TagDTO> tags) {
    this.category = category;
    this.id = id;
    this.name = name;
    this.photoUrls = photoUrls != null ? new ArrayList<>(photoUrls) : new ArrayList<>();
    this.status = status;
    this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
  }

  public List<String> getPhotoUrls() {
    return photoUrls != null ? new ArrayList<>(photoUrls) : new ArrayList<>();
  }

  public List<TagDTO> getTags() {
    return tags != null ? new ArrayList<>(tags) : new ArrayList<>();
  }
}

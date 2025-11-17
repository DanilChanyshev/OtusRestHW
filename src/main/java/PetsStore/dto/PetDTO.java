package PetsStore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PetDTO {

    private CategoryDTO category;
    private Long id;
    private String name;
    private List<String> photoUrls;
    private String status;
    private List<TagDTO> tags;

}

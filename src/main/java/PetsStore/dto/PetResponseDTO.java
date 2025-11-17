
package PetsStore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetResponseDTO {

    private CategoryDTO category;
    private Long id;
    private String name;
    private List<String> photoUrls;
    private String status;
    private List<TagDTO> tags;

}

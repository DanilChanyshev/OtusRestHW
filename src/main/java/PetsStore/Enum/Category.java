package PetsStore.Enum;

import PetsStore.dto.CategoryDTO;

public enum Category {
    DOG(1L, "dog's"),
    CAT(2L, "cat's");

    private final Long id;
    private final String name;

    Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO getCategory() {
        return CategoryDTO.builder()
                .id(id)
                .name(name)
                .build();
    }
}
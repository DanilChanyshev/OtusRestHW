package petsstore.model;

import petsstore.dto.TagDTO;

public enum Tags {
  DOG_TAG(18L, "dog"),
  CAT_TAG(28L, "cat"),
  COLOR_WHITE_TAG(91L, "white");

  private final Long id;
  private final String name;

  Tags(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public TagDTO getTag() {
    return TagDTO.builder()
            .id(id)
            .name(name)
            .build();
  }
}

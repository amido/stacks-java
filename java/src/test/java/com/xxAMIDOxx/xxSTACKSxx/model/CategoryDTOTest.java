package com.xxAMIDOxx.xxSTACKSxx.model;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.CategoryDTO;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
public class CategoryDTOTest {

  @Test
  public void equalsContract() {
    EqualsVerifier.simple().forClass(CategoryDTO.class).verify();
  }
}

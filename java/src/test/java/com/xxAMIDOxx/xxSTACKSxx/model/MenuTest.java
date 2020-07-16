package com.xxAMIDOxx.xxSTACKSxx.model;

import com.xxAMIDOxx.xxSTACKSxx.api.v1.menu.dto.responses.MenuDTO;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Unit")
public class MenuTest {

  @Test
  public void equalsContract() {
    EqualsVerifier.simple().forClass(MenuDTO.class).verify();
  }
}

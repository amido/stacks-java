package com.xxAMIDOxx.xxSTACKSxx.menu.api.v2.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenu;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getBaseURL;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.dto.response.MenuDTO;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.mappers.DomainToDtoMapper;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
    exclude = {CosmosDbRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class QueryMenuControllerImplV2Test {

  private final String GET_MENU_BY_ID = "%s/v2/menu/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepositoryAdapter menuRepositoryAdapter;

  @Test
  void getMenuById() {
    // Given
    Menu menu = createMenu(0);
    MenuDTO expectedResponse = DomainToDtoMapper.toMenuDto(menu);

    when(menuRepositoryAdapter.findById(menu.getId())).thenReturn(Optional.of(menu));

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format(GET_MENU_BY_ID, getBaseURL(port), menu.getId()), MenuDTO.class);

    // Then
    then(response.getBody()).isEqualTo(expectedResponse);
  }

  @Test
  void getMenuByInvalidId() {
    // Given
    Menu menu = createMenu(0);

    when(menuRepositoryAdapter.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    var response =
        this.testRestTemplate.getForEntity(
            String.format(GET_MENU_BY_ID, getBaseURL(port), UUID.randomUUID()), MenuDTO.class);

    // Then
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }
}

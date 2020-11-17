package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.CategoryHelper.createCategory;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenu;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getBaseURL;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getRequestHttpEntity;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.core.api.dto.ErrorResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/** @author ArathyKrishna */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
    exclude = {CosmosDbRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class DeleteItemControllerImplTest {

  public static final String DELETE_ITEM = "%s/v1/menu/%s/category/%s/items/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepositoryAdapter menuRepositoryAdapter;

  @AfterEach
  void tearDown() {
    menuRepositoryAdapter.deleteAll();
  }

  @Test
  void testDeleteItemSuccess() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    Item item = new Item(UUID.randomUUID().toString(), "New Item", "Item description", 12.2d, true);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);
    when(menuRepositoryAdapter.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(DELETE_ITEM, getBaseURL(port), menu.getId(), category.getId(), item.getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ResponseEntity.class);

    // Then
    verify(menuRepositoryAdapter, times(1)).save(menu);
    then(response.getStatusCode()).isEqualTo(OK);
    Optional<Menu> optMenu = menuRepositoryAdapter.findById(menu.getId());
    Menu updated = optMenu.get();
    then(updated.getCategories()).hasSize(1);
    then(updated.getCategories().get(0).getItems()).isNotNull();
  }

  @Test
  void testDeleteItemWithInvalidCategoryId() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    Item item = new Item(UUID.randomUUID().toString(), "New Item", "Item description", 12.2d, true);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);
    when(menuRepositoryAdapter.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(DELETE_ITEM, getBaseURL(port), menu.getId(), item.getId(), item.getId());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    verify(menuRepositoryAdapter, times(0)).save(menu);
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }

  @Test
  void testDeleteItemWithInvalidItemId() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    Item item = new Item(UUID.randomUUID().toString(), "New Item", "Item description", 12.2d, true);
    category.addOrUpdateItem(item);
    menu.addOrUpdateCategory(category);
    when(menuRepositoryAdapter.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(
            DELETE_ITEM, getBaseURL(port), menu.getId(), category.getId(), UUID.randomUUID());

    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    verify(menuRepositoryAdapter, times(0)).save(menu);
    then(response.getStatusCode()).isEqualTo(NOT_FOUND);
  }
}

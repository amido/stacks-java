package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.CategoryHelper.createCategories;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.CategoryHelper.createCategory;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.ItemHelper.createItem;
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
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
    exclude = {CosmosDbRepositoriesAutoConfiguration.class, CosmosAutoConfiguration.class})
@Tag("Integration")
@ActiveProfiles("test")
class DeleteCategoryControllerImplTest {

  public static final String DELETE_CATEGORY = "%s/v1/menu/%s/category/%s";

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate testRestTemplate;

  @MockBean private MenuRepositoryAdapter menuRepositoryAdapter;

  @Test
  void testDeleteCategorySuccess() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    menu.setCategories(List.of(category));
    when(menuRepositoryAdapter.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), category.getId());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    verify(menuRepositoryAdapter, times(1)).save(menu);
    then(response.getStatusCode()).isEqualTo(OK);
    Optional<Menu> optMenu = menuRepositoryAdapter.findById(menu.getId());
    Menu updated = optMenu.get();
    then(updated.getCategories()).isNotNull();
  }

  @Test
  void testDeleteCategoryWithAnItem() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    category.addOrUpdateItem(createItem(0));
    menu.addOrUpdateCategory(category);

    when(menuRepositoryAdapter.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), category.getId());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    then(response.getStatusCode()).isEqualTo(OK);
    ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
    verify(menuRepositoryAdapter, times(1)).save(captor.capture());
    Menu updatedMenu = captor.getValue();
    then(updatedMenu.getCategories()).isNotNull();
  }

  @Test
  void testDeleteCategoryWithInvalidCategoryId() {
    // Given
    Menu menu = createMenu(1);
    Category category = createCategory(0);
    menu.setCategories(List.of(category));
    when(menuRepositoryAdapter.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), menu.getId());
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
  void testDeleteACategoryFromList() {
    // Given
    Menu menu = createMenu(1);
    List<Category> categories = createCategories(2);
    menu.setCategories(categories);
    when(menuRepositoryAdapter.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

    // When
    String requestUrl =
        String.format(DELETE_CATEGORY, getBaseURL(port), menu.getId(), categories.get(0).getId());
    var response =
        this.testRestTemplate.exchange(
            requestUrl,
            HttpMethod.DELETE,
            new HttpEntity<>(getRequestHttpEntity()),
            ErrorResponse.class);

    // Then
    verify(menuRepositoryAdapter, times(1)).save(menu);
    then(response.getStatusCode()).isEqualTo(OK);
    Optional<Menu> byId = menuRepositoryAdapter.findById(menu.getId());
    Menu updatedMenu = byId.get();
    then(updatedMenu.getCategories()).hasSize(1);
  }
}

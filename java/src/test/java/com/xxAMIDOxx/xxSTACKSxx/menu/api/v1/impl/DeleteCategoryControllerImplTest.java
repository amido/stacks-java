package com.xxAMIDOxx.xxSTACKSxx.menu.api.v1.impl;

import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosAutoConfiguration;
import com.microsoft.azure.spring.autoconfigure.cosmosdb.CosmosDbRepositoriesAutoConfiguration;
import com.xxAMIDOxx.xxSTACKSxx.core.api.dto.ErrorResponse;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Item;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepository;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.CategoryHelper.createCategory;
import static com.xxAMIDOxx.xxSTACKSxx.menu.domain.MenuHelper.createMenu;
import static com.xxAMIDOxx.xxSTACKSxx.util.TestHelper.getBaseURL;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration(
        exclude = {
                CosmosDbRepositoriesAutoConfiguration.class,
                CosmosAutoConfiguration.class
        })
@Tag("Integration")
class DeleteCategoryControllerImplTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private MenuRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testDeleteCategorySuccess() {
        // Given
        Menu menu = createMenu(1);
        Category category = createCategory(0);
        menu.setCategories(List.of(category));
        when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

        // When
        var requestEntity = getObjectHttpEntity();

        String requestUrl =
                String.format("%s/v1/menu/%s/category/%s", getBaseURL(port), menu.getId(), category.getId());
        var response =
                this.testRestTemplate.exchange(requestUrl, HttpMethod.DELETE, requestEntity, ErrorResponse.class);

        // Then
        ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
        verify(repository, times(1)).save(menu);
        then(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    void testDeleteCategoryWithAnItem() {
        // Given
        Menu menu = createMenu(1);
        Category category = createCategory(0);
        Item item =
                new Item(UUID.randomUUID().toString(), "New Item", "New item description", 14.2d, true);
        category.setItems(List.of(item));
        menu.addUpdateCategory(category);
        when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

        // When
        var requestEntity = getObjectHttpEntity();

        String requestUrl =
                String.format("%s/v1/menu/%s/category/%s", getBaseURL(port), menu.getId(), category.getId());
        var response =
                this.testRestTemplate.exchange(requestUrl, HttpMethod.DELETE, requestEntity, ErrorResponse.class);

        // Then
        ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
        verify(repository, times(0)).save(menu);
        then(response.getStatusCode()).isEqualTo(CONFLICT);
    }

    private HttpEntity<Object> getObjectHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(headers);
    }

    @Test
    void testDeleteCategoryWithInvalidCategoryId() {
        // Given
        Menu menu = createMenu(1);
        Category category = createCategory(0);
        menu.setCategories(List.of(category));
        when(repository.findById(eq(menu.getId()))).thenReturn(Optional.of(menu));

        // When
        var requestEntity = getObjectHttpEntity();

        String requestUrl =
                String.format("%s/v1/menu/%s/category/%s", getBaseURL(port), menu.getId(), menu.getId());
        var response =
                this.testRestTemplate.exchange(requestUrl, HttpMethod.DELETE, requestEntity, ErrorResponse.class);

        // Then
        ArgumentCaptor<Menu> captor = ArgumentCaptor.forClass(Menu.class);
        verify(repository, times(0)).save(menu);
        then(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

}
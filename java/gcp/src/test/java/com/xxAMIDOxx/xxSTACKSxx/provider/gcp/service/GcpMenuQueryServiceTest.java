package com.xxAMIDOxx.xxSTACKSxx.provider.gcp.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Category;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import com.xxAMIDOxx.xxSTACKSxx.menu.service.MenuQueryService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Tag("Unit")
public class GcpMenuQueryServiceTest {

  @Test
  void findAll() {

    MenuRepositoryAdapter menuRepositoryAdapter = mock(MenuRepositoryAdapter.class);
    MenuQueryService menuQueryServiceImpl = new GcpMenuQueryService(menuRepositoryAdapter);

    Pageable pageable = mock(Pageable.class);

    List<Menu> results = createMenus(2);
    Page<Menu> page1 = new PageImpl<>(results, pageable, 2);
    Page<Menu> page2 = new PageImpl<>(results, pageable, 2);

    // Given
    given(menuRepositoryAdapter.findAll(any(Pageable.class))).willReturn(page1);
    given(menuRepositoryAdapter.findAll(eq(pageable))).willReturn(page2);

    // When
    List<Menu> actualResults = menuQueryServiceImpl.findAll(2, 5);

    // Then
    then(actualResults).isEqualTo(results);
  }

  public static List<Menu> createMenus(int count) {
    List<Menu> menuList = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      menuList.add(createMenu(i));
    }
    return menuList;
  }

  public static Menu createMenu(int counter) {
    return new Menu(
        UUID.randomUUID().toString(),
        UUID.randomUUID().toString(),
        counter + " Menu",
        counter + " Menu Description",
        new ArrayList<Category>(),
        true);
  }
}

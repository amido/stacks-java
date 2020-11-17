package com.xxAMIDOxx.xxSTACKSxx.provider.azure.repository;

import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AzureMenuRepositoryAdapter implements MenuRepositoryAdapter {

  @Autowired protected AzureMenuRepository azureMenuRepository;

  public Page<Menu> findAllByRestaurantId(String restaurantId, Pageable pageable) {

    Page<Menu> menus =
        azureMenuRepository
            .findAllByRestaurantId(restaurantId, pageable)
            .map(this::azureMenuToMenu);
    return menus;
  }

  @Override
  public Menu save(Menu menu) {
    AzureMenu result = azureMenuRepository.save(this.menuToAzureMenu(menu));
    return this.azureMenuToMenu(result);
  }

  @Override
  public Page<Menu> findAllByRestaurantIdAndName(
      String restaurantId, String name, Pageable pageable) {
    Page<Menu> menus =
        azureMenuRepository
            .findAllByRestaurantIdAndName(restaurantId, name, pageable)
            .map(this::azureMenuToMenu);
    return menus;
  }

  @Override
  public Page<Menu> findAllByNameContaining(String searchTerm, Pageable pageable) {
    Page<Menu> menus =
        azureMenuRepository
            .findAllByNameContaining(searchTerm, pageable)
            .map(this::azureMenuToMenu);
    return menus;
  }

  @Override
  public Page<Menu> findAllByRestaurantIdAndNameContaining(
      String restaurantId, String searchTerm, Pageable pageable) {
    Page<Menu> menus =
        azureMenuRepository
            .findAllByRestaurantIdAndNameContaining(restaurantId, searchTerm, pageable)
            .map(this::azureMenuToMenu);
    return menus;
  }

  @Override
  public Optional<Menu> findById(String menuId) {
    Optional<AzureMenu> result = azureMenuRepository.findById(menuId);
    if (result.isPresent()) {
      return Optional.of(azureMenuToMenu(result.get()));
    }
    return Optional.empty();
  }

  @Override
  public void delete(Menu menu) {
    azureMenuRepository.delete(menuToAzureMenu(menu));
  }

  @Override
  public Page<Menu> findAll(Pageable pageable) {
    Page<Menu> menus = azureMenuRepository.findAll(pageable).map(this::azureMenuToMenu);
    return menus;
  }

  @Override
  public void deleteAll() {
    azureMenuRepository.deleteAll();
  }

  private AzureMenu menuToAzureMenu(Menu menu) {
    AzureMenu azureMenu;
    azureMenu =
        new AzureMenu(
            menu.getId(),
            menu.getRestaurantId(),
            menu.getName(),
            menu.getDescription(),
            menu.getCategories(),
            menu.getEnabled());

    return azureMenu;
  }

  private Menu azureMenuToMenu(AzureMenu azureMenu) {
    Menu menu;
    menu =
        Menu.builder()
            .id(azureMenu.getId())
            .restaurantId(azureMenu.getRestaurantId())
            .name(azureMenu.getName())
            .description(azureMenu.getDescription())
            .categories(azureMenu.getCategories())
            .enabled(azureMenu.getEnabled())
            .build();

    return menu;
  }
}

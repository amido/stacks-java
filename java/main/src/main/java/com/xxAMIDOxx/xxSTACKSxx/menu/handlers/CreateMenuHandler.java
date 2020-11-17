package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.cqrs.handler.CommandHandler;
import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.CreateMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuCreatedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.exception.MenuAlreadyExistsException;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class CreateMenuHandler implements CommandHandler<CreateMenuCommand> {

  protected MenuRepositoryAdapter menuAdapter;

  private ApplicationEventPublisher applicationEventPublisher;

  public CreateMenuHandler(
      MenuRepositoryAdapter menuAdapter, ApplicationEventPublisher applicationEventPublisher) {
    this.menuAdapter = menuAdapter;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  @Override
  public Optional<UUID> handle(CreateMenuCommand command) {

    verifyMenuNotAlreadyExisting(command);

    final UUID id = UUID.randomUUID();
    final Menu menu =
        new Menu(
            id.toString(),
            command.getRestaurantId().toString(),
            command.getName(),
            command.getDescription(),
            new ArrayList<>(),
            command.getEnabled());

    menuAdapter.save(menu);

    applicationEventPublisher.publish(new MenuCreatedEvent(command, id));

    return Optional.of(id);
  }

  protected void verifyMenuNotAlreadyExisting(CreateMenuCommand command) {
    Page<Menu> existing =
        menuAdapter.findAllByRestaurantIdAndName(
            command.getRestaurantId().toString(), command.getName(), PageRequest.of(0, 1));
    if (!existing.getContent().isEmpty()
        && existing.get().anyMatch(m -> m.getName().equals(command.getName()))) {
      throw new MenuAlreadyExistsException(command, command.getRestaurantId(), command.getName());
    }
  }
}

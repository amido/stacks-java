package com.xxAMIDOxx.xxSTACKSxx.menu.handlers;

import com.xxAMIDOxx.xxSTACKSxx.core.messaging.publish.ApplicationEventPublisher;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.DeleteMenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.domain.Menu;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuDeletedEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.events.MenuEvent;
import com.xxAMIDOxx.xxSTACKSxx.menu.repository.MenuRepositoryAdapter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Handler for deleting menu
 *
 * @author ArathyKrishna
 */
@Component
public class DeleteMenuHandler extends MenuBaseCommandHandler<DeleteMenuCommand> {

  public DeleteMenuHandler(
      MenuRepositoryAdapter menuRepositoryAdapter,
      ApplicationEventPublisher applicationEventPublisher) {
    super(menuRepositoryAdapter, applicationEventPublisher);
  }

  @Override
  Optional<UUID> handleCommand(Menu menu, DeleteMenuCommand command) {
    menuRepositoryAdapter.delete(menu);
    return Optional.empty();
  }

  @Override
  List<MenuEvent> raiseApplicationEvents(Menu menu, DeleteMenuCommand command) {
    return Collections.singletonList(new MenuDeletedEvent(command));
  }
}

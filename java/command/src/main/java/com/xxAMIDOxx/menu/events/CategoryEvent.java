package com.xxAMIDOxx.menu.events;

import com.xxAMIDOxx.menu.commands.MenuCommand;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public abstract class CategoryEvent extends MenuEvent {
  private UUID categoryId;

  public CategoryEvent(MenuCommand menuCommand, EventCode eventCode, UUID categoryId) {
    super(menuCommand, eventCode, menuCommand.getMenuId());
    this.categoryId = categoryId;
  }
}

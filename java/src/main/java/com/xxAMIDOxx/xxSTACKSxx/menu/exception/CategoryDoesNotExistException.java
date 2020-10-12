package com.xxAMIDOxx.xxSTACKSxx.menu.exception;

import com.xxAMIDOxx.xxSTACKSxx.menu.commands.MenuCommand;
import com.xxAMIDOxx.xxSTACKSxx.menu.commands.OperationCode;

import java.util.UUID;

public class CategoryDoesNotExistException extends MenuApiException {
  public CategoryDoesNotExistException(MenuCommand command, UUID categoryId) {
    super(
        String.format(
            "A category with the id '%s' does not exist for menu with id '%s'.",
            categoryId, command.getMenuId()),
        ExceptionCode.CATEGORY_DOES_NOT_EXIST,
        command);
  }

  public CategoryDoesNotExistException(String categoryId, String menuId, int operationCode, String correlationId) {
    super(
            String.format(
                    "A category with the id '%s' does not exist for menu with id '%s'.",
                    categoryId, menuId),
            ExceptionCode.CATEGORY_DOES_NOT_EXIST,
            operationCode, correlationId);
  }
}
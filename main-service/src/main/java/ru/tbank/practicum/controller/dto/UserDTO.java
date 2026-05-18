package ru.tbank.practicum.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.tbank.practicum.repository.entity.User;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
  private String login;

  public UserDTO(User user) {
    this.login = user.getLogin();
  }
}

package ru.tbank.practicum.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tbank.practicum.repository.entity.User;

@Slf4j
@RequiredArgsConstructor
@Service
public class MQTTAuthService {
  private final UserService userService;

  @Value("${mqtt-auth.bridge-login}")
  private String bridge_login;

  @Value("${mqtt-auth.bridge-pass}")
  private String bridge_pass;

  public boolean isAuthorized(String username, String password) {
    // Bridge authentication
    if (username.equals(bridge_login) && password.equals(bridge_pass)) {
      log.debug("MQTT authentication granted for bridge user '{}'", username);
      return true;
    }

    // Regular user authentication via UserService
    try {
      userService.getUserByLoginAndPass(username, password);
      log.debug("MQTT authentication succeeded for user '{}'", username);
      return true;
    } catch (IllegalArgumentException e) {
      log.info("MQTT authentication failed for user '{}' - invalid credentials", username);
      return false;
    }
  }

  public boolean isAuthorizedAcl(String username, String topic, boolean isSub) {
    // Bridge ACL always allowed
    if (username.equals(bridge_login)) {
      log.debug("MQTT ACL granted for bridge user '{}' on topic '{}'", username, topic);
      return true;
    }

    // Regular user ACL check
    User user;
    try {
      user = userService.getUserByLogin(username);
    } catch (IllegalArgumentException e) {
      log.info("MQTT ACL denied for unknown user '{}' on topic '{}'", username, topic);
      return false;
    }

    if (user.getRoles().contains("USER")) {
      if (isSub) {
        boolean allowed = Objects.equals(topic, "hubs/" + user.getLogin() + "/commands");
        if (allowed) {
          log.debug("MQTT ACL SUBSCRIBE allowed for user '{}' on topic '{}'", username, topic);
        } else {
          log.info("MQTT ACL SUBSCRIBE denied for user '{}' on topic '{}' (expected '{}')",
                  username, topic, "hubs/" + user.getLogin() + "/commands");
        }
        return allowed;
      } else {
        boolean allowed = Objects.equals(topic, "hubs/data");
        if (allowed) {
          log.debug("MQTT ACL PUBLISH allowed for user '{}' on topic '{}'", username, topic);
        } else {
          log.info("MQTT ACL PUBLISH denied for user '{}' on topic '{}' (only 'hubs/data' is allowed)",
                  username, topic);
        }
        return allowed;
      }
    }

    log.info("MQTT ACL denied for user '{}' on topic '{}' - insufficient roles", username, topic);
    return false;
  }
}
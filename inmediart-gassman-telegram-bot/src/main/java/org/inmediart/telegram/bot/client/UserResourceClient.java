package org.inmediart.telegram.bot.client;

import org.inmediart.telegram.bot.configuration.FeignClientConfiguration;
import org.inmediart.telegram.bot.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@FeignClient(name = "users", url = "${feign.url}/users", configuration = FeignClientConfiguration.class)
public interface UserResourceClient {
    @GetMapping("/telegram/{id}")
    UserDTO findUserByTelegramId(@PathVariable("id") Integer id);

    @PostMapping()
    UserDTO addUser(@RequestBody UserDTO userDTO);

    @DeleteMapping("/telegram/{id}")
    Boolean deleteUser(@PathVariable("id") Integer id);

    @GetMapping("/administrator")
    List<UserDTO> getAdministrators();

    @GetMapping
    List<UserDTO> getUsers();

    @GetMapping("/mail/{mail}")
    UserDTO findUserByMail(@PathVariable("mail") String mail);
}

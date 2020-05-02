package org.inmediart.gui.client;

import org.inmediart.gui.configuration.FeignClientConfiguration;
import org.inmediart.gui.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "users", url = "${feign.url}/users", configuration = FeignClientConfiguration.class)
public interface UserResourceClient {
    @GetMapping("/")
    List<UserDTO> findByActiveTrue();

    @GetMapping("/{id}")
    UserDTO findById(@PathVariable("id") Long id);

    @GetMapping("/telegram/{id}")
    UserDTO findUserByTelegramId(@PathVariable("id") Integer id);

    @PostMapping()
    UserDTO addUser(@RequestBody UserDTO userDTO);

    @PutMapping("/{id}")
    UserDTO updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO);

    @DeleteMapping("/telegram/{id}")
    Boolean deleteUser(@PathVariable("id") Integer id);

    @GetMapping("/administrator")
    List<UserDTO> getAdministrators();

    @GetMapping
    List<UserDTO> getUsers();

    @GetMapping("/mail/{mail}")
    UserDTO findUserByMail(@PathVariable("mail") String mail);
}

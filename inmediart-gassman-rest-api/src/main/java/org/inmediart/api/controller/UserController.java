package org.inmediart.api.controller;

import org.inmediart.commons.binding.MessageSender;
import org.inmediart.model.entity.User;
import org.inmediart.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController extends MessageSender<User> {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageChannel userRegistrationChannel;

    @Autowired
    private MessageChannel userCancellationChannel;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userRepository.findByActiveTrue(), HttpStatus.OK);
    }

    @GetMapping("/administrator")
    public ResponseEntity<List<User>> getAdministrators(){
        return new ResponseEntity<>(userRepository.findByAdministratorTrue(), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> findUserById(@PathVariable Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("ID %d does not exists",id), null);
        }

    }

    @GetMapping("/mail/{mail}")
    public ResponseEntity<User> findUserByMail(@PathVariable String mail){
        return new ResponseEntity<>(userRepository.findByMail(mail), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> postUser(@RequestBody User user){
        if(user.getTelegramUserId() != null){
            Optional<User> userOptional = userRepository.findByTelegramUserId(user.getTelegramUserId());
            if(userOptional.isPresent()){
                userOptional.get().setActive(Boolean.TRUE);
                userOptional.get().setName(user.getName());
                userOptional.get().setSurname(user.getSurname());
                userOptional.get().setMail(user.getMail());
                userRepository.save(userOptional.get());
                sendUserRegistrationMessage(userOptional.get());
                return new ResponseEntity<>(userOptional.get(), HttpStatus.CREATED);
            }
        }

        User userInDB = userRepository.findByMail(user.getMail());
        if(userInDB != null) {
            userInDB.setActive(Boolean.TRUE);
            userInDB.setName(user.getName());
            userInDB.setSurname(user.getSurname());
            userInDB.setTelegramUserId(user.getTelegramUserId());
            userRepository.save(userInDB);
            sendUserRegistrationMessage(userInDB);
            return new ResponseEntity<>(userInDB, HttpStatus.CREATED);
        } else {
            User userPersisted = userRepository.save(user);
            sendUserRegistrationMessage(user);
            return new ResponseEntity<>(userPersisted, HttpStatus.CREATED);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<User> putUser(@PathVariable Long id, @RequestBody User user){
        if(userRepository.existsById(id)){
            user.setId(id);
            return new ResponseEntity<>(userRepository.save(user), HttpStatus.ACCEPTED);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("ID %d does not exists",id), null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id){
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return deleteUser(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("ID %d does not exists",id), null);
        }
    }

    @GetMapping("/telegram/{id}")
    public ResponseEntity<User> findUserByTelegram(@PathVariable Integer id){
        Optional<User> user = userRepository.findByTelegramUserIdAndActiveTrue(id);
        if(user.isPresent()){
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("ID %d does not exists",id), null);
        }
    }

    @DeleteMapping("/telegram/{id}")
    public ResponseEntity<Boolean> deleteUserByTelegram(@PathVariable Integer id){
        Optional<User> user = userRepository.findByTelegramUserId(id);
        if(user.isPresent()){
            return deleteUser(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("ID %d does not exists",id), null);
        }
    }

    private void sendUserRegistrationMessage(@RequestBody User user) {
        sendMessage(userRegistrationChannel,user);
    }

    private ResponseEntity<Boolean> deleteUser(Optional<User> user) {
        user.get().setActive(Boolean.FALSE);
        userRepository.save(user.get());
        sendMessage(userCancellationChannel,user.get());
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
    }
}

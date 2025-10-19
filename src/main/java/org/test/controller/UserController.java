package org.test.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.dto.UserDto;
import org.test.mapper.UserMapper;
import org.test.model.User;
import org.test.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User created = userService.createUser(
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAge()
        );
        return ResponseEntity.ok(UserMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto userDto
    ) {
        User existing = userService.findUserById(id);
        existing.setName(userDto.getName());
        existing.setEmail(userDto.getEmail());
        existing.setAge(userDto.getAge());

        userService.updateUser(existing);
        return ResponseEntity.ok(UserMapper.toDto(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

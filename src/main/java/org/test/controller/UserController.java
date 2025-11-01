package org.test.controller;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.test.dto.UserDto;
import org.test.mapper.UserMapper;
import org.test.model.User;
import org.test.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<UserDto>>> getAllUsers() {
        List<EntityModel<UserDto>> users = userService.getAllUsers()
                .stream()
                .map(UserMapper::toDto)
                .map(userDto -> EntityModel.of(userDto,
                        linkTo(methodOn(UserController.class).getUserById(userDto.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAllUsers()).withRel("users")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(users,
                        linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> getUserById(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        UserDto dto = UserMapper.toDto(user);

        EntityModel<UserDto> model = EntityModel.of(dto,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"),
                linkTo(methodOn(UserController.class).updateUser(id, dto)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(id)).withRel("delete")
        );

        return ResponseEntity.ok(model);
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> createUser(@RequestBody UserDto userDto) {
        User created = userService.createUser(
                userDto.getName(),
                userDto.getEmail(),
                userDto.getAge()
        );

        UserDto dto = UserMapper.toDto(created);
        EntityModel<UserDto> model = EntityModel.of(dto,
                linkTo(methodOn(UserController.class).getUserById(dto.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("users")
        );

        return ResponseEntity.ok(model);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> updateUser(
            @PathVariable("id") Long id,
            @RequestBody UserDto userDto
    ) {
        User existing = userService.findUserById(id);
        existing.setName(userDto.getName());
        existing.setEmail(userDto.getEmail());
        existing.setAge(userDto.getAge());

        userService.updateUser(existing);

        UserDto dto = UserMapper.toDto(existing);
        EntityModel<UserDto> model = EntityModel.of(dto,
                linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers()).withRel("users")
        );

        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

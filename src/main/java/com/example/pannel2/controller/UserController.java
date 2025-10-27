package com.example.pannel2.controller;

import com.example.pannel2.dto.UserRequestDTO;
import com.example.pannel2.dto.UserResponseDTO;
import com.example.pannel2.entity.User;
import com.example.pannel2.repository.UserRepository;
import com.example.pannel2.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController

@CrossOrigin(origins = "http://localhost:3000")// Allow Next.js (or any frontend) to make requests
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /*@PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }*/
    @PostMapping("/createuser")
    public UserResponseDTO createUser(@RequestBody UserRequestDTO requestDTO) {
        User user = new User(
                requestDTO.getEmail(),
                requestDTO.getFullname(),
                requestDTO.getPassword(),
                requestDTO.getPhone(),
                requestDTO.getBalance(),
                requestDTO.getRole(),
                requestDTO.getParentId(),
                requestDTO.getTelegramkey(),
                requestDTO.getFa()

        );
        User saved = userService.createUser(user);
        return new UserResponseDTO(saved);
    }

    //get user by id
    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Long id) {
        User user = userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserResponseDTO(user);
    }

    /*@GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }*/

    //get all users
    @GetMapping("/getallusers")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }


    //update a user
    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    //delete a user
    @DeleteMapping("/deleteuser/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }


    //get children of a user
    @GetMapping("/by-parent/{parentId}")
    public List<User> getUsersByParent(@PathVariable Long parentId) {
        return userService.getUsersByParentId(parentId);
    }

    //get user with childern
    @GetMapping("/{id}/with-children")
    public User getUserWithChildren(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @GetMapping("/getuser/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional
                .map(user -> ResponseEntity.ok(new UserResponseDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/editbalance/{userId}/{monton}")
    public void updatebalance (@PathVariable Long userId, @PathVariable Double monton) {userService.updatebalance(userId, monton);}

    @PostMapping("/editbalancedec/{userId}/{monton}")
    public void updatebalancedec (@PathVariable Long userId, @PathVariable Double monton) {userService.updatebalancedec(userId, monton);}


}

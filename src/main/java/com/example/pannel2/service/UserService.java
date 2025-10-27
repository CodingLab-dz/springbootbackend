package com.example.pannel2.service;

import com.example.pannel2.dto.UserResponseDTO;
import com.example.pannel2.entity.User;
import com.example.pannel2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create user
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Read user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Read all users
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll().stream().map(UserResponseDTO :: new).collect(Collectors.toList());
    }

    public String updatebalance(Long userId, Double monton){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));

        user.setBalance(user.getBalance() + monton);
        userRepository.save(user);
        return ("ok");

    }
    public String updatebalancedec( Long userId, Double monton){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));

        user.setBalance(user.getBalance() - monton);
        userRepository.save(user);
        return ("ok");

    }

    // Update user
    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setPhone(updatedUser.getPhone());
            user.setBalance(updatedUser.getBalance());
            user.setRole(updatedUser.getRole());
            user.setParentId(updatedUser.getParentId());
            user.setFullname(updatedUser.getFullname());
            user.setTelegramkey(updatedUser.getTelegramkey());
            user.setFa(updatedUser.getFa());
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getUsersByParentId(Long parentId) {
        return userRepository.findByParentId(parentId);
    }

    public void update(Long parentId, Long userId, Double balance){

        Optional<User> parentOpt = userRepository.findById(parentId);

        Optional<User> useropt = userRepository.findById(userId);
        if (parentOpt.isPresent() & useropt.isPresent()){
            User parent = parentOpt.get();
            User user = useropt.get();
            if (parent.getBalance() >= balance){
                Double newbalanceuser = user.getBalance() + balance;
                Double newbalanceparent = parent.getBalance() - balance;
                user.setBalance(newbalanceuser);
                userRepository.save(user);
                parent.setBalance(newbalanceparent);
                userRepository.save(parent);
            }else {
                throw new IllegalArgumentException("insufisent balance");
            }
        }else {
            throw new IllegalArgumentException("User ID and parent ID must not be null");
        }
    }
}

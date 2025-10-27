package com.example.pannel2.service;


import com.example.pannel2.dto.*;
import com.example.pannel2.entity.Demande;
import com.example.pannel2.entity.Historic;
import com.example.pannel2.entity.Product;
import com.example.pannel2.entity.User;
import com.example.pannel2.repository.DemandeRepository;
import com.example.pannel2.repository.HistoricRepository;
import com.example.pannel2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoricService {
    private final HistoricRepository historicRepo;
    private final UserRepository userRepository;
    private final UserService userService;

    public HistoricService (HistoricRepository historicRepo, UserRepository userRepository, UserService userService){
        this.historicRepo = historicRepo;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<HistoricDTO> getallhistori(){
        List<Historic> list = historicRepo.findAll();
        return list.stream().map(this::mapUsers).collect(Collectors.toList());
    }

    public List<HistoricDTO> getHistoribyuser(Long userId){
        List<Historic> historicList = historicRepo.findByUser_Id(userId);
        return historicList.stream().map(this::mapAll).collect(Collectors.toList());
    }

    public List<HistoricDTO> getHistoricuserandsons(Long userId){
        // Collect all descendant IDs (sons, grandsons, etc.)
        List<Long> descendantIds = getAllDescendantIds(userId);

        // Add the current userId itself
        descendantIds.add(userId);

        List<Historic> historicList = historicRepo.findByUser_IdIn(descendantIds);

        return historicList.stream()
                .map(this::mapAll)
                .collect(Collectors.toList());

    }

    public List<HistoricDTO> getHistoricDirectSons(Long userId){
        //List<User> listUser= userRepository.findByParentId(userId);
        List<Long> listSons = getDirectsonsIds(userId);
        List<Historic> historicList = historicRepo.findByUser_IdIn(listSons);
        return  historicList.stream().map(this::mapAll).collect(Collectors.toList());
    }
    private List<Long> getDirectsonsIds(Long parentId){
        List<Long> result = new ArrayList<>();
        List<User> listUsers= userRepository.findByParentId(parentId);
        for (User u: listUsers){
            result.add(u.getId());
        }
        return result;
    }

    private List<Long> getAllDescendantIds(Long parentId) {
        List<Long> result = new ArrayList<>();

        List<User> directChildren = userRepository.findByParentId(parentId);
        for (User child : directChildren) {
            result.add(child.getId());
            // recursion for grandsons, etc.
            result.addAll(getAllDescendantIds(child.getId()));
        }

        return result;
    }
    public  HistoricDTO mapUsers(Historic historic){
        Product product = historic.getProduct();
        User user = historic.getUser();
        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());

        ProductRequest productDto = new ProductRequest();
        productDto.setId(product.getId());
        productDto.setName(product.getName());

        HistoricDTO dto = new HistoricDTO();
        dto.setId(historic.getId());
        dto.setProductname(productDto.getName());
        dto.setAddedAt(historic.getAddedAt());
        dto.setUseremail(user.getEmail());
        dto.setUsername(user.getFullname());
        dto.setUserrole(user.getRole());
        dto.setBuyingprice(historic.getBuyingprice());
        dto.setPrice(historic.getPrice());
        dto.setProducttype(product.getCategory().getType());

        return dto;

    }



    public HistoricDTO mapAll(Historic historic){
        Product product = historic.getProduct();
        User user = historic.getUser();

        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());

        ProductRequest productDto = new ProductRequest();
        productDto.setId(product.getId());
        productDto.setName(product.getName());

        HistoricDTO dto = new HistoricDTO();
        dto.setId(historic.getId());
        dto.setProductname(productDto.getName());
        dto.setAddedAt(historic.getAddedAt());
        dto.setUseremail(user.getEmail());
        dto.setUsername(user.getFullname());
        dto.setUserrole(user.getRole());
        dto.setBuyingprice(historic.getBuyingprice());
        dto.setProducttype(product.getCategory().getType());
        dto.setPrice(historic.getPrice());

        return dto;
    }

}



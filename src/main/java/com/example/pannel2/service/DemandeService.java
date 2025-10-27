package com.example.pannel2.service;


import com.example.pannel2.dto.DemandDTO;
import com.example.pannel2.dto.DemandeUserDTO;
import com.example.pannel2.dto.ProductRequest;
import com.example.pannel2.dto.UserResponseDTO;
import com.example.pannel2.entity.Demande;
import com.example.pannel2.entity.Product;
import com.example.pannel2.entity.User;
import com.example.pannel2.repository.DemandeRepository;
import com.example.pannel2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandeService {

    private final DemandeRepository demandeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public DemandeService (DemandeRepository demandeRepository, UserRepository userRepository, UserService userService){
        this.demandeRepository = demandeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public List<DemandeUserDTO> getDemandebyuser(Long userId){
        List<Demande> demandeList = demandeRepository.findByUser_Id(userId);
        return demandeList.stream().map(this::mapAll).collect(Collectors.toList());
    }
    public List <DemandDTO> getDemandeUsersons(Long userId){
        // 1. Fetch buyer and product
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        List<User> listsons = userService.getUsersByParentId(userId);
        List<Long> descendantIds = listsons.stream()
                .map(User::getId)
                .collect(Collectors.toList());

        descendantIds.add(userId);
        List<Demande> demandeList = demandeRepository.findByUser_IdIn(descendantIds);


        // Fetch DEMAND type product demands for all those users
       // return demandeRepository.findByUserIdInAndProduct_Category_Type(descendantIds);
        return demandeList.stream().map(this::mapUsers).collect(Collectors.toList());
    }
    public List<DemandDTO> getalldamndes(){
        List<Demande> list = demandeRepository.findAll();
        return list.stream().map(this::mapUsers).collect(Collectors.toList());
    }


    public  DemandDTO mapUsers(Demande demande){
        Product product = demande.getProduct();
        User user = demande.getUser();
        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());

        ProductRequest productDto = new ProductRequest();
        productDto.setId(product.getId());
        productDto.setName(product.getName());

        DemandDTO dto = new DemandDTO();
        dto.setId(demande.getId());
        dto.setProduct(productDto);
        dto.setAddedAt(demande.getAddedAt());
        dto.setUser(demande.getUser());
        dto.setBuyingprice(BigDecimal.valueOf(demande.getBuyingprice()));
        dto.setState(demande.getState());
        dto.setDemande(demande.getDemande());
        dto.setRepense(demande.getRepense());

        return dto;

    }

    public DemandeUserDTO mapAll(Demande demande){
        Product product = demande.getProduct();
        User user = demande.getUser();

        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFullname(user.getFullname());

        ProductRequest productDto = new ProductRequest();
        productDto.setId(product.getId());
        productDto.setName(product.getName());

        DemandeUserDTO dto = new DemandeUserDTO();
        dto.setId(demande.getId());
        dto.setProduct(productDto);
        dto.setAddedAt(demande.getAddedAt());
        dto.setState(demande.getState());
        dto.setBuyingprice(BigDecimal.valueOf(demande.getBuyingprice()));
        dto.setDemande(demande.getDemande());
        dto.setRepense(demande.getRepense());
        return dto;
    }

}

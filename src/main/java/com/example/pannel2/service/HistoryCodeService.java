package com.example.pannel2.service;


import com.example.pannel2.dto.*;
import com.example.pannel2.entity.*;
import com.example.pannel2.enums.UserRole;
import com.example.pannel2.repository.HistorycodeRepository;
import com.example.pannel2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryCodeService {

    private final HistorycodeRepository historycodeRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public HistoryCodeService (HistorycodeRepository historycoderepository, UserRepository userRepository, UserService userService){this.historycodeRepository= historycoderepository; this.userRepository= userRepository; this.userService = userService;}

    public String addhistorycode (User user, Product product, ProductCode productcode, Double byingprice, LocalDateTime byingdate){
        if (user == null || product== null || productcode == null || byingprice== null){
            throw new IllegalArgumentException("User ID and Product ID must not be null");
        }
        HistoryCode historyCode= new HistoryCode(productcode, user, byingprice, byingdate, product);
        historycodeRepository.save(historyCode);
        return "histo add";
    }

    public List<HistoryCodeResponce> getHistorycodebyuserid(Long userId) {
        List<HistoryCode> historyList = historycodeRepository.findByUserId(userId);
        return historyList.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public List<HistoryCodeDTO> getallHistorycode(){
        List<HistoryCode> historyCodeList = historycodeRepository.findAll();
        return historyCodeList.stream().map(this::mapAll).collect(Collectors.toList());
    }

    public List<HistoryCodeDTO> getsonshitrory(Long userId){
        // 1. Fetch buyer and product
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        List<User> listsons = userService.getUsersByParentId(userId);
        List<Long> descendantIds = listsons.stream()
                .map(User::getId)
                .collect(Collectors.toList());
        if (descendantIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<HistoryCode> demandeList = historycodeRepository.findByUser_IdIn(descendantIds);


        // Fetch DEMAND type product demands for all those users
        // return demandeRepository.findByUserIdInAndProduct_Category_Type(descendantIds);
        return demandeList.stream().map(this::mapAll).collect(Collectors.toList());
    }


    private HistoryCodeDTO mapAll(HistoryCode historyCode){
        ProductCode code = historyCode.getCode();
        User user = historyCode.getUser();
        Product product = historyCode.getProduct();

        CodeRequest codeDto = new CodeRequest();
        codeDto.setId(code.getId());
        codeDto.setCode(code.getCode());
        codeDto.setStatus(code.getStatus());

        UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setBalance(user.getBalance());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().toString());
        userDto.setFullname(user.getFullname());

        ProductRequest productDto = new ProductRequest();
        productDto.setId(product.getId());
        productDto.setName(product.getName());

        HistoryCodeDTO dto = new HistoryCodeDTO();
        dto.setId(historyCode.getId());
        dto.setCode(codeDto);
        dto.setUser(userDto);
        dto.setBuyingprice(historyCode.getBuyingprice());
        dto.setAddedAt(historyCode.getAddedAt());
        dto.setProduct(productDto);

        return dto;

    }

    private HistoryCodeResponce mapToDto(HistoryCode history) {
        ProductCode code = history.getCode();
        //User user = history.getUser();
        Product product = history.getProduct();

        CodeRequest codeDto = new CodeRequest();
        codeDto.setId(code.getId());
        codeDto.setCode(code.getCode());
        codeDto.setStatus(code.getStatus());

        /*UserResponseDTO userDto = new UserResponseDTO();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());*/

        ProductRequest productDto = new ProductRequest();
        productDto.setId(product.getId());
        productDto.setName(product.getName());

        HistoryCodeResponce dto = new HistoryCodeResponce();
        dto.setId(history.getId());
        dto.setCode(codeDto);
        //dto.setUser(userDto);
        dto.setBuyingprice(history.getBuyingprice());
        dto.setAddedAt(history.getAddedAt());
        dto.setProduct(productDto);

        return dto;
    }




    /*public List<HistoryCodeDTO> getHistorycodebyuserid (Long userId){
        List<HistoryCodeDTO> result = new ArrayList<>();
        List<HistoryCode> historyCodeList = historycodeRepository.findByUserId(userId);
        if (historyCodeList.isEmpty()){
            throw new IllegalArgumentException("user don't have any codes");
        }
        for (HistoryCode hc : historyCodeList){
            ProductCode code = hc.getCode();

            result.add(new HistoryCodeDTO(hc.getId(), hc.getCode(), hc.getUser(), hc.getBuyingprice() , hc.getAddedAt(), hc.getProduct()));
        }

        return result;
    }*/
}

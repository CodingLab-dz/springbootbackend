package com.example.pannel2.service;

import com.example.pannel2.dto.ProductDTO;
import com.example.pannel2.dto.ProductWithPriceDTO;
import com.example.pannel2.entity.Product;
import com.example.pannel2.entity.User;

import java.util.List;

public interface ProductService {
    List<ProductWithPriceDTO> getProductsForUser(User user, Long category);
    List<ProductDTO> getProductOnlysForUser(User user);

    List<ProductDTO> getProductcreatedbyuser(User user);
    void increaseProductPrice(User user, Product product, Double increment);
    void decreaseProductPrice(User user, Product product, Double decrement);
}

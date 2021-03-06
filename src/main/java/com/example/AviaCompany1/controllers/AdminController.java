package com.example.AviaCompany1.controllers;


import com.example.AviaCompany1.dto.ProductDTO;
import com.example.AviaCompany1.model.OrderedProduct;
import com.example.AviaCompany1.model.Product;
import com.example.AviaCompany1.repo.OrderedProductRepository;
import com.example.AviaCompany1.repo.ProductRepository;
import com.example.AviaCompany1.service.ProductService;
import com.example.AviaCompany1.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {


    @Autowired
    StatisticsService statisticsService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderedProductRepository orderedProductRepository;

    @GetMapping("/admin")
    public String adminHome(){
        return "adminHome";
    }


    @GetMapping("/admin/products")
    public String products(Model model){

        model.addAttribute("products",productService.getAllProduct());

        Map<String,Integer> stat=statisticsService.numberstat();
        model.addAttribute("count",stat.get("count"));
        model.addAttribute("price",stat.get("price"));
        ArrayList<String> popular=statisticsService.populartickets();
        model.addAttribute("popular",popular);


        return "products";
    }

    @GetMapping("/admin/products/add")
    public String productsAddGet(Model model){
        model.addAttribute("productDTO",new ProductDTO());
        return "productsAdd";
    }

    @PostMapping("/admin/products/add")
    public String productsAddPost(@ModelAttribute("productDTO")ProductDTO productDTO){

        Product product=new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setNamein(productDTO.getNamein());
        product.setFlyin(productDTO.getFlyin());
        product.setFlyout(productDTO.getFlyout());
        product.setPrice(productDTO.getPrice());
        product.setPlaces(productDTO.getPlaces());
        product.setDescription(productDTO.getDescription());

        productService.addProduct(product);

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable long id){
        productService.removeProductById(id);
    return "redirect:/admin/products";
    }


    @GetMapping("/admin/product/update/{id}")
    public String updateProductGet(@PathVariable long id,Model model){
        Product product=productService.getProductById(id).get();
        ProductDTO productDTO=new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setNamein(product.getNamein());
        productDTO.setFlyout(product.getFlyout());
        productDTO.setFlyin(product.getFlyin());
        productDTO.setPrice(product.getPrice());
        productDTO.setPlaces(product.getPlaces());
        productDTO.setDescription(product.getDescription());
        model.addAttribute("productDTO",productDTO);
        return "productsAdd";
    }


}

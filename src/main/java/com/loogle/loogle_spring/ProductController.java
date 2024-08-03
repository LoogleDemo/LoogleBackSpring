package com.loogle.loogle_spring;

import com.loogle.loogle_spring.dto.ProductSaveRequest;
import com.loogle.loogle_spring.dto.SearchItemResponse;
import com.loogle.loogle_spring.dto.StringResponse;
import com.loogle.loogle_spring.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @PostMapping("create")
    public StringResponse createProducts(@RequestBody List<ProductSaveRequest> requestList) {
        productService.autoCreateProducts(requestList);
        return new StringResponse("상품 리스트 DB 저장 완료");
    }

    @GetMapping("/search/keyword")
    public List<SearchItemResponse> searchByKeyword(@RequestParam String keywordName) {
        return productService.searchByKeyword(keywordName);
    }

    @PostMapping("/search/image")
    public List<SearchItemResponse> searchByImage(@RequestPart MultipartFile imageFile) {
        return productService.searchByImage(imageFile);
    }

    @GetMapping("/health")
    public String healthCheck(){
        return "성공";
    }



}
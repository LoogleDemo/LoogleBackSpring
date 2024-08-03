package com.loogle.loogle_spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loogle.loogle_spring.domain.Brand;
import com.loogle.loogle_spring.domain.Category;
import com.loogle.loogle_spring.domain.Colour;
import com.loogle.loogle_spring.domain.Product;
import com.loogle.loogle_spring.dto.ProductResponse;
import com.loogle.loogle_spring.dto.ProductSaveRequest;
import com.loogle.loogle_spring.dto.SearchItemResponse;
import com.loogle.loogle_spring.exception.LoogleErrorCode;
import com.loogle.loogle_spring.exception.LoogleException;
import com.loogle.loogle_spring.repositories.BrandRepository;
import com.loogle.loogle_spring.repositories.CategoryRepository;
import com.loogle.loogle_spring.repositories.ColourRepository;
import com.loogle.loogle_spring.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ColourRepository colourRepository;
    private final CategoryRepository categoryRepository;

    private static Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Transactional
    public void autoCreateProducts(List<ProductSaveRequest> requestList){
        for (ProductSaveRequest request : requestList) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(()-> new LoogleException(LoogleErrorCode.BRAND_NOT_FOUND_BY_ID));
            log.info("product title duplicated : {}", request.getTitle());
            if(productRepository.findByTitle(request.getTitle()).isPresent()){
                throw new LoogleException(LoogleErrorCode.PRODUCT_TITLE_DUPLICATED);
            }
            Set<Colour> colours = new HashSet<>();
            for (String colourName : request.getColourNames()) {
                log.info("product title : {}", request.getTitle());
                Colour colour = colourRepository.findByColourName(colourName)
                        .orElseThrow(()-> new LoogleException(LoogleErrorCode.COLOUR_NOT_FOUND_BY_COLOUR_NAME));
                colours.add(colour);
            }
            Category category = categoryRepository.findByCategoryName(request.getCategoryName())
                    .orElseThrow(()-> new LoogleException(LoogleErrorCode.CATEGORY_NOT_FOUND_BY_CATEGORY_NAME));
            Product product = request.toProduct();
            product.setBrand(brand);
            product.setColours(colours);
            product.setCategory(category);
            productRepository.save(product);
        }
    }



    @Transactional
    public List<SearchItemResponse> searchByKeyword(String keywordName) {
        List<SearchItemResponse> searchResponses = new ArrayList<>();
        List<ProductResponse> dtos = new ArrayList<>();
        log.info("여기까지 갔니?");
        List<Map<String, String>> results = springToFlaskDic(keywordName);
        log.info("testtesttesttest");
        // Extract titles and fetch products in a single query
        List<String> titles = results.stream().map(map -> map.get("title")).collect(Collectors.toList());
        List<Product> products = productRepository.findByTitleIn(titles);

        // Create productIds and dtos
        Map<Long, ProductResponse> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getId(), new ProductResponse(
                    product.getId(), product.getPrice(), product.getBrand().getId(),
                    product.getTitle(), product.getProductImgUrl(), product.getDetailUrl()));
        }

        // Create SearchItemResponse objects
        List<Long> productIds = new ArrayList<>();
        List<Double> similarities = new ArrayList<>();

        for (Map<String, String> result : results) {
            String title = result.get("title");
            String similarityStr = result.get("similarity");
            Double similarity = Double.parseDouble(similarityStr); // 문자열을 Double로 변환

            Product product = products.stream().filter(p -> p.getTitle().equals(title)).findFirst().orElse(null);
            if (product != null) {
                productIds.add(product.getId());
                similarities.add(similarity);
                dtos.add(productMap.get(product.getId()));
            }
        }

        // SearchItemResponse 객체 생성
        searchResponses.add(new SearchItemResponse(productIds, similarities, dtos));
        log.info("프론트한테 보내줬나요???");
        return searchResponses;
    }

    @Transactional
    public List<SearchItemResponse> searchByImage(MultipartFile imageFile){
        List<SearchItemResponse> searchResponses = new ArrayList<>();
        List<ProductResponse> dtos = new ArrayList<>();
        log.info("spring image step 1");
        List<Map<String, String>> results = springToFlaskImage(imageFile);
        log.info("spring image step 2 ");
        List<String> titles = results.stream().map(map -> map.get("title")).collect(Collectors.toList());
        List<Product> products = productRepository.findByTitleIn(titles);

        // Create productIds and dtos
        Map<Long, ProductResponse> productMap = new HashMap<>();
        for (Product product : products) {
            productMap.put(product.getId(), new ProductResponse(
                    product.getId(), product.getPrice(), product.getBrand().getId(),
                    product.getTitle(), product.getProductImgUrl(), product.getDetailUrl()));
        }

        // Create SearchItemResponse objects
        List<Long> productIds = new ArrayList<>();
        List<Double> similarities = new ArrayList<>();

        for (Map<String, String> result : results) {
            String title = result.get("title");
            String similarityStr = result.get("similarity");
            Double similarity = Double.parseDouble(similarityStr); // 문자열을 Double로 변환

            Product product = products.stream().filter(p -> p.getTitle().equals(title)).findFirst().orElse(null);
            if (product != null) {
                productIds.add(product.getId());
                similarities.add(similarity);
                dtos.add(productMap.get(product.getId()));
            }
        }

        // SearchItemResponse 객체 생성
        searchResponses.add(new SearchItemResponse(productIds, similarities, dtos));

        return searchResponses;

    }

    public List<Map<String, String>> springToFlaskDic(String keywordName) {
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://localhost:5000/search";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> map = new HashMap<>();
        map.put("name", keywordName);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;
        try {
            json = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<Map<String, List<Object>>> responseEntity = restTemplate.exchange(
                flaskUrl,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<Map<String, List<Object>>>() {}
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, List<Object>> body = responseEntity.getBody();

            // titles 변환
            List<Object> titleObjects = body.get("titles");
            List<String> titles = new ArrayList<>();
            if (titleObjects != null) {
                for (Object titleObject : titleObjects) {
                    titles.add(String.valueOf(titleObject)); // 각 요소를 String으로 변환하여 추가
                }
            }

            // similarities 변환
            List<Object> similarities = body.get("similarities");
            List<Map<String, String>> results = new ArrayList<>();
            for (int i = 0; i < titles.size(); i++) {
                Map<String, String> result = new HashMap<>();
                result.put("title", titles.get(i));

                // similarities의 각 요소를 String으로 변환하여 추가
                String similarityString;
                if (similarities != null && similarities.size() > i && similarities.get(i) != null) {
                    similarityString = String.valueOf(similarities.get(i));
                } else {
                    similarityString = "N/A"; // 또는 적절한 기본값
                }
                result.put("similarity", similarityString);

                results.add(result);
            }
            return results;
        } else {
            // 에러 처리
            System.out.println("Flask API 호출 실패: " + responseEntity.getStatusCode());
            return null; // 또는 빈 리스트를 반환할 수 있음
        }
    }


    @SneakyThrows
    public List<Map<String, String>> springToFlaskImage(MultipartFile imageFile) {
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://localhost:5000/imageSearch";

        // HttpHeaders 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // MultipartFile을 ByteArrayResource로 변환
        ByteArrayResource byteArrayResource = new ByteArrayResource(imageFile.getBytes()) {
            @Override
            public String getFilename() {
                return imageFile.getOriginalFilename(); // 원본 파일 이름 설정
            }
        };

        // MultiValueMap을 사용하여 요청 본문 구성
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("image", byteArrayResource); // "image"라는 이름으로 첨부

        // HttpEntity 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Flask에 POST 요청
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(flaskUrl, requestEntity, Map.class);

        // 결과 처리
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, List<Object>> responseBody = responseEntity.getBody();

            // titles 변환
            List<Object> titleObjects = responseBody.get("titles");
            List<String> titles = new ArrayList<>();
            if (titleObjects != null) {
                for (Object titleObject : titleObjects) {
                    titles.add(String.valueOf(titleObject)); // 각 요소를 String으로 변환하여 추가
                }
            }

            // similarities 변환
            List<Object> similarities = responseBody.get("similarities");
            List<Map<String, String>> results = new ArrayList<>();
            for (int i = 0; i < titles.size(); i++) {
                Map<String, String> result = new HashMap<>();
                result.put("title", titles.get(i));

                // similarities의 각 요소를 String으로 변환하여 추가
                String similarityString;
                if (similarities != null && similarities.size() > i && similarities.get(i) != null) {
                    similarityString = String.valueOf(similarities.get(i));
                } else {
                    similarityString = "N/A"; // 또는 적절한 기본값
                }
                result.put("similarity", similarityString);

                results.add(result);
            }
            return results;
        } else {
            // 에러 처리
            System.out.println("Flask API 호출 실패 (image) : " + responseEntity.getStatusCode());
            return null; // 또는 빈 리스트를 반환할 수 있음
        }
    }


}
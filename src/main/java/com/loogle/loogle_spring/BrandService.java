package com.loogle.loogle_spring;

import com.loogle.loogle_spring.domain.Brand;
import com.loogle.loogle_spring.dto.BrandSaveRequest;
import com.loogle.loogle_spring.exception.LoogleErrorCode;
import com.loogle.loogle_spring.exception.LoogleException;
import com.loogle.loogle_spring.repositories.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    @Transactional
    public void autoCreateBrands(List<BrandSaveRequest> requestList){
        for (BrandSaveRequest request : requestList) {
            if(brandRepository.findByBrandName(request.getBrandName()).isPresent()){
                throw new LoogleException(LoogleErrorCode.BRAND_NAME_DUPLICATED);
            }
            brandRepository.save(new Brand(request.getBrandName(), request.getIconUrl(), request.getBannerUrl()));
        }
    }

    @Transactional
    public List<BrandListResponse> getAllBrands(){
        List<BrandListResponse> dtos = new ArrayList<>();
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands) {
            dtos.add(new BrandListResponse(brand.getId(), brand.getBrandName(), brand.getIconUrl()));
        }
        return dtos;
    }



}
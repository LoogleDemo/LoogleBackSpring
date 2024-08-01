package com.loogle.loogle_spring;

import com.loogle.loogle_spring.dto.BrandSaveRequest;
import com.loogle.loogle_spring.dto.StringResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/brand")
public class BrandController {
    private final BrandService brandService;
    @PostMapping("add/hj")
    public StringResponse autoCreateBrands(@RequestBody List<BrandSaveRequest> requestList){
        brandService.autoCreateBrands(requestList);
        return new StringResponse("브랜드 리스트 DB 저장 완료");
    }

}
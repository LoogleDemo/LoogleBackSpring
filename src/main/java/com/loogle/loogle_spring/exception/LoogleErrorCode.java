package com.loogle.loogle_spring.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum LoogleErrorCode {

    BRAND_NOT_FOUND_BY_ID("해당 브랜드의 아이디로 브랜드를 찾을 수 없습니다."),
    BRAND_NOT_FOUND_BY_NAME("해당 브랜드의 이름으로 브랜드를 찾을 수 없습니다"),
    BRAND_NAME_DUPLICATED("브랜드 이름이 중복입니다."),
    BRAND_NOT_FOUND_BY_PRODUCTID("상품 아이디로 브랜드를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND_BY_ID("해당 상품의 아이디로 상품을 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND_BY_BRAND_ID("브랜드 아이디로 상품을 찾을 수 없습니다."),
    PRODUCT_TITLE_DUPLICATED("해당 상품과 같은 이름을 가진 상품이 있습니다."),
    COLOUR_NOT_FOUND_BY_COLOUR_NAME("해당 색상이 없습니다."),
    CATEGORY_NOT_FOUND_BY_CATEGORY_NAME("카테고리 이름이 존재하지 않습니다.");
    private String defaultMessage;
}

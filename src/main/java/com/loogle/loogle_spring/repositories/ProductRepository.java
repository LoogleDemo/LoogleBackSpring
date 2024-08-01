package com.loogle.loogle_spring.repositories;

import com.loogle.loogle_spring.domain.Category;
import com.loogle.loogle_spring.domain.Colour;
import com.loogle.loogle_spring.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findByTitle(String title);
    List<Product> findByBrandId(Long brandId);
//    List<Product> findByCategory(Category category);
//    List<Product> findByCategoryOrderByCreatedAtDesc(Category category);
//    List<Product> findByCategoryOrderByHitsDesc(Category category);
    List<Product> findByIdIn(List<Long> ids);
//    List<Product> findByColoursContains(Colour colour);
//    List<Product> findByCategoryIn(List<Category> categories);
//    List<Product> findByCategoryInOrderByHitsDesc(List<Category> categories);
//    List<Product> findByCategoryInOrderByCreatedAtDesc(List<Category> categories);
//    List<Product> findByBrandIdAndCategoryIn(Long brandId, List<Category> categories);
//    Page<Product> findByCategoryIn(List<Category> categories, Pageable pageable);
    List<Product> findByTitleIn(List<String> titles);


}
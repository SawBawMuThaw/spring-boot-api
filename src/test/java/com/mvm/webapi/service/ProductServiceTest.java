package com.mvm.webapi.service;

import com.mvm.webapi.model.Product;
import com.mvm.webapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts() {
        List<Product> products = Arrays.asList(
                new Product(1L, "Apple iPhone 14", 999.0, "Black"),
                new Product(2L, "Apple iPhone 13", 799.0, "White"));
        when(productRepository.findAll()).thenReturn(products);
        List<Product> result = productService.getAllProducts();
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_found() {
        Product product = new Product(1L, "Apple iPhone 14", 999.0, "Black");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Optional<Product> result = productService.getProductById(1L);
        assertTrue(result.isPresent());
        assertEquals("Apple iPhone 14", result.get().getName());
    }

    @Test
    void getProductById_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Product> result = productService.getProductById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void createProduct() {
        Product product = new Product(null, "Apple iPhone 14", 999.0, "Black");
        Product saved = new Product(1L, "Apple iPhone 14", 999.0, "Black");
        when(productRepository.save(product)).thenReturn(saved);
        Product result = productService.createProduct(product);
        assertEquals(1L, result.getId());
    }

    @Test
    void updateProduct_found() {
        Product existing = new Product(1L, "Old", 100.0, "Red");
        Product update = new Product(null, "New", 200.0, "Blue");
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));
        Optional<Product> result = productService.updateProduct(1L, update);
        assertTrue(result.isPresent());
        assertEquals("New", result.get().getName());
        assertEquals(200.0, result.get().getPrice());
        assertEquals("Blue", result.get().getColor());
    }

    @Test
    void updateProduct_notFound() {
        Product update = new Product(null, "New", 200.0, "Blue");
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<Product> result = productService.updateProduct(1L, update);
        assertFalse(result.isPresent());
    }

    @Test
    void deleteProduct_found() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));
        boolean deleted = productService.deleteProduct(1L);
        assertTrue(deleted);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteProduct_notFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        boolean deleted = productService.deleteProduct(1L);
        assertFalse(deleted);
        verify(productRepository, never()).deleteById(anyLong());
    }
}

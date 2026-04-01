package com.mvm.webapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mvm.webapi.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void getProductById_found() throws Exception {
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", containsString("Apple")));
    }

    @Test
    void getProductById_notFound() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct() throws Exception {
        Product product = new Product(null, "Apple AirPods Pro", 249.0, "White");
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Apple AirPods Pro")));
    }

    @Test
    void updateProduct_found() throws Exception {
        // Tạo mới một sản phẩm để lấy id
        Product created = new Product(null, "Temp Product", 100.0, "Gray");
        String response = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readTree(response).get("id").asLong();

        Product update = new Product(null, "Apple iPhone 15", 1099.0, "Gold");
        mockMvc.perform(put("/api/products/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Apple iPhone 15")));
    }

    @Test
    void updateProduct_notFound() throws Exception {
        Product update = new Product(null, "Apple iPhone 15", 1099.0, "Gold");
        mockMvc.perform(put("/api/products/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_found() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_notFound() throws Exception {
        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isNotFound());
    }
}

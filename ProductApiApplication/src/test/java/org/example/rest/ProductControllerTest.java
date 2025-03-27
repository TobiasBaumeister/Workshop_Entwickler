package org.example.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.database.exception.ResourceNotFoundException;
import org.example.database.model.Product;
import org.example.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testGetAllProducts() throws Exception {
    List<Product> products = Arrays.asList(
        new Product(1L, "Laptop", 999.99),
        new Product(2L, "Phone", 499.99)
    );
    when(productService.getAllProducts()).thenReturn(products);

    mockMvc.perform(get("/api/v1/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(products.size()))
        .andExpect(jsonPath("$[0].name").value("Laptop"));
  }

  @Test
  void testGetProductByIdExists() throws Exception {
    Product product = new Product(1L, "Laptop", 999.99);
    when(productService.getProductById(1L)).thenReturn(Optional.of(product));

    mockMvc.perform(get("/api/v1/products/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Laptop"));
  }

  @Test
  void testGetProductByIdNotExists() throws Exception {
    when(productService.getProductById(1L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/v1/products/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testCreateProduct() throws Exception {
    Product product = new Product(null, "Tablet", 299.99);
    Product savedProduct = new Product(3L, "Tablet", 299.99);
    when(productService.createProduct(Mockito.any(Product.class))).thenReturn(savedProduct);

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(product)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.name").value("Tablet"));
  }

  @Test
  void testUpdateProductExists() throws Exception {
    Product updatedDetails = new Product(null, "Gaming Laptop", 1299.99);
    Product updatedProduct = new Product(1L, "Gaming Laptop", 1299.99);
    when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

    mockMvc.perform(put("/api/v1/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedDetails)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Gaming Laptop"));
  }

  @Test
  void testUpdateProductNotExists() throws Exception {
    Product updatedDetails = new Product(null, "Gaming Laptop", 1299.99);
    when(productService.updateProduct(eq(1L), any(Product.class)))
        .thenThrow(new ResourceNotFoundException("Produkt nicht gefunden mit der ID: 1"));

    mockMvc.perform(put("/api/v1/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedDetails)))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteProductExists() throws Exception {
    doNothing().when(productService).deleteProduct(1L);
    mockMvc.perform(delete("/api/v1/products/1"))
        .andExpect(status().isNoContent());
  }

  @Test
  void testDeleteProductNotExists() throws Exception {
    doThrow(new ResourceNotFoundException("Produkt nicht gefunden mit der ID: 1")).when(productService).deleteProduct(1L);
    mockMvc.perform(delete("/api/v1/products/1"))
        .andExpect(status().isNotFound());
  }
}


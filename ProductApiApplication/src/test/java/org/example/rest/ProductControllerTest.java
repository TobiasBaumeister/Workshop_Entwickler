package org.example.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.example.dto.ProductDTO;
import org.example.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  @Autowired
  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testGetAllProducts() throws Exception {
    List<ProductDTO> productList = List.of(
        createProductDTO(1L, "Laptop", 999.99, 1L, "Laptops"),
        createProductDTO(2L, "Phone", 499.99, 2L, "Phones")
    );

    when(productService.getAllProducts()).thenReturn(productList);

    mockMvc.perform(get("/api/v1/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Laptop"))
        .andExpect(jsonPath("$[1].price").value(499.99));
  }

  @Test
  void testGetProductByIdExists() throws Exception {
    ProductDTO dto = createProductDTO(1L, "Tablet", 299.99, 3L, "Tablets");

    when(productService.getProductById(1L)).thenReturn(dto);

    mockMvc.perform(get("/api/v1/products/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Tablet"))
        .andExpect(jsonPath("$.categoryName").value("Tablets"));
  }

  @Test
  void testCreateProduct() throws Exception {
    ProductDTO request = createProductDTO(null, "Gaming Laptop", 1499.99, 1L, null);
    ProductDTO response = createProductDTO(10L, "Gaming Laptop", 1499.99, 1L, "Laptops");

    when(productService.createProduct(any(ProductDTO.class))).thenReturn(response);

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(10L))
        .andExpect(jsonPath("$.name").value("Gaming Laptop"))
        .andExpect(jsonPath("$.categoryId").value(1L))
        .andExpect(jsonPath("$.categoryName").value("Laptops"));
  }

  @Test
  void testUpdateProduct() throws Exception {
    ProductDTO request = createProductDTO(null, "Updated Laptop", 1599.99, 1L, null);
    ProductDTO response = createProductDTO(1L, "Updated Laptop", 1599.99, 1L, "Laptops");

    when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(response);

    mockMvc.perform(put("/api/v1/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Laptop"))
        .andExpect(jsonPath("$.price").value(1599.99))
        .andExpect(jsonPath("$.categoryName").value("Laptops"));
  }

  @Test
  void testDeleteProduct() throws Exception {
    mockMvc.perform(delete("/api/v1/products/1"))
        .andExpect(status().isNoContent());
  }

  private ProductDTO createProductDTO(Long id, String name, Double price, Long categoryId, String categoryName) {
    ProductDTO dto = new ProductDTO();
    dto.setId(id);
    dto.setName(name);
    dto.setPrice(price);
    dto.setCategoryId(categoryId);
    dto.setCategoryName(categoryName);
    return dto;
  }
}

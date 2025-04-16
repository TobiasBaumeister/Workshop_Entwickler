package org.example.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.database.model.Category;
import org.example.database.repository.CategoryRepository;
import org.example.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ObjectMapper objectMapper;

  private Long categoryId;

  @BeforeEach
  void setUp() {
    Category category = new Category();
    category.setName("TestCategory");
    category = categoryRepository.save(category);
    categoryId = category.getId();
  }

  @Test
  void testCreateProduct() throws Exception {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Test Product");
    productDTO.setPrice(100.0);
    productDTO.setCategoryId(categoryId);

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name").value("Test Product"))
        .andExpect(jsonPath("$.price").value(100.0))
        .andExpect(jsonPath("$.categoryId").value(categoryId))
        .andExpect(jsonPath("$.categoryName").value("TestCategory"));
  }

  @Test
  void testGetAllProducts() throws Exception {
    // Lege zwei Produkte an
    ProductDTO product1 = new ProductDTO();
    product1.setName("Product1");
    product1.setPrice(50.0);
    product1.setCategoryId(categoryId);

    ProductDTO product2 = new ProductDTO();
    product2.setName("Product2");
    product2.setPrice(75.0);
    product2.setCategoryId(categoryId);

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(product1)))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(product2)))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/api/v1/products"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2));
  }

  @Test
  void testGetProductById() throws Exception {
    // Produkt anlegen
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Single Product");
    productDTO.setPrice(200.0);
    productDTO.setCategoryId(categoryId);

    String response = mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDTO)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    ProductDTO created = objectMapper.readValue(response, ProductDTO.class);

    mockMvc.perform(get("/api/v1/products/" + created.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(created.getId()))
        .andExpect(jsonPath("$.name").value("Single Product"));
  }

  @Test
  void testUpdateProduct() throws Exception {
    // Produkt anlegen
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Old Name");
    productDTO.setPrice(150.0);
    productDTO.setCategoryId(categoryId);

    String response = mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDTO)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    ProductDTO created = objectMapper.readValue(response, ProductDTO.class);

    // Produkt aktualisieren
    ProductDTO updateDTO = new ProductDTO();
    updateDTO.setName("Updated Name");
    updateDTO.setPrice(175.0);
    updateDTO.setCategoryId(categoryId);

    mockMvc.perform(put("/api/v1/products/" + created.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated Name"))
        .andExpect(jsonPath("$.price").value(175.0));
  }

  @Test
  void testDeleteProduct() throws Exception {
    // Produkt anlegen
    ProductDTO productDTO = new ProductDTO();
    productDTO.setName("Product to Delete");
    productDTO.setPrice(120.0);
    productDTO.setCategoryId(categoryId);

    String response = mockMvc.perform(post("/api/v1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productDTO)))
        .andExpect(status().isCreated())
        .andReturn().getResponse().getContentAsString();

    ProductDTO created = objectMapper.readValue(response, ProductDTO.class);

    // Produkt löschen
    mockMvc.perform(delete("/api/v1/products/" + created.getId()))
        .andExpect(status().isNoContent());

    // Überprüfen, dass das Produkt nicht mehr gefunden wird
    mockMvc.perform(get("/api/v1/products/" + created.getId()))
        .andExpect(status().isNotFound());
  }
}

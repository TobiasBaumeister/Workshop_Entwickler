package org.example.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.database.model.Category;
import org.example.database.repository.CategoryRepository;
import org.example.database.repository.ProductRepository;
import org.example.dto.CategoryDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class CategoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private CategoryRepository categoryRepository;

  @Autowired
  private ProductRepository productRepository;


  @BeforeEach
  void setUp() {
    categoryRepository.deleteAll();
  }

  @Test
  void testGetAllCategories() throws Exception {
    Category cat1 = categoryRepository.save(new Category(null, "Laptops"));
    Category cat2 = categoryRepository.save(new Category(null, "Phones"));

    mockMvc.perform(get("/api/v1/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].id").value(cat1.getId()))
        .andExpect(jsonPath("$[0].name").value("Laptops"))
        .andExpect(jsonPath("$[1].id").value(cat2.getId()))
        .andExpect(jsonPath("$[1].name").value("Phones"));
  }

  @Test
  void testCreateCategory() throws Exception {
    CategoryDTO request = new CategoryDTO();
    request.setName("Tablets");

    MvcResult result = mockMvc.perform(post("/api/v1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name").value("Tablets"))
        .andReturn();

    String responseString = result.getResponse().getContentAsString();
    CategoryDTO createdDto = objectMapper.readValue(responseString, CategoryDTO.class);
    Long createdId = createdDto.getId();

    assertThat(categoryRepository.findById(createdId)).isPresent();
    assertThat(categoryRepository.findById(createdId).get().getName()).isEqualTo("Tablets");
  }

  @Test
  void testCreateCategoryWithEmptyName_ShouldFail() throws Exception {
    CategoryDTO request = new CategoryDTO();
    request.setName("");

    mockMvc.perform(post("/api/v1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest()); // Erwarte HTTP 400 wegen Validation

    request.setName(null);
    mockMvc.perform(post("/api/v1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetCategoryByIdFound() throws Exception {
    Category savedCategory = categoryRepository.save(new Category(null, "Monitors"));
    Long categoryId = savedCategory.getId();

    mockMvc.perform(get("/api/v1/categories/" + categoryId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(categoryId))
        .andExpect(jsonPath("$.name").value("Monitors"));
  }

  @Test
  void testGetCategoryByIdNotFound() throws Exception {
    Long nonExistentId = 9999L;
    assertThat(categoryRepository.findById(nonExistentId)).isNotPresent();

    mockMvc.perform(get("/api/v1/categories/" + nonExistentId))
        .andExpect(status().isNotFound());
  }

  @Test
  void testUpdateCategory() throws Exception {
    Category category = new Category();
    category.setName("Old Name");
    Category savedCategory = categoryRepository.save(category);
    Long categoryId = savedCategory.getId();

    CategoryDTO updateRequest = new CategoryDTO();
    updateRequest.setName("New Updated Name");

    mockMvc.perform(put("/api/v1/categories/" + categoryId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(categoryId))
        .andExpect(jsonPath("$.name").value("New Updated Name"));

    assertThat(categoryRepository.findById(categoryId)).isPresent();
    assertThat(categoryRepository.findById(categoryId).get().getName()).isEqualTo("New Updated Name");
  }


  @Test
  void testDeleteCategory() throws Exception {
    Category categoryToDelete = categoryRepository.save(new Category(null, "ToDelete"));
    Long categoryId = categoryToDelete.getId();
    assertThat(categoryRepository.findById(categoryId)).isPresent();

    mockMvc.perform(delete("/api/v1/categories/" + categoryId))
        .andExpect(status().isNoContent());

    assertThat(categoryRepository.findById(categoryId)).isNotPresent();
  }
}
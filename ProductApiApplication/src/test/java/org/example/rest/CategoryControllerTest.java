package org.example.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.example.dto.CategoryDTO;
import org.example.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CategoryService categoryService;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testGetAllCategories() throws Exception {
    List<CategoryDTO> categories = List.of(
        new CategoryDTO() {{
          setId(1L);
          setName("Laptops");
        }},
        new CategoryDTO() {{
          setId(2L);
          setName("Phones");
        }}
    );

    when(categoryService.getAllCategories()).thenReturn(categories);

    mockMvc.perform(get("/api/v1/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(2))
        .andExpect(jsonPath("$[0].name").value("Laptops"));
  }

  @Test
  void testCreateCategory() throws Exception { //TODO: Fehler
    CategoryDTO request = new CategoryDTO();
    request.setName("Tablets");

    CategoryDTO response = new CategoryDTO();
    response.setId(3L);
    response.setName("Tablets");

    when(categoryService.createCategory(any(CategoryDTO.class))).thenReturn(response);

    String content = objectMapper.writeValueAsString(request);
    System.out.println(content); // {"id":null,"name":"Tablets"}
    mockMvc.perform(post("/api/v1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(3L))
        .andExpect(jsonPath("$.name").value("Tablets"));
  }

  @Test
  void testCreateCategoryWithEmptyName() throws Exception {
    CategoryDTO request = new CategoryDTO();

    mockMvc.perform(post("/api/v1/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testGetCategoryByIdFound() throws Exception {
    CategoryDTO dto = new CategoryDTO();
    dto.setId(1L);
    dto.setName("Laptops");

    when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(dto));

    mockMvc.perform(get("/api/v1/categories/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Laptops"));
  }

  @Test
  void testGetCategoryByIdNotFound() throws Exception {
    when(categoryService.getCategoryById(999L)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/v1/categories/999"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testDeleteCategory() throws Exception {
    doNothing().when(categoryService).deleteCategory(1L);

    mockMvc.perform(delete("/api/v1/categories/1"))
        .andExpect(status().isNoContent());
  }
}

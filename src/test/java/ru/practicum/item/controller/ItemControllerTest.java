package ru.practicum.item.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnItemsOfUser() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Later-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].url").value("https://example.com/item2"))
                .andExpect(jsonPath("$[1].url").value("https://example.com/item1"));
    }

    @Test
    public void shouldReturnItemsSortedByTitle() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Later-User-Id", 1L)
                        .param("sort", "TITLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Item 1"))
                .andExpect(jsonPath("$[1].title").value("Item 2"));
    }

    @Test
    public void shouldReturnItemsSortedByNewest() throws Exception {
        mockMvc.perform(get("/items")
                        .header("X-Later-User-Id", 1L)
                        .param("sort", "NEWEST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Item 2"))
                .andExpect(jsonPath("$[1].title").value("Item 1"));
    }

    @Test
    public void shouldAddNewItem() throws Exception {
        String itemJson = "{\"url\": \"https://example.com/newitem\"}";
        mockMvc.perform(post("/items")
                        .header("X-Later-User-Id", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.url").value("https://example.com/newitem"))
                .andExpect(jsonPath("$.userId").value(2L));
    }

    @Test
    public void shouldDeleteItem() throws Exception {
        mockMvc.perform(delete("/items/3")
                        .header("X-Later-User-Id", 2L))
                .andExpect(status().isOk());

        mockMvc.perform(get("/items")
                        .header("X-Later-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}

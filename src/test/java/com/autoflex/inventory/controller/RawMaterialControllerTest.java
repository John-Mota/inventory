package com.autoflex.inventory.controller;

import com.autoflex.inventory.dto.request.RawMaterialRequest;
import com.autoflex.inventory.dto.response.RawMaterialResponse;
import com.autoflex.inventory.service.RawMaterialService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RawMaterialController.class)
class RawMaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RawMaterialService rawMaterialService;

    @Autowired
    private ObjectMapper objectMapper;

    private RawMaterialRequest rawMaterialRequest;
    private RawMaterialResponse rawMaterialResponse;
    private UUID rawMaterialId;

    @BeforeEach
    void setUp() {
        rawMaterialId = UUID.randomUUID();
        rawMaterialRequest = new RawMaterialRequest("Test Material", 100.0);
        rawMaterialResponse = new RawMaterialResponse(rawMaterialId, "Test Material", 100.0);
    }

    @Test
    void findAll_ShouldReturnListOfRawMaterials() throws Exception {
        // Deve retornar uma lista de matérias-primas com status 200 OK
        when(rawMaterialService.findAll()).thenReturn(List.of(rawMaterialResponse));

        mockMvc.perform(get("/raw-materials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(rawMaterialId.toString()))
                .andExpect(jsonPath("$[0].name").value("Test Material"));
    }

    @Test
    void findById_ShouldReturnRawMaterial() throws Exception {
        // Deve retornar uma matéria-prima específica pelo ID com status 200 OK
        when(rawMaterialService.findById(rawMaterialId)).thenReturn(rawMaterialResponse);

        mockMvc.perform(get("/raw-materials/{id}", rawMaterialId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rawMaterialId.toString()))
                .andExpect(jsonPath("$.name").value("Test Material"));
    }

    @Test
    void create_ShouldReturnCreatedRawMaterial() throws Exception {
        // Deve criar uma nova matéria-prima e retornar status 201 Created
        when(rawMaterialService.create(any(RawMaterialRequest.class))).thenReturn(rawMaterialResponse);

        mockMvc.perform(post("/raw-materials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rawMaterialRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(rawMaterialId.toString()))
                .andExpect(jsonPath("$.name").value("Test Material"));
    }

    @Test
    void update_ShouldReturnUpdatedRawMaterial() throws Exception {
        // Deve atualizar uma matéria-prima existente e retornar status 200 OK
        when(rawMaterialService.update(eq(rawMaterialId), any(RawMaterialRequest.class))).thenReturn(rawMaterialResponse);

        mockMvc.perform(put("/raw-materials/{id}", rawMaterialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(rawMaterialRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(rawMaterialId.toString()))
                .andExpect(jsonPath("$.name").value("Test Material"));
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        // Deve excluir uma matéria-prima e retornar status 204 No Content
        doNothing().when(rawMaterialService).delete(rawMaterialId);

        mockMvc.perform(delete("/raw-materials/{id}", rawMaterialId))
                .andExpect(status().isNoContent());
    }
}

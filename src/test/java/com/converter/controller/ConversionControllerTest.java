package com.converter.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ConversionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listConvertersReturnsNonEmptyList() throws Exception {
        mockMvc.perform(get("/api/converters"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThan(0)));
    }

    @Test
    void listTypesReturnsKnownTypes() throws Exception {
        mockMvc.perform(get("/api/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@ == 'Person')]").exists())
                .andExpect(jsonPath("$[?(@ == 'JsonString')]").exists());
    }

    @Test
    void convertStringToLocalDate() throws Exception {
        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sourceType":"String","targetType":"LocalDate","value":"2024-06-15"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result").value("2024-06-15"));
    }

    @Test
    void convertPersonToJsonString() throws Exception {
        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "sourceType": "Person",
                                  "targetType": "JsonString",
                                  "value": "{\\"name\\":\\"Alice\\",\\"age\\":30,\\"email\\":\\"alice@example.com\\",\\"address\\":{\\"street\\":\\"123 Main St\\",\\"city\\":\\"Springfield\\",\\"state\\":\\"IL\\",\\"zipCode\\":\\"62701\\",\\"country\\":\\"US\\"}}"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result").value(Matchers.containsString("Alice")));
    }

    @Test
    void convertCsvStringToList() throws Exception {
        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sourceType":"CsvString","targetType":"List","value":"name,age\\nAlice,30\\nBob,25"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result").value(Matchers.containsString("Alice")));
    }

    @Test
    void missingSourceTypeReturns400() throws Exception {
        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sourceType":"","targetType":"LocalDate","value":"2024-06-15"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void unknownTypeReturns422() throws Exception {
        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sourceType":"GhostType","targetType":"LocalDate","value":"2024-06-15"}
                                """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void invalidDateValueReturns422() throws Exception {
        mockMvc.perform(post("/api/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"sourceType":"String","targetType":"LocalDate","value":"not-a-date"}
                                """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.success").value(false));
    }
}

package com.example.stage2025.dto;
import lombok.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
@Data @NoArgsConstructor @AllArgsConstructor
public class SoapFieldDto { private String name; private String type;




// You need to have the dependency: implementation 'com.fasterxml.jackson.core:jackson-databind:2.15.2'


    public static List<SoapFieldDto> fromJson(String json) {
        if (json == null || json.isEmpty()) return List.of();
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<SoapFieldDto>>() {});
        } catch (Exception ex) {
            throw new RuntimeException("Failed to parse SoapFieldDto JSON", ex);
        }
    }

    public static String toJson(List<SoapFieldDto> fields) {
        if (fields == null) return "[]";
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(fields);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to serialize SoapFieldDto JSON", ex);
        }
    }
}

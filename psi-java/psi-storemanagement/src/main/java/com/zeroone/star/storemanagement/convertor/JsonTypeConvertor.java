package com.zeroone.star.storemanagement.convertor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeroone.star.project.dto.j2.store.FileDTO;
import com.zeroone.star.project.dto.j2.store.LogisticsDTO;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonTypeConvertor {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Named("logisticsToJson")
    public String LogisticsToJson(LogisticsDTO logisticsDTO){
        if (logisticsDTO == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(logisticsDTO);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON失败", e);
        }
    }

    @Named("fileToJson")
    public String FileToJson(List<FileDTO> fileDTOList){
        if (fileDTOList == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(fileDTOList);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("对象转JSON失败", e);
        }
    }
    @Named("jsonToLogistics")
    public LogisticsDTO JsonToLogistics(String json){
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, LogisticsDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转对象失败", e);
        }
    }
    @Named("jsonToFile")
    public List<FileDTO> JsonToFile(String json){
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, List.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON转对象失败", e);
        }
    }

}

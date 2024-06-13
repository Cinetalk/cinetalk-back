package com.back.cinetalk.movie.service;

import com.back.cinetalk.movie.dto.CastDTO;
import com.back.cinetalk.movie.dto.CrewDTO;
import com.back.cinetalk.movie.dto.GenreDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataExtractor {

    public List<GenreDTO> extractGenres(List<Map<String, Object>> genresMapList) {
        return genresMapList.stream()
                .map(this::mapToGenreDTO)
                .collect(Collectors.toList());
    }

    public List<CastDTO> extractCast(List<Map<String, Object>> castMapList) {
        return castMapList.stream()
                .map(this::mapToCastDTO)
                .collect(Collectors.toList());
    }

    public List<CrewDTO> extractCrew(List<Map<String, Object>> crewMapList) {
        return crewMapList.stream()
                .map(this::mapToCrewDTO)
                .collect(Collectors.toList());
    }

    private GenreDTO mapToGenreDTO(Map<String, Object> genreMap) {
        return GenreDTO.builder()
                .id((Integer) genreMap.get("id"))
                .name((String) genreMap.get("name"))
                .build();
    }

    private CastDTO mapToCastDTO(Map<String, Object> castMap) {
        return CastDTO.builder()
                .id((Integer) castMap.get("id"))
                .name((String) castMap.get("name"))
                .character((String) castMap.get("character"))
                .order((Integer) castMap.get("order"))
                .profilePath((String) castMap.get("profilePath"))
                .build();
    }

    private CrewDTO mapToCrewDTO(Map<String, Object> crewMap) {
        return CrewDTO.builder()
                .id((Integer) crewMap.get("id"))
                .name((String) crewMap.get("name"))
                .job((String) crewMap.get("job"))
                .profilePath((String) crewMap.get("profilePath"))
                .build();
    }
}

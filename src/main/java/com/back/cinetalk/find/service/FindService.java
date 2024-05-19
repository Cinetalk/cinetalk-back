package com.back.cinetalk.find.service;

import com.back.cinetalk.find.dto.FindDTO;
import com.back.cinetalk.find.entity.FindEntity;
import com.back.cinetalk.find.repository.FindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindService {

    private final FindRepository findRepository;

    public ResponseEntity<?> WordSave(String keword){

        FindDTO findDTO = new FindDTO();

        findDTO.setKeword(keword);

        FindEntity findEntity = FindEntity.ToFindEntity(findDTO);

        findRepository.save(findEntity);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}

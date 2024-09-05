package com.back.cinetalk.admin.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AMainService {

    public ResponseEntity<?> userCountList(){



        return new ResponseEntity<>("", HttpStatus.OK);
    }
}

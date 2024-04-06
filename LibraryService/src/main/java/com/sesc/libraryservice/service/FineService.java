package com.sesc.libraryservice.service;

import com.sesc.libraryservice.repository.FineRepository;
import org.springframework.stereotype.Service;

@Service
public class FineService {

    private final FineRepository fineRepository;

    public FineService(FineRepository fineRepository) {
        this.fineRepository = fineRepository;
    }
}

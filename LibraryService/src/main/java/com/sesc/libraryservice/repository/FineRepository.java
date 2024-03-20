package com.sesc.libraryservice.repository;

import com.sesc.libraryservice.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, Long> {

}

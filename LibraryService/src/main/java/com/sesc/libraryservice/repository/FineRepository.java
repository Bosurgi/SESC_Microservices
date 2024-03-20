package com.sesc.libraryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository<Fine> extends JpaRepository<Fine, Long> {

}

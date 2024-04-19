package com.sesc.studentportal.services;

import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.repository.ModuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ModuleServiceTest {

    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private ModuleService moduleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllModules() {
        // Arrange
        Module module1 = new Module("Module 1", "Module 1 Description", 100.0);
        Module module2 = new Module("Module 2", "Module 2 Description", 200.0);
        List<Module> expectedModules = Arrays.asList(module1, module2);
        when(moduleRepository.findAll()).thenReturn(expectedModules);

        // Act
        List<Module> result = moduleService.getAllModules();

        // Assert
        assertEquals(expectedModules.size(), result.size());
        assertEquals(expectedModules, result);
    }

    @Test
    void testSearchModules() {
        // Arrange
        String keyword = "keyword";
        Module module1 = new Module("Module 1", "Module 1 Description", 100.0);
        Module module2 = new Module("Module 2", "Module 2 Description", 200.0);
        List<Module> expectedModules = Arrays.asList(module1, module2);
        when(moduleRepository.search(keyword)).thenReturn(expectedModules);

        // Act
        List<Module> result = moduleService.searchModules(keyword);

        // Assert
        assertEquals(expectedModules.size(), result.size());
        assertEquals(expectedModules, result);
    }

    @Test
    void testSearchModulesWithEmptyKeyword() {
        // Arrange
        String emptyKeyword = "";
        List<Module> emptyList = List.of();

        // Act
        List<Module> result = moduleService.searchModules(emptyKeyword);

        // Assert
        assertEquals(emptyList, result);
    }

    @Test
    void testSearchModulesWithNullKeyword() {
        // Arrange
        List<Module> emptyList = List.of();

        // Act
        List<Module> result = moduleService.searchModules(null);

        // Assert
        assertEquals(emptyList, result);
    }
}
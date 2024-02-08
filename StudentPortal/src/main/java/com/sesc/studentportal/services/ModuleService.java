package com.sesc.studentportal.services;

import com.sesc.studentportal.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    /***
     * Get all Modules from the Database
     * @return List of Modules
     */
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    /***
     * Get a Module by the Keyword from the Database
     * @param keyword the keyword to search for
     * @return a List of Modules
     */
    public List<Module> searchModules(@RequestParam String keyword) {
        return moduleRepository.search(keyword);
    }

    /***
     * Constructor for the ModuleService.
     * Using AutoWired annotation to inject the ModuleRepository into the ModuleService.
     * @param moduleRepository the Repository for the Module
     */
    @Autowired
    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }
}

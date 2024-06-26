package com.sesc.studentportal.controller;

import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.services.ModuleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/module")
public class ModuleController {

    /***
     * ModuleRepository to access the database with Dependency Injection.
     */
    private final ModuleService moduleService;

    /***
     * Get all Modules from Database
     * @return List of Modules
     */
    @GetMapping("/api/v1/modules")
    public List<Module> getModules() {
        return moduleService.getAllModules();
    }

    /***
     * Get a Module by the Keyword from Database
     * @param keyword the keyword to search for.
     * @return a List of Modules
     */
    @GetMapping("/api/v1/modules/search")
    public List<Module> getModule(@RequestParam String keyword) {
        return moduleService.searchModules(keyword);
    }


    // CONSTRUCTOR //

    /***
     * Constructor for the ModuleController.
     * @param moduleService The Service for the Module
     */
    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }
}

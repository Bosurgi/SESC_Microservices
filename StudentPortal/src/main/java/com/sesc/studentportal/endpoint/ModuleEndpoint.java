package com.sesc.studentportal.endpoint;

import com.sesc.studentportal.model.Module;
import com.sesc.studentportal.services.ModuleService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.Endpoint;

import java.util.List;

@Endpoint
// For Testing reasons allowed by everyone - in production this should be removed
@AnonymousAllowed
public class ModuleEndpoint {
    private final ModuleService moduleService;

    // CONSTRUCTOR FOR SERVICE //
    public ModuleEndpoint(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    // ENDPOINT FOR MODULES //
    public List<Module> getModules() {
        return moduleService.getAllModules();
    }
}

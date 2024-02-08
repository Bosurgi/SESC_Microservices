package com.sesc.studentportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/***
 * ModuleRepository which allows CRUD operations on the Module entity in the database.
 */
@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    /***
     * Find a module by its id
     * @param moduleId the module ID
     * @return the Module
     */
    Module findModuleByModuleId(Long moduleId);

    /***
     * Find a module by its module name
     * @param moduleName the module name
     * @return the Module
     */
    Module findModuleByModuleName(String moduleName);

    /***
     * Find a module by its module Title by querying the database
     * @param keyword the keyword to search for
     * @return the List of Modules corresponding to the search
     */
    @Query("SELECT m FROM Module m WHERE m.title LIKE %?1%")
    public List<Module> search(String keyword);
}

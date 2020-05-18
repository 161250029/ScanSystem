package com.example.demo.Dao;

import com.example.demo.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectDao extends JpaRepository<Project, Long> {
    @Query(value = "select p from Project p where p.projectname = :projectname")
    public Project findProjectbyName(@Param("projectname")String projectname);
}

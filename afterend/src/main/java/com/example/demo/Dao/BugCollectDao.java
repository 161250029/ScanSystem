package com.example.demo.Dao;

import com.example.demo.Entity.BugCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugCollectDao extends JpaRepository<BugCollect , Long> {
    @Query(value = "select b from BugCollect b where b.jar_location = :jarpath")
    public List<BugCollect> findbugsbyjar(@Param("jarpath") String jarpath);

    @Query(value = "select b from BugCollect b where b.sourceFile = :filename and b.toolName = :toolname")
    public List<BugCollect> findbugsByFileNameAndToolName(@Param("filename") String filename , @Param("toolname") String toolname);
}

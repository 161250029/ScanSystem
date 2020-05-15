package com.example.demo.Dao;

import com.example.demo.Entity.Bug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BugDao extends JpaRepository<Bug, Long> {


}

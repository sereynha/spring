package com.school.project.repository;

import com.school.project.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>, JpaSpecificationExecutor<Category> {
    @Query("select e from Category e where e.name = ?1 and (?2 is null or e.id != ?2)")
    Category findExistedName(String name, Long id);
}

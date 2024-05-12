package com.vr61v.notebook.port.repository;

import com.vr61v.notebook.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> { }

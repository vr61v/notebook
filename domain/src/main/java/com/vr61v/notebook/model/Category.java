package com.vr61v.notebook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Category {
    @Id
    private String name;

    @ManyToMany(targetEntity = Category.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "category_children",
            joinColumns = {@JoinColumn(name = "parent", referencedColumnName = "name")},
            inverseJoinColumns = {@JoinColumn(name = "child", referencedColumnName = "name")}
    )
    private List<Category> children;

    public boolean isLeaf() {
        return name.matches("^[0-9]{4}$");
    }
}

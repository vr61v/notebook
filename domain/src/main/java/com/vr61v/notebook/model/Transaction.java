package com.vr61v.notebook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Month;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "value", nullable = false)
    private int value;

    @Enumerated(EnumType.STRING)
    @Column(name = "month", nullable = false)
    private Month month;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mcc")
    private Category mcc;

    public Transaction(String name, int value, Month month, Category mcc) {
        this.name = name;
        this.value = value;
        this.month = month;
        this.mcc = mcc;
    }
}

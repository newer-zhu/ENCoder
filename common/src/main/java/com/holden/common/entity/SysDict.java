package com.holden.common.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sys_dict")
@Data
public class SysDict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dictType;
    private String dictKey;

    @Column(columnDefinition = "TEXT")
    private String dictValue;
}

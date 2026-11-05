package com.example.gymsaas.i18n.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "languages")
public class Language {

    @Id
    @Column(name = "code", length = 5)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;
}

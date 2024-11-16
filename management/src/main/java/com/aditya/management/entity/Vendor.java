package com.aditya.management.entity;

import com.aditya.management.constant.TableName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = TableName.VENDOR)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Vendor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;

    @Column(name = "vendor_name", nullable = false)
    private String name;
}

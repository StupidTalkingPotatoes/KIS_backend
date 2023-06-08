package org.stupid_talking_potatoes.kis.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@ToString
@Table(name = "route_seq", uniqueConstraints = @UniqueConstraint(columnNames = {"node_id", "route_id"}))
@NoArgsConstructor
@AllArgsConstructor
public class RouteSeq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
    
    @ManyToOne
    @JoinColumn(name = "node_id")
    private Node node;
    
    private Integer nodeOrder;
}
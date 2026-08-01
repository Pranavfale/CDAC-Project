package com.talentbridge.entity;

import com.talentbridge.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "offers")
@Getter
@Setter
public class Offer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "application_id", nullable = false)
	private Application application;

	@Column(nullable = false)
	private Double offeredSalary;

	@Column(nullable = false)
	private LocalDate joiningDate;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OfferStatus offerStatus;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

}
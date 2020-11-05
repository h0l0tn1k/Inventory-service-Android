package com.sap.grc.risk.assessment.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A RiskAssessment.
 */
@Entity
@Table(name = "risk_assessment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RiskAssessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Min(value = 1)
    @Column(name = "version", nullable = false)
    private Integer version;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @NotNull
    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo;

    @NotNull
    @Column(name = "risk_id", nullable = false)
    private UUID riskId;

    @OneToMany(mappedBy = "riskAssessment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AnalysisValues> analysisValues = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public RiskAssessment name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public RiskAssessment version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public RiskAssessment description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public RiskAssessment validFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public RiskAssessment validTo(LocalDate validTo) {
        this.validTo = validTo;
        return this;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public UUID getRiskId() {
        return riskId;
    }

    public RiskAssessment riskId(UUID riskId) {
        this.riskId = riskId;
        return this;
    }

    public void setRiskId(UUID riskId) {
        this.riskId = riskId;
    }

    public Set<AnalysisValues> getAnalysisValues() {
        return analysisValues;
    }

    public RiskAssessment analysisValues(Set<AnalysisValues> analysisValues) {
        this.analysisValues = analysisValues;
        return this;
    }

    public RiskAssessment addAnalysisValues(AnalysisValues analysisValues) {
        this.analysisValues.add(analysisValues);
        analysisValues.setRiskAssessment(this);
        return this;
    }

    public RiskAssessment removeAnalysisValues(AnalysisValues analysisValues) {
        this.analysisValues.remove(analysisValues);
        analysisValues.setRiskAssessment(null);
        return this;
    }

    public void setAnalysisValues(Set<AnalysisValues> analysisValues) {
        this.analysisValues = analysisValues;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RiskAssessment)) {
            return false;
        }
        return id != null && id.equals(((RiskAssessment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RiskAssessment{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", version=" + getVersion() +
            ", description='" + getDescription() + "'" +
            ", validFrom='" + getValidFrom() + "'" +
            ", validTo='" + getValidTo() + "'" +
            ", riskId='" + getRiskId() + "'" +
            "}";
    }
}

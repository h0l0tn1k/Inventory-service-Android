package com.sap.grc.risk.assessment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A AnalysisValues.
 */
@Entity
@Table(name = "analysis_values")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AnalysisValues implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties(value = "analysisValues", allowSetters = true)
    private RiskAssessment riskAssessment;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public AnalysisValues value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RiskAssessment getRiskAssessment() {
        return riskAssessment;
    }

    public AnalysisValues riskAssessment(RiskAssessment riskAssessment) {
        this.riskAssessment = riskAssessment;
        return this;
    }

    public void setRiskAssessment(RiskAssessment riskAssessment) {
        this.riskAssessment = riskAssessment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnalysisValues)) {
            return false;
        }
        return id != null && id.equals(((AnalysisValues) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnalysisValues{" +
            "id=" + getId() +
            ", value='" + getValue() + "'" +
            "}";
    }
}

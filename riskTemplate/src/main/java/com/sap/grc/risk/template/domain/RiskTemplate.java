package com.sap.grc.risk.template.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A RiskTemplate.
 */
@Entity
@Table(name = "risk_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RiskTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "risk_description")
    private String riskDescription;

    @Column(name = "assessment_description")
    private String assessmentDescription;

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

    public RiskTemplate name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public RiskTemplate description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRiskDescription() {
        return riskDescription;
    }

    public RiskTemplate riskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
        return this;
    }

    public void setRiskDescription(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    public String getAssessmentDescription() {
        return assessmentDescription;
    }

    public RiskTemplate assessmentDescription(String assessmentDescription) {
        this.assessmentDescription = assessmentDescription;
        return this;
    }

    public void setAssessmentDescription(String assessmentDescription) {
        this.assessmentDescription = assessmentDescription;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RiskTemplate)) {
            return false;
        }
        return id != null && id.equals(((RiskTemplate) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RiskTemplate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", riskDescription='" + getRiskDescription() + "'" +
            ", assessmentDescription='" + getAssessmentDescription() + "'" +
            "}";
    }
}

package com.sap.grc.riskframework.web.rest;

import com.sap.grc.riskframework.domain.RiskAssessment;
import com.sap.grc.riskframework.repository.RiskAssessmentRepository;
import com.sap.grc.riskframework.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sap.grc.riskframework.domain.RiskAssessment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RiskAssessmentResource {

    private final Logger log = LoggerFactory.getLogger(RiskAssessmentResource.class);

    private static final String ENTITY_NAME = "riskAssessment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RiskAssessmentRepository riskAssessmentRepository;

    public RiskAssessmentResource(RiskAssessmentRepository riskAssessmentRepository) {
        this.riskAssessmentRepository = riskAssessmentRepository;
    }

    /**
     * {@code POST  /risk-assessments} : Create a new riskAssessment.
     *
     * @param riskAssessment the riskAssessment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new riskAssessment, or with status {@code 400 (Bad Request)} if the riskAssessment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/risk-assessments")
    public ResponseEntity<RiskAssessment> createRiskAssessment(@Valid @RequestBody RiskAssessment riskAssessment) throws URISyntaxException {
        log.debug("REST request to save RiskAssessment : {}", riskAssessment);
        if (riskAssessment.getId() != null) {
            throw new BadRequestAlertException("A new riskAssessment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RiskAssessment result = riskAssessmentRepository.save(riskAssessment);
        return ResponseEntity.created(new URI("/api/risk-assessments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /risk-assessments} : Updates an existing riskAssessment.
     *
     * @param riskAssessment the riskAssessment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated riskAssessment,
     * or with status {@code 400 (Bad Request)} if the riskAssessment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the riskAssessment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/risk-assessments")
    public ResponseEntity<RiskAssessment> updateRiskAssessment(@Valid @RequestBody RiskAssessment riskAssessment) throws URISyntaxException {
        log.debug("REST request to update RiskAssessment : {}", riskAssessment);
        if (riskAssessment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RiskAssessment result = riskAssessmentRepository.save(riskAssessment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, riskAssessment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /risk-assessments} : get all the riskAssessments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of riskAssessments in body.
     */
    @GetMapping("/risk-assessments")
    public List<RiskAssessment> getAllRiskAssessments() {
        log.debug("REST request to get all RiskAssessments");
        return riskAssessmentRepository.findAll();
    }

    /**
     * {@code GET  /risk-assessments/:id} : get the "id" riskAssessment.
     *
     * @param id the id of the riskAssessment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the riskAssessment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/risk-assessments/{id}")
    public ResponseEntity<RiskAssessment> getRiskAssessment(@PathVariable Long id) {
        log.debug("REST request to get RiskAssessment : {}", id);
        Optional<RiskAssessment> riskAssessment = riskAssessmentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(riskAssessment);
    }

    /**
     * {@code DELETE  /risk-assessments/:id} : delete the "id" riskAssessment.
     *
     * @param id the id of the riskAssessment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/risk-assessments/{id}")
    public ResponseEntity<Void> deleteRiskAssessment(@PathVariable Long id) {
        log.debug("REST request to delete RiskAssessment : {}", id);
        riskAssessmentRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

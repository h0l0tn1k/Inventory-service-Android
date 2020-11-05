package com.sap.grc.risk.assessment.web.rest;

import com.sap.grc.risk.assessment.domain.AnalysisValues;
import com.sap.grc.risk.assessment.repository.AnalysisValuesRepository;
import com.sap.grc.risk.assessment.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.sap.grc.risk.assessment.domain.AnalysisValues}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AnalysisValuesResource {

    private final Logger log = LoggerFactory.getLogger(AnalysisValuesResource.class);

    private static final String ENTITY_NAME = "assessmentAnalysisValues";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnalysisValuesRepository analysisValuesRepository;

    public AnalysisValuesResource(AnalysisValuesRepository analysisValuesRepository) {
        this.analysisValuesRepository = analysisValuesRepository;
    }

    /**
     * {@code POST  /analysis-values} : Create a new analysisValues.
     *
     * @param analysisValues the analysisValues to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new analysisValues, or with status {@code 400 (Bad Request)} if the analysisValues has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/analysis-values")
    public ResponseEntity<AnalysisValues> createAnalysisValues(@RequestBody AnalysisValues analysisValues) throws URISyntaxException {
        log.debug("REST request to save AnalysisValues : {}", analysisValues);
        if (analysisValues.getId() != null) {
            throw new BadRequestAlertException("A new analysisValues cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnalysisValues result = analysisValuesRepository.save(analysisValues);
        return ResponseEntity.created(new URI("/api/analysis-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /analysis-values} : Updates an existing analysisValues.
     *
     * @param analysisValues the analysisValues to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analysisValues,
     * or with status {@code 400 (Bad Request)} if the analysisValues is not valid,
     * or with status {@code 500 (Internal Server Error)} if the analysisValues couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/analysis-values")
    public ResponseEntity<AnalysisValues> updateAnalysisValues(@RequestBody AnalysisValues analysisValues) throws URISyntaxException {
        log.debug("REST request to update AnalysisValues : {}", analysisValues);
        if (analysisValues.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnalysisValues result = analysisValuesRepository.save(analysisValues);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, analysisValues.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /analysis-values} : get all the analysisValues.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of analysisValues in body.
     */
    @GetMapping("/analysis-values")
    public List<AnalysisValues> getAllAnalysisValues() {
        log.debug("REST request to get all AnalysisValues");
        return analysisValuesRepository.findAll();
    }

    /**
     * {@code GET  /analysis-values/:id} : get the "id" analysisValues.
     *
     * @param id the id of the analysisValues to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the analysisValues, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/analysis-values/{id}")
    public ResponseEntity<AnalysisValues> getAnalysisValues(@PathVariable Long id) {
        log.debug("REST request to get AnalysisValues : {}", id);
        Optional<AnalysisValues> analysisValues = analysisValuesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(analysisValues);
    }

    /**
     * {@code DELETE  /analysis-values/:id} : delete the "id" analysisValues.
     *
     * @param id the id of the analysisValues to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/analysis-values/{id}")
    public ResponseEntity<Void> deleteAnalysisValues(@PathVariable Long id) {
        log.debug("REST request to delete AnalysisValues : {}", id);
        analysisValuesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

package com.sap.grc.risk.template.web.rest;

import com.sap.grc.risk.template.domain.RiskTemplate;
import com.sap.grc.risk.template.repository.RiskTemplateRepository;
import com.sap.grc.risk.template.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.sap.grc.risk.template.domain.RiskTemplate}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RiskTemplateResource {

    private final Logger log = LoggerFactory.getLogger(RiskTemplateResource.class);

    private static final String ENTITY_NAME = "riskTemplateRiskTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RiskTemplateRepository riskTemplateRepository;

    public RiskTemplateResource(RiskTemplateRepository riskTemplateRepository) {
        this.riskTemplateRepository = riskTemplateRepository;
    }

    /**
     * {@code POST  /risk-templates} : Create a new riskTemplate.
     *
     * @param riskTemplate the riskTemplate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new riskTemplate, or with status {@code 400 (Bad Request)} if the riskTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/risk-templates")
    public ResponseEntity<RiskTemplate> createRiskTemplate(@Valid @RequestBody RiskTemplate riskTemplate) throws URISyntaxException {
        log.debug("REST request to save RiskTemplate : {}", riskTemplate);
        if (riskTemplate.getId() != null) {
            throw new BadRequestAlertException("A new riskTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RiskTemplate result = riskTemplateRepository.save(riskTemplate);
        return ResponseEntity.created(new URI("/api/risk-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /risk-templates} : Updates an existing riskTemplate.
     *
     * @param riskTemplate the riskTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated riskTemplate,
     * or with status {@code 400 (Bad Request)} if the riskTemplate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the riskTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/risk-templates")
    public ResponseEntity<RiskTemplate> updateRiskTemplate(@Valid @RequestBody RiskTemplate riskTemplate) throws URISyntaxException {
        log.debug("REST request to update RiskTemplate : {}", riskTemplate);
        if (riskTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RiskTemplate result = riskTemplateRepository.save(riskTemplate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, riskTemplate.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /risk-templates} : get all the riskTemplates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of riskTemplates in body.
     */
    @GetMapping("/risk-templates")
    public List<RiskTemplate> getAllRiskTemplates() {
        log.debug("REST request to get all RiskTemplates");
        return riskTemplateRepository.findAll();
    }

    /**
     * {@code GET  /risk-templates/:id} : get the "id" riskTemplate.
     *
     * @param id the id of the riskTemplate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the riskTemplate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/risk-templates/{id}")
    public ResponseEntity<RiskTemplate> getRiskTemplate(@PathVariable Long id) {
        log.debug("REST request to get RiskTemplate : {}", id);
        Optional<RiskTemplate> riskTemplate = riskTemplateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(riskTemplate);
    }

    /**
     * {@code DELETE  /risk-templates/:id} : delete the "id" riskTemplate.
     *
     * @param id the id of the riskTemplate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/risk-templates/{id}")
    public ResponseEntity<Void> deleteRiskTemplate(@PathVariable Long id) {
        log.debug("REST request to delete RiskTemplate : {}", id);
        riskTemplateRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

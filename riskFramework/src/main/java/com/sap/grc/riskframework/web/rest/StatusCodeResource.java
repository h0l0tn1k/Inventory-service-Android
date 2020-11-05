package com.sap.grc.riskframework.web.rest;

import com.sap.grc.riskframework.domain.StatusCode;
import com.sap.grc.riskframework.repository.StatusCodeRepository;
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
 * REST controller for managing {@link com.sap.grc.riskframework.domain.StatusCode}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StatusCodeResource {

    private final Logger log = LoggerFactory.getLogger(StatusCodeResource.class);

    private static final String ENTITY_NAME = "statusCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StatusCodeRepository statusCodeRepository;

    public StatusCodeResource(StatusCodeRepository statusCodeRepository) {
        this.statusCodeRepository = statusCodeRepository;
    }

    /**
     * {@code POST  /status-codes} : Create a new statusCode.
     *
     * @param statusCode the statusCode to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new statusCode, or with status {@code 400 (Bad Request)} if the statusCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/status-codes")
    public ResponseEntity<StatusCode> createStatusCode(@Valid @RequestBody StatusCode statusCode) throws URISyntaxException {
        log.debug("REST request to save StatusCode : {}", statusCode);
        if (statusCode.getId() != null) {
            throw new BadRequestAlertException("A new statusCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StatusCode result = statusCodeRepository.save(statusCode);
        return ResponseEntity.created(new URI("/api/status-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /status-codes} : Updates an existing statusCode.
     *
     * @param statusCode the statusCode to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated statusCode,
     * or with status {@code 400 (Bad Request)} if the statusCode is not valid,
     * or with status {@code 500 (Internal Server Error)} if the statusCode couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/status-codes")
    public ResponseEntity<StatusCode> updateStatusCode(@Valid @RequestBody StatusCode statusCode) throws URISyntaxException {
        log.debug("REST request to update StatusCode : {}", statusCode);
        if (statusCode.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StatusCode result = statusCodeRepository.save(statusCode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, statusCode.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /status-codes} : get all the statusCodes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of statusCodes in body.
     */
    @GetMapping("/status-codes")
    public List<StatusCode> getAllStatusCodes() {
        log.debug("REST request to get all StatusCodes");
        return statusCodeRepository.findAll();
    }

    /**
     * {@code GET  /status-codes/:id} : get the "id" statusCode.
     *
     * @param id the id of the statusCode to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the statusCode, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/status-codes/{id}")
    public ResponseEntity<StatusCode> getStatusCode(@PathVariable Long id) {
        log.debug("REST request to get StatusCode : {}", id);
        Optional<StatusCode> statusCode = statusCodeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(statusCode);
    }

    /**
     * {@code DELETE  /status-codes/:id} : delete the "id" statusCode.
     *
     * @param id the id of the statusCode to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/status-codes/{id}")
    public ResponseEntity<Void> deleteStatusCode(@PathVariable Long id) {
        log.debug("REST request to delete StatusCode : {}", id);
        statusCodeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}

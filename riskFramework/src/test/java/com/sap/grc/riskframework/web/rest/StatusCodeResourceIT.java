package com.sap.grc.riskframework.web.rest;

import com.sap.grc.riskframework.RiskFrameworkApp;
import com.sap.grc.riskframework.domain.StatusCode;
import com.sap.grc.riskframework.repository.StatusCodeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StatusCodeResource} REST controller.
 */
@SpringBootTest(classes = RiskFrameworkApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class StatusCodeResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Autowired
    private StatusCodeRepository statusCodeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStatusCodeMockMvc;

    private StatusCode statusCode;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusCode createEntity(EntityManager em) {
        StatusCode statusCode = new StatusCode()
            .code(DEFAULT_CODE);
        return statusCode;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StatusCode createUpdatedEntity(EntityManager em) {
        StatusCode statusCode = new StatusCode()
            .code(UPDATED_CODE);
        return statusCode;
    }

    @BeforeEach
    public void initTest() {
        statusCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createStatusCode() throws Exception {
        int databaseSizeBeforeCreate = statusCodeRepository.findAll().size();
        // Create the StatusCode
        restStatusCodeMockMvc.perform(post("/api/status-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statusCode)))
            .andExpect(status().isCreated());

        // Validate the StatusCode in the database
        List<StatusCode> statusCodeList = statusCodeRepository.findAll();
        assertThat(statusCodeList).hasSize(databaseSizeBeforeCreate + 1);
        StatusCode testStatusCode = statusCodeList.get(statusCodeList.size() - 1);
        assertThat(testStatusCode.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createStatusCodeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = statusCodeRepository.findAll().size();

        // Create the StatusCode with an existing ID
        statusCode.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStatusCodeMockMvc.perform(post("/api/status-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statusCode)))
            .andExpect(status().isBadRequest());

        // Validate the StatusCode in the database
        List<StatusCode> statusCodeList = statusCodeRepository.findAll();
        assertThat(statusCodeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = statusCodeRepository.findAll().size();
        // set the field null
        statusCode.setCode(null);

        // Create the StatusCode, which fails.


        restStatusCodeMockMvc.perform(post("/api/status-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statusCode)))
            .andExpect(status().isBadRequest());

        List<StatusCode> statusCodeList = statusCodeRepository.findAll();
        assertThat(statusCodeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStatusCodes() throws Exception {
        // Initialize the database
        statusCodeRepository.saveAndFlush(statusCode);

        // Get all the statusCodeList
        restStatusCodeMockMvc.perform(get("/api/status-codes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statusCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)));
    }
    
    @Test
    @Transactional
    public void getStatusCode() throws Exception {
        // Initialize the database
        statusCodeRepository.saveAndFlush(statusCode);

        // Get the statusCode
        restStatusCodeMockMvc.perform(get("/api/status-codes/{id}", statusCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(statusCode.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE));
    }
    @Test
    @Transactional
    public void getNonExistingStatusCode() throws Exception {
        // Get the statusCode
        restStatusCodeMockMvc.perform(get("/api/status-codes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatusCode() throws Exception {
        // Initialize the database
        statusCodeRepository.saveAndFlush(statusCode);

        int databaseSizeBeforeUpdate = statusCodeRepository.findAll().size();

        // Update the statusCode
        StatusCode updatedStatusCode = statusCodeRepository.findById(statusCode.getId()).get();
        // Disconnect from session so that the updates on updatedStatusCode are not directly saved in db
        em.detach(updatedStatusCode);
        updatedStatusCode
            .code(UPDATED_CODE);

        restStatusCodeMockMvc.perform(put("/api/status-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStatusCode)))
            .andExpect(status().isOk());

        // Validate the StatusCode in the database
        List<StatusCode> statusCodeList = statusCodeRepository.findAll();
        assertThat(statusCodeList).hasSize(databaseSizeBeforeUpdate);
        StatusCode testStatusCode = statusCodeList.get(statusCodeList.size() - 1);
        assertThat(testStatusCode.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingStatusCode() throws Exception {
        int databaseSizeBeforeUpdate = statusCodeRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStatusCodeMockMvc.perform(put("/api/status-codes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(statusCode)))
            .andExpect(status().isBadRequest());

        // Validate the StatusCode in the database
        List<StatusCode> statusCodeList = statusCodeRepository.findAll();
        assertThat(statusCodeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStatusCode() throws Exception {
        // Initialize the database
        statusCodeRepository.saveAndFlush(statusCode);

        int databaseSizeBeforeDelete = statusCodeRepository.findAll().size();

        // Delete the statusCode
        restStatusCodeMockMvc.perform(delete("/api/status-codes/{id}", statusCode.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StatusCode> statusCodeList = statusCodeRepository.findAll();
        assertThat(statusCodeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

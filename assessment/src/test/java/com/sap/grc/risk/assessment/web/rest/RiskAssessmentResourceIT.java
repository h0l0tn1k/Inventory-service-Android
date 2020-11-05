package com.sap.grc.risk.assessment.web.rest;

import com.sap.grc.risk.assessment.AssessmentApp;
import com.sap.grc.risk.assessment.domain.RiskAssessment;
import com.sap.grc.risk.assessment.repository.RiskAssessmentRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link RiskAssessmentResource} REST controller.
 */
@SpringBootTest(classes = AssessmentApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RiskAssessmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_VALID_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_VALID_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VALID_TO = LocalDate.now(ZoneId.systemDefault());

    private static final UUID DEFAULT_RISK_ID = UUID.randomUUID();
    private static final UUID UPDATED_RISK_ID = UUID.randomUUID();

    @Autowired
    private RiskAssessmentRepository riskAssessmentRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRiskAssessmentMockMvc;

    private RiskAssessment riskAssessment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RiskAssessment createEntity(EntityManager em) {
        RiskAssessment riskAssessment = new RiskAssessment()
            .name(DEFAULT_NAME)
            .version(DEFAULT_VERSION)
            .description(DEFAULT_DESCRIPTION)
            .validFrom(DEFAULT_VALID_FROM)
            .validTo(DEFAULT_VALID_TO)
            .riskId(DEFAULT_RISK_ID);
        return riskAssessment;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RiskAssessment createUpdatedEntity(EntityManager em) {
        RiskAssessment riskAssessment = new RiskAssessment()
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .description(UPDATED_DESCRIPTION)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .riskId(UPDATED_RISK_ID);
        return riskAssessment;
    }

    @BeforeEach
    public void initTest() {
        riskAssessment = createEntity(em);
    }

    @Test
    @Transactional
    public void createRiskAssessment() throws Exception {
        int databaseSizeBeforeCreate = riskAssessmentRepository.findAll().size();
        // Create the RiskAssessment
        restRiskAssessmentMockMvc.perform(post("/api/risk-assessments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskAssessment)))
            .andExpect(status().isCreated());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeCreate + 1);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(riskAssessmentList.size() - 1);
        assertThat(testRiskAssessment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRiskAssessment.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testRiskAssessment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRiskAssessment.getValidFrom()).isEqualTo(DEFAULT_VALID_FROM);
        assertThat(testRiskAssessment.getValidTo()).isEqualTo(DEFAULT_VALID_TO);
        assertThat(testRiskAssessment.getRiskId()).isEqualTo(DEFAULT_RISK_ID);
    }

    @Test
    @Transactional
    public void createRiskAssessmentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = riskAssessmentRepository.findAll().size();

        // Create the RiskAssessment with an existing ID
        riskAssessment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRiskAssessmentMockMvc.perform(post("/api/risk-assessments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskAssessment)))
            .andExpect(status().isBadRequest());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = riskAssessmentRepository.findAll().size();
        // set the field null
        riskAssessment.setName(null);

        // Create the RiskAssessment, which fails.


        restRiskAssessmentMockMvc.perform(post("/api/risk-assessments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskAssessment)))
            .andExpect(status().isBadRequest());

        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = riskAssessmentRepository.findAll().size();
        // set the field null
        riskAssessment.setVersion(null);

        // Create the RiskAssessment, which fails.


        restRiskAssessmentMockMvc.perform(post("/api/risk-assessments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskAssessment)))
            .andExpect(status().isBadRequest());

        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = riskAssessmentRepository.findAll().size();
        // set the field null
        riskAssessment.setDescription(null);

        // Create the RiskAssessment, which fails.


        restRiskAssessmentMockMvc.perform(post("/api/risk-assessments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskAssessment)))
            .andExpect(status().isBadRequest());

        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = riskAssessmentRepository.findAll().size();
        // set the field null
        riskAssessment.setValidFrom(null);

        // Create the RiskAssessment, which fails.


        restRiskAssessmentMockMvc.perform(post("/api/risk-assessments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskAssessment)))
            .andExpect(status().isBadRequest());

        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValidToIsRequired() throws Exception {
        int databaseSizeBeforeTest = riskAssessmentRepository.findAll().size();
        // set the field null
        riskAssessment.setValidTo(null);

        // Create the RiskAssessment, which fails.


        restRiskAssessmentMockMvc.perform(post("/api/risk-assessments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskAssessment)))
            .andExpect(status().isBadRequest());

        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRiskIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = riskAssessmentRepository.findAll().size();
        // set the field null
        riskAssessment.setRiskId(null);

        // Create the RiskAssessment, which fails.


        restRiskAssessmentMockMvc.perform(post("/api/risk-assessments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskAssessment)))
            .andExpect(status().isBadRequest());

        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRiskAssessments() throws Exception {
        // Initialize the database
        riskAssessmentRepository.saveAndFlush(riskAssessment);

        // Get all the riskAssessmentList
        restRiskAssessmentMockMvc.perform(get("/api/risk-assessments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(riskAssessment.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].validFrom").value(hasItem(DEFAULT_VALID_FROM.toString())))
            .andExpect(jsonPath("$.[*].validTo").value(hasItem(DEFAULT_VALID_TO.toString())))
            .andExpect(jsonPath("$.[*].riskId").value(hasItem(DEFAULT_RISK_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getRiskAssessment() throws Exception {
        // Initialize the database
        riskAssessmentRepository.saveAndFlush(riskAssessment);

        // Get the riskAssessment
        restRiskAssessmentMockMvc.perform(get("/api/risk-assessments/{id}", riskAssessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(riskAssessment.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.validFrom").value(DEFAULT_VALID_FROM.toString()))
            .andExpect(jsonPath("$.validTo").value(DEFAULT_VALID_TO.toString()))
            .andExpect(jsonPath("$.riskId").value(DEFAULT_RISK_ID.toString()));
    }
    @Test
    @Transactional
    public void getNonExistingRiskAssessment() throws Exception {
        // Get the riskAssessment
        restRiskAssessmentMockMvc.perform(get("/api/risk-assessments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRiskAssessment() throws Exception {
        // Initialize the database
        riskAssessmentRepository.saveAndFlush(riskAssessment);

        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();

        // Update the riskAssessment
        RiskAssessment updatedRiskAssessment = riskAssessmentRepository.findById(riskAssessment.getId()).get();
        // Disconnect from session so that the updates on updatedRiskAssessment are not directly saved in db
        em.detach(updatedRiskAssessment);
        updatedRiskAssessment
            .name(UPDATED_NAME)
            .version(UPDATED_VERSION)
            .description(UPDATED_DESCRIPTION)
            .validFrom(UPDATED_VALID_FROM)
            .validTo(UPDATED_VALID_TO)
            .riskId(UPDATED_RISK_ID);

        restRiskAssessmentMockMvc.perform(put("/api/risk-assessments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRiskAssessment)))
            .andExpect(status().isOk());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
        RiskAssessment testRiskAssessment = riskAssessmentList.get(riskAssessmentList.size() - 1);
        assertThat(testRiskAssessment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRiskAssessment.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testRiskAssessment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRiskAssessment.getValidFrom()).isEqualTo(UPDATED_VALID_FROM);
        assertThat(testRiskAssessment.getValidTo()).isEqualTo(UPDATED_VALID_TO);
        assertThat(testRiskAssessment.getRiskId()).isEqualTo(UPDATED_RISK_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingRiskAssessment() throws Exception {
        int databaseSizeBeforeUpdate = riskAssessmentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRiskAssessmentMockMvc.perform(put("/api/risk-assessments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskAssessment)))
            .andExpect(status().isBadRequest());

        // Validate the RiskAssessment in the database
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRiskAssessment() throws Exception {
        // Initialize the database
        riskAssessmentRepository.saveAndFlush(riskAssessment);

        int databaseSizeBeforeDelete = riskAssessmentRepository.findAll().size();

        // Delete the riskAssessment
        restRiskAssessmentMockMvc.perform(delete("/api/risk-assessments/{id}", riskAssessment.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RiskAssessment> riskAssessmentList = riskAssessmentRepository.findAll();
        assertThat(riskAssessmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

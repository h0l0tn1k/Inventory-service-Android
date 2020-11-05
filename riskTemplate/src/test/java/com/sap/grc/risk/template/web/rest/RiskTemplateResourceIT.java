package com.sap.grc.risk.template.web.rest;

import com.sap.grc.risk.template.RiskTemplateApp;
import com.sap.grc.risk.template.domain.RiskTemplate;
import com.sap.grc.risk.template.repository.RiskTemplateRepository;

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
 * Integration tests for the {@link RiskTemplateResource} REST controller.
 */
@SpringBootTest(classes = RiskTemplateApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class RiskTemplateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_RISK_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_RISK_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ASSESSMENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ASSESSMENT_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RiskTemplateRepository riskTemplateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRiskTemplateMockMvc;

    private RiskTemplate riskTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RiskTemplate createEntity(EntityManager em) {
        RiskTemplate riskTemplate = new RiskTemplate()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .riskDescription(DEFAULT_RISK_DESCRIPTION)
            .assessmentDescription(DEFAULT_ASSESSMENT_DESCRIPTION);
        return riskTemplate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RiskTemplate createUpdatedEntity(EntityManager em) {
        RiskTemplate riskTemplate = new RiskTemplate()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .riskDescription(UPDATED_RISK_DESCRIPTION)
            .assessmentDescription(UPDATED_ASSESSMENT_DESCRIPTION);
        return riskTemplate;
    }

    @BeforeEach
    public void initTest() {
        riskTemplate = createEntity(em);
    }

    @Test
    @Transactional
    public void createRiskTemplate() throws Exception {
        int databaseSizeBeforeCreate = riskTemplateRepository.findAll().size();
        // Create the RiskTemplate
        restRiskTemplateMockMvc.perform(post("/api/risk-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskTemplate)))
            .andExpect(status().isCreated());

        // Validate the RiskTemplate in the database
        List<RiskTemplate> riskTemplateList = riskTemplateRepository.findAll();
        assertThat(riskTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        RiskTemplate testRiskTemplate = riskTemplateList.get(riskTemplateList.size() - 1);
        assertThat(testRiskTemplate.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRiskTemplate.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRiskTemplate.getRiskDescription()).isEqualTo(DEFAULT_RISK_DESCRIPTION);
        assertThat(testRiskTemplate.getAssessmentDescription()).isEqualTo(DEFAULT_ASSESSMENT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRiskTemplateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = riskTemplateRepository.findAll().size();

        // Create the RiskTemplate with an existing ID
        riskTemplate.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRiskTemplateMockMvc.perform(post("/api/risk-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the RiskTemplate in the database
        List<RiskTemplate> riskTemplateList = riskTemplateRepository.findAll();
        assertThat(riskTemplateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = riskTemplateRepository.findAll().size();
        // set the field null
        riskTemplate.setName(null);

        // Create the RiskTemplate, which fails.


        restRiskTemplateMockMvc.perform(post("/api/risk-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskTemplate)))
            .andExpect(status().isBadRequest());

        List<RiskTemplate> riskTemplateList = riskTemplateRepository.findAll();
        assertThat(riskTemplateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRiskTemplates() throws Exception {
        // Initialize the database
        riskTemplateRepository.saveAndFlush(riskTemplate);

        // Get all the riskTemplateList
        restRiskTemplateMockMvc.perform(get("/api/risk-templates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(riskTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].riskDescription").value(hasItem(DEFAULT_RISK_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].assessmentDescription").value(hasItem(DEFAULT_ASSESSMENT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getRiskTemplate() throws Exception {
        // Initialize the database
        riskTemplateRepository.saveAndFlush(riskTemplate);

        // Get the riskTemplate
        restRiskTemplateMockMvc.perform(get("/api/risk-templates/{id}", riskTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(riskTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.riskDescription").value(DEFAULT_RISK_DESCRIPTION))
            .andExpect(jsonPath("$.assessmentDescription").value(DEFAULT_ASSESSMENT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingRiskTemplate() throws Exception {
        // Get the riskTemplate
        restRiskTemplateMockMvc.perform(get("/api/risk-templates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRiskTemplate() throws Exception {
        // Initialize the database
        riskTemplateRepository.saveAndFlush(riskTemplate);

        int databaseSizeBeforeUpdate = riskTemplateRepository.findAll().size();

        // Update the riskTemplate
        RiskTemplate updatedRiskTemplate = riskTemplateRepository.findById(riskTemplate.getId()).get();
        // Disconnect from session so that the updates on updatedRiskTemplate are not directly saved in db
        em.detach(updatedRiskTemplate);
        updatedRiskTemplate
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .riskDescription(UPDATED_RISK_DESCRIPTION)
            .assessmentDescription(UPDATED_ASSESSMENT_DESCRIPTION);

        restRiskTemplateMockMvc.perform(put("/api/risk-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRiskTemplate)))
            .andExpect(status().isOk());

        // Validate the RiskTemplate in the database
        List<RiskTemplate> riskTemplateList = riskTemplateRepository.findAll();
        assertThat(riskTemplateList).hasSize(databaseSizeBeforeUpdate);
        RiskTemplate testRiskTemplate = riskTemplateList.get(riskTemplateList.size() - 1);
        assertThat(testRiskTemplate.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRiskTemplate.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRiskTemplate.getRiskDescription()).isEqualTo(UPDATED_RISK_DESCRIPTION);
        assertThat(testRiskTemplate.getAssessmentDescription()).isEqualTo(UPDATED_ASSESSMENT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRiskTemplate() throws Exception {
        int databaseSizeBeforeUpdate = riskTemplateRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRiskTemplateMockMvc.perform(put("/api/risk-templates")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(riskTemplate)))
            .andExpect(status().isBadRequest());

        // Validate the RiskTemplate in the database
        List<RiskTemplate> riskTemplateList = riskTemplateRepository.findAll();
        assertThat(riskTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRiskTemplate() throws Exception {
        // Initialize the database
        riskTemplateRepository.saveAndFlush(riskTemplate);

        int databaseSizeBeforeDelete = riskTemplateRepository.findAll().size();

        // Delete the riskTemplate
        restRiskTemplateMockMvc.perform(delete("/api/risk-templates/{id}", riskTemplate.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RiskTemplate> riskTemplateList = riskTemplateRepository.findAll();
        assertThat(riskTemplateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

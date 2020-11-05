package com.sap.grc.riskframework.web.rest;

import com.sap.grc.riskframework.RiskFrameworkApp;
import com.sap.grc.riskframework.domain.AnalysisValues;
import com.sap.grc.riskframework.repository.AnalysisValuesRepository;

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
 * Integration tests for the {@link AnalysisValuesResource} REST controller.
 */
@SpringBootTest(classes = RiskFrameworkApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class AnalysisValuesResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private AnalysisValuesRepository analysisValuesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnalysisValuesMockMvc;

    private AnalysisValues analysisValues;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalysisValues createEntity(EntityManager em) {
        AnalysisValues analysisValues = new AnalysisValues()
            .value(DEFAULT_VALUE);
        return analysisValues;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalysisValues createUpdatedEntity(EntityManager em) {
        AnalysisValues analysisValues = new AnalysisValues()
            .value(UPDATED_VALUE);
        return analysisValues;
    }

    @BeforeEach
    public void initTest() {
        analysisValues = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnalysisValues() throws Exception {
        int databaseSizeBeforeCreate = analysisValuesRepository.findAll().size();
        // Create the AnalysisValues
        restAnalysisValuesMockMvc.perform(post("/api/analysis-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(analysisValues)))
            .andExpect(status().isCreated());

        // Validate the AnalysisValues in the database
        List<AnalysisValues> analysisValuesList = analysisValuesRepository.findAll();
        assertThat(analysisValuesList).hasSize(databaseSizeBeforeCreate + 1);
        AnalysisValues testAnalysisValues = analysisValuesList.get(analysisValuesList.size() - 1);
        assertThat(testAnalysisValues.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createAnalysisValuesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analysisValuesRepository.findAll().size();

        // Create the AnalysisValues with an existing ID
        analysisValues.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalysisValuesMockMvc.perform(post("/api/analysis-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(analysisValues)))
            .andExpect(status().isBadRequest());

        // Validate the AnalysisValues in the database
        List<AnalysisValues> analysisValuesList = analysisValuesRepository.findAll();
        assertThat(analysisValuesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllAnalysisValues() throws Exception {
        // Initialize the database
        analysisValuesRepository.saveAndFlush(analysisValues);

        // Get all the analysisValuesList
        restAnalysisValuesMockMvc.perform(get("/api/analysis-values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisValues.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }
    
    @Test
    @Transactional
    public void getAnalysisValues() throws Exception {
        // Initialize the database
        analysisValuesRepository.saveAndFlush(analysisValues);

        // Get the analysisValues
        restAnalysisValuesMockMvc.perform(get("/api/analysis-values/{id}", analysisValues.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(analysisValues.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }
    @Test
    @Transactional
    public void getNonExistingAnalysisValues() throws Exception {
        // Get the analysisValues
        restAnalysisValuesMockMvc.perform(get("/api/analysis-values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalysisValues() throws Exception {
        // Initialize the database
        analysisValuesRepository.saveAndFlush(analysisValues);

        int databaseSizeBeforeUpdate = analysisValuesRepository.findAll().size();

        // Update the analysisValues
        AnalysisValues updatedAnalysisValues = analysisValuesRepository.findById(analysisValues.getId()).get();
        // Disconnect from session so that the updates on updatedAnalysisValues are not directly saved in db
        em.detach(updatedAnalysisValues);
        updatedAnalysisValues
            .value(UPDATED_VALUE);

        restAnalysisValuesMockMvc.perform(put("/api/analysis-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalysisValues)))
            .andExpect(status().isOk());

        // Validate the AnalysisValues in the database
        List<AnalysisValues> analysisValuesList = analysisValuesRepository.findAll();
        assertThat(analysisValuesList).hasSize(databaseSizeBeforeUpdate);
        AnalysisValues testAnalysisValues = analysisValuesList.get(analysisValuesList.size() - 1);
        assertThat(testAnalysisValues.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingAnalysisValues() throws Exception {
        int databaseSizeBeforeUpdate = analysisValuesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalysisValuesMockMvc.perform(put("/api/analysis-values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(analysisValues)))
            .andExpect(status().isBadRequest());

        // Validate the AnalysisValues in the database
        List<AnalysisValues> analysisValuesList = analysisValuesRepository.findAll();
        assertThat(analysisValuesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnalysisValues() throws Exception {
        // Initialize the database
        analysisValuesRepository.saveAndFlush(analysisValues);

        int databaseSizeBeforeDelete = analysisValuesRepository.findAll().size();

        // Delete the analysisValues
        restAnalysisValuesMockMvc.perform(delete("/api/analysis-values/{id}", analysisValues.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnalysisValues> analysisValuesList = analysisValuesRepository.findAll();
        assertThat(analysisValuesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

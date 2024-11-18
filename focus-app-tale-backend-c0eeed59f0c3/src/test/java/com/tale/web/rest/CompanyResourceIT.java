package com.tale.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tale.IntegrationTest;
import com.tale.domain.Company;
import com.tale.repository.CompanyRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyResourceIT {

    private static final Integer DEFAULT_COMPANY_ID = 1;
    private static final Integer UPDATED_COMPANY_ID = 2;

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_INDUSTRY = "AAAAAAAAAA";
    private static final String UPDATED_INDUSTRY = "BBBBBBBBBB";

    private static final Float DEFAULT_REVENUE = 1F;
    private static final Float UPDATED_REVENUE = 2F;

    private static final Integer DEFAULT_NUMBER_OF_EMPLOYEES = 1;
    private static final Integer UPDATED_NUMBER_OF_EMPLOYEES = 2;

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity(EntityManager em) {
        Company company = new Company()
            .companyId(DEFAULT_COMPANY_ID)
            .companyName(DEFAULT_COMPANY_NAME)
            .industry(DEFAULT_INDUSTRY)
            .revenue(DEFAULT_REVENUE)
            .numberOfEmployees(DEFAULT_NUMBER_OF_EMPLOYEES)
            .address(DEFAULT_ADDRESS)
            .website(DEFAULT_WEBSITE)
            .status(DEFAULT_STATUS);
        return company;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity(EntityManager em) {
        Company company = new Company()
            .companyId(UPDATED_COMPANY_ID)
            .companyName(UPDATED_COMPANY_NAME)
            .industry(UPDATED_INDUSTRY)
            .revenue(UPDATED_REVENUE)
            .numberOfEmployees(UPDATED_NUMBER_OF_EMPLOYEES)
            .address(UPDATED_ADDRESS)
            .website(UPDATED_WEBSITE)
            .status(UPDATED_STATUS);
        return company;
    }

    @BeforeEach
    public void initTest() {
        company = createEntity(em);
    }

    @Test
    @Transactional
    void createCompany() throws Exception {
        int databaseSizeBeforeCreate = companyRepository.findAll().size();
        // Create the Company
        restCompanyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isCreated());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate + 1);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testCompany.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testCompany.getIndustry()).isEqualTo(DEFAULT_INDUSTRY);
        assertThat(testCompany.getRevenue()).isEqualTo(DEFAULT_REVENUE);
        assertThat(testCompany.getNumberOfEmployees()).isEqualTo(DEFAULT_NUMBER_OF_EMPLOYEES);
        assertThat(testCompany.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCompany.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testCompany.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        company.setId(1L);

        int databaseSizeBeforeCreate = companyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyRepository.findAll().size();
        // set the field null
        company.setCompanyName(null);

        // Create the Company, which fails.

        restCompanyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyId").value(hasItem(DEFAULT_COMPANY_ID)))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].industry").value(hasItem(DEFAULT_INDUSTRY)))
            .andExpect(jsonPath("$.[*].revenue").value(hasItem(DEFAULT_REVENUE.doubleValue())))
            .andExpect(jsonPath("$.[*].numberOfEmployees").value(hasItem(DEFAULT_NUMBER_OF_EMPLOYEES)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.companyId").value(DEFAULT_COMPANY_ID))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.industry").value(DEFAULT_INDUSTRY))
            .andExpect(jsonPath("$.revenue").value(DEFAULT_REVENUE.doubleValue()))
            .andExpect(jsonPath("$.numberOfEmployees").value(DEFAULT_NUMBER_OF_EMPLOYEES))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .companyId(UPDATED_COMPANY_ID)
            .companyName(UPDATED_COMPANY_NAME)
            .industry(UPDATED_INDUSTRY)
            .revenue(UPDATED_REVENUE)
            .numberOfEmployees(UPDATED_NUMBER_OF_EMPLOYEES)
            .address(UPDATED_ADDRESS)
            .website(UPDATED_WEBSITE)
            .status(UPDATED_STATUS);

        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompany.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
        assertThat(testCompany.getRevenue()).isEqualTo(UPDATED_REVENUE);
        assertThat(testCompany.getNumberOfEmployees()).isEqualTo(UPDATED_NUMBER_OF_EMPLOYEES);
        assertThat(testCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompany.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCompany.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, company.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany.companyName(UPDATED_COMPANY_NAME).website(UPDATED_WEBSITE);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyId()).isEqualTo(DEFAULT_COMPANY_ID);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getIndustry()).isEqualTo(DEFAULT_INDUSTRY);
        assertThat(testCompany.getRevenue()).isEqualTo(DEFAULT_REVENUE);
        assertThat(testCompany.getNumberOfEmployees()).isEqualTo(DEFAULT_NUMBER_OF_EMPLOYEES);
        assertThat(testCompany.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCompany.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCompany.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeUpdate = companyRepository.findAll().size();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .companyId(UPDATED_COMPANY_ID)
            .companyName(UPDATED_COMPANY_NAME)
            .industry(UPDATED_INDUSTRY)
            .revenue(UPDATED_REVENUE)
            .numberOfEmployees(UPDATED_NUMBER_OF_EMPLOYEES)
            .address(UPDATED_ADDRESS)
            .website(UPDATED_WEBSITE)
            .status(UPDATED_STATUS);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
        Company testCompany = companyList.get(companyList.size() - 1);
        assertThat(testCompany.getCompanyId()).isEqualTo(UPDATED_COMPANY_ID);
        assertThat(testCompany.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testCompany.getIndustry()).isEqualTo(UPDATED_INDUSTRY);
        assertThat(testCompany.getRevenue()).isEqualTo(UPDATED_REVENUE);
        assertThat(testCompany.getNumberOfEmployees()).isEqualTo(UPDATED_NUMBER_OF_EMPLOYEES);
        assertThat(testCompany.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCompany.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testCompany.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, company.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompany() throws Exception {
        int databaseSizeBeforeUpdate = companyRepository.findAll().size();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(company))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompany() throws Exception {
        // Initialize the database
        companyRepository.saveAndFlush(company);

        int databaseSizeBeforeDelete = companyRepository.findAll().size();

        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, company.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Company> companyList = companyRepository.findAll();
        assertThat(companyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

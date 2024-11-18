package com.tale.domain;

import static com.tale.domain.CompanyTestSamples.*;
import static com.tale.domain.CompanyUserTestSamples.*;
import static com.tale.domain.FocusGroupTestSamples.*;
import static com.tale.domain.SurveyInsightTestSamples.*;
import static com.tale.domain.SurveyRequirementTestSamples.*;
import static com.tale.domain.SurveyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CompanyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Company.class);
        Company company1 = getCompanySample1();
        Company company2 = new Company();
        assertThat(company1).isNotEqualTo(company2);

        company2.setId(company1.getId());
        assertThat(company1).isEqualTo(company2);

        company2 = getCompanySample2();
        assertThat(company1).isNotEqualTo(company2);
    }

    @Test
    void createdSurveysTest() throws Exception {
        Company company = getCompanyRandomSampleGenerator();
        Survey surveyBack = getSurveyRandomSampleGenerator();

        company.addCreatedSurveys(surveyBack);
        assertThat(company.getCreatedSurveys()).containsOnly(surveyBack);
        assertThat(surveyBack.getCompany()).isEqualTo(company);

        company.removeCreatedSurveys(surveyBack);
        assertThat(company.getCreatedSurveys()).doesNotContain(surveyBack);
        assertThat(surveyBack.getCompany()).isNull();

        company.createdSurveys(new HashSet<>(Set.of(surveyBack)));
        assertThat(company.getCreatedSurveys()).containsOnly(surveyBack);
        assertThat(surveyBack.getCompany()).isEqualTo(company);

        company.setCreatedSurveys(new HashSet<>());
        assertThat(company.getCreatedSurveys()).doesNotContain(surveyBack);
        assertThat(surveyBack.getCompany()).isNull();
    }

    @Test
    void createdFocusGroupsTest() throws Exception {
        Company company = getCompanyRandomSampleGenerator();
        FocusGroup focusGroupBack = getFocusGroupRandomSampleGenerator();

        company.addCreatedFocusGroups(focusGroupBack);
        assertThat(company.getCreatedFocusGroups()).containsOnly(focusGroupBack);
        assertThat(focusGroupBack.getCompany()).isEqualTo(company);

        company.removeCreatedFocusGroups(focusGroupBack);
        assertThat(company.getCreatedFocusGroups()).doesNotContain(focusGroupBack);
        assertThat(focusGroupBack.getCompany()).isNull();

        company.createdFocusGroups(new HashSet<>(Set.of(focusGroupBack)));
        assertThat(company.getCreatedFocusGroups()).containsOnly(focusGroupBack);
        assertThat(focusGroupBack.getCompany()).isEqualTo(company);

        company.setCreatedFocusGroups(new HashSet<>());
        assertThat(company.getCreatedFocusGroups()).doesNotContain(focusGroupBack);
        assertThat(focusGroupBack.getCompany()).isNull();
    }

    @Test
    void surveyInsightsTest() throws Exception {
        Company company = getCompanyRandomSampleGenerator();
        SurveyInsight surveyInsightBack = getSurveyInsightRandomSampleGenerator();

        company.addSurveyInsights(surveyInsightBack);
        assertThat(company.getSurveyInsights()).containsOnly(surveyInsightBack);
        assertThat(surveyInsightBack.getCompany()).isEqualTo(company);

        company.removeSurveyInsights(surveyInsightBack);
        assertThat(company.getSurveyInsights()).doesNotContain(surveyInsightBack);
        assertThat(surveyInsightBack.getCompany()).isNull();

        company.surveyInsights(new HashSet<>(Set.of(surveyInsightBack)));
        assertThat(company.getSurveyInsights()).containsOnly(surveyInsightBack);
        assertThat(surveyInsightBack.getCompany()).isEqualTo(company);

        company.setSurveyInsights(new HashSet<>());
        assertThat(company.getSurveyInsights()).doesNotContain(surveyInsightBack);
        assertThat(surveyInsightBack.getCompany()).isNull();
    }

    @Test
    void companyUsersTest() throws Exception {
        Company company = getCompanyRandomSampleGenerator();
        CompanyUser companyUserBack = getCompanyUserRandomSampleGenerator();

        company.addCompanyUsers(companyUserBack);
        assertThat(company.getCompanyUsers()).containsOnly(companyUserBack);
        assertThat(companyUserBack.getCompany()).isEqualTo(company);

        company.removeCompanyUsers(companyUserBack);
        assertThat(company.getCompanyUsers()).doesNotContain(companyUserBack);
        assertThat(companyUserBack.getCompany()).isNull();

        company.companyUsers(new HashSet<>(Set.of(companyUserBack)));
        assertThat(company.getCompanyUsers()).containsOnly(companyUserBack);
        assertThat(companyUserBack.getCompany()).isEqualTo(company);

        company.setCompanyUsers(new HashSet<>());
        assertThat(company.getCompanyUsers()).doesNotContain(companyUserBack);
        assertThat(companyUserBack.getCompany()).isNull();
    }

    @Test
    void surveyRequirementsTest() throws Exception {
        Company company = getCompanyRandomSampleGenerator();
        SurveyRequirement surveyRequirementBack = getSurveyRequirementRandomSampleGenerator();

        company.addSurveyRequirements(surveyRequirementBack);
        assertThat(company.getSurveyRequirements()).containsOnly(surveyRequirementBack);
        assertThat(surveyRequirementBack.getCompany()).isEqualTo(company);

        company.removeSurveyRequirements(surveyRequirementBack);
        assertThat(company.getSurveyRequirements()).doesNotContain(surveyRequirementBack);
        assertThat(surveyRequirementBack.getCompany()).isNull();

        company.surveyRequirements(new HashSet<>(Set.of(surveyRequirementBack)));
        assertThat(company.getSurveyRequirements()).containsOnly(surveyRequirementBack);
        assertThat(surveyRequirementBack.getCompany()).isEqualTo(company);

        company.setSurveyRequirements(new HashSet<>());
        assertThat(company.getSurveyRequirements()).doesNotContain(surveyRequirementBack);
        assertThat(surveyRequirementBack.getCompany()).isNull();
    }
}

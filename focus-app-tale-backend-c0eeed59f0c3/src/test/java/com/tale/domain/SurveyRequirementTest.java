package com.tale.domain;

import static com.tale.domain.CompanyTestSamples.*;
import static com.tale.domain.SurveyRequirementTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SurveyRequirementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurveyRequirement.class);
        SurveyRequirement surveyRequirement1 = getSurveyRequirementSample1();
        SurveyRequirement surveyRequirement2 = new SurveyRequirement();
        assertThat(surveyRequirement1).isNotEqualTo(surveyRequirement2);

        surveyRequirement2.setId(surveyRequirement1.getId());
        assertThat(surveyRequirement1).isEqualTo(surveyRequirement2);

        surveyRequirement2 = getSurveyRequirementSample2();
        assertThat(surveyRequirement1).isNotEqualTo(surveyRequirement2);
    }

    @Test
    void companyTest() throws Exception {
        SurveyRequirement surveyRequirement = getSurveyRequirementRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        surveyRequirement.setCompany(companyBack);
        assertThat(surveyRequirement.getCompany()).isEqualTo(companyBack);

        surveyRequirement.company(null);
        assertThat(surveyRequirement.getCompany()).isNull();
    }
}

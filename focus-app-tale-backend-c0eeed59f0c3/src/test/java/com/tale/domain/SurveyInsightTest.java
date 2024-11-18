package com.tale.domain;

import static com.tale.domain.CompanyTestSamples.*;
import static com.tale.domain.SurveyInsightTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SurveyInsightTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurveyInsight.class);
        SurveyInsight surveyInsight1 = getSurveyInsightSample1();
        SurveyInsight surveyInsight2 = new SurveyInsight();
        assertThat(surveyInsight1).isNotEqualTo(surveyInsight2);

        surveyInsight2.setId(surveyInsight1.getId());
        assertThat(surveyInsight1).isEqualTo(surveyInsight2);

        surveyInsight2 = getSurveyInsightSample2();
        assertThat(surveyInsight1).isNotEqualTo(surveyInsight2);
    }

    @Test
    void companyTest() throws Exception {
        SurveyInsight surveyInsight = getSurveyInsightRandomSampleGenerator();
        Company companyBack = getCompanyRandomSampleGenerator();

        surveyInsight.setCompany(companyBack);
        assertThat(surveyInsight.getCompany()).isEqualTo(companyBack);

        surveyInsight.company(null);
        assertThat(surveyInsight.getCompany()).isNull();
    }
}

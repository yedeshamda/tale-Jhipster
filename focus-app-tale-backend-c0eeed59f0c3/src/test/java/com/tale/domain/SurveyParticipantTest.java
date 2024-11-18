package com.tale.domain;

import static com.tale.domain.NormalUserTestSamples.*;
import static com.tale.domain.SurveyParticipantTestSamples.*;
import static com.tale.domain.SurveyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SurveyParticipantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SurveyParticipant.class);
        SurveyParticipant surveyParticipant1 = getSurveyParticipantSample1();
        SurveyParticipant surveyParticipant2 = new SurveyParticipant();
        assertThat(surveyParticipant1).isNotEqualTo(surveyParticipant2);

        surveyParticipant2.setId(surveyParticipant1.getId());
        assertThat(surveyParticipant1).isEqualTo(surveyParticipant2);

        surveyParticipant2 = getSurveyParticipantSample2();
        assertThat(surveyParticipant1).isNotEqualTo(surveyParticipant2);
    }

    @Test
    void surveysTest() throws Exception {
        SurveyParticipant surveyParticipant = getSurveyParticipantRandomSampleGenerator();
        Survey surveyBack = getSurveyRandomSampleGenerator();

        surveyParticipant.addSurveys(surveyBack);
        assertThat(surveyParticipant.getSurveys()).containsOnly(surveyBack);
        assertThat(surveyBack.getParticipants()).containsOnly(surveyParticipant);

        surveyParticipant.removeSurveys(surveyBack);
        assertThat(surveyParticipant.getSurveys()).doesNotContain(surveyBack);
        assertThat(surveyBack.getParticipants()).doesNotContain(surveyParticipant);

        surveyParticipant.surveys(new HashSet<>(Set.of(surveyBack)));
        assertThat(surveyParticipant.getSurveys()).containsOnly(surveyBack);
        assertThat(surveyBack.getParticipants()).containsOnly(surveyParticipant);

        surveyParticipant.setSurveys(new HashSet<>());
        assertThat(surveyParticipant.getSurveys()).doesNotContain(surveyBack);
        assertThat(surveyBack.getParticipants()).doesNotContain(surveyParticipant);
    }

    @Test
    void normalUserTest() throws Exception {
        SurveyParticipant surveyParticipant = getSurveyParticipantRandomSampleGenerator();
        NormalUser normalUserBack = getNormalUserRandomSampleGenerator();

        surveyParticipant.setNormalUser(normalUserBack);
        assertThat(surveyParticipant.getNormalUser()).isEqualTo(normalUserBack);

        surveyParticipant.normalUser(null);
        assertThat(surveyParticipant.getNormalUser()).isNull();
    }
}

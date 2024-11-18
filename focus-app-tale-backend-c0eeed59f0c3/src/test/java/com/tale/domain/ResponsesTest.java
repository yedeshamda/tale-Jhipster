package com.tale.domain;

import static com.tale.domain.AnalyticssTestSamples.*;
import static com.tale.domain.NormalUserTestSamples.*;
import static com.tale.domain.QuestionTestSamples.*;
import static com.tale.domain.ResponsesTestSamples.*;
import static com.tale.domain.SurveyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ResponsesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Responses.class);
        Responses responses1 = getResponsesSample1();
        Responses responses2 = new Responses();
        assertThat(responses1).isNotEqualTo(responses2);

        responses2.setId(responses1.getId());
        assertThat(responses1).isEqualTo(responses2);

        responses2 = getResponsesSample2();
        assertThat(responses1).isNotEqualTo(responses2);
    }

    @Test
    void analyticsTest() throws Exception {
        Responses responses = getResponsesRandomSampleGenerator();
        Analyticss analyticssBack = getAnalyticssRandomSampleGenerator();

        responses.addAnalytics(analyticssBack);
        assertThat(responses.getAnalytics()).containsOnly(analyticssBack);
        assertThat(analyticssBack.getResponses()).isEqualTo(responses);

        responses.removeAnalytics(analyticssBack);
        assertThat(responses.getAnalytics()).doesNotContain(analyticssBack);
        assertThat(analyticssBack.getResponses()).isNull();

        responses.analytics(new HashSet<>(Set.of(analyticssBack)));
        assertThat(responses.getAnalytics()).containsOnly(analyticssBack);
        assertThat(analyticssBack.getResponses()).isEqualTo(responses);

        responses.setAnalytics(new HashSet<>());
        assertThat(responses.getAnalytics()).doesNotContain(analyticssBack);
        assertThat(analyticssBack.getResponses()).isNull();
    }

    @Test
    void normalUserTest() throws Exception {
        Responses responses = getResponsesRandomSampleGenerator();
        NormalUser normalUserBack = getNormalUserRandomSampleGenerator();

        responses.setNormalUser(normalUserBack);
        assertThat(responses.getNormalUser()).isEqualTo(normalUserBack);

        responses.normalUser(null);
        assertThat(responses.getNormalUser()).isNull();
    }

    @Test
    void surveyTest() throws Exception {
        Responses responses = getResponsesRandomSampleGenerator();
        Survey surveyBack = getSurveyRandomSampleGenerator();

        responses.setSurvey(surveyBack);
        assertThat(responses.getSurvey()).isEqualTo(surveyBack);

        responses.survey(null);
        assertThat(responses.getSurvey()).isNull();
    }

    @Test
    void questionTest() throws Exception {
        Responses responses = getResponsesRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        responses.setQuestion(questionBack);
        assertThat(responses.getQuestion()).isEqualTo(questionBack);

        responses.question(null);
        assertThat(responses.getQuestion()).isNull();
    }
}

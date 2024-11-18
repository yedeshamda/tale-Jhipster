package com.tale.domain;

import static com.tale.domain.AnalyticssTestSamples.*;
import static com.tale.domain.QuestionTestSamples.*;
import static com.tale.domain.SectionTestSamples.*;
import static com.tale.domain.SurveyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Section.class);
        Section section1 = getSectionSample1();
        Section section2 = new Section();
        assertThat(section1).isNotEqualTo(section2);

        section2.setId(section1.getId());
        assertThat(section1).isEqualTo(section2);

        section2 = getSectionSample2();
        assertThat(section1).isNotEqualTo(section2);
    }

    @Test
    void questionTest() throws Exception {
        Section section = getSectionRandomSampleGenerator();
        Question questionBack = getQuestionRandomSampleGenerator();

        section.addQuestion(questionBack);
        assertThat(section.getQuestions()).containsOnly(questionBack);
        assertThat(questionBack.getSection()).isEqualTo(section);

        section.removeQuestion(questionBack);
        assertThat(section.getQuestions()).doesNotContain(questionBack);
        assertThat(questionBack.getSection()).isNull();

        section.questions(new HashSet<>(Set.of(questionBack)));
        assertThat(section.getQuestions()).containsOnly(questionBack);
        assertThat(questionBack.getSection()).isEqualTo(section);

        section.setQuestions(new HashSet<>());
        assertThat(section.getQuestions()).doesNotContain(questionBack);
        assertThat(questionBack.getSection()).isNull();
    }

    @Test
    void analyticsTest() throws Exception {
        Section section = getSectionRandomSampleGenerator();
        Analyticss analyticssBack = getAnalyticssRandomSampleGenerator();

        section.addAnalytics(analyticssBack);
        assertThat(section.getAnalytics()).containsOnly(analyticssBack);
        assertThat(analyticssBack.getSection()).isEqualTo(section);

        section.removeAnalytics(analyticssBack);
        assertThat(section.getAnalytics()).doesNotContain(analyticssBack);
        assertThat(analyticssBack.getSection()).isNull();

        section.analytics(new HashSet<>(Set.of(analyticssBack)));
        assertThat(section.getAnalytics()).containsOnly(analyticssBack);
        assertThat(analyticssBack.getSection()).isEqualTo(section);

        section.setAnalytics(new HashSet<>());
        assertThat(section.getAnalytics()).doesNotContain(analyticssBack);
        assertThat(analyticssBack.getSection()).isNull();
    }

    @Test
    void surveyTest() throws Exception {
        Section section = getSectionRandomSampleGenerator();
        Survey surveyBack = getSurveyRandomSampleGenerator();

        section.setSurvey(surveyBack);
        assertThat(section.getSurvey()).isEqualTo(surveyBack);

        section.survey(null);
        assertThat(section.getSurvey()).isNull();
    }
}

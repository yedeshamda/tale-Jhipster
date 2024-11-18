package com.tale.domain;

import static com.tale.domain.QuestionTestSamples.*;
import static com.tale.domain.ResponsesTestSamples.*;
import static com.tale.domain.SectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Question.class);
        Question question1 = getQuestionSample1();
        Question question2 = new Question();
        assertThat(question1).isNotEqualTo(question2);

        question2.setId(question1.getId());
        assertThat(question1).isEqualTo(question2);

        question2 = getQuestionSample2();
        assertThat(question1).isNotEqualTo(question2);
    }

    @Test
    void responseTest() throws Exception {
        Question question = getQuestionRandomSampleGenerator();
        Responses responsesBack = getResponsesRandomSampleGenerator();

        question.addResponse(responsesBack);
        assertThat(question.getResponses()).containsOnly(responsesBack);
        assertThat(responsesBack.getQuestion()).isEqualTo(question);

        question.removeResponse(responsesBack);
        assertThat(question.getResponses()).doesNotContain(responsesBack);
        assertThat(responsesBack.getQuestion()).isNull();

        question.responses(new HashSet<>(Set.of(responsesBack)));
        assertThat(question.getResponses()).containsOnly(responsesBack);
        assertThat(responsesBack.getQuestion()).isEqualTo(question);

        question.setResponses(new HashSet<>());
        assertThat(question.getResponses()).doesNotContain(responsesBack);
        assertThat(responsesBack.getQuestion()).isNull();
    }

    @Test
    void sectionTest() throws Exception {
        Question question = getQuestionRandomSampleGenerator();
        Section sectionBack = getSectionRandomSampleGenerator();

        question.setSection(sectionBack);
        assertThat(question.getSection()).isEqualTo(sectionBack);

        question.section(null);
        assertThat(question.getSection()).isNull();
    }
}

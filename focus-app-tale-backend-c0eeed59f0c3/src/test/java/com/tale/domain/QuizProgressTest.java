package com.tale.domain;

import static com.tale.domain.NormalUserTestSamples.*;
import static com.tale.domain.QuizProgressTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class QuizProgressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizProgress.class);
        QuizProgress quizProgress1 = getQuizProgressSample1();
        QuizProgress quizProgress2 = new QuizProgress();
        assertThat(quizProgress1).isNotEqualTo(quizProgress2);

        quizProgress2.setId(quizProgress1.getId());
        assertThat(quizProgress1).isEqualTo(quizProgress2);

        quizProgress2 = getQuizProgressSample2();
        assertThat(quizProgress1).isNotEqualTo(quizProgress2);
    }

    @Test
    void normalUserTest() throws Exception {
        QuizProgress quizProgress = getQuizProgressRandomSampleGenerator();
        NormalUser normalUserBack = getNormalUserRandomSampleGenerator();

        quizProgress.setNormalUser(normalUserBack);
        assertThat(quizProgress.getNormalUser()).isEqualTo(normalUserBack);

        quizProgress.normalUser(null);
        assertThat(quizProgress.getNormalUser()).isNull();
    }
}

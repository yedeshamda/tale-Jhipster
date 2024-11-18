package com.tale.domain;

import static com.tale.domain.NormalUserTestSamples.*;
import static com.tale.domain.NotificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.tale.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void normalUserTest() throws Exception {
        Notification notification = getNotificationRandomSampleGenerator();
        NormalUser normalUserBack = getNormalUserRandomSampleGenerator();

        notification.setNormalUser(normalUserBack);
        assertThat(notification.getNormalUser()).isEqualTo(normalUserBack);

        notification.normalUser(null);
        assertThat(notification.getNormalUser()).isNull();
    }
}

package com.cv.maker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cv.maker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CvTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cv.class);
        Cv cv1 = new Cv();
        cv1.setId("id1");
        Cv cv2 = new Cv();
        cv2.setId(cv1.getId());
        assertThat(cv1).isEqualTo(cv2);
        cv2.setId("id2");
        assertThat(cv1).isNotEqualTo(cv2);
        cv1.setId(null);
        assertThat(cv1).isNotEqualTo(cv2);
    }
}

package com.cv.maker.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cv.maker.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FileEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileEntry.class);
        FileEntry fileEntry1 = new FileEntry();
        fileEntry1.setId("id1");
        FileEntry fileEntry2 = new FileEntry();
        fileEntry2.setId(fileEntry1.getId());
        assertThat(fileEntry1).isEqualTo(fileEntry2);
        fileEntry2.setId("id2");
        assertThat(fileEntry1).isNotEqualTo(fileEntry2);
        fileEntry1.setId(null);
        assertThat(fileEntry1).isNotEqualTo(fileEntry2);
    }
}

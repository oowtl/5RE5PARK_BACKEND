package com.oreo.finalproject_5re5_be.vc.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.oreo.finalproject_5re5_be.global.component.AudioInfo;
import com.oreo.finalproject_5re5_be.global.component.S3Service;
import com.oreo.finalproject_5re5_be.vc.service.VcService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@Nested
@ExtendWith(MockitoExtension.class)
class VcControllerTest {
    @Mock private VcService vcService;

    @Mock private AudioInfo audioInfo;

    @Mock private S3Service s3Service;

    @InjectMocks private VcController vcController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void srcSave() {}

    @Test
    void trgSave() {}

    @Test
    void resultSave() {}

    @Test
    void textSave() {}

    @Test
    void srcURL() {}

    @Test
    void resultURL() {}

    @Test
    void vc() {}

    @Test
    void deleteSrc() {}

    @Test
    void updateText() {}
}

package wanted.preonboarding.backend.domain.apply.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wanted.preonboarding.backend.domain.apply.business.service.ApplyService;
import wanted.preonboarding.backend.domain.apply.web.controller.ApplyController;
import wanted.preonboarding.backend.domain.apply.web.dto.request.ApplyCreateRequest;
import wanted.preonboarding.backend.global.exception.BusinessException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wanted.preonboarding.backend.global.exception.ErrorCode.*;
import static wanted.preonboarding.backend.utils.JsonUtils.*;

@WebMvcTest(ApplyController.class)
class ApplyControllerTest {

    @MockBean
    ApplyService applyService;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("해당 채용공고에 지원 성공 테스트")
    @Test
    void applyRecruit() throws Exception {
        when(applyService.applyRecruit(anyLong(), anyLong())).thenReturn(1L);

        ApplyCreateRequest applyCreateRequest = ApplyCreateRequest.builder().userId(1L).recruitId(1L).build();
        mockMvc.perform(post("/applies")
                        .content(asJsonString(applyCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.applyId").value(1L))
                .andDo(print());
    }

    @DisplayName("해당 채용공고에 지원 실패 테스트 - 해당 채용공고 지원내역 이미 존재")
    @Test
    void applyRecruitFail() throws Exception {
        when(applyService.applyRecruit(anyLong(), anyLong())).thenThrow(new BusinessException(ALREADY_EXIST_APPLY));

        ApplyCreateRequest applyCreateRequest = ApplyCreateRequest.builder().userId(1L).recruitId(1L).build();
        mockMvc.perform(post("/applies")
                        .content(asJsonString(applyCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(ALREADY_EXIST_APPLY.getHttpStatus().value()))
                .andExpect(jsonPath("$.error").value(ALREADY_EXIST_APPLY.getHttpStatus().name()))
                .andExpect(jsonPath("$.code").value(ALREADY_EXIST_APPLY.name()))
                .andExpect(jsonPath("$.message").value(ALREADY_EXIST_APPLY.getMessage()))
                .andDo(print());
    }
}
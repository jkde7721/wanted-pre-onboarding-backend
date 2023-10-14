package wanted.preonboarding.backend.domain.apply.web.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wanted.preonboarding.backend.domain.apply.business.service.ApplyService;
import wanted.preonboarding.backend.domain.apply.web.dto.request.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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
        //given
        when(applyService.applyRecruit(anyLong(), anyLong())).thenReturn(1L);

        //when, then
        ApplyCreateRequest applyCreateRequest = ApplyCreateRequest.builder().userId(1L).recruitId(1L).build();
        mockMvc.perform(post("/applies")
                        .content(asJsonString(applyCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.applyId").value(1L))
                .andDo(print());
        verify(applyService, times(1)).applyRecruit(anyLong(), anyLong());
    }

    @DisplayName("해당 채용공고에 지원 실패 테스트 - validation error 발생")
    @Test
    void applyRecruitFail() throws Exception {
        //given
        when(applyService.applyRecruit(anyLong(), anyLong())).thenReturn(1L);

        //when, then
        ApplyCreateRequest applyCreateRequest = ApplyCreateRequest.builder().userId(null).recruitId(null).build();
        mockMvc.perform(post("/applies")
                        .content(asJsonString(applyCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.recruitId").exists())
                .andDo(print());
        verify(applyService, times(0)).applyRecruit(anyLong(), anyLong());
    }
}
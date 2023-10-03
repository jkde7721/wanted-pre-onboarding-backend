package wanted.preonboarding.backend.recruit.web.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wanted.preonboarding.backend.exception.BusinessException;
import wanted.preonboarding.backend.recruit.business.dto.request.*;
import wanted.preonboarding.backend.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.recruit.web.dto.request.*;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wanted.preonboarding.backend.exception.ErrorCode.*;
import static wanted.preonboarding.backend.utils.JsonUtils.asJsonString;

@WebMvcTest(RecruitController.class)
class RecruitControllerTest {

    @MockBean
    RecruitService recruitService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void registerRecruit() throws Exception {
        Mockito.when(recruitService.registerRecruit(anyLong(), any(RecruitSaveRequest.class)))
                .thenReturn(1L);

        RecruitCreateRequest recruitCreateRequest = RecruitCreateRequest.builder().companyId(1L)
                .position("백엔드 주니어 개발자")
                .compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..")
                .skills("Python").build();
        mockMvc.perform(post("/recruits")
                        .content(asJsonString(recruitCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recruitId").value(1L))
                .andDo(print());
    }

    @Test
    void modifyRecruit() throws Exception {
        RecruitUpdateRequest recruitUpdateRequest = RecruitUpdateRequest.builder()
                .position("백엔드 시니어 개발자")
                .compensationFee(1500000L)
                .details("네이버에서 백엔드 시니어 개발자를 채용합니다.")
                .skills("Spring").build();
        mockMvc.perform(put("/recruits/{recruitId}", 1L)
                        .content(asJsonString(recruitUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void modifyRecruitFail() throws Exception {
        Mockito.doThrow(new BusinessException(RECRUIT_NOT_FOUND))
                .when(recruitService).modifyRecruit(anyLong(), any(RecruitModifyRequest.class));

        RecruitUpdateRequest recruitUpdateRequest = RecruitUpdateRequest.builder()
                .position("백엔드 시니어 개발자")
                .compensationFee(1500000L)
                .details("네이버에서 백엔드 시니어 개발자를 채용합니다.")
                .skills("Spring").build();
        mockMvc.perform(put("/recruits/{recruitId}", 1L)
                        .content(asJsonString(recruitUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value(RECRUIT_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("error").value(RECRUIT_NOT_FOUND.getHttpStatus().name()))
                .andExpect(jsonPath("code").value(RECRUIT_NOT_FOUND.name()))
                .andExpect(jsonPath("message").value(RECRUIT_NOT_FOUND.getMessage()))
                .andDo(print());
    }

    @Test
    void removeRecruit() throws Exception {
        mockMvc.perform(delete("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void removeRecruitFail() throws Exception {
        Mockito.doThrow(new BusinessException(RECRUIT_NOT_FOUND))
                .when(recruitService).removeRecruit(anyLong());

        mockMvc.perform(delete("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value(RECRUIT_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("error").value(RECRUIT_NOT_FOUND.getHttpStatus().name()))
                .andExpect(jsonPath("code").value(RECRUIT_NOT_FOUND.name()))
                .andExpect(jsonPath("message").value(RECRUIT_NOT_FOUND.getMessage()))
                .andDo(print());
    }
}
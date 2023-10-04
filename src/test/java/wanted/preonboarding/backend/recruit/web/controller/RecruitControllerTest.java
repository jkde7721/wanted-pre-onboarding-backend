package wanted.preonboarding.backend.recruit.web.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wanted.preonboarding.backend.company.persistence.entity.Company;
import wanted.preonboarding.backend.exception.BusinessException;
import wanted.preonboarding.backend.recruit.business.dto.request.*;
import wanted.preonboarding.backend.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.recruit.web.dto.request.*;

import java.util.List;

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

    @DisplayName("채용공고 등록 성공 테스트")
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

    @DisplayName("채용공고 수정 성공 테스트")
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

    @DisplayName("채용공고 수정 실패 테스트 - 해당 채용공고 조회 실패")
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
                .andExpect(jsonPath("$.status").value(RECRUIT_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.error").value(RECRUIT_NOT_FOUND.getHttpStatus().name()))
                .andExpect(jsonPath("$.code").value(RECRUIT_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(RECRUIT_NOT_FOUND.getMessage()))
                .andDo(print());
    }

    @DisplayName("채용공고 삭제 성공 테스트")
    @Test
    void removeRecruit() throws Exception {
        mockMvc.perform(delete("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("채용공고 삭제 실패 테스트 - 해당 채용공고 조회 실패")
    @Test
    void removeRecruitFail() throws Exception {
        Mockito.doThrow(new BusinessException(RECRUIT_NOT_FOUND))
                .when(recruitService).removeRecruit(anyLong());

        mockMvc.perform(delete("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(RECRUIT_NOT_FOUND.getHttpStatus().value()))
                .andExpect(jsonPath("$.error").value(RECRUIT_NOT_FOUND.getHttpStatus().name()))
                .andExpect(jsonPath("$.code").value(RECRUIT_NOT_FOUND.name()))
                .andExpect(jsonPath("$.message").value(RECRUIT_NOT_FOUND.getMessage()))
                .andDo(print());
    }

    @DisplayName("채용공고 목록 조회 성공 테스트 - 채용공고 1개 이상")
    @Test
    void getRecruitList() throws Exception {
        Company company1 = Company.builder().id(1L).name("원티드랩").nation("한국").region("서울").build();
        Company company2 = Company.builder().id(2L).name("네이버").nation("한국").region("판교").build();
        Recruit recruit1 = Recruit.builder().id(1L).company(company1)
                .position("백엔드 주니어 개발자").compensationFee(1000000L)
                .details("원티드랩에서 백엔드 주니어 개발자를 채용합니다.").skills("Python").build();
        Recruit recruit2 = Recruit.builder().id(2L).company(company1)
                .position("백엔드 시니어 개발자").compensationFee(1500000L)
                .details("원티드랩에서 백엔드 시니어 개발자를 채용합니다.").skills("Spring").build();
        Recruit recruit3 = Recruit.builder().id(3L).company(company2)
                .position("프론트엔드 시니어 개발자").compensationFee(1500000L)
                .details("네이버에서 프론트엔드 시니어 개발자를 채용합니다.").skills("React").build();
        Mockito.when(recruitService.getRecruitList()).thenReturn(List.of(recruit1, recruit2, recruit3));

        mockMvc.perform(get("/recruits")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].recruitId").value(recruit1.getId()))
                .andExpect(jsonPath("$[0].companyName").value(company1.getName()))
                .andExpect(jsonPath("$[0].nation").value(company1.getNation()))
                .andExpect(jsonPath("$[0].region").value(company1.getRegion()))
                .andExpect(jsonPath("$[0].position").value(recruit1.getPosition()))
                .andExpect(jsonPath("$[0].compensationFee").value(recruit1.getCompensationFee()))
                .andExpect(jsonPath("$[0].skills").value(recruit1.getSkills()))
                .andDo(print());
    }

    @DisplayName("채용공고 목록 조회 성공 테스트 - 채용공고 0개")
    @Test
    void getRecruitListNone() throws Exception {
        Mockito.when(recruitService.getRecruitList()).thenReturn(List.of());

        mockMvc.perform(get("/recruits")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty())
                .andDo(print());
    }
}
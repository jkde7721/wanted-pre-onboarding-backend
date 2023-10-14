package wanted.preonboarding.backend.domain.recruit.web.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import wanted.preonboarding.backend.domain.company.persistence.entity.Company;
import wanted.preonboarding.backend.domain.recruit.business.dto.request.*;
import wanted.preonboarding.backend.domain.recruit.web.dto.request.*;
import wanted.preonboarding.backend.domain.recruit.business.dto.response.*;
import wanted.preonboarding.backend.domain.recruit.business.service.RecruitService;
import wanted.preonboarding.backend.domain.recruit.persistence.entity.Recruit;
import wanted.preonboarding.backend.global.paging.*;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static wanted.preonboarding.backend.utils.Fixtures.*;
import static wanted.preonboarding.backend.utils.JsonUtils.*;

@WebMvcTest(RecruitController.class)
class RecruitControllerTest {

    @MockBean
    RecruitService recruitService;

    @Autowired
    MockMvc mockMvc;

    @DisplayName("채용공고 등록 성공 테스트")
    @Test
    void registerRecruit() throws Exception {
        //given
        when(recruitService.registerRecruit(anyLong(), any(RecruitSaveRequest.class))).thenReturn(1L);

        //when, then
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
        verify(recruitService, times(1)).registerRecruit(anyLong(), any(RecruitSaveRequest.class));
    }

    @DisplayName("채용공고 등록 실패 테스트 - validation error 발생")
    @Test
    void registerRecruitFail() throws Exception {
        //given
        when(recruitService.registerRecruit(anyLong(), any(RecruitSaveRequest.class))).thenReturn(1L);

        //when, then
        RecruitCreateRequest recruitCreateRequest = RecruitCreateRequest.builder()
                .companyId(null).position(null).compensationFee(null).details("").skills("  ").build();
        mockMvc.perform(post("/recruits")
                        .content(asJsonString(recruitCreateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.companyId").exists())
                .andExpect(jsonPath("$.position").exists())
                .andExpect(jsonPath("$.compensationFee").exists())
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.skills").exists())
                .andDo(print());
        verify(recruitService, times(0)).registerRecruit(anyLong(), any(RecruitSaveRequest.class));
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
        verify(recruitService, times(1)).modifyRecruit(anyLong(), any(RecruitModifyRequest.class));
    }

    @DisplayName("채용공고 수정 실패 테스트 - validation error 발생")
    @Test
    void modifyRecruitFail() throws Exception {
        RecruitUpdateRequest recruitUpdateRequest = RecruitUpdateRequest.builder()
                .position(null).compensationFee(null).details("").skills("  ").build();
        mockMvc.perform(put("/recruits/{recruitId}", 1L)
                        .content(asJsonString(recruitUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.position").exists())
                .andExpect(jsonPath("$.compensationFee").exists())
                .andExpect(jsonPath("$.details").exists())
                .andExpect(jsonPath("$.skills").exists())
                .andDo(print());
        verify(recruitService, times(0)).modifyRecruit(anyLong(), any(RecruitModifyRequest.class));
    }

    @DisplayName("채용공고 삭제 성공 테스트")
    @Test
    void removeRecruit() throws Exception {
        mockMvc.perform(delete("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
        verify(recruitService, times(1)).removeRecruit(anyLong());
    }

    @DisplayName("채용공고 목록 조회 성공 테스트")
    @Test
    void getRecruitList() throws Exception {
        //given
        Company company = aCompany().build();
        Company company2 = aCompany2().build();
        Recruit recruit = aRecruit().company(company).build();
        Recruit recruit2 = aRecruit2().company(company).build();
        Recruit recruit3 = aRecruit3().company(company2).build();
        PageResponse<Recruit> recruitListPage = new PageResponse<>(List.of(recruit, recruit2, recruit3), 1, 10, 3, 1, 3L, true, true, false);
        when(recruitService.getRecruitListBySearch(isNull(String.class), any(PageRequest.class))).thenReturn(recruitListPage);

        //when, then
        mockMvc.perform(get("/recruits")
                        .param("page", "1")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(3)))
                .andExpect(jsonPath("$.content[0].recruitId").value(recruit.getId()))
                .andExpect(jsonPath("$.content[0].companyName").value(company.getName()))
                .andExpect(jsonPath("$.content[0].nation").value(company.getNation()))
                .andExpect(jsonPath("$.content[0].region").value(company.getRegion()))
                .andExpect(jsonPath("$.content[0].position").value(recruit.getPosition()))
                .andExpect(jsonPath("$.content[0].compensationFee").value(recruit.getCompensationFee()))
                .andExpect(jsonPath("$.content[0].skills").value(recruit.getSkills()))

                .andExpect(jsonPath("$.pageNumber").value(recruitListPage.getPageNumber()))
                .andExpect(jsonPath("$.pageSize").value(recruitListPage.getPageSize()))
                .andExpect(jsonPath("$.numberOfElements").value(recruitListPage.getNumberOfElements()))
                .andExpect(jsonPath("$.totalPages").value(recruitListPage.getTotalPages()))
                .andExpect(jsonPath("$.totalElements").value(recruitListPage.getTotalElements()))
                .andExpect(jsonPath("$.first").value(recruitListPage.getFirst()))
                .andExpect(jsonPath("$.last").value(recruitListPage.getLast()))
                .andExpect(jsonPath("$.empty").value(recruitListPage.getEmpty()))
                .andDo(print());
        verify(recruitService, times(1)).getRecruitListBySearch(isNull(String.class), any(PageRequest.class));
    }

    @DisplayName("채용공고 목록 조회 성공 테스트 with 검색 기능")
    @Test
    void getRecruitListNone() throws Exception {
        //given
        Company company = aCompany().build();
        Recruit recruit = aRecruit().company(company).build();
        PageResponse<Recruit> recruitListPage = new PageResponse<>(List.of(recruit), 1, 10, 1, 1, 1L, true, true, false);
        when(recruitService.getRecruitListBySearch(anyString(), any(PageRequest.class))).thenReturn(recruitListPage);

        //when, then
        mockMvc.perform(get("/recruits")
                        .param("search", "Spring")
                        .param("page", "1")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", Matchers.hasSize(1)))
                .andDo(print());
        verify(recruitService, times(1)).getRecruitListBySearch(anyString(), any(PageRequest.class));
    }

    @DisplayName("채용공고 상세 조회 성공 테스트 - 해당 회사의 다른 채용공고 1개 이상")
    @Test
    void getRecruitWithAnotherOfTheCompany() throws Exception {
        //given
        Company company = aCompany().build();
        Recruit recruit = aRecruit().company(company).build();
        Recruit recruit2 = aRecruit2().company(company).build();
        Recruit recruit3 = aRecruit3().company(company).build();
        RecruitWithAnotherResponse recruitWithAnother = new RecruitWithAnotherResponse(recruit, List.of(recruit2, recruit3));
        when(recruitService.getRecruitWithAnotherOfTheCompany(anyLong())).thenReturn(recruitWithAnother);

        //when, then
        mockMvc.perform(get("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recruitId").value(recruit.getId()))
                .andExpect(jsonPath("$.companyName").value(company.getName()))
                .andExpect(jsonPath("$.nation").value(company.getNation()))
                .andExpect(jsonPath("$.region").value(company.getRegion()))
                .andExpect(jsonPath("$.position").value(recruit.getPosition()))
                .andExpect(jsonPath("$.compensationFee").value(recruit.getCompensationFee()))
                .andExpect(jsonPath("$.skills").value(recruit.getSkills()))
                .andExpect(jsonPath("$.details").value(recruit.getDetails()))
                .andExpect(jsonPath("$.anotherRecruitList[0]").value(recruit2.getId()))
                .andExpect(jsonPath("$.anotherRecruitList[1]").value(recruit3.getId()))
                .andDo(print());
        verify(recruitService, times(1)).getRecruitWithAnotherOfTheCompany(anyLong());
    }

    @DisplayName("채용공고 상세 조회 성공 테스트 - 해당 회사의 다른 채용공고 0개")
    @Test
    void getRecruitWithAnotherOfTheCompanyWithZeroAnother() throws Exception {
        //given
        Company company = aCompany().build();
        Recruit recruit = aRecruit().company(company).build();
        RecruitWithAnotherResponse recruitWithAnother = new RecruitWithAnotherResponse(recruit, List.of());
        when(recruitService.getRecruitWithAnotherOfTheCompany(anyLong())).thenReturn(recruitWithAnother);

        //when, then
        mockMvc.perform(get("/recruits/{recruitId}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recruitId").value(recruit.getId()))
                .andExpect(jsonPath("$.companyName").value(company.getName()))
                .andExpect(jsonPath("$.nation").value(company.getNation()))
                .andExpect(jsonPath("$.region").value(company.getRegion()))
                .andExpect(jsonPath("$.position").value(recruit.getPosition()))
                .andExpect(jsonPath("$.compensationFee").value(recruit.getCompensationFee()))
                .andExpect(jsonPath("$.skills").value(recruit.getSkills()))
                .andExpect(jsonPath("$.details").value(recruit.getDetails()))
                .andExpect(jsonPath("$.anotherRecruitList").isEmpty())
                .andDo(print());
        verify(recruitService, times(1)).getRecruitWithAnotherOfTheCompany(anyLong());
    }
}
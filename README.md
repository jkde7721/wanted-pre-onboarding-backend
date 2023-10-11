# 요구사항 분석 및 구현 과정

## DB 모델
![Wanted Backend](https://github.com/jkde7721/wanted-pre-onboarding-backend/assets/65665065/d628ea5c-b769-4ce2-a3c1-7dcf3cc4b44a)

<br/>

## 기본 요구사항

### 채용공고 등록
> 지정한 회사의 채용공고를 등록합니다. `회사id`, `채용포지션`, `채용보상금`, `채용내용`, `사용기술` 데이터가 필요합니다.

**요청** 

`POST /recruits`
```JSON
{
    "companyId": 1,
    "position": "백엔드 주니어 개발자",
    "compensationFee": 1000000,
    "details": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
    "skills": "Python"
}
```

**응답** 

`201 Created`
```JSON
{
    "recruitId": 1
}
```

<br/>

### 채용공고 수정 
> 지정한 채용공고의 내용을 수정합니다. `회사id` 이외 모두 수정 가능하며 `채용포지션`, `채용보상금`, `채용내용`, `사용기술` 데이터가 필요합니다.

**요청**

`PUT /recruits/{recruitId}`
```JSON
{
    "position": "백엔드 시니어 개발자",
    "compensationFee": 1500000,
    "details": "원티드랩에서 백엔드 시니어 개발자를 채용합니다. 자격요건은..",
    "skills": "Spring"
}
```

**응답**

`200 OK`

<br/>

### 채용공고 삭제 
> 지정한 채용공고가 존재하는 경우 해당 공고를 삭제합니다.

**요청**

`DELETE /recruits/{recruitId}`

**응답**

`204 No Content`

<br/>

### 채용공고 목록 조회 
> 현재까지 등록된 모든 채용공고를 최신순으로 조회합니다. (페이징 적용)

**요청**

`GET /recruits?page=0&size=10`

**응답**

`200 OK`
```JSON
{
    "content": [
        {
            "recruitId": 1,
            "companyName":"원티드랩",
            "nation":"한국",
            "region":"서울",
            "position":"백엔드 주니어 개발자",
            "compensationFee":1500000,
            "skills":"Python"
        },
        {
            "recruitId": 2,
            "companyName":"네이버",
            "nation":"한국",
            "region":"판교",
            "position":"Django 백엔드 개발자",
            "compensationFee":1000000,
            "skills":"Django"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 2,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
}
```

<br/>

### 채용공고 검색
> 지정한 검색조건으로 현재까지 등록된 모든 채용공고를 최신순으로 조회합니다. 가능한 검색조건은 `회사명`, `채용포지션`, `사용기술`입니다. (페이징 적용) 

**요청**

`GET /recruits/search?query=Spring&page=0&size=10`

**응답**

`200 OK`

Example - 1) `/recruits/search?query=원티드&page=0&size=10`
```JSON
{
    "content": [
        {
            "recruitId": 1,
            "companyName":"원티드랩",
            "nation":"한국",
            "region":"서울",
            "position":"백엔드 주니어 개발자",
            "compensationFee":1500000,
            "skills":"Python"
        },
        {
            "recruitId": 2,
            "companyName":"원티드코리아",
            "nation":"한국",
            "region":"부산",
            "position":"프론트엔드 개발자",
            "compensationFee":500000,
            "skills":"javascript"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 2,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
}
```

Example - 2) `/recruits/search?query=Django&page=0&size=10`
```JSON
{
    "content": [
        {
            "recruitId": 3,
            "companyName":"네이버",
            "nation":"한국",
            "region":"판교",
            "position":"Django 백엔드 개발자",
            "compensationFee":1000000,
            "skills":"Django"
        },
        {
            "recruitId": 4,
            "companyName":"카카오",
            "nation":"한국",
            "region":"판교",
            "position":"Django 백엔드 개발자",
            "compensationFee":500000,
            "skills":"Python"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "last": true,
    "totalElements": 2,
    "totalPages": 1,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": true,
        "sorted": false,
        "unsorted": true
    },
    "first": true,
    "numberOfElements": 2,
    "empty": false
}
```

<br/>

### 채용공고 상세 조회 
> 지정한 채용공고를 상세 조회합니다. 목록 조회와 달리 `채용내용`이 추가 조회되며, 해당 회사가 올린 다른 채용공고도 포함되어 조회됩니다.

**요청**

`GET /recuits/{recruitId}`

**응답**

`200 OK`
```JSON
{
    "recruitId": 1,
    "companyName":"원티드랩",
    "nation":"한국",
    "region":"서울",
    "position":"백엔드 주니어 개발자",
    "compensationFee":1500000,
    "skills":"Python",
    "details": "원티드랩에서 백엔드 주니어 개발자를 채용합니다. 자격요건은..",
    "anotherRecruits": [ 2, 3, 5 ]
}
```

<br/>

### 채용공고 지원 
> 사용자가 지정한 채용공고에 지원합니다. 사용자는 같은 채용공고에 최대 1회 지원 가능하며, `사용자id`, `채용공고id` 데이터가 필요합니다.

**요청**

`POST /applies`
```JSON
{
    "userId": 1,
    "recruitId": 2
}
```

**응답**

`201 Created`
```JSON
{
    "applyId": 1
}
```

<br/>

## 추가 요구사항

### Unit Test 수행

[JUnit5 블로그 정리](https://daeun21dev.tistory.com/23)

[Mockito 블로그 정리](https://daeun21dev.tistory.com/35)

### Git Flow 브랜치 전략

[Git Flow 블로그 정리](https://daeun21dev.tistory.com/25)

<br/>
# BalanceEat 프로젝트 레이아웃 및 기능 정리

## 1. 프로젝트 디렉토리 구조

```
src/
├── main/
│   ├── java/
│   │   └── com/balanceeat/demo/domain/
│   │       ├── user/        // 회원 관리
│   │       ├── diet/        // 식단 관리
│   │       ├── nutrition/   // 영양소 관리
│   │       └── auth/        // 인증 관리
│   └── webapp/
│       └── WEB-INF/
│           └── views/
│               ├── user/       // 회원 관련 화면
│               ├── diet/       // 식단 관련 화면
│               ├── nutrition/  // 영양 정보 화면
│               ├── auth/       // 로그인/회원가입 화면
│               ├── layout/
│               │   ├── header.jsp
│               │   └── footer.jsp
│               ├── main.jsp
│               └── index.jsp
```

---

## 2. 레이아웃 구성요소

### 2.1 `header.jsp`
- **파일 크기**: 4KB (99줄)
- **주요 기능**:
  - 네비게이션 바
  - 사이드바
  - 메타데이터 및 스타일시트 링크
  - 사용자 인증 상태에 따라 메뉴 동적 렌더링
- **사용 기술**:
  - Bootstrap 5.3.0
  - FullCalendar 5.11.3
  - 커스텀 CSS 적용

### 2.2 `footer.jsp`
- **파일 크기**: 132B (4줄)
- **주요 기능**:
  - Bootstrap JavaScript 번들 로딩
  - HTML 구조 마무리

---

## 3. 주요 레이아웃 기능

| 기능 | 설명 |
|:---|:---|
| 네비게이션 바 | 로고, 메인 메뉴(영양 정보, 식단 캘린더), 사용자 메뉴(로그인/회원가입 또는 사용자 이름/마이페이지/로그아웃) |
| 사이드바 | 영양 정보 조회, 나의 식단 캘린더, 마이페이지(로그인 시) |
| 반응형 디자인 | 모바일 화면 대응 (Bootstrap 그리드 및 메뉴 토글) |
| 사용자 인증 상태 처리 | 로그인 여부에 따라 다른 메뉴 렌더링 (`<c:choose>`) |

---

## 4. 기술 스택

### 4.1 프론트엔드
- **Bootstrap 5.3.0**
- **FullCalendar 5.11.3**
- **JSP & JSTL**
- **커스텀 CSS**

### 4.2 백엔드
- **Spring 기반**
- **JSP 템플릿 엔진**
- **세션 기반 인증 처리**

---

## 5. 기능적 요구사항 (유즈케이스 기반)

### 5.1 사용자 관리
- 회원가입 (`POST /users`)
- 로그인 (`POST /auth/login`)
- 로그아웃 (`POST /auth/logout`)
- 회원정보 조회 (`GET /users/{userId}`)
- 회원정보 수정 (`PUT /users/{userId}`)
- 회원 탈퇴 / 비활성화 (`PATCH /users/{userId}/deactivate`)

### 5.2 식단 관리
- 식단 작성 (`POST /diets`)
- 식단 조회 (`GET /diets/{dietId}`)
- 식단 수정 (`PUT /diets/{dietId}`)
- 식단 삭제 (`DELETE /diets/{dietId}`)
- 식단 분석 (AI 또는 공식 기반) (`GET /diets/{dietId}/analysis`)

### 5.3 영양 정보 관리
- 영양 데이터 조회
- 영양 통계 확인 (추후 확장 가능)

---

## 6. 회원가입 기능

| 항목 | 설명 |
|:---|:---|
| 세션 관리 | 세션 스코프를 활용하여 사용자 로그인 상태 유지 |
| 조건부 렌더링 | 로그인/비로그인 상태별로 메뉴 구분 렌더링 (`<c:choose>`) |
| 인증 상태 확인 | 모든 주요 화면 및 기능 접근 제어 적용 예정 |
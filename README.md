# 👟 KickRush

> 한정판 신발 드롭 시스템 - First-Come, First-Served 방식의 신발 발매 플랫폼

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.2-6DB33F?style=flat-square&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-02303A?style=flat-square&logo=gradle&logoColor=white)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)](LICENSE)

---

## 📖 프로젝트 소개

**KickRush**는 한정판 신발 발매(드롭) 시 발생하는 **대규모 트래픽**과 **동시성 문제**를 해결하기 위한 백엔드 시스템입니다. 

실제 Nike SNKRS, Adidas Confirmed 등의 드롭 시스템에서 발생하는 기술적 도전을 학습하고 구현하기 위해 개발되었습니다.

### 주요 도전 과제
- ⚡ **동시성 제어**: 동시 재고 차감 시 초과 판매 방지 (Pessimistic Lock)
- 🚀 **대용량 트래픽 처리**: 발매 시점에 발생하는 스파이크 트래픽 대응
- 🔒 **중복 주문 방지**: 동일 사용자의 중복 구매 차단
- 🎯 **재고 정합성**: 실시간 재고 관리 및 원자적 차감

---

## 🏗️ 아키텍처

### 멀티 모듈 구조

```
KickRush/
├── kickrush-api        # REST API 계층 (Controller, Security)
├── kickrush-core       # 비즈니스 로직 (Domain, Service, Repository)
└── kickrush-common     # 공통 유틸리티 및 예외 처리
```

| 모듈 | 역할 | 주요 의존성 |
|------|------|-------------|
| `kickrush-api` | 외부 요청 처리, 인증/인가 | Spring Web, Spring Security, Actuator |
| `kickrush-core` | 도메인 모델, 비즈니스 로직 | Spring Data JPA, Spring Data Redis, Validation |
| `kickrush-common` | 공통 유틸리티, 예외 정의 | - |

---

## 🛠️ 기술 스택

### Backend
- **Language**: Java 17
- **Framework**: Spring Boot 3.2.2
- **ORM**: Spring Data JPA
- **Cache**: Spring Data Redis
- **Security**: Spring Security (JWT 기반 인증 예정)
- **Build**: Gradle (멀티 모듈)

### Testing
- **Unit Test**: JUnit 5
- **Test DB**: H2 Database (인메모리)
- **방법론**: TDD (Test-Driven Development)

---

## 📦 도메인 모델

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    Shoe     │──────<│   Release   │>──────│    Stock    │
│  (상품)      │       │   (발매)     │       │   (재고)     │
└─────────────┘       └─────────────┘       └─────────────┘
                             │
                             ▼
                      ┌─────────────┐
                      │    Order    │
                      │   (주문)     │
                      └─────────────┘
                             │
                             ▼
                      ┌─────────────┐
                      │   Member    │
                      │   (회원)     │
                      └─────────────┘
```

---

## 🚀 시작하기

### 사전 요구사항
- Java 17+
- Gradle 8.x

### 빌드 및 실행

```bash
# 프로젝트 클론
git clone https://github.com/your-username/KickRush.git
cd KickRush

# 빌드
./gradlew build

# 테스트 실행
./gradlew test

# 애플리케이션 실행
./gradlew :kickrush-api:bootRun
```

---

## 🧪 개발 방법론

본 프로젝트는 **Kent Beck의 TDD(Test-Driven Development)** 원칙을 따릅니다.

### TDD 사이클
1. 🔴 **Red**: 실패하는 테스트 작성
2. 🟢 **Green**: 테스트를 통과하는 최소한의 코드 구현
3. 🔵 **Refactor**: 코드 구조 개선 (동작 변경 없이)

### Tidy First 원칙
- **구조적 변경**과 **행동 변경**을 별도의 커밋으로 분리
- 테스트가 통과한 상태에서만 커밋

---

## 📋 개발 로드맵

### Phase 1: 기본 도메인 설정 ✅
- [x] `Shoe` 엔티티 및 Repository
- [x] `Release` 엔티티 및 Repository
- [x] 기본 Service 레이어

### Phase 2: 재고 관리 🚧
- [ ] `Stock` 엔티티 및 CRUD
- [ ] 재고 차감 로직
- [ ] 동시성 처리 (Pessimistic Lock)

### Phase 3: 주문 처리
- [ ] `Order` 엔티티 및 주문 생성
- [ ] 중복 주문 방지
- [ ] 주문 취소 및 재고 복구

### Phase 4: 회원 관리
- [ ] `Member` 엔티티
- [ ] JWT 기반 인증

### Phase 5: API 레이어
- [ ] REST API 구현
- [ ] API 문서화

---

## 📁 프로젝트 구조

```
kickrush-core/
└── src/
    ├── main/java/com/kanga/kickrush/
    │   └── domain/
    │       ├── shoe/
    │       │   ├── Shoe.java
    │       │   ├── ShoeRepository.java
    │       │   └── ShoeService.java
    │       └── release/
    │           ├── Release.java
    │           ├── ReleaseRepository.java
    │           └── ReleaseService.java
    └── test/java/com/kanga/kickrush/
        └── domain/
            ├── shoe/
            │   ├── ShoeTest.java
            │   ├── ShoeRepositoryTest.java
            │   └── ShoeServiceTest.java
            └── release/
                ├── ReleaseTest.java
                ├── ReleaseRepositoryTest.java
                └── ReleaseServiceTest.java
```

---

## 🤝 Contributing

기여를 환영합니다! Pull Request를 보내기 전에 다음을 확인해주세요:

1. 모든 테스트가 통과하는지 확인
2. TDD 사이클을 준수
3. 구조적 변경과 행동 변경을 별도 커밋으로 분리

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

Made with ❤️ by **Kanga**

</div>

# 예술적인 소프트웨어 D:Code BE



## 📚 Git 브랜치 전략 (Git Flow 방식)
우리 프로젝트는 Git Flow 방식을 기반으로 브랜치를 관리합니다. GitHub에 익숙하지 않아도 이해하기 쉬운 구조로 운영하고 있으니, 아래 내용을 참고해서 브랜치를 만들고 작업해주세요.


## ✅ 브랜치 종류
| 브랜치 이름             | 설명                                                                                              |
| ------------------ | ----------------------------------------------------------------------------------------------- |
| `main`             | 실제 서비스에 배포되는 **최종 코드**가 있는 브랜치입니다. 항상 안정적인 상태를 유지해야 합니다.                                        |
| `develop`          | 기능 개발을 통합하는 **개발 메인 브랜치**입니다. 모든 기능(feature) 브랜치는 여기로 병합됩니다.                                    |
| `feature/*`        | 새로운 기능을 개발할 때 사용하는 브랜치입니다. 완료되면 `develop`으로 PR을 보냅니다.<br>예: `feature/login`, `feature/cart-api` |
| `hotfix/*`         | 배포 후 발견된 **긴급 버그**를 수정할 때 사용합니다. 수정 후 `main`과 `develop`에 병합합니다.                                 |
| `release/*` *(선택)* | 배포 전 최종 점검을 위한 브랜치입니다. 보통 버전 태그와 함께 사용됩니다.<br>예: `release/v1.0.0`                               |



## 💡 작업 예시
1. `develop` 브랜치에서 `feature/login` 브랜치 생성

2. 로그인 기능 개발

3. 개발 완료 후 `develop`에 Pull Request 생성

4. 테스트와 코드 리뷰 후 `develop`에 병합

5. 배포 시 `develop` → `main`으로 병합



## 📌 브랜치 네이밍 규칙
- `feature/기능이름`

- `hotfix/버그이름`

- `release/버전이름`

### 예시:
- `feature/signup-form`, `hotfix/crash-on-load`, `release/v1.0.2`

## 🔗 참고 링크
- [GitHub 협업 이것만은 알자 (Velog)](https://velog.io/@pgmjun/Github-%ED%98%91%EC%97%85-%EC%9D%B4%EA%B2%83%EB%A7%8C%EC%9D%80-%EC%95%8C%EC%9E%90-Branch)

- [Git Flow VS GitHub Flow (Velog)](https://velog.io/@gmlstjq123/Git-Flow-VS-Github-Flow)


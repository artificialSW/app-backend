📚 Git 브랜치 전략 (Git Flow 방식)

✅ 브랜치 종류
브랜치 이름	설명
main	실제 서비스에 배포되는 최종 코드가 있는 브랜치입니다. 항상 안정적인 상태를 유지해야 합니다.
develop	기능 개발을 통합하는 개발 메인 브랜치입니다. 모든 기능(feature) 브랜치는 여기로 병합됩니다.
feature/*	새로운 기능을 개발할 때 사용하는 브랜치입니다. 완료되면 develop으로 PR을 보냅니다.
예: feature/login, feature/cart-api
hotfix/*	배포 후 발견된 긴급 버그를 수정할 때 사용합니다. 수정 후 main과 develop에 병합합니다.
release/* (선택)	배포 전 최종 점검을 위한 브랜치입니다. 보통 버전 태그와 함께 사용됩니다.
예: release/v1.0.0

💡 작업 예시
develop 브랜치에서 feature/login 브랜치 생성

로그인 기능 개발

개발 완료 후 develop에 Pull Request 생성

테스트와 코드 리뷰 후 develop에 병합

배포 시 develop → main으로 병합

📌 브랜치 네이밍 규칙
feature/기능이름

hotfix/버그이름

release/버전이름

예시:
feature/signup-form, hotfix/crash-on-load, release/v1.0.2

🔗 참고 링크
GitHub 협업 이것만은 알자 (Velog)

Git Flow VS GitHub Flow (Velog)


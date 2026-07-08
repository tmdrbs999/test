# enc-tool

`files/*.properties`(평문)를 사내 암호화 모듈로 암호화하여 `result.yaml`(ENC(...) 형태)로 뽑는 도구.
확산기관 values.yaml의 env에 붙일 암호문을 만드는 용도.

## 폴더 구조
```
enc-tool/
├── pom.xml
├── files/
│   └── test.properties        # 기관별 DB_HOST, DB_PORT (평문)
└── src/main/java/
    └── ENC.java               # 위키 코드 (패키지 없음 → 이 위치, 파일명 ENC.java)
```
실행하면 프로젝트 루트에 `result.yaml`이 생성됨.

## 전제 (행망 PC)
- `iop-cm-crypto-1.0.0` 이 로컬 `.m2`에 있어야 함
  → `.m2\repository\kr\go\iop\cm\module\iop-cm-crypto\1.0.0\iop-cm-crypto-1.0.0.jar`
  (없으면 그 jar를 `mvn install:install-file`로 로컬 저장소에 설치)
- eGov 암호화(`org.egovframe.rte.fdl.cryptography`)는 위 모듈이 전이로 가져옴
- JDK 17, Maven, VSCode "Extension Pack for Java"

## 실행
1. `files/test.properties`에 기관 값 입력
   ```
   DB_HOST=10.x.x.x
   DB_PORT=3306
   ```
2. VSCode로 **이 폴더(enc-tool)** 를 연다 (pom.xml 있는 곳) → Maven 임포트 대기
3. 실행:
   - 터미널: `mvn compile exec:java`
   - 또는 `ENC.java`의 main 위 `Run` 클릭
4. → `result.yaml` 생성:
   ```yaml
   # ====== test.properties ========
     - name: DB_HOST
       value: ENC(암호문)
     - name: DB_PORT
       value: ENC(암호문)
   ```
5. 이 `- name/value` 를 해당 확산기관 values.yaml의 env에 복붙

## 기관별 반복
`files/test.properties`의 값만 기관 HOST/PORT로 바꿔 다시 실행 → 그 기관 values.yaml에 반영.

## 검증(권장)
넣기 전, 기존 기관의 `ENC(...)` 값 하나를 `service.decrypt(...)`로 풀어 진짜 값이 나오는지 확인하면 호환 확실.

## 참고
- `iop-cm-crypto` 안에 들어있던 pom.xml 은 "그 모듈을 빌드하는 pom"이라 여기 쓰면 안 됨.
  우리 프로젝트는 그 모듈을 **의존성으로 선언**하는 위 pom.xml 을 쓴다.
- ⚠️ `files/*.properties`(평문)와 암호화 키는 민감정보. git 커밋/외부 공유 주의.

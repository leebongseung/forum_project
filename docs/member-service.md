# member

## 엔티티 설계
엔티티는 크게 BaseEntity 를 상속한 MemberEntity를 구현하였다.
여기서 크게 눈여겨 봐야할 점은 다음과 같다
1. BaseEntity 설계하기
2. MemberEntity 설계하기

### 1. BaseEntity 설계과정
고려사항은 다음과 같다.
1. 상위 클래스이기 때문에 @MappedSuperclass 을 적용해야함

- 이유 : JPA에서 상속 관계를 나타내는 애너테이션으로, 공통 필드나 매핑할 때 꼭 필요함.
- 장점 : 공통적으로 사용하는 필드를 정의 가능, 유지보수 용이

2. @EntityListeners(AuditingEntityListener.class) 와 @CreatedDate, @LastModifiedDate 를 사용하면서 생성시간, 수정시간을 기록함.
- 이유 : 자동화된 시간 기록이 가능
- 장점 : 
  - 코드 간결성, 자동화, 
  - JPA Auditing(엔티티의 생성 및 수정 시간 외에도 추가 감사가 가능, 확장하면 가능하다.)
- 단점 : 
  - 서버 시간에 의존적, 
  - 성능 문제(대량의 엔티티 상태 변경 시에 성능에 영향을 미칠 수 있다.)
  - 제어의 어려움(아직 경험하지 못했다.)
- 보완할점 
  - 시간 동기화 : 서버 시간을 조정해서 db에 저장되도록 해야함.
  - 성능 최적화 : 모니터링 시스템을 도입하여 캐싱 또는 배치처리를 고민할 것.

### 2. MemberEntity 설계하기
1. MemberRole을 enum 타입으로 설계하여 관리자와 멤버 역할을 정의하였습니다. @Enumerated(EnumType.STRING)을 사용하여 enum 값을 문자열 형태로 데이터베이스에 저장하도록 설정하였습니다.
2. 비밀번호를 BCryptPasswordEncoder로 암호화 하였음.
   - 효과 : 비밀번호를 안전하게 암호화하기 위해 BCrypt 해시 함수를 사용하여 해시와 솔트를 생성하고, 이를 통해 비밀번호의 보안을 강화가능
3. memberId를 UUID로 설정하고 자동 증가(auto-increment)가 아닌 UUID를 사용함으로써 보안을 강화
    - 설명 : 고유하고 예측 불가능한 식별자를 부여함으로써 예측불가능하게 만듬.
4. PK 생성전략으로  GenerationType.IDENTITY 로  데이터베이스의 고유 ID 생성 기능을 사용하여 기본 키 값을 설정하였음.
    - 장점 : 간편하고, 트랜잭션에 안정적이고 일관성을 유지가능.
    - 단점 : 대량의 데이터 삽입 시 성능이 저하 될 수 있으며, 데이터베이스에 종속적이다.
## 회원가입 로직
회원가입은 로직은 크게 5가지로 구성됨.
1. 이메일 중복 여부 확인
2. 아이디 중복 여부 확인
3. 비밀번호 BCryptPasswordEncoder 로 암호화 하기
4. MemberId UUID.randomUUID()로 유추 불가능하게 랜덤한 멤버아이디 가지게 하기
5. DB에 save 하기

## 트러블 슈팅
### 1. @EntityListeners(AuditingEntityListener.class) 가 동작하지 않았음.
   - 이유
       1. JPA Auditing 비활성화: @EnableJpaAuditing 애너테이션이 없으면 JPA Auditing 기능이 활성화되지 않음. Auditing 관련 이벤트가 처리되지 않기 때문에 @EntityListeners(AuditingEntityListener.class)가 기대하는 Auditing 이벤트가 발생하지 않았다.
       2. AuditingEntityListener 등록 실패: @EnableJpaAuditing 애너테이션이 없어 AuditingEntityListener가 엔티티 리스너로 등록되지 않아서 엔티티의 생성 시간 및 수정 시간을 자동으로 기록하지 못했음.
   - 해결방안
       - src/main/java/com/example/forum_project/config/JpaAuditingConfig.java 파일을 생성해 JPA Auditing 기능을 활성화하였다.
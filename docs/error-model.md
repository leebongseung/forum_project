# 에러 응답 모델 설계
## 1. 통일된 Error Response 객체 참조하기!
Error Response 객체를 두어 message, status, errors, code 각종 상황에 대해 리턴 해줌
- message : 에러에 대한 설명(ex : 아이디 중복 등)
- status : Http 상태 코드에 해당(200, 400, ...)
- errors : @Valid 어노테이션으로 검증하거 등, 절대 null이 반환 되지 않는다.
- code : 에러 식별 코드(ex : M001, M002, ...)

## 2. ControllerAdvice로 모든 예외 핸들링 하기!
이 과정에서 오랜만에 구현하다보니 낯선 것들만 다시한번 정리하였다!
- handleMethodArgumentNotValidException : binding 에러 시 발생한다, 주로 @RequestBody, @RequestPart 어노테이션에서 발생 함.
- handleBindException : @ModelAttribute 의 bindingError 시에 발생함
- MethodArgumentTypeMismatchException : enum type 일치하지 않아 binding 못할 경우 발생
- handleHttpRequestMethodNotSupportedException : 지원하지 않은 http method를 호출할 경우
- handleAccessDeniedException : 권한을 보유하지 않았을 때

## 3. 최상위 BusinessException
이 글을 보면서 가장 인상 깊었다. 에러를 상속해서 유효하지 않는 값일 경우, 엔티티를 못찾았을 경우로 나누어서 에러를 여러개 상속받고 관리하는 것을 보았는데 프로젝트 규모가 크다면 아주 효과적으로 에러를 관리할 수 있을 것 같았다.


## reference
1. https://cheese10yun.github.io/spring-guide-exception/#undefined
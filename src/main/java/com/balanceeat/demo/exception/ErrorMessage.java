package com.balanceeat.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {
    private final String message;
    private final int status;
    
    // Common
    public static final String INTERNAL_SERVER_ERROR = "내부 서버 에러가 발생했습니다.";
    
    // Api
    public static final String API_CONTENT_NOT_FOUND = "응답을 생성하는데 실패했습니다.";
    public static final String API_REQUEST_TIMEOUT = "API 호출 시간이 초과됐습니다.";
    public static final String API_UNKNOWN_FINISH_REASON = "알 수 없는 이유로 응답을 불러올 수 없습니다.";
    
    // Auth
    public static final String INVALID_TOKEN_EXCEPTION = "유효하지 않은 토큰입니다.";
    public static final String UNAUTHORIZED_EXCEPTION = "유효한 인증이 필요합니다.";
    public static final String LOGOUT_USER_NOT_FOUND = "로그아웃 된 사용자입니다.";
    public static final String TOKEN_MISMATCH_EXCEPTION = "토큰의 유저 정보가 일치하지 않습니다.";
    public static final String INVALID_EMAIL_FORMAT = "유효하지 않은 이메일 형식입니다.";
    public static final String INVALID_PASSWORD_FORMAT = "비밀번호는 8~20자의 영문, 숫자, 특수문자를 포함해야 합니다.";
    public static final String ACCOUNT_LOCKED = "계정이 잠겨있습니다. 관리자에게 문의하세요.";
    public static final String TOO_MANY_LOGIN_ATTEMPTS = "로그인 시도 횟수가 초과되었습니다. 잠시 후 다시 시도해주세요.";
    public static final String CONFIRMED_PASSWORD_IS_NOT_SAME = "비밀번호 확인과 비밀번호가 일치하지 않습니다.";
    
    // User
    public static final String USER_ALREADY_LOGOUT = "이미 로그아웃 된 사용자입니다.";
    public static final String USER_ALREADY_EXIST = "이미 존재하는 아이디입니다.";
    public static final String USER_NOT_FOUND = "존재하지 않는 사용자입니다.";
    public static final String USER_ACCOUNT_DISABLED = "비활성화된 계정입니다.";
    public static final String USER_CREDENTIALS_EXPIRED = "비밀번호가 만료되었습니다.";

    // 데이터 무결성 관련 에러 메시지
    public static final String DATA_INTEGRITY_VIOLATION = "데이터 무결성 제약 조건 위반이 발생했습니다.";
    public static final String DUPLICATE_ENTRY = "이미 존재하는 데이터입니다.";
    public static final String INVALID_DATA_FORMAT = "잘못된 데이터 형식입니다.";
    public static final String REQUIRED_FIELD_MISSING = "필수 입력 필드가 누락되었습니다.";
    public static final String DATA_TOO_LONG = "입력된 데이터가 허용된 길이를 초과했습니다.";
    public static final String INVALID_DATA_TYPE = "잘못된 데이터 타입입니다.";
}

//package org.neit.musicapp.exception;
//
//
//import org.neit.musicapp.dto.ApiResponse;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.text.ParseException;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(value = RuntimeException.class)
//    public ResponseEntity<ApiResponse<?>> runtimeExceptionHandler(AppException exception) {
//        ErrorCode errorCode = exception.getErrorCode();
//        ApiResponse<?> response = new ApiResponse<>();
//        response.setCode(errorCode.getCode());
//        response.setMessage(errorCode.getMessage());
//
//        return ResponseEntity
//                .status(errorCode.getStatusCode())
//                .body(response);
//    }
//    @ExceptionHandler(value = AccessDeniedException.class)
//    public ResponseEntity<ApiResponse> accessDeniedExceptionHandler(AccessDeniedException exception) {
//        System.out.println(exception.getMessage());
//        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
//        ApiResponse response = new ApiResponse();
//        response.setCode(errorCode.getCode());
//        response.setMessage(errorCode.getMessage());
//
//        return ResponseEntity
//                .status(errorCode.getStatusCode())
//                .body(response);
//    }
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
//        System.out.println(exception.getMessage());
//        String enymKey = exception.getFieldError().getDefaultMessage();
//        ErrorCode errorCode = ErrorCode.INVALID_KEY;
//
//        try {
//            errorCode = ErrorCode.valueOf(enymKey);
//        } catch (IllegalArgumentException e) {
//
//        }
//
//        ApiResponse response = new ApiResponse();
//
//        response.setCode(errorCode.getCode());
//        response.setMessage(errorCode.getMessage());
//
//        return ResponseEntity.badRequest().body(response);
//    }
//    @ExceptionHandler(value = ParseException.class)
//    public ResponseEntity<ApiResponse> handlingParseException(ParseException exception) {
//        System.out.println(exception.getMessage());
//        ErrorCode errorCode = ErrorCode.REQUEST_FAILED;
//        ApiResponse response = new ApiResponse();
//        response.setCode(errorCode.getCode());
//        response.setMessage(errorCode.getMessage());
//
//        return ResponseEntity
//                .status(errorCode.getStatusCode())
//                .body(response);
//    }
//}

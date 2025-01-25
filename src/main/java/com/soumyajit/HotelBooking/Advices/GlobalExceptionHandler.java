package com.soumyajit.HotelBooking.Advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(com.soumyajit.HotelBooking.Exception.ResourceNotFound.class)
    public ResponseEntity<ApiResponse<?>> noEmployeeFound(com.soumyajit.HotelBooking.Exception.ResourceNotFound exception){
        //ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,"Resoure Not Found");
        ApiError apiError = ApiError.builder().status(HttpStatus.NOT_FOUND).message(exception.getMessage()).build();
        return buildErrorResponseEntity(apiError);
    }

    private ResponseEntity<ApiResponse<?>> buildErrorResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(new ApiResponse<>(apiError),apiError.getStatus());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> InternalServererror(Exception e){
        ApiError apiError = ApiError
                .builder().status(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(e.getMessage()).build();
        return buildErrorResponseEntity(apiError);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> invalidInputs(MethodArgumentNotValidException exception){
        List<String> errors = exception.getBindingResult()
                .getAllErrors()
                .stream().map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError apiError = ApiError
                .builder().status(HttpStatus.BAD_REQUEST)
                .message(errors.toString()).build();
        return buildErrorResponseEntity(apiError);
    }
}

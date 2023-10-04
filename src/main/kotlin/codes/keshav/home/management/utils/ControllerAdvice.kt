package codes.keshav.home.management.utils

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

//@ControllerAdvice
//class GlobalExceptionHandler {
//
//	@ExceptionHandler(RuntimeException::class)
//	@ResponseStatus(HttpStatus.CONFLICT)
//	fun handleRuntimeException(ex: RuntimeException): ResponseEntity<String> {
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.message)
//	}
//}
package ru.bmstu.testsystem.gateway.model

import java.util.stream.Collectors
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.validation.ObjectError


data class GenericResponse (

    var message: String? = null,
    var type: String
) {
    constructor(allErrors: List<ObjectError>, error: String) : this(
        allErrors
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(System.lineSeparator())),
        error)

    }

package ru.bmstu.testsystem.services

import ru.bmstu.testsystem.model.TableData
import java.util.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface TableService {

    // таблица пользователей
    fun getUserTable(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): TableData

    // таблица тестов
    fun getExamTable(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): TableData

    // таблица результатов
    fun getResultsTable(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): TableData
}

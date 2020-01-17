package ru.bmstu.testsystem.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.bmstu.testsystem.model.Cell
import ru.bmstu.testsystem.model.Row
import ru.bmstu.testsystem.model.TableData
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service("tableService")
class TableServiceImpl : TableService {

    @Autowired
    private lateinit var examService: ExamServiceImpl

    @Autowired
    private lateinit var userService: UserServiceImpl

    @Autowired
    private lateinit var examResultService: ExamResultServiceImpl

    // таблица пользователей
    override fun getUserTable(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): TableData {
        val headers = arrayListOf("Пользователь", "Управление")
        val rows = arrayListOf<Row>()
        val users = userService.getUsers(page, limit, request, response)
        for (usr in users) {
                    val row = Row(arrayListOf(
                    Cell(usr.username, "/edit_profile/${usr.id}"),
                    Cell("Удалить", "/delete/user/${usr.id}")
            ))
            rows.add(row)
        }
        val pageNumbers = IntRange(1, users.totalPages).toList()
        
        return TableData(headers, rows, pageNumbers, "/all_users", users.totalPages, users.size, users.number)
    }

    override fun getResultsTable(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): TableData {
        val headers = arrayListOf("Тест", "Пользователь", "Результат", "Дата прохождения")
        val rows = arrayListOf<Row>()
        val results = examResultService.getResults(page, limit, request, response)
        for (res in results) {
            val row = Row(arrayListOf(
                    if (res.exam != null) Cell(res.exam!!.name, "/exam_page/${res.exam!!.id}") else Cell("Неизвестно"),
                    if (res.user != null) Cell(res.user!!.username, "/edit_profile/${res.user!!.id}") else Cell("Неизвестно"),
                    Cell(res.result),
                    Cell(res.passedAt)
            ))
            rows.add(row)
        }
        val pageNumbers = IntRange(1, results.totalPages).toList()
        
        return TableData(headers, rows, pageNumbers, "/all_results", results.totalPages, results.size, results.number)
    }

    override fun getExamTable(page: Int, limit: Int,
                              request: HttpServletRequest, response: HttpServletResponse): TableData {
        val headers = arrayListOf("Тест", "Описание", "Число прохождений", "Управление")
        val rows = arrayListOf<Row>()
        val exams = this.examService.getExams(page, limit, request, response)
        for (exam in exams) {
            val row = Row(arrayListOf(
                    Cell(exam.name, "/exam_page/${exam.id}"),
                    Cell(exam.description),
                    Cell(exam.passes),
                    Cell("Удалить", "/delete/exam/${exam.id}")
            ))
            rows.add(row)
        }
        val pageNumbers = IntRange(1, exams.totalPages).toList()
        
        return TableData(headers, rows, pageNumbers, "/all_exams", exams.totalPages, exams.size, exams.number)
    }
}

package ru.bmstu.testsystem.exams.domain


import ru.bmstu.testsystem.exams.util.JpaQuestionConverterJson
import java.util.UUID
import java.sql.Date
import javax.persistence.*


@Entity
@Table(name = "exams")
data class Exam (

        val name: String,

        val description: String,

        var createdAt: Date,

        @Convert(converter = JpaQuestionConverterJson::class)
        val questions: List<Question>,

        @Column(name = "is_deleted")
        val isDeleted: Boolean = false
) {
    @GeneratedValue
    @Id
    var id: UUID = UUID.randomUUID()

    var passCount: Int = 0

}




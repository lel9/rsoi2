package ru.bmstu.testsystem.model

data class TableData (
        var headers: MutableList<String> = arrayListOf(),
        var rows: MutableList<Row> = arrayListOf(),
        var pageNumbers: List<Int> = arrayListOf(),
        var endpoint: String = "",
        var totalPages: Int,
        var size: Int,
        var number: Int
)

data class Row (
        var cells: MutableList<Cell> = arrayListOf()
)

data class Cell (
        var data: Any = "",
        var link: String = ""
)

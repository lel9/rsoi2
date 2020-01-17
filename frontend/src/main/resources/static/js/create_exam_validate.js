function validateQuestion(question) {
    if (question.getElementsByClassName("right-answer-input").length !== 0)
        return true;

    var res = false;
    var errorMsgText;

    var variants = question.getElementsByClassName("form-check-input");
    if (variants.length === 0) {
        errorMsgText = "Добавьте хотя бы один вариант ответа";
        res = false;
    }
    else {
        for (var v = 0; v < variants.length && !res; v++) {
            if (variants[v].checked)
                res = true;
        }
        if (!res)
            errorMsgText = "Отметьте правильные варианты ответа";
    }

    if (!res) {
        var errorMsgs = question.getElementsByClassName("error-message");
        if (errorMsgs.length === 0) {
            var errorMsg = document.createElement('div');
            errorMsg.className = "col-12 error-message";
            var delAnsBlock = question.getElementsByClassName("del-answer-block")[0];
            delAnsBlock.parentElement.insertBefore(errorMsg, delAnsBlock);
        }
        else {
            errorMsg = errorMsgs[0];
        }
        errorMsg.innerHTML = errorMsgText;
    }
    return res;
}

function validate() {
    var questions = document.getElementsByClassName("question");
    if (questions.length === 0) {
        var errorMsg;
        var errorMsgs = document.getElementsByClassName("error-message");
        if (errorMsgs.length > 0)
            errorMsg = errorMsgs[0];
        else
            errorMsg = document.createElement('div');
        var addQuestionBtn = document.getElementById("addQuestion");
        errorMsg.className = "error-message";
        errorMsg.innerHTML = "Добавьте хотя бы один вопрос";
        addQuestionBtn.parentElement.insertBefore(errorMsg, addQuestionBtn);
        return false;
    }
    for (var i = 0; i < questions.length; i++)
        if (!validateQuestion(questions[i]))
            return false;
    return true;
};

function validateAndPrepare() {
    var res = true;
    if (!validate())
        res = false;
    else
        prepareForm();
    return res;
};

function prepareForm() {
    var questions = document.getElementsByClassName("question");
    for (var i = 0; i < questions.length; i++) {
        var text = questions[i].getElementsByClassName("questionText")[0];
        text.setAttribute('name', 'questionIns[' + i + '].questionText');

        // правильный ответ
        var inputs = questions[i].getElementsByClassName("right-answer-input");
        if (inputs.length !== 0) {
            var input = inputs[0];
            input.setAttribute('name', 'questionIns[' + i + '].correctInputAnswer');
        }

        // варианты ответа
        var variants = questions[i].getElementsByClassName("variant");
        if (variants.length !== 0) {
            for (var j = 0; j < variants.length; j++) {
                var variant = variants[j];
                variant.setAttribute('name', 'questionIns[' + i + '].variants[' + j + ']');
            }
        }

        // правильные варианты
        var rightVariants = questions[i].getElementsByClassName("form-check-input");
        if (rightVariants.length !== 0) {
            for (var j = 0; j < rightVariants.length; j++) {
                var correctVariant = rightVariants[j];
                correctVariant.setAttribute('name', 'questionIns[' + i + '].correctVariants');
                correctVariant.setAttribute('value', j);
            }
        }

        // тип вопроса
        var types = questions[i].getElementsByTagName("select")[0];
        types.setAttribute('name', 'questionIns[' + i + '].type');
    }
};

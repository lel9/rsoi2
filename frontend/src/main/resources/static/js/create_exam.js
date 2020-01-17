function onSelectChange(questionId) {

    var questionCard = document.getElementById('question' + questionId);

    var elems = questionCard.getElementsByClassName("form-check-input");

    var selectedIndex = questionCard.getElementsByTagName('select')[0].options.selectedIndex;

    if (selectedIndex < 2) {
        for (var i = 0; i < elems.length; i++) {
            var checkbox = document.createElement('input');
            checkbox.className = "form-check-input mt-2";
            if (selectedIndex === 0)
                checkbox.type = "radio";
            else
                checkbox.type = "checkbox";
            elems[i].parentNode.replaceChild(checkbox, elems[i]);
        }

        var rightAnswer = questionCard.getElementsByClassName("right-answer-input");
        if (rightAnswer.length !== 0) {
            rightAnswer[0].parentNode.remove();

            var row = questionCard.getElementsByClassName("row")[0];

            var col10 = document.createElement('div');
            col10.className = "col-10 add-answer-block inputAnswer";

            var inputAns = document.createElement('input');
            inputAns.type = "text";
            inputAns.className = "form-control mt-3 answer-input";
            inputAns.placeholder = "Вариант ответа";

            col10.appendChild(inputAns);

            var col2 = document.createElement('div');
            col2.className = "col-2 add-answer-block";

            var button = document.createElement('button');
            button.type = "button";
            button.className = "btn mt-3 btn-outline-primary";
            button.innerHTML = "Добавить";

            button.addEventListener("click", function(){ onAddAnswerClick(questionId) });

            col2.appendChild(button);

            row.insertBefore(col2, row.getElementsByClassName("del-answer-block")[0]);
            row.insertBefore(col10, col2);

        }
    }

    else if (selectedIndex === 2) {
        var removeElems = questionCard.getElementsByClassName("ans-list");

        while (removeElems.length > 0) {
            removeElems[0].remove();
        }

        var removeInput = questionCard.getElementsByClassName("add-answer-block");
        while (removeInput.length > 0) {
            removeInput[0].remove();
        }


        var row = questionCard.getElementsByClassName("row")[0];

        var col12 = document.createElement('div');
        col12.className = "col-12 add-answer-block";

        var inputAns = document.createElement('input');
        inputAns.type = "text";
        inputAns.className = "form-control mt-3 right-answer-input";
        inputAns.placeholder = "Правильный ответ";
        inputAns.required = true;

        col12.appendChild(inputAns);

        row.insertBefore(col12, row.getElementsByClassName("del-answer-block")[0]);

    }
};

function onAddAnswerClick(questionId){

    var questionCard = document.getElementById('question' + questionId);
    var answerInput = questionCard.getElementsByClassName('answer-input');

    var answer = answerInput[0].value;

    if (answer === "")
        return;

    var checkbox = document.createElement('input');
    checkbox.className = "form-check-input mt-2";
    checkbox.setAttribute('name', questionId);

    var selectedIndex = questionCard.getElementsByTagName('select')[0].options.selectedIndex;
    if (selectedIndex === 0)
        checkbox.type = "radio";
    else if (selectedIndex === 1)
        checkbox.type = "checkbox";


    var div = document.createElement('div');
    div.className = "form-check col-10 mt-3 ans-list";

    var btnDiv = document.createElement('div');
    btnDiv.className = "col-2 mt-3 ans-list";

    var buttonDel = document.createElement('button');
    buttonDel.setAttribute('type', 'button');
    buttonDel.className="btn btn-outline-danger";
    buttonDel.textContent="Удалить";
    buttonDel.addEventListener("click", function(){ var prev = this.parentNode.previousSibling; prev.remove(); this.parentNode.remove(); });

    btnDiv.appendChild(buttonDel);

    var v = document.createElement('input');
    v.className = "form-control variant";
    v.value = answer;
    v.required = true;

    div.appendChild(checkbox);
    div.appendChild(v);

    var row = questionCard.getElementsByClassName("row")[0];
    var inputField = row.getElementsByClassName("inputAnswer")[0];
    row.insertBefore(div, inputField);
    row.insertBefore(btnDiv, inputField);

    answerInput[0].value = "";
};

function onAddQuestionClick () {

    var questionCard = document.createElement('div');
    questionCard.className = "card mb-4 question";

    var id = document.getElementsByClassName('question').length;
    questionCard.id = "question" + id;

    var cardBody = document.createElement('div');
    cardBody.className = "card-body";

    var container = document.createElement('div');
    container.className = "container";

    var row = document.createElement('div');
    row.className = "row";

    var col9 = document.createElement('div');
    col9.className = "col-9";

    var input = document.createElement('input');
    input.type = "text";
    input.className = "form-control questionText";
    input.placeholder = "Текст вопроса";
    input.required = true;

    col9.appendChild(input);

    var col3 = document.createElement('div');
    col3.className = "col-3";

    var select = document.createElement('select');
    select.className = "form-control";

    var option1 = document.createElement('option');
    option1.setAttribute('value','SINGLE_ANSWER')
    option1.innerHTML = "Один ответ";

    var option2 = document.createElement('option');
    option2.setAttribute('value','MULTIPLE_ANSWER')
    option2.innerHTML = "Несколько ответов";

    var option3 = document.createElement('option');
    option3.setAttribute('value','NO_ANSWER')
    option3.innerHTML = "Без выбора ответа";

    select.appendChild(option1);
    select.appendChild(option2);
    select.appendChild(option3);

    select.addEventListener("change", function(){ onSelectChange(id) });

    col3.appendChild(select);

    var col10 = document.createElement('div');
    col10.className = "col-10 add-answer-block inputAnswer";

    var inputAns = document.createElement('input');
    inputAns.type = "text";
    inputAns.className = "form-control mt-3 answer-input";
    inputAns.placeholder = "Вариант ответа";

    col10.appendChild(inputAns);

    var col2 = document.createElement('div');
    col2.className = "col-2 add-answer-block";

    var button = document.createElement('button');
    button.type = "button";
    button.className = "btn btn-outline-primary mt-3";
    button.innerHTML = "Добавить";

    col2.appendChild(button);

    var delAnsBlock = document.createElement('div');
    delAnsBlock.className="col-12 del-answer-block";

    var delAnsBtn = document.createElement('button');
    delAnsBtn.className="btn btn-outline-danger mt-3";
    delAnsBtn.addEventListener("click", function(){ document.getElementById(questionCard.id).remove() });
    delAnsBtn.textContent="Удалить вопрос";

    delAnsBlock.appendChild(delAnsBtn);

    row.appendChild(col9);
    row.appendChild(col3);
    row.appendChild(col10);
    row.appendChild(col2);
    row.appendChild(delAnsBlock);

    container.appendChild(row);
    cardBody.appendChild(container);
    questionCard.appendChild(cardBody);

    button.addEventListener("click", function(){ onAddAnswerClick(id) });

    document.getElementById("questionList").appendChild(questionCard);

};


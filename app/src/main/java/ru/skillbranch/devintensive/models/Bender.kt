package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
//        return if (question.answers.contains(answer)) {
//            question = question.nextQuestion()
//            "Отлично - ты справился\n${question.question}" to status.color
//        } else {
//            var again = question.getAnswerHint(answer)
//            if (status == Status.CRITICAL) {
//                again += ".\n Давай все по новой"
//                question = Question.NAME
//            }
//            status = status.nextStatus()
//            "Это неправильный ответ$again\n${question.question}" to status.color
//        }
        var again = question.getAnswerHint(answer)
        if (again.isEmpty()) {
            return if (question.answers.contains(answer)) {
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            } else {
                if (question==Question.IDLE){
                    question.question to status.color
                }else {
                    if (status == Status.CRITICAL) {
                        again = ".\n Давай все по новой"
                        question = Question.NAME
                    }
                    status = status.nextStatus()
                    "Это неправильный ответ$again\n${question.question}" to status.color
                }
            }
        } else {
            return "$again\n${question.question}" to status.color
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("Бендер", "Bender")) {
            override fun nextQuestion(): Question = PROFESSION
            override fun getAnswerHint(answer: String): String {
                return when (answer) {
                    answer.capitalize() -> ""
                    else -> "Имя должно начинаться с заглавной буквы"
                }
            }
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
            override fun getAnswerHint(answer: String): String {
                return when (answer) {
                    answer.decapitalize() -> ""
                    else -> "Профессия должна начинаться со строчной буквы"
                }
            }
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
            override fun getAnswerHint(answer: String): String {
                return if (answer.contains("""\d""".toRegex())) {
                    "Материал не должен содержать цифр"
                } else {
                    ""
                }
            }
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
            override fun getAnswerHint(answer: String): String {
                return if (answer.contains("""\D""".toRegex())) {
                    "Год моего рождения должен содержать только цифры"
                } else {
                    ""
                }
            }
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
            override fun getAnswerHint(answer: String): String {
                return if (answer.contains("""\D""".toRegex()) || answer.length != 7) {
                    "Серийный номер содержит только цифры, и их 7"
                } else {
                    ""
                }
            }
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
            override fun getAnswerHint(answer: String) = ""
        };

        abstract fun nextQuestion(): Question
        abstract fun getAnswerHint(answer: String): String
    }
}
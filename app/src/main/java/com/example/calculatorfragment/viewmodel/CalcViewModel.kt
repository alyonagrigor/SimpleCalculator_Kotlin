package com.example.calculatorfragment.viewmodel

/**

ДОКУМЕНТАЦИЯ
1. Структура приложения.
Приложение состоит из одной активити, двух фрагментов (ButtonFragment и JournalFragment) и Shared
ViewModel. В горизонтальной ориентации JournalFragment находится слева и занимает
2/3 экрана, в портретной ориентации находится сверху.
Приложение содержит отдельные layout для портретной и альбомной ориентации, а также dimens для
планшетов sw600dp и sw720dp.
В файле styles.xml заданы стили для кнопок.

2. Функционал приложения.
2.1. Для хранения информации о состоянии приложения используются переменные:
String stringInput - используется для хранения числа, которое в данный момент вводит пользователь.
Может содержать только цифры или минус для отрицательного числа. Для вывода цифр в текстовом поле
используется String переменная showInput, содержание которой не всегда совпадает со stringInput.
Double num1 - первое введенное число в цепочке операций или результат предыдущих операций, то
есть левый операнд.
Double num2 - число, считанное из строки stringInput, то есть правый операнд.
Переменная char operator используется для хранения последней заданной пользователем операции
(только 4 арифметические операции в виде '+').
Boolean hasNum1 - изначально false, присваивается true, в момент нажатия кнопки с арифметической
операцией, если пользователь уже ввел одно число.
Boolean isLastPressedOperation - принимает значение true, если последняя нажатая кнопка - одна из
четырех основных арифметических операций.
Boolean isBsAvailable - используется для работы Backspace. Записывается как true, если была нажата
кнопка с цифрой или точкой, и перезаписывается как false, если была нажата любая другая кнопка.
String journal - используется для хранения и вывода истории, включая цифры, арифметические
действия, проценты и минус для отрицательного числа.

2.2. При нажатии кнопок с цифрами вызывается функция enterDigit(), который принимает аргумент типа
Integer - собственно цифра, которую нажал пользователь. В функции происходит:
1. Цифра записывается в строку истории journal.
2. Цифра записывается в строку для вывода stringInput.
3. Цифра выводится в строке showInput.
4. Булевой переменной isLastPressedOperation присваивается значение false.

2.3. При нажатии кнопки "Точка" btnDot происходят 4 действия, аналогичные п. 2.2.

2.4. При нажатии кнопок с арифметическими операциями +, -, *, / вызывается функция
MainOperation(), в который передается аргумент - char operator, например, "+".
MainOperation() предусматривает 5 сценариев:
1. Если еще не было введено ни одной цифры, выдает подсказку "Введите первое число".
2. Если уже была введена одна цифра, то записывает введенный оператор для дальнейших действий.
3. Если уже прошло несколько операций, то записывает введенный оператор для дальнейших действий.
4. Если были введены 2 цифры и оператор до этого, то срабатывает как кнопка равно.
5. Если до этого была нажата другая кнопка с арифметической операцией, то перезаписывает operator,
заменяя на новую арифметическую операцию.

2.5. ФункцияoperationCalc() получает аргумент - char sign из функции MainOperation(), а затем:
1. Получает num2 из строки stringInput.
2. Выполняет соответствующие арифметические операции с числами num1 и num2.
3. Полученное значение перезаписывается в num1 и выводится на экран, num2 обнуляется.
4. Функцияoutput() сохраняет результат операции в stringInput, выводит его в showInput,
а также удаляет .0, если получилось целое число.

2.6. Функцияclear() вызывается при нажатии на кнопку "С".

2.7. При нажатии кнопки "=" вызывается функция operationCalc(), после его выполнения:
1. Последнее введенное число записывается в journal.
2. Переменной operator присваивается значение '0'.
3. Значение isLastPressedOperation = false.

2.8. Кнопка "%" вызывает функцию percent(), которые проверяет состояние приложения и
запускает 1 из 4 сценариев.
1. Если еще не было введено ни одного числа, появляется Toast с просьбой ввести число.
2. Если было введено только 1 число и не нажата арифметическая операция, то рассчитывается 1% от
числа.
3. Если имеются 2 операнда и 1 оператор, то срабатывает стандартный функционал.
4. Если есть 1 операнд и нажата арифметическая операция, то появляется Toast с просьбой
ввести второе число.

2.9. Кнопка "Backspace" вызывает функция backspace(), который срабатывает при условии что
isBsAvailable  == true и запускает 1 из 4 сценариев:
1. Если в строке stringInput > 2 символов, то последняя цифра удаляется из этой строки и из journal.
2. Если в строке stringInput 2 символа и первый из них не '-', то аналогично.
3. Если в строке stringInput 1 символ, то она заменяется в строке stringInput на ноль, удаляется
из journal и isLastPressedDigit присваивается false (т.к. больше удалять ничего).
4. Если в строке stringInput одна цифра и минус, то заменяется в строке stringInput на ноль,
удаляется из journal и isBsAvailable присваивается false (т.к. больше удалять ничего).
Если последней нажатой кнопкой была арифметическая операция, =, % или negate, то isBsAvailable
присвоено значение false, и никакие действия не выполняются.

2.10. Смена знака числа осуществляется 2 способами: нажатие на "-" прежде чем введены какие-то
числа, нажатие на кнопку Negate. При нажатии на Negate вызывается функция negate(), который
проверяет состояние приложения. Разные действия выполняются если:
1. В данный момент в строке stringInput находится первое введенное число, у которого знак минус был
добавлен кнопкой "-".
2. Если не была введена ни одна цифра либо последним действием нажат арифметический оператор - тогда
появляется сообщение, что нужно ввести какое-либо число.
3. Если в данный момент в строке string Input находится число, которое является результатом
предыдущих вычислений. Тогда вся предыдущая история операций обнуляется.
4. "Нормальный" случай, когда число введено и нажата кнопка Negate.

 * */

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalcViewModel : ViewModel() {

    private val _journal = MutableLiveData<String>()
    val journal: LiveData<String>
        get() = _journal

    private val _showInput = MutableLiveData<String>()
    val showInput: LiveData<String>
        get() = _showInput

    private val _stringInput = MutableLiveData<String>()
    private val _operator = MutableLiveData<Char>()
    private val _num1 = MutableLiveData<Double>()
    private val _num2 = MutableLiveData<Double>()
    private val _hasNum1 = MutableLiveData<Boolean>()
    private val _isLastPressedOperation = MutableLiveData<Boolean>()
    private val _isBsAvailable = MutableLiveData<Boolean>()


    private val _showToast1 = MutableLiveData<Boolean>()
    val showToast1: LiveData<Boolean>
        get() = _showToast1

    private val _showToast2 = MutableLiveData<Boolean>()
    val showToast2: LiveData<Boolean>
        get() = _showToast2


    init {
        _journal.value = ""
        _stringInput.value = ""
        _showInput.value = ""
        _operator.value = '0'
        _num1.value = 0.0
        _num2.value = 0.0
        _hasNum1.value = false
        _isLastPressedOperation.value = false
        _isBsAvailable.value = false
        _showToast1.value = false
        _showToast2.value = false
    }

    /*метод, вызываемый при нажатии кнопок с цифрой*/
    fun enterDigit(n: Int) {
        /*если первая введенная цифра в числе 0, то убираем его*/
        if (_stringInput.value == "0" && _journal.value != "") {
            _stringInput.value = ""
            _showInput.value = _stringInput.value
            _journal.value = _journal.value?.dropLast(1)
        }
        _stringInput.value += n
        _showInput.value = _stringInput.value
        _journal.value += n
        _isLastPressedOperation.value = false
        _isBsAvailable.value = true
    }

    /*кнопка точка*/
    fun dot() {
        _journal.value += "."
        _stringInput.value += "." //записывается в строке для инпута
        _showInput.value = _stringInput.value
        _isLastPressedOperation.value = false
        _isBsAvailable.value = true
    }

    /*кнопка С*/
    fun clear() {
        _num2.value = 0.0
        _num1.value = 0.0
        _hasNum1.value = false
        _isLastPressedOperation.value = false
        _isBsAvailable.value = false
        _stringInput.value = ""
        _showInput.value = ""
        _journal.value = ""
        _operator.value = '0'
    }

    fun add() {
        mainOperation('+')
        _isLastPressedOperation.value = true
        _isBsAvailable.value = false
    }

    fun mult() {
        mainOperation('*')
        _isLastPressedOperation.value = true
        _isBsAvailable.value = false
    }

    fun sub() {
        /*если нажать минус перед вводом первого числа, то число будет отрицательным*/
        if (_hasNum1.value == false && _stringInput.value == "") {
            _stringInput.value += "-"
            _showInput.value = _stringInput.value
            _journal.value = "-" + "\u200b"
            _isLastPressedOperation.value = false
            _isBsAvailable.value = true

        } else {
            mainOperation('-')
            _isLastPressedOperation.value = true
            _isBsAvailable.value = false
        }
    }

    fun div() {
        mainOperation('/')
        _isLastPressedOperation.value = true
        _isBsAvailable.value = false
    }

    /* кнопка равно */
    fun calc() {
        if (_hasNum1.value == false && _stringInput.value == "") {
            _showToast1.value = true //если еще ничего не введено

        } else if (_hasNum1.value == false || _stringInput.value == "") {
            _showToast2.value = true //если нет _num1, либо нет правого операнда

        } else {
            operationCalc()
            _operator.value = '0'
            _isLastPressedOperation.value = false
            _isBsAvailable.value = false
        }
    }

    /* Методы для расчета */
    private fun mainOperation(sign: Char) {

        if (_hasNum1.value == false
            && _stringInput.value == ""
        ) {
            _showToast1.value = true

            /*если в строке _sInput записано число, но оператор и _num1 еще не записан*/
        } else if (_hasNum1.value == false) {

            _operator.value = sign //записываем в текущий оператор
            _journal.value = _journal.value + "\u200b" + _operator.value + "\u200b"
            _num1.value = _stringInput.value?.toDouble() //записываем первое введенное число в _num1
            _hasNum1.value = true // _num1 теперь не пустое
            _showInput.value = _stringInput.value
            _stringInput.value = ""

            /*последующие операции после первой*/
        } else if (_stringInput.value == ""

            && _operator.value == '0'
            && _isLastPressedOperation.value == false
        ) {
            _operator.value = sign //записываем в текущий оператор
            _journal.value = _journal.value + "\u200b" + _operator.value + "\u200b"

            /*если в строке _sInput записано  число, есть _num1 и оператор, то срабатывает
            как кнопка = */
        } else if (_stringInput.value != ""
            && _operator.value != '0'
            && _isLastPressedOperation.value == false
        ) {
            operationCalc()
            _operator.value = sign //записываем в текущий оператор
            _journal.value = _journal.value + "\u200b" + _operator.value + "\u200b"

            /*если перед этим была нажата кнопка другой арифметической операции, заменяем operator*/
        } else if (_isLastPressedOperation.value == true) {

            _operator.value = sign //записываем в текущий оператор
            _journal.value =
                _journal.value?.dropLast(3)  // удаляем предыдущий оператор из истории
            _journal.value =
                _journal.value + "\u200b" + _operator.value + "\u200b"//записываем новый оператор
        }
    }

    private fun operationCalc() {

        _num2.value = _stringInput.value?.toDouble() // получаем введенное число

        when (_operator.value) {//производим вычисление и записываем результат в _num1
            '+' -> _num1.value = _num1.value?.plus(_num2.value!!)
            '-' -> _num1.value = _num1.value?.minus(_num2.value!!)
            '*' -> _num1.value = _num1.value?.times(_num2.value!!)
            '/' -> _num1.value = _num1.value?.div(_num2.value!!)
        }

        output(_num1.value)

        /*обнуляем _num2, чтобы потом получить новое значение во время следующей операции*/
        _num2.value = 0.0
        _stringInput.value = "" //очищаем строку для следующей операции
    }

    /*выводим результат в _showInput, обрезаем .0, если есть*/
    private fun output(d: Double?) {

        var sD = d.toString()
        if (sD.endsWith(".0")) {
            val x = sD.indexOf(".")
            sD = sD.substring(0, x)
        }
        _stringInput.value = sD
        _showInput.value = _stringInput.value
    }


    /*прочие мат. операции*/
    fun percent() {

        /*если не введено ни одно число и не нажата ни одна арифметическая операция*/
        if (_hasNum1.value == false && _stringInput.value == "") {

            _showToast1.value = true //выводим подсказку


            /*если введено только 1 число, а кнопка арифметической операции не нажата, то
            рассчитать 1 процент*/
        } else if (_hasNum1.value == false) {

            _num1.value = _stringInput.value?.toDouble()
            _journal.value =
                _journal.value + "\u200b * 1% \u200b" //записываем в историю, что мы рассчитали 1%
            _num2.value = _num1.value?.div(100)
            output(_num2.value) //обрезаем ноль, если нужно, и выводим в строке showInput
            _isLastPressedOperation.value = false
            _isBsAvailable.value = false
            _stringInput.value = _num2.value.toString() //передаем результат в _stringInput.value
            _num1.value = _num2.value //передаем в _num1.value
            _num2.value = 0.0 //обнуляем _num2.value


            /*если есть _num1.value и оператор, но num2 нет - то выводим подсказку*/
        } else if (_stringInput.value == ""
            && _isLastPressedOperation.value == true
        ) {
            _showToast2.value = true


            /*если заданы 2 операнда и арифметическая операция в operator - рассчитываем
            _num2.value процентов от _num1.value*/
        } else if (_stringInput.value != ""
            && _operator.value != '0'
            && _isLastPressedOperation.value == false

        ) {
            /*сначала вычисляем _num2.value процентов от _num1.value и перезаписываем _num2.value*/
            _num2.value = _stringInput.value?.toDouble()
            _num2.value = _num2.value!! * _num1.value!!.div(100)
            _stringInput.value = _num2.value.toString()
            operationCalc() //выполняем заданную арифм. операцию
            _journal.value =
                journal.value + "% \u200b" //записываем в строку журнала знак % и пробел
            _isLastPressedOperation.value = false
            _isBsAvailable.value = false
            _operator.value = '0'
            _num2.value = 0.0
        }
    }

    fun backspace() {

        /*учитываем, что может в строке stringInput может находиться отрицательное число,
        тогда перед числом стоит знак пробел и знак минус*/

        /*если в строке _stringInput больше 3 символов, то неважно, есть ли минус,
        просто удаляем последнюю цифру*/

        /*вычисляем длину строки*/
        val length = _stringInput.value?.length ?: -1
        val secondChar = _stringInput.value?.get(1) ?: '1'

        if (_isBsAvailable.value == true && length > 3

            /*либо если в строке осталось 2 символа, то минуса точно нет*/
            || _isBsAvailable.value == true && length == 2
        ) {
            _stringInput.value = _stringInput.value?.dropLast(1)
            _journal.value = _journal.value?.dropLast(1)


            /*если в строке 3 символа и второй - не минус, то последний символ можно стирать*/
        } else if (_isBsAvailable.value == true
            && length == 3
            && secondChar != '-'
        ) {
            _stringInput.value = _stringInput.value?.dropLast(1)
            _journal.value = _journal.value?.dropLast(1)


            /*если в строке 3 символа и второй - минус, то нужно стирать все 3 символа:
            цифру, минус, пробел*/
        } else if (_isBsAvailable.value == true
            && length == 3
            && secondChar != '-'
        ) {
            _stringInput.value = "0"
            _journal.value = _journal.value?.dropLast(3)
            _isBsAvailable.value = false


            /*если в строке осталась только 1 цифра, заменяем ее на ноль*/
        } else if (_isBsAvailable.value == true
            && length == 1
        ) {
            _stringInput.value = "0"
            _journal.value = _journal.value?.dropLast(1)
            _isBsAvailable.value = false
        }
        _showInput.value = _stringInput.value
    }


    fun negate() {

        /*если еще не была введена ни одна цифра*/
        if (_hasNum1.value == false
            && _stringInput.value == ""
        ) {
            _showToast1.value = true //выводим подсказку

            /*если строка не пустая, то получаем первый символ*/

            val firstChar = _stringInput.value?.get(0) ?: 'E'


            /*если нажат арифметический оператор, а новое число не введено*/
            if (_isLastPressedOperation.value == true) {
                _showToast2.value = true


                /*если Negate нажимают когда ввели первое число отрицательное кнопкой минус*/
            } else if (_hasNum1.value == false
                && firstChar == '-'
            ) {
                _stringInput.value =
                    _stringInput.value?.drop(1) //обрезаем минус в строке _stringInput.value
                _showInput.value = _stringInput.value
                _journal.value = _stringInput.value //записываем результат в journal


                /*если Negate нажимают когда ввели первое число положительное*/
            } else if (_hasNum1.value == false
                && firstChar != '-'
            ) {
                _stringInput.value =
                    "-" + _stringInput.value //добавляем минус в строке _stringInput.value
                _showInput.value = _stringInput.value
                _journal.value = "\u200b" + _stringInput.value //записываем результат в journal


                /*если введенное в строке число - результат предыдущих вычислений*/
            } else if (_hasNum1.value == true
                && _stringInput.value == ""
                && _showInput.value != ""
            ) {
                _stringInput.value = _showInput.value //получаем значение из текстового поля

                /*т.к. цепочка вычислений перезатирается, стираем историю предыдущих операций
                в journal*/
                _journal.value = ""

                /*если в результате предыдущих операций получили отриц. число*/
                if (firstChar == '-') {
                    _stringInput.value =
                        _stringInput.value?.drop(1) //обрезаем минус в строке _stringInput.value

                } else if (firstChar != '-') {
                    _stringInput.value =
                        "-" + _stringInput.value //добавляем минус в строке _stringInput.value
                }
                _showInput.value = _stringInput.value
                _journal.value = _stringInput.value //записываем результат в journal
                _operator.value = '0'
                _hasNum1.value = false


                /*если Negate нажат после числа по ходу вычислений*/
                /*проверяем, что сейчас в строке положительное число, тогда добавляем минус*/
            } else if (firstChar != '-'
                && _operator.value != '0'
            ) {
                /*получаем длину числа в строке, чтобы обрезать его из истории*/
                val x = _stringInput.value?.length ?: -1
                _journal.value = _journal.value?.dropLast(x) //обрезаем
                _stringInput.value =
                    "-" + _stringInput.value //добавляем минус в строке _stringInput.value

                /*и вставляем снова в journal вместе с пробелом и добавляем скобки*/
                _journal.value = _journal.value.plus("\u200b(")
                    .plus(_stringInput.value).plus(")")


                /*проверяем, что сейчас в строке отрицательное число, тогда убираем минус*/
            } else if (firstChar == '-'
                && _operator.value != '0'
            ) {
                /*получаем длину числа в строке, чтобы обрезать его из истории*/
                val y = _stringInput.value?.length ?: -1
                _journal.value = _journal.value?.dropLast(y) //обрезаем
                _stringInput.value =
                    _stringInput.value?.drop(1) //обрезаем минус в строке _stringInput.value
            }

            _showInput.value = _stringInput.value //в конце выводим _stringInput.value в _showInput
            _isLastPressedOperation.value = false
            _isBsAvailable.value = false
        }
    }

    /*event listeners для toast*/
    fun onToast1ShownComplete() {
        _showToast1.value = false
    }

    fun onToast2ShownComplete() {
        _showToast2.value = false
    }
}
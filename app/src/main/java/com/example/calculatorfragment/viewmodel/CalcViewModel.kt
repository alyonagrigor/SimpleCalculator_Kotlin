package com.example.calculatorfragment.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalcViewModel : ViewModel() {
    private val _sHistory = MutableLiveData<String>()
    val sHistory: LiveData<String>
        get() = _sHistory

    private val _stringInput = MutableLiveData<String>()
    val stringInput: LiveData<String>
        get() = _stringInput

    private val _showInput = MutableLiveData<String>()
    val showInput: LiveData<String>
        get() = _showInput

    private val _operator = MutableLiveData<Char>()
    val operator: LiveData<Char>
        get() = _operator

    private val _num1 = MutableLiveData<Double>()
    val num1: LiveData<Double>
        get() = _num1

    private val _num2 = MutableLiveData<Double>()
    val num2: LiveData<Double>
        get() = _num2

    private val _hasNum1 = MutableLiveData<Boolean>()
    val hasNum1: LiveData<Boolean>
        get() = _hasNum1

    private val _isLastPressedOperation = MutableLiveData<Boolean>()
    val isLastPressedOperation: LiveData<Boolean>
        get() = _isLastPressedOperation

    private val _isBSAvailable = MutableLiveData<Boolean>()
    val isBSAvailable: LiveData<Boolean>
        get() = _isBSAvailable

    private val _showToast1 = MutableLiveData<Boolean>()
    val showToast1: LiveData<Boolean>
        get() = _showToast1

    private val _showToast2 = MutableLiveData<Boolean>()
    val showToast2: LiveData<Boolean>
        get() = _showToast2


    init {
        _sHistory.value = ""
        _stringInput.value = ""
        _showInput.value = ""
        _operator.value = '0'
        _num1.value = 0.0
        _num2.value = 0.0
        _hasNum1.value = false
        _isLastPressedOperation.value = false
        _isBSAvailable.value = false
        _showToast1.value = false
        _showToast2.value = false
    }

    /*метод, вызываемый при нажатии кнопок с цифрой*/
    fun enterDigit(n: Int) {
        //если первая введенная цифра в числе 0, то убираем его
        if (_stringInput.value == "0" && _sHistory.value != "") {
            _stringInput.value = ""
            _showInput.value = _stringInput.value
            _sHistory.value = _sHistory.value?.dropLast(1)
        }
        _stringInput.value += n
        _showInput.value = _stringInput.value
        _sHistory.value += n
        _isLastPressedOperation.value = false
        _isBSAvailable.value = true
    }

    /*кнопка точка*/
    fun dot() {
        _sHistory.value += "."
        _stringInput.value += "." //записывается в строке для инпута
        _showInput.value = _stringInput.value
        _isLastPressedOperation.value = false
        _isBSAvailable.value = true
    }

    /*кнопка С*/
    fun clear() {
        _num2.value = 0.0
        _num1.value = 0.0
        _hasNum1.value = false
        _isLastPressedOperation.value = false
        _isBSAvailable.value = false
        _stringInput.value = ""
        _showInput.value = ""
        _sHistory.value = ""
        _operator.value = '0'
    }

    fun add() {
        mainOperation('+')
        _isLastPressedOperation.value = true
        _isBSAvailable.value = false
    }

    fun mult() {
        mainOperation('*')
        _isLastPressedOperation.value = true
        _isBSAvailable.value = false
    }

    fun sub() {
        //если нажать минус перед вводом первого числа, то число будет отрицательным
        if (_hasNum1.value == false && _stringInput.value == "") {
            _stringInput.value += "-"
            _showInput.value = _stringInput.value
            _sHistory.value = "-" + "\u200b"
            _isLastPressedOperation.value = false
            _isBSAvailable.value = true
        } else {
            mainOperation('-')
            _isLastPressedOperation.value = true
            _isBSAvailable.value = false
        }
    }

    fun div() {
        mainOperation('/')
        _isLastPressedOperation.value = true
        _isBSAvailable.value = false
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
            _isBSAvailable.value = false
        }
    }

    /* Методы для расчета */
    private fun mainOperation(sign: Char) {
        Log.d("myTag", "вызвана функция Mainoperation")
        if (_hasNum1.value == false
            && _stringInput.value == ""
        ) {
            _showToast1.value = true
        } else if (_hasNum1.value == false) {
            Log.d("myTag", "запущен вариант 2")
            //если в строке _sInput записано число, но оператор и _num1 еще не записан
            _operator.value = sign //записываем в текущий оператор
            _sHistory.value = _sHistory.value + "\u200b" + _operator.value + "\u200b"
            _num1.value = _stringInput.value?.toDouble() //записываем первое введенное число в _num1
            _hasNum1.value = true // _num1 теперь не пустое
            _showInput.value = _stringInput.value
            _stringInput.value = ""

        } else if (_stringInput.value == ""
            && _operator.value == '0'
            && _isLastPressedOperation.value == false
        ) {
            //последующие операции после первой
            _operator.value = sign //записываем в текущий оператор
            _sHistory.value = _sHistory.value + "\u200b" + _operator.value + "\u200b"

        } else if (_stringInput.value != ""
            && _operator.value != '0'
            && _isLastPressedOperation.value == false
        ) {
            //если в строке _sInput записано  число, есть _num1 и оператор, то срабатывает как кнопка =
            operationCalc()
            _operator.value = sign //записываем в текущий оператор
            _sHistory.value = _sHistory.value + "\u200b" + _operator.value + "\u200b"

        } else if (_isLastPressedOperation.value == true) { //если был только что нажат другой оператор
            _operator.value = sign //записываем в текущий оператор
            _sHistory.value =
                _sHistory.value?.dropLast(3)  // удаляем предыдущий оператор из истории
            _sHistory.value =
                _sHistory.value + "\u200b" + _operator.value + "\u200b"//записываем новый оператор
        }
    }

    private fun operationCalc() {
        Log.d("myTag", "вызвана функция operationCalc()")
        Log.d("myTag", "num1 = $_num1.value")

        _num2.value = _stringInput.value?.toDouble() // получаем введенное число
        Log.d("myTag", "num1 = $_num2.value")

        when (_operator.value) {//производим вычисление и записываем результат в _num1
            '+' -> _num1.value = _num1.value?.plus(_num2.value!!)
            '-' -> _num1.value = _num1.value?.minus(_num2.value!!)
            '*' -> _num1.value = _num1.value?.times(_num2.value!!)
            '/' -> _num1.value = _num1.value?.div(_num2.value!!)
        }
        Log.d("myTag", "num1 = $_operator.value")
        cutZeroOutput(_num1.value!!)
        _num2.value =
            0.0 //обнуляем _num2, чтобы потом получить новое значение во время следующей операции
        _stringInput.value = "" //очищаем строку дял следующей операции
    }

    /*обрезаем .0 при выводе в input*/
    private fun cutZeroOutput(d: Double) {

        var sD = d.toString()
        if (sD.endsWith(".0")) {
            val x = sD.indexOf(".")
            sD = sD.substring(0, x)
        }
        _stringInput.value = sD
        _showInput.value = _stringInput.value //_num2 отображаем в строке input
    }


    /*прочие мат. операции*/
    fun percent() {

        if (_hasNum1.value == false && _stringInput.value == "") {
            _showToast1.value = true // если не введено ни одно число и не нажата ни одна
            // арифметическая операция

        } else if (_hasNum1.value == false) { //если введено только 1 число, а кнопка арифметической операции
            // не нажата, то рассчитать 1 процент
            _num1.value = _stringInput.value!!.toDouble()
            _sHistory.value =
                _sHistory.value + "\u200b * 1% \u200b" //записываем в историю, что мы рассчитали 1%
            _num2.value = _num1.value!!.div(100)
            cutZeroOutput(_num2.value!!) //обрезаем ноль, если нужно, и выводим в строке input
            _isLastPressedOperation.value = false
            _isBSAvailable.value = false
            _stringInput.value = _num2.value.toString()
            //передаем результат в _stringInput.value и _num1.value и обнуляем _num2.value
            _num1.value = _num2.value
            _num2.value = 0.0

        } else if (_stringInput.value == "" && _isLastPressedOperation.value == true) {
            //если есть _num1.value и оператор, но правого операнда нет - то выводим подсказку
            _showToast2.value = true

        } else if (_stringInput.value != ""
            && _operator.value != '0'
            && _isLastPressedOperation.value == false
        ) {
            //если заданы 2 операнда и арифметическая операция в operator - рассчитываем
            // _num2.value процентов от _num1.value
            _num2.value = _stringInput.value!!.toDouble()
            //сначала вычисляем _num2.value процентов от _num1.value и перезаписываем _num2.value
            _num2.value = _num2.value!! * _num1.value!!.div(100)
            _stringInput.value = _num2.value.toString()
            operationCalc() //выполняем заданную арифм. операцию
            _sHistory.value = sHistory.value + "% \u200b" //записываем в строку памяти знак процента
            // и пробел
            _isLastPressedOperation.value = false
            _isBSAvailable.value = false
            _operator.value = '0'
            _num2.value = 0.0
        }
    }

    fun backspace() {

        /*учитываем, что может в строке stringInput может находиться отрицательное число,
        * тогда перед числом стоит знак пробел и знак минус*/
        if (_isBSAvailable.value == true && _stringInput.value!!.length > 3 //если больше 3 символов,
            //то неважно, есть ли минус, просто удаляем последнюю цифру
            || _isBSAvailable.value == true && _stringInput.value!!.length == 2
        ) {
            //если в строке осталось 2 символа, то минуса точно нет
            _stringInput.value = _stringInput.value!!.dropLast(1)
            _sHistory.value = _sHistory.value!!.dropLast(1)

        } else if (_isBSAvailable.value == true
            && _stringInput.value!!.length == 3
            && _stringInput.value!!.get(1) != '-'
        ) {
            //если в строке 3 символа и второй - не минус, то можно стирать последний
            //символ безопасно
            _stringInput.value = _stringInput.value!!.dropLast(1)
            _sHistory.value = _sHistory.value!!.dropLast(1)

        } else if (_isBSAvailable.value == true
            && _stringInput.value!!.length == 3
            && _stringInput.value!!.get(1) != '-'
        ) {
            //если в строке 3 символа и второй - минус, то можно стирать все 3 символа:
            //цифру, минус, пробел
            _stringInput.value = "0"
            _sHistory.value = _sHistory.value!!.dropLast(3)
            _isBSAvailable.value = false

        } else if (_isBSAvailable.value == true
            && _stringInput.value!!.length == 1
        ) {
            //если в строке осталась только 1 цифра, заменяем ее на ноль
            _stringInput.value = "0"
            _sHistory.value = _sHistory.value!!.dropLast(1)
            _isBSAvailable.value = false
        }
        _showInput.value = _stringInput.value
    }


    fun negate() {
        if (_hasNum1.value == false
            && _stringInput.value == ""
        ) { //если еще не была введена ни одна цифра

            _showToast1.value = true

        } else if (_isLastPressedOperation.value == true) {
            //если нажат арифметический оператор, а новое число не введено
            _showToast2.value = true

        } else if (_hasNum1.value == false
            && _stringInput.value!!.get(0) == '-'
        ) {
            //если Negate нажимают когда ввели первое число отрицательное кнопкой минус

            _stringInput.value =
                _stringInput.value!!.drop(1) //обрезаем минус в строке _stringInput.value
            _showInput.value = _stringInput.value
            _sHistory.value = _stringInput.value //записываем результат в sHistory

        } else if (_hasNum1.value == false
            && _stringInput.value!!.get(0) != '-'
        ) {
            //если Negate нажимают когда ввели первое число положительное

            _stringInput.value =
                "-" + _stringInput.value //добавляем минус в строке _stringInput.value
            _showInput.value = _stringInput.value
            _sHistory.value = "\u200b" + _stringInput.value //записываем результат в sHistory

        } else if (_hasNum1.value == true
            && _stringInput.value == ""
            && _showInput.value != ""
        ) {
            //если введенное в строке число - результат предыдущих вычислений

            _stringInput.value = _showInput.value //получаем значение из текстового поля
            //т.к. цепочка вычислений перезатирается, стираем историю предыдущих операций в sHistory
            _sHistory.value = ""
            if (_stringInput.value!!.get(0) == '-') { //если получили отриц. число в
                //результате предыдущих операций
                _stringInput.value =
                    _stringInput.value!!.drop(1) //обрезаем минус в строке _stringInput.value
            } else if (_stringInput.value!!.get(0) != '-') {
                _stringInput.value =
                    "-" + _stringInput.value //добавляем минус в строке _stringInput.value
            }
            _showInput.value = _stringInput.value
            _sHistory.value = _stringInput.value //записываем результат в sHistory
            _operator.value = '0'
            _hasNum1.value = false

            /*стандартные случаи, когда Negate нажат после числа по ходу вычислений*/
        } else if (_stringInput.value!!.get(0) != '-'
            && _operator.value != '0'
        ) {
            //проверка, что сейчас введено положительное число, тогда добавляем минус
            val x =
                _stringInput.value!!.length //получаем длину числа в строке, чтобы обрезать его из истории
            _sHistory.value = _sHistory.value!!.dropLast(x) //обрезаем
            _stringInput.value =
                "-$_stringInput.value" //добавляем минус в строке _stringInput.value
            _sHistory.value =
                _sHistory.value.plus("\u200b").plus("($_stringInput.value)")
            //и вставляем снова в sHistory вместе с пробелом и добавляем скобки

        } else if (_stringInput.value!!.get(0) == '-' && _operator.value != '0') {
            //проверка, что сейчас введено отрицательное число, тогда убираем минус
            val x =
                _stringInput.value!!.length //получаем длину числа в строке, чтобы обрезать его из истории
            _sHistory.value = _sHistory.value!!.dropLast(x) //обрезаем

            _stringInput.value =
                _stringInput.value!!.drop(1) //обрезаем минус в строке _stringInput.value
        }

        _showInput.value = _stringInput.value
        _isLastPressedOperation.value = false
        _isBSAvailable.value = false
    }

    fun onToast1ShownComplete() {
        _showToast1.value = false
    }

    fun onToast2ShownComplete() {
        _showToast2.value = false
    }


}
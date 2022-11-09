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
        Log.d("myTag","введено число $n")
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
          //TODO  showToastFirstDigit() //если еще ничего не введено
        } else if (_hasNum1.value == false || _stringInput.value == "") {
            //TODO showToastNextDigit() //если нет _num1, либо нет правого операнда
        } else {
            operationCalc()
            _operator.value = '0'
            _isLastPressedOperation.value = false
            _isBSAvailable.value = false
        }
    }

    /* Методы для расчета */
    private fun mainOperation(sign: Char) {
        Log.d("myTag","вызвана функция Mainoperation")
        if (_hasNum1.value == false
            && _stringInput.value == "") {
            //TODO  showToastFirstDigit()
        } else if (_hasNum1.value == false) {
            Log.d("myTag","запущен вариант 2")
            //если в строке _sInput записано число, но оператор и _num1 еще не записан
            _operator.value = sign //записываем в текущий оператор
            _sHistory.value = _sHistory.value  + "\u200b" + _operator.value + "\u200b"
            _num1.value = _stringInput.value?.toDouble() //записываем первое введенное число в _num1
            _hasNum1.value = true // _num1 теперь не пустое
            _showInput.value = _stringInput.value
            _stringInput.value = ""

        } else if (_stringInput.value == ""
            && _operator.value == '0'
            && _isLastPressedOperation.value == false) {
            //последующие операции после первой
            _operator.value = sign //записываем в текущий оператор
            _sHistory.value = _sHistory.value  + "\u200b" + _operator.value + "\u200b"

        } else if (_stringInput.value != ""
            && _operator.value != '0'
            && _isLastPressedOperation.value == false) {
            //если в строке _sInput записано  число, есть _num1 и оператор, то срабатывает как кнопка =
            operationCalc()
            _operator.value = sign //записываем в текущий оператор
            _sHistory.value = _sHistory.value  + "\u200b" + _operator.value + "\u200b"

        } else if (_isLastPressedOperation.value == true) { //если был только что нажат другой оператор
            _operator.value = sign //записываем в текущий оператор
            _sHistory.value = _sHistory.value?.dropLast(3)  // удаляем предыдущий оператор из истории
            _sHistory.value = _sHistory.value  + "\u200b" + _operator.value + "\u200b"//записываем новыйй оператор
        }
    }

    private fun operationCalc() {
        Log.d("myTag","вызвана функция operationCalc()")
        Log.d("myTag","num1 = $_num1.value")

        _num2.value = _stringInput.value?.toDouble() // получаем введенное число
        Log.d("myTag","num1 = $_num2.value")

        when (_operator.value) {//производим вычисление и записываем результат в _num1
            '+' -> _num1.value = _num1.value?.plus(_num2.value!!)
            '-' -> _num1.value = _num1.value?.minus(_num2.value!!)
            '*' -> _num1.value = _num1.value?.times(_num2.value!!)
            '/' -> _num1.value = _num1.value?.div(_num2.value!!)
        }
        Log.d("myTag","num1 = $_operator.value")
        cutZeroOutput(_num1.value!!)
        _num2.value = 0.0 //обнуляем _num2, чтобы потом получить новое значение во время следующей операции
        _stringInput.value = "" //очищаем строку дял следующей операции
    }

    private fun cutZeroOutput(d: Double) {

        var sD = d.toString() // обрезаем .0 при выводе в input
        if (sD.endsWith(".0")) {
            val x = sD.indexOf(".")
            sD = sD.substring(0, x)
        }
        _stringInput.value = sD
        _showInput.value = _stringInput.value //_num2 отображаем в строке input
    }

}
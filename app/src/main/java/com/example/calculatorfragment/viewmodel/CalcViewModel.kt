package com.example.calculatorfragment.viewmodel

import android.util.Log
import android.util.Log.INFO
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.logging.Level.INFO

class CalcViewModel : ViewModel() {
    private val _sHistory = MutableLiveData<String>()
    val sHistory : LiveData<String>
        get() = _sHistory

    private val _sInput = MutableLiveData<String>()
    val sInput : LiveData<String>
        get() = _sInput

    private val _operator = MutableLiveData<Char>()
    val operator : LiveData<Char>
        get() = _operator

    private val _num1 = MutableLiveData<Double>()
    val num1 : LiveData<Double>
        get() = _num1

    private val _num2 = MutableLiveData<Double>()
    val num2 : LiveData<Double>
        get() = _num2

    private val _hasNum1 = MutableLiveData<Boolean>()
    val hasNum1 : LiveData<Boolean>
        get() = _hasNum1

    private val _isLastPressedOperation = MutableLiveData<Boolean>()
    val isLastPressedOperation : LiveData<Boolean>
        get() = _isLastPressedOperation

    private val _isBSAvailable = MutableLiveData<Boolean>()
    val isBSAvailable : LiveData<Boolean>
        get() = _isBSAvailable

    private val _showToast1 = MutableLiveData<Boolean>()
    val showToast1 : LiveData<Boolean>
        get() = _showToast1

    private val _showToast2 = MutableLiveData<Boolean>()
    val showToast2 : LiveData<Boolean>
        get() = _showToast2


    init {
        _sHistory.value = ""
        Log.d("Mylog", "_sHistory равно ${_sHistory.value}")
        _sInput.value = ""
        _operator.value = '0'
        _num1.value = 0.0
        _num2.value = 0.0
        _hasNum1.value = false
        _isLastPressedOperation.value = false
        _isBSAvailable.value = false
    }

    /*метод, вызываемый при нажатии кнопки с цифрой*/
    fun enterDigit(n: Int) {
        //если первая введенная цифра в числе 0, то убираем его
        if (_sInput.value == "0" && _sHistory.value != "") {
            _sInput.value = ""
            _sHistory.value = _sHistory.value?.dropLast(1)
        }
        _sInput.value += n
        _sHistory.value += n
        Log.d("Mylog", "_sHistory равно ${_sHistory.value}")
        _isLastPressedOperation.value = false
        _isBSAvailable.value = true
    }

    /*кнопка точка*/
    fun dot() {
        _sHistory.value += "."
        _sInput.value  += "." //записывается в строке для инпута
        Log.d("Mylog", "_sHistory равно ${_sHistory.value}")
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
        _sInput.value = ""
        _sHistory.value = ""
        Log.d("Mylog", "_sHistory равно ${_sHistory.value}")
        _operator.value = '0'
    }

    override fun onCleared() {
        super.onCleared()
    }
}
package com.example.calculatorfragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    }

    override fun onCleared() {
        super.onCleared()
    }
}
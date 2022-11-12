package com.example.calculatorfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.calculatorfragment.databinding.FragmentButtonBinding
import com.example.calculatorfragment.viewmodel.CalcViewModel

/**

ДОКУМЕНТАЦИЯ
1. Структура приложения.
Приложение состоит из одной активити и двух фрагментов: ButtonFragment и JournalFragment. В горизонтальной
ориентации JournalFragment находится слева и занимает примерно половину экрана, в портретной
ориентации находится сверху. Фрагменты взаимодействуют с помощью интерфейса
OnFragmentSendDataListener.
Приложение содержит отдельные layout для портретной и альбомной ориентации, а также dimens для
планшетов sw600dp и sw720dp.
В файле styles.xml заданы стили для кнопок.

2. Функционал приложения.
2.1. Для хранения информации о состоянии приложения используются переменные:
String sInput - используется для хранения числа, которое в данный момент вводит пользователь. Может
содержать только цифры или минус для отрицательного числа. Одновременно нажимаемые пользователем
цифры выводятся в текстовое поле EditText etInput.
Double num1 - первое введенное число в цепочке операций или результат предыдущих операций, то
есть левый операнд.
Double num2 - число, считанное из строки sInput, то есть правый операнд, введенный
в поле EditText etInput.
Переменная char operator используется для хранения последней заданной пользователем операции
(только 4 арифметические операции в виде '+').
Boolean hasNum1 - изначально false, присваивается true, в момент нажатия кнопки с арифметической
операцией, если пользователь уже ввел одно число.
Boolean isLastPressedOperation - принимает значение true, если последняя нажатая кнопка - одна из
четырех основных арифметических операций.
Boolean isBSAvailable - используется для работы Backspace. Записывается как true, если была нажата
 кнопка с числом или точкой, и перезаписывается как false, если была нажата любая другая кнопка.
StringBuilder sbHistory - используется для хранения и вывода истории, включая цифры, арифметические
 действия, проценты и минус для отрицательного числа. При сохранении состояния преобразуется в
 String sHistory.

2.2. При нажатии кнопок с цифрами вызывается метод enterDigit(), который принимает аргумент типа
Integer - собственно цифра, которую нажал пользователь. В методе происходит:
1. Цифра записывается в строку истории sbHistory.
2. Цифра записывается в строку для вывода sInput.
3. Цифра выводится в строке для вывода.
4. Булевой переменной isLastPressedOperation присваивается значение false.

2.3. При нажатии кнопки "Точка" btnDot происходят 4 действия, аналогичные п. 2.2.

2.4. При нажатии кнопок с арифметическими операциями +, -, *, / слушатели вызывают метод
MainOperation(), в который передается аргумент - char operator, например, "+".
MainOperation() предусматривает 5 сценариев:
1. Если еще не было введено ни одной цифры, выдает подсказку "Введите первое число".
2. Если уже была введена одна цифра, то записывает введенный оператор для дальнейших действий.
3. Если уже прошло несколько операций, то записывает введенный оператор для дальнейших действий.
4. Если были введены 2 цифры и оператор до этого, то срабатывает как кнопка равно.
5. Если до этого была нажата другая кнопка с арифметической операцией, то перезаписывает operator,
заменяя на новую арифметическую операцию.

2.5. Метод operationCalc() получает аргумент - char sign из метода MainOperation(), а затем:
1. Считывает num2 из строки sInput.
2. Выполняет соответствующие арифметические операции с числами num1 и num2.
3. Полученное значение перезаписывается в num1 и выводится на экран, num2 обнуляется.
4. Метод output() выводит число в поле EditText input, а также удаляет .0 если получилось целое число.
5. При делении на ноль метод clearAll() обнуляет все переменные состояния, а также очищает историю.

2.6. Метод clearAll() также вызывается при нажатии на кнопку "С".

2.7. При нажатии кнопки "=" вызывается метод operationCalc(), после его выполнения:
1. Последнее введенное число записывается в sbHistory.
2. sbHistory отправляется во фрагмент JournalFragment.
3. Переменной operator присваивается значение '0'.
4. Значение isLastPressedOperation = false.

2.8. Кнопка "%" вызывает метод percentOperation(), которые проверяет состояние приложения и
запускает 1 из 4 сценариев.
1. Если еще не было введено ни одного числа, появляется Toast с просьбой ввести число.
2. Если было введено только 1 число и не нажата арифметическая операция, то рассчитывается 1% от
числа.
3. Если имеются 2 операнда и 1 оператор, то срабатывает стандартный функционал.
4. Если есть 1 операнд и нажата арифметическая операция, то появляется Toast с просьбой
ввести второе число.

2.9. Кнопка "Backspace" вызывает метод bSOperation(), который срабатывает при условии что
isBSAvailable  == true и запускает 1 из 4 сценариев:
1. Если в строке sInput > 2 символов, то последняя цифра удаляется из этой строки и из sbHistory.
2. Если в строке sInput 2 символа и первый из них не '-', то аналогично.
3. Если в строке sInput 1 символ, то она заменяется в строке sInput на ноль, удаляется из sbHistory
 и isLastPressedDigit присваивается false (т.к. больше удалять ничего).
4. Если в строке sInput одна цифра и минус, то заменяется в строке sInput на ноль, удаляется из
sbHistory и isBSAvailable присваивается false (т.к. больше удалять ничего).
Если последней нажатой кнопкой была арифметическая операция, =, % или negate, то isBSAvailable
присвоено значение false, и никакие действия не выполняются.

2.10. Смена знака числа осуществляется 2 способами: нажатие на "-" прежде чем введены какие-то
числа, нажатие на кнопку Negate. При нажатии на Negate вызывается метод negateOperation(), который
 проверяет состояние приложения. Разные действия выполняются если:
1. В данный момент в строке sInput находится первое введенное число, у которого знак минус был
добавлен кнопкой "-".
2. Если не была введена ни одна цифра либо последним действием нажат арифметический оператор - тогда
появляется сообщение, что нужно ввести какое-либо число.
3. Если в данный момент в строке sInput находится число, которое является результатом предыдущих
вычислений. Тогда вся предыдущая история операций обнуляется.
4. "Нормальный" случай, когда число введено и нажата кнопка Negate.

* */

class ButtonFragment: Fragment(R.layout.fragment_button) {

    private lateinit var binding: FragmentButtonBinding
    private lateinit var viewModel: CalcViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_button,
            container,
            false
        )

        viewModel = ViewModelProvider(requireActivity()).get(CalcViewModel::class.java)

        binding.calcViewModel = viewModel

        binding.setLifecycleOwner(this)

        viewModel.showToast1.observe(viewLifecycleOwner, Observer { doShow ->
            if (doShow) {
                showToastFirstDigit()
                viewModel.onToast1ShownComplete()
            }
        })

        viewModel.showToast2.observe(viewLifecycleOwner, Observer { doShow ->
            if (doShow) {
                showToastNextDigit()
                viewModel.onToast2ShownComplete()
            }
        })

        return binding.root
    }

    fun showToastFirstDigit() {
        Toast.makeText(activity, "Введите хотя бы одно число", Toast.LENGTH_LONG).show()
    }

    fun showToastNextDigit() {
        Toast.makeText(activity, "Введите следующее число", Toast.LENGTH_LONG).show()
    }


}
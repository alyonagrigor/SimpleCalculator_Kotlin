package com.example.calculatorfragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.calculatorfragment.databinding.FragmentButtonBinding
import java.lang.ClassCastException
import java.lang.StringBuilder

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
class ButtonFragment : Fragment(R.layout.fragment_button) {
    /* интерфейс для передачи данных в JournalFragment */
    internal interface OnFragmentSendDataListener {
        fun onSendData(data: StringBuilder?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSendDataListener = try {
            context as OnFragmentSendDataListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                context
                    .toString() + " должен реализовывать интерфейс OnFragmentInteractionListener"
            )
        }
    }

    private var _binding: FragmentButtonBinding? = null
    private val binding get() = _binding!!
    private var fragmentSendDataListener: OnFragmentSendDataListener? = null
    private var sbHistory = StringBuilder("")
    private var sHistory: String = ""
    private var sInput: String = ""
    private var operator = '0'
    private var num1 = 0.0
    private var num2 = 0.0
    private var hasNum1 = false
    private var isLastPressedOperation = false
    private var isBSAvailable = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentButtonBinding.inflate(inflater, container, false)
        val view: View = binding.root

        /* Восстановление состояния */
        if (savedInstanceState != null) {
            sHistory = savedInstanceState.getString(sHistoryKey).toString()
            sbHistory.delete(0, sbHistory.length)
            sbHistory.append(sHistory)
            operator = savedInstanceState.getChar(operatorKey)
            sInput = savedInstanceState.getString(sInputKey).toString()
            num1 = savedInstanceState.getDouble(num1Key)
            num2 = savedInstanceState.getDouble(num2Key)
            hasNum1 = savedInstanceState.getBoolean(hasNum1Key)
            isLastPressedOperation = savedInstanceState.getBoolean(isLastPressedOperationKey)
            isBSAvailable = savedInstanceState.getBoolean(isBSAvailableKey)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etInput.setText(sInput) //выводим пустые строки
        fragmentSendDataListener!!.onSendData(sbHistory)

        /* Слушатели для цифр */
        binding.btn1.setOnClickListener { enterDigit(1) }
        binding.btn2.setOnClickListener { enterDigit(2) }
        binding.btn3.setOnClickListener { enterDigit(3) }
        binding.btn4.setOnClickListener { enterDigit(4) }
        binding.btn5.setOnClickListener { enterDigit(5) }
        binding.btn6.setOnClickListener { enterDigit(6) }
        binding.btn7.setOnClickListener { enterDigit(7) }
        binding.btn8.setOnClickListener { enterDigit(8) }
        binding.btn9.setOnClickListener { enterDigit(9) }
        binding.btn0.setOnClickListener { enterDigit(0) }
        binding.btnDot.setOnClickListener {
            sbHistory = sbHistory.append(".")
            sInput = "$sInput." //записывается в строке для инпута
            binding.etInput.setText(sInput) //выводится на экран
            isLastPressedOperation = false
            isBSAvailable = true
        }

        /* Слушатели для кнопок действий */
        binding.btnClear.setOnClickListener { clearAll() }
        binding.btnNegate.setOnClickListener { negateOperation() }
        binding.btnBack.setOnClickListener { bSOperation() }
        binding.btnPercent.setOnClickListener { percentOperation() }
        binding.add.setOnClickListener {
            mainOperation('+')
            isLastPressedOperation = true
            isBSAvailable = false
        }
        binding.mult.setOnClickListener {
            mainOperation('*')
            isLastPressedOperation = true
            isBSAvailable = false
        }
        binding.sub.setOnClickListener {
            //если нажать минус перед вводом первого числа, то число будет отрицательным
            if (!hasNum1 && sInput == "") {
                sInput = "-"
                binding.etInput.setText(sInput)
                sbHistory = sbHistory.append("\u200b-")
                fragmentSendDataListener!!.onSendData(sbHistory)
                isLastPressedOperation = false
                isBSAvailable = true
            } else {
                mainOperation('-')
                isLastPressedOperation = true
                isBSAvailable = false
            }
        }
        binding.div.setOnClickListener {
            mainOperation('/')
            isLastPressedOperation = true
            isBSAvailable = false
        }

        /* кнопка равно */
        binding.calc.setOnClickListener {
            if (!hasNum1 && sInput == "") {
                showToastFirstDigit() //если еще ничего не введено
            } else if (!hasNum1 || sInput == "") {
                showToastNextDigit() //если нет num1, либо нет правого операнда
            } else {
                operationCalc()
                fragmentSendDataListener!!.onSendData(sbHistory)
                operator = '0'
                isLastPressedOperation = false
                isBSAvailable = false
            }
        }
    }

    /* Основные методы */
    private fun mainOperation(sign: Char) {
        if (!hasNum1 && sInput == "") {
            showToastFirstDigit()
        } else if (!hasNum1) {
            //если в строке sInput записано число, но оператор и num1 еще не записан
            operator = sign //записываем в текущий оператор
            sendOperatorToJournal()
            num1 = sInput.toDouble() //записываем первое введенное число в num1
            hasNum1 = true // num1 теперь не пустое
            sInput = "" // очищаем для ввода следующего операнда, но не отображаем
            binding.etInput.setText(sInput)
        } else if (sInput == "" && operator == '0' && !isLastPressedOperation) {
            //последующие операции после первой
            operator = sign //записываем в текущий оператор
            sendOperatorToJournal()
        } else if (sInput != "" && operator != '0' && !isLastPressedOperation) {
            //если в строке sInput записано  число, есть num1 и оператор, то срабатывает как кнопка =
            operationCalc()
            operator = sign //записываем в текущий оператор
            sendOperatorToJournal()
        } else if (isLastPressedOperation) { //если был только что нажат другой оператор
            operator = sign //записываем в текущий оператор
            sbHistory.delete(
                sbHistory.length - 3,
                sbHistory.length
            ) // удаляем предыдущий оператор из истории
            sendOperatorToJournal()
        }
    }

    private fun operationCalc() {
        num2 = sInput.toDouble() // получаем введенное число
        when (operator) {
            '+' -> num1 += num2
            '-' -> num1 -= num2
            '*' -> num1 *= num2
            '/' -> num1 /= num2
        }
        cutZeroOutput(num1)
        num2 = 0.0
        sInput = ""
    }

    /*метод, вызываемый при нажатии кнопки с цифрой*/
    private fun enterDigit(n: Int) {
        //если первая введенная цифра в числе 0, то убираем его
        if (sInput == "0" && sbHistory.toString() != "") {
            sInput = ""
            sbHistory.deleteCharAt(sbHistory.length - 1)
        }
        sInput += n
        sbHistory.append(n)
        binding.etInput.setText(sInput)
        isLastPressedOperation = false
        isBSAvailable = true
    }

    private fun clearAll() {
        binding.etInput.setText("")
        num2 = 0.0
        num1 = 0.0
        hasNum1 = false
        isLastPressedOperation = false
        isBSAvailable = false
        sInput = ""
        operator = '0'
        sbHistory.delete(0, sbHistory.length)
        sHistory = ""
        fragmentSendDataListener!!.onSendData(sbHistory)
    }

    private fun negateOperation() {
        if (!hasNum1 && sInput == "") { //если еще не была введена ни одна цифра
            showToastFirstDigit()
        } else if (isLastPressedOperation) {
            //если нажат арифметический оператор, а новое число не введено
            showToastNextDigit()
        } else if (!hasNum1 && sInput[0] == '-') {
            //если Negate нажимают когда ввели первое число с минусом
            sbHistory.delete(0, sbHistory.length) //стираем число из строки sbHistory
            sInput = sInput.substring(1) //обрезаем минус и пробел в строке sInput
            binding.etInput.setText(sInput)
            sendSInputToJournal()
        } else if (!hasNum1 && sInput[0] != '-') {
            //если Negate нажимают когда ввели первое число положительное
            sbHistory.delete(0, sbHistory.length) //стираем число из строки sbHistory
            sInput = "-$sInput" //добавляем минус в строке sInput
            sendSInputToJournal()
        } else if (hasNum1 && sInput == "" && binding.etInput.text.toString() != "") {
            //если введенное в строке число - результат предыдущих вычислений
            sInput = binding.etInput.text.toString() //получаем значение из текстового поля
            //т.к. цепочка вычислений перезатирается, перезаписываем sbHistory
            sbHistory.delete(0, sbHistory.length)
            if (sInput[0] == '-') { //если получили отриц. число в
                //результате предыдущих операций
                sInput = sInput.substring(1) //обрезаем минус в строке sInput
            } else if (sInput[0] != '-') {
                sInput = "-$sInput" //добавляем минус в строке sInput
            }
            sendSInputToJournal()
            operator = '0'
            hasNum1 = false

            /*стандартные случаи, когда Negate нажат после числа по ходу вычислений*/
        } else if (sInput[0] != '-' && operator != '0') {
            val x = sbHistory.lastIndexOf(sInput) //получаем индекс, чтобы обрезать текущее число
            sbHistory.delete(x, sbHistory.length) //обрезаем
            sInput = "-$sInput" //добавляем минус в строке sInput
            sbHistory.append("\u200b").append("(").append(sInput).append(")") //и вставляем снова
            // в sbHistory вместе с пробелом и добавляем скобки
            fragmentSendDataListener!!.onSendData(sbHistory)
        } else if (sInput[0] == '-' && operator != '0') {
            val x = sbHistory.lastIndexOf(sInput)
            sbHistory.delete(x - 2, sbHistory.length) //обрезаем число с минусом и скобками
            sInput = sInput.substring(1) //обрезаем минус в строке sInput
            sendSInputToJournal()
        }
        binding.etInput.setText(sInput)
        isLastPressedOperation = false
        isBSAvailable = false
    }

    private fun bSOperation() {
        if (isBSAvailable && sInput.length > 3 //в строке могут быть пробел и
            // минус, поэтому безопасно стирать можно если больше 3 символов
            || isBSAvailable && sInput.length == 2
        ) {
            //или если в строке осталось 2 символа, то есть 2 цифры без минуса
            sInput = sInput.substring(0, sInput.length - 1)
            sbHistory.deleteCharAt(sbHistory.length - 1)
        } else if (isBSAvailable && sInput.length == 3 && sInput[1] != '-') {
            //если в строке 3 символа и второй - не минус, то можно стирать последний
            //символ безопасно
            sInput = sInput.substring(0, 2)
            sbHistory.deleteCharAt(sbHistory.length - 1)
        } else if (isBSAvailable && sInput.length == 3 && sInput[1] == '-') {
            //если в строке 3 символа и второй - минус, то можно стирать все 3 символа:
            //цифру, минус, пробел
            sInput = "0"
            sbHistory.delete(sbHistory.length - 3, sbHistory.length)
            isBSAvailable = false
        } else if (isBSAvailable && sInput.length == 1) {
            //если в строке осталась только 1 цифра, заменяем ее на ноль
            sInput = "0"
            sbHistory.deleteCharAt(sbHistory.length - 1)
            isBSAvailable = false
        }
        binding.etInput.setText(sInput)
        fragmentSendDataListener!!.onSendData(sbHistory)
    }

    private fun percentOperation() {
        if (!hasNum1 && sInput == "") {
            showToastFirstDigit() // если не введено ни одно число и не нажата ни одна
            // арифметическая операция
        } else if (!hasNum1) { //если введено только 1 число, а кнопка арифметической операции
            // не нажата, то рассчитать 1 процент
            num1 = sInput.toDouble()
            sbHistory.append("\u200b * 1% \u200b") //записываем первое число в
            // историю и указываем, что мы рассчитали 1%
            fragmentSendDataListener!!.onSendData(sbHistory)
            num2 = num1 / 100
            cutZeroOutput(num2) //обрезаем ноль, если нужно, и выводим в строке input
            isLastPressedOperation = false
            isBSAvailable = false
            sInput =
                num2.toString() //передаем результат в sInput и num1 и обнуляем num2
            num1 = num2
            num2 = 0.0
        } else if (sInput == "" && isLastPressedOperation) {
            //если есть num1 и оператор, но правого операнда нет - то выводим подсказку
            showToastNextDigit()
        } else if (sInput != "" && operator != '0' && !isLastPressedOperation) {
            //заданы 2 операнда и арифметическая операция в operator - рассчитываем
            // num2 процентов от num1
            num2 = sInput.toDouble()
            //сначала вычисляем num2 процентов от num1 и перезаписываем num2
            num2 *= num1 / 100
            sInput = num2.toString()
            operationCalc() //выполняем заданную арифм. операцию
            sbHistory.append("% \u200b") //записываем в строку памяти знак процента
            // и пробел
            fragmentSendDataListener!!.onSendData(sbHistory)
            isLastPressedOperation = false
            isBSAvailable = false
            operator = '0'
            num2 = 0.0
        }
    }

    /* Вспомогательные методы */
    private fun cutZeroOutput(d: Double) {
        var sD = d.toString() // обрезаем .0 при выводе в input
        if (sD.endsWith(".0")) {
            val x = sD.indexOf(".")
            sD = sD.substring(0, x)
        }
        binding.etInput.setText(sD)
    }

    private fun sendSInputToJournal() {
        sbHistory.append(sInput)
        fragmentSendDataListener!!.onSendData(sbHistory)
    }

    private fun sendOperatorToJournal() {
        sbHistory.append("\u200b").append(operator).append("\u200b") //записываем в историю
        fragmentSendDataListener!!.onSendData(sbHistory) //выводим в поле с историей
    }

    private fun showToastFirstDigit() {
        Toast.makeText(activity, "Введите хотя бы одно число", Toast.LENGTH_SHORT).show()
    }

    private fun showToastNextDigit() {
        Toast.makeText(activity, "Введите следующее число", Toast.LENGTH_SHORT).show()
    }

    /* Сохранение состояния */
    override fun onSaveInstanceState(outState: Bundle) {
        sHistory = sbHistory.toString()
        outState.putString(sHistoryKey, sHistory)
        outState.putChar(operatorKey, operator)
        outState.putString(sInputKey, sInput)
        outState.putDouble(num1Key, num1)
        outState.putDouble(num2Key, num2)
        outState.putBoolean(hasNum1Key, hasNum1)
        outState.putBoolean(isLastPressedOperationKey, isLastPressedOperation)
        outState.putBoolean(isBSAvailableKey, isBSAvailable)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /* константы для сохранения состояния */
        const val sHistoryKey = "sHistoryKey"
        const val operatorKey = "operatorKey"
        const val sInputKey = "sInputKey"
        const val num1Key = "num1Key"
        const val num2Key = "num2Key"
        const val hasNum1Key = "hasNum1Key"
        const val isLastPressedOperationKey = "isLastPressedOperationKey"
        const val isBSAvailableKey = "isBSAvailableKey"
    }
}
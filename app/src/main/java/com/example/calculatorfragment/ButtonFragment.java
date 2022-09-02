package com.example.calculatorfragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.calculatorfragment.databinding.FragmentButtonBinding;
/*

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
цифры выводятся в текстовое поле EditText etInput. Для сохранения состояния преобразуется в
переменную типа String.
Double num1 - первое введенное число в цепочке операций или результат предыдущих операций, то
есть левый операнд.
Double num2 - число, считанное из строки sInput, то есть правый операнд, введеный в EditText etInput.
Переменная char operator используется для хранения последней заданной пользователем операции
(только 4 арифметические операции в виде '+').
Boolean hasNum1 - изначально false, присваивается true, если пользователь уже ввел одно число
в момент нажатия кнопки с арифметической операцией, то есть num1 - левый операнд.
Boolean isLastPressedOperation - принимает значение true, если последняя нажатая кнопка - одна из
четырех основных арифметических операций.
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
MainOperation(), в который передается аргумент - char operator, например, "+". После выполнения
MainOperation() переменной isLastPressedOperation присваивается true.
MainOperation() выполняет действия:
0. Проверяет, были ли введены какие-то цифры, иначе выдает подсказку "Введите число".
1. Проверяет, не была ли нажата другая кнопка с арифметической операцией до этой.
2. Если да, то перезаписывает operator, заменяя на новую арифметическую операцию.
2.5. Если нет, то записывает новую операцию в operator.
3. Записывает знак арифметической операции в строку sbHistory.
4. Отправляет sbHistory во фрагмент JournalFragment.
5. Сохраняет число из строки sInput в double num1 (если это первое вводимое число).
6. Проверяет, были ли введены ранее 1 или больше чисел, записан ли какой-то знак в operator.
7. Вызывает метод operationCalc() в случае, если кнопка арифметической операции должна сработать
как кнопка "=", то есть уже были введены 2 или больше числа и операторы +, -, *, / между ними.

2.5. Метод operationCalc() получает аргумент - char sign из метода MainOperation(), а затем:
1. Считывает num2 из строки sInput.
2. Выполняет соответствующие арифметические операции с числами num1 и num2.
3. Полученное значение перезаписывается в num1 и выводится на экран, num2 обнуляется.
4. Метод output() выводит число в поле EditText input, а также удаляет .0 если получилось целое число.
5. При делении на ноль метод clearAll() обнуляет все переменные состояния, а также вызывает метод clearHistory(),
который очищает историю.

2.6. Метод clearAll() также вызывается при нажатии на кнопку "С" (вместе с методом clearHistory()
соответственно).

2.7. При нажатии кнопки "=" вызывается метод operationCalc(), после его выполнения:
1. Последнее введенное число записывается в sbHistory.
2. sbHistory отправляется во фрагмент JournalFragment.
3. Переменной operator присваивается значение '0'.
4. Значение isLastPressedOperation = false.

2.8. Кнопка "%" вызывает метод operationPercent(), которые проверяет состояние приложения и
запускает 1 из 4 сценариев.
1. Если еще не было введено ни одного числа, появляется Toast с просьбой ввести число.
2. Если было введено только 1 число и не нажата арифметическая операция, то рассчитывается 1% от
числа.
3. Если имеются 2 операнда и 1 оператор, то срабатывает стандартный функционал.
4. Если есть 1 операнд и нажата арифметическая операция, то появляется Toast с просьбой
ввести второе число.

2.9. Для работы кнопки "Backspace" используется булева переменная isBSAvailable  - она
перезаписывается как true, если была нажата кнопка с числом или точкой, и перезаписывается как false
если была нажата любая другая кнопка.
Кнопка "Backspace" срабатываем при условии что isBSAvailable  == true и запускает 1 из 4 сценариев:
1. Если в строке sInput > 2 символов, то последняя цифра удаляется из этой строки и из sbHistory.
2. Если в строке sInput 2 символа и первый из них не '-', то аналогично.
3. Если в строке sInput 1 символ, то она заменяется в строке sInput на ноль, удаляется из sbHistory
 и isLastPressedDigit присваивается false (т.к. больше удалять ничего).
4. Если в строке sInput одна цифра и минус, то заменяется в строке sInput на ноль, удаляется из
sbHistory и isBSAvailable присваивается false (т.к. больше удалять ничего).
Если последней нажатой кнопкой была арифметическая операция, =, % или negate, то isBSAvailable
присвоено значение false, и никакие действия не выполняются.

2.10. Смена знака числа осуществляется 2 способами: нажатие на "-" прежде чем введены какие-то
числа, нажатие на кнопку Negate. При нажатии на Negate проверяется состояние приложения. Разные
действия выполняются если:
1. В данный момент в строке sInput находится первое введенное число, у которого знак минус был
добавлен кнопкой "-".
2. Если не была введена ни одна цифра либо последним действием нажат арифметический оператор - тогда
появляется сообщение, что нужно ввести какое-либо число.
3. Если в данный момент в строке sInput находится число, которое является результатом предыдущих
вычислений. Тогда вся предыдущая история операций обнуляется.
4. "Нормальный" случай, когда число введено и нажата кнопка Negate.

* */

public class ButtonFragment extends Fragment {

    public ButtonFragment() {
        super(R.layout.fragment_button);
    }

    //интерфейс для передачи данных в JournalFragment
    interface OnFragmentSendDataListener {
        void onSendData(StringBuilder data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentSendDataListener = (OnFragmentSendDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    //глобальные переменные
    private FragmentButtonBinding binding;
    private OnFragmentSendDataListener fragmentSendDataListener;
    StringBuilder sbHistory = new StringBuilder("");
    String sHistory, sInput = "";
    char operator = '0';
    double num1, num2 = 0;
    boolean hasNum1, isLastPressedOperation, isBSAvailable = false;
    private static final String TAG = "myLogs";

    //переменные для сохранения состояния
    final static String sHistoryKey= "sHistoryKey";
    final static String operatorKey= "operatorKey";
    final static String sInputKey= "sInputKey";
    final static String num1Key= "num1Key";
    final static String num2Key= "num2Key";
    final static String hasNum1Key= "hasNum1Key";
    final static String isLastPressedOperationKey= "isLastPressedOperationKey";
    final static String isBSAvailableKey = "isBSAvailableKey";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {

        binding = FragmentButtonBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //восстановление состояния
        if (savedInstanceState != null) {
            sHistory = savedInstanceState.getString(sHistoryKey);
            sbHistory.delete(0, sbHistory.length());
            sbHistory.append(sHistory);
            operator = savedInstanceState.getChar(operatorKey);
            sInput = savedInstanceState.getString(sInputKey);
            num1 = savedInstanceState.getDouble(num1Key);
            num2 = savedInstanceState.getDouble(num2Key);
            hasNum1 = savedInstanceState.getBoolean(hasNum1Key);
            isLastPressedOperation = savedInstanceState.getBoolean(isLastPressedOperationKey);
            isBSAvailable = savedInstanceState.getBoolean(isBSAvailableKey);
        }

        return view;
    }

    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //выводим пустые строки
        binding.etInput.setText(sInput);
        fragmentSendDataListener.onSendData(sbHistory);

        //определяем слушатели для кнопок действий
        binding.add.setOnClickListener(v -> {
            MainOperation('+');
            isLastPressedOperation = true;
            isBSAvailable = false;
        });

        binding.mult.setOnClickListener(v -> {
            MainOperation('*');
            isLastPressedOperation = true;
            isBSAvailable = false;
        });

        binding.sub.setOnClickListener(v -> {
            //если нажимаем минус прежде чем ввести какое-либо число, то число будет
            //отрицательным
            if (!hasNum1 && sInput.equals("")) {
                sInput = "-";
                binding.etInput.setText(sInput);
                sbHistory = sbHistory.append("\u200b-");
                fragmentSendDataListener.onSendData(sbHistory);
            } else {
                MainOperation('-');
                isLastPressedOperation = true;
                isBSAvailable = false;
            }
        });

        binding.div.setOnClickListener(v -> {
            MainOperation('/');
            isLastPressedOperation = true;
            isBSAvailable = false;
        });

        //кнопка равно
        binding.calc.setOnClickListener(v -> {
            if (!hasNum1 && sInput.equals("") && operator == '0') {
                showToastFirstDigit(); //если еще ничего не введено
            } else if (hasNum1 && sInput.equals("") && operator != '0') {
                showToastNextDigit(); //если есть результат предыдущих операций
                //и арифм.оператор, но нет правого операнда
            } else if (hasNum1 && sInput.equals("") && operator == '0') {
                showToastNextDigit(); //если есть результат предыдущих операций, но нет
                //правого операнда и арифм.оператора
            } else {
                operationCalc();
                //выводим историю, включая последнее число, но без оператора
                fragmentSendDataListener.onSendData(sbHistory);
                operator = '0';
                isLastPressedOperation = false;
                isBSAvailable = false;
            }
        });



        //кнопка отрицательное/положительное число
        binding.btnNegate.setOnClickListener(v -> {
            if (!hasNum1 && sInput.equals("") //если еще не была введена ни одна цифра
                    || isLastPressedOperation) {
                //или если нажат арифметический оператор, а новый операнд не введен
                showToastNextDigit();

            } else if (!hasNum1 && !sInput.equals("") && sInput.charAt(1) == '-'
                    && operator == '0') {
                //если негейт нажимают когда ввели первое число с минусом
                sbHistory.delete(0, sbHistory.length()); //стираем число из строки sbHistory
                sInput = sInput.substring(2); //обрезаем минус и пробел в строке sInput
                binding.etInput.setText(sInput);
                sbHistory.append(sInput); //и вставляем снова в sbHistory
                fragmentSendDataListener.onSendData(sbHistory);

            } else if (!hasNum1 && !sInput.equals("") && sInput.charAt(1) != '-'
                    && operator == '0') {
                //если негейт нажимают когда ввели первое число положительное
                sbHistory.delete(0, sbHistory.length()); //стираем число из строки sbHistory
                sInput = "-" + sInput; //добавляем минус в строке sInput
                sbHistory.append("\u200b").append(sInput); //и вставляем снова в sbHistory
                fragmentSendDataListener.onSendData(sbHistory);

            } else if (hasNum1 && sInput.equals("")
                    && !binding.etInput.getText().toString().equals("")) {
                //если введенное в строке число - результат предыдущих вычислений
                sInput = binding.etInput.getText().toString(); //получаем значение из текстового поля
                //т.к. цепочка вычислений перезатирается, перезаписываем sbHistory
                sbHistory.delete(0, sbHistory.length());
                if (sInput.charAt(0) == '-') { //если получили отриц. число в
                    //результате предыдущих операций, то пробела перед ним нет
                    sInput = sInput.substring(1); //обрезаем минус в строке sInput

                    sbHistory.append(sInput); //и вставляем снова в sbHistory
                } else if (sInput.charAt(0) != '-') {
                    sInput = "-" + sInput; //добавляем минус в строке sInput (пробел будет
                    // мешать вычислениям)
                    sbHistory.append("\u200b").append(sInput); //и вставляем в sbHistory вместе с пробелом
                }

                fragmentSendDataListener.onSendData(sbHistory);
                operator = '0';
                hasNum1 = false;

            } else if (!sInput.equals("") && sInput.charAt(1) != '-' && operator != '0') {
                int x = sbHistory.lastIndexOf(sInput); //получаем индекс, чтобы обрезать текущее число
                sbHistory.delete(x, sbHistory.length()); //обрезаем
                sInput = "-" + sInput; //добавляем минус в строке sInput
                sbHistory.append("\u200b").append("(").append(sInput).append(")"); //и вставляем снова
                // в sbHistory вместе с пробелом и добавляем скобки
                fragmentSendDataListener.onSendData(sbHistory);

            } else if (!sInput.equals("") && sInput.charAt(1) == '-' && operator != '0') {
                int x = sbHistory.lastIndexOf(sInput);
                sbHistory.delete(x - 1, sbHistory.length()); //обрезаем число с минусом и скобками
                sInput = sInput.substring(1); //обрезаем минус в строке sInput
                sbHistory.append(sInput);
                //и вставляем снова в sbHistory, из которой вырезаем скобки
                fragmentSendDataListener.onSendData(sbHistory);
            }

            binding.etInput.setText(sInput);
            isLastPressedOperation = false;
            isBSAvailable = false;
        });

        //кнопка Backspace
        binding.btnBack.setOnClickListener(v -> {
            if (isBSAvailable && sInput.length() > 3 //в строке могут быть пробел и
                    // минус, поэтому безопасно стирать можно если больше 3 символов
                    || isBSAvailable && sInput.length() == 2) {
                //или если в строке осталось 2 символа, то есть 2 цифры без минуса
                sInput = sInput.substring(0, sInput.length() - 1);
                sbHistory.deleteCharAt(sbHistory.length() - 1);
            } else if (isBSAvailable && sInput.length() == 3 && sInput.charAt(1) != '-') {
                //если в строке 3 символа и второй - не минус, то можно стирать последний
                //символ безопасно
                sInput = sInput.substring(0, 2);
                sbHistory.deleteCharAt(sbHistory.length() - 1);
            } else if (isBSAvailable && sInput.length() == 3 && sInput.charAt(1) == '-') {
                //если в строке 3 символа и второй - минус, то можно стирать все 3 символа:
                //цифру, минус, пробел
                sInput = "0";
                sbHistory.delete(sbHistory.length() - 3, sbHistory.length());
                isBSAvailable = false;
            } else if (isBSAvailable && sInput.length() == 1) {
                //если в строке осталась только 1 цифра, заменяем ее на ноль
                sInput = "0";
                sbHistory.deleteCharAt(sbHistory.length() - 1);
                isBSAvailable = false;
            }
            binding.etInput.setText(sInput);
            fragmentSendDataListener.onSendData(sbHistory);
        });

        //кнопка процент
        binding.btnPercent.setOnClickListener(v -> {
            if (!hasNum1 && sInput.equals("") && operator == '0') {
                showToastFirstDigit(); // если не введено ни одно число и не нажата ни одна
                // арифметическая операция

            } else if (!hasNum1 && !sInput.equals("") && operator == '0') {
                //если введено только 1 число, а кнопка арифметической операции не нажата, то
                // рассчитать 1 процент
                num1 = Double.parseDouble(sInput);
                sbHistory.append(sInput).append("\u200b/100"); //записываем первое число в
                // историю и указываем, что мы рассчитали 1%
                fragmentSendDataListener.onSendData(sbHistory);
                num2 = num1 / 100;
                cutZeroOutput(num2); //обрезаем ноль, если нужно, и выводим в строке input, num2
                // при этом не изменяется
                isLastPressedOperation = false;
                isBSAvailable = false;
                sInput = Double.toString(num2); //передаем результат в sInput и num1 и обнуляем num2
                num1 = num2;
                num2 = 0;

            } else if (hasNum1 && sInput.equals("") && operator != '0' && isLastPressedOperation) {
                //если введено 1 числ
                // о и оператор, но 2-е число не введено - то выводим подсказку
                showToastNextDigit();

            } else if (hasNum1 && !sInput.equals("") && operator != '0' && !isLastPressedOperation) {
                //заданы 2 операнда и арифметическая операция в operator - рассчитываем num2 процентов от num1
                num2 = Double.parseDouble(sInput);
                Log.d(TAG, "num1 = " + num1);
                Log.d(TAG, "num2 = " + num2);
                //сначала вычисляем num2 процентов от num1 и перезаписываем num2
                num2 = num1 / 100 * num2;
                sInput = Double.toString(num2);
                Log.d(TAG, "промежуточный num2 = " + num2);
                operationCalc(); //выполняем заданную арифм. операцию
                Log.d(TAG, "num1 = " + num1);
                sbHistory.append("%").append("\u200b"); //записываем в строку памяти знак процента
                // и пробел
                fragmentSendDataListener.onSendData(sbHistory);
                isLastPressedOperation = false;
                isBSAvailable = false;
                sInput = "";
                operator = '0';
                num2 = 0;
            }
        });

        binding.btnClear.setOnClickListener(v -> clearAll());

        /* НАЧАЛО добавляем слушатели для цифр*/
        binding.btn1.setOnClickListener(v -> enterDigit(1));
        binding.btn2.setOnClickListener(v -> enterDigit(2));
        binding.btn3.setOnClickListener(v -> enterDigit(3));
        binding.btn4.setOnClickListener(v -> enterDigit(4));
        binding.btn5.setOnClickListener(v -> enterDigit(5));
        binding.btn6.setOnClickListener(v -> enterDigit(6));
        binding.btn7.setOnClickListener(v -> enterDigit(7));
        binding.btn8.setOnClickListener(v -> enterDigit(8));
        binding.btn9.setOnClickListener(v -> enterDigit(9));
        binding.btn0.setOnClickListener(v -> enterDigit(0));

        binding.btnDot.setOnClickListener(v -> {
            sbHistory = sbHistory.append(".");
            sInput = sInput + "."; //записывается в строке для инпута
            binding.etInput.setText(sInput); //выводится на экран
            isLastPressedOperation = false;
            isBSAvailable = true;
        });
    } // КОНЕЦ ONVIEWCREATED

    public void MainOperation (char sign){

        if (!isLastPressedOperation) {

            if (!hasNum1 && sInput.equals("") && operator == '0') {
                showToastFirstDigit();
            }

            if (!hasNum1 && !sInput.equals("") && operator == '0') { //если в строке записано 1 число,
                // но оператор еще не записан
                operator = sign; //записываем в текущий оператор
                sbHistory.append("\u200b").append(operator).append("\u200b"); //записываем в историю
                fragmentSendDataListener.onSendData(sbHistory);//выводим в поле с историей
                num1 = Double.parseDouble(sInput); //записываем первое введенное число в num1
                hasNum1 = true; // num1 теперь не пустое
                sInput = ""; // очищаем для ввода следующего операнда, но не отображаем
                binding.etInput.setText(sInput);
            }

            if (hasNum1 && !sInput.equals("") && operator != '0') { //если в строке записано 1 число,
                // есть num1 и оператор, то срабатывает как кнопка =
                operationCalc();
                operator = sign; //записываем в текущий оператор
                sbHistory.append("\u200b").append(operator).append("\u200b"); //записываем в историю
                fragmentSendDataListener.onSendData(sbHistory);
            }

            if (hasNum1 && sInput.equals("") && operator == '0') { //последующие операции после первой
                operator = sign; //записываем в текущий оператор
                sbHistory.append("\u200b").append(operator).append("\u200b"); //записываем в историю
                fragmentSendDataListener.onSendData(sbHistory);

            }
        } else { //если был только что нажат другой оператор
            operator = sign; //записываем в текущий оператор
            sbHistory.delete(sbHistory.length() - 3, sbHistory.length()); // удаляем предыдущий оператор из истории
            sbHistory.append("\u200b").append(operator).append("\u200b"); //записываем в историю новый оператор
            fragmentSendDataListener.onSendData(sbHistory);
        }
    }

    //метод выполняет основную операцию - расчет
    public void operationCalc () {
        num2 = Double.parseDouble(sInput); // получаем введенное число

        switch (operator) {
            case ('+'):
                num1 += num2;
                cutZeroOutput(num1);
                break;

            case ('-'):
                num1 = num1 - num2;
                cutZeroOutput(num1);
                break;

            case ('*'):
                num1 *= num2;
                cutZeroOutput(num1);
                break;

            case ('/'):
                if (num2 != 0) {
                    num1 = num1 / num2;
                    cutZeroOutput(num1);
                } else {
                    binding.etInput.setText(R.string.error);
                    clearAll();
                }
                break;
        }

        num2 = 0;
        sInput = "";
    }

    // обрезаем .0 при выводе в input
    public void cutZeroOutput(double d){
        String sD= String.valueOf(d);
        if (sD.endsWith(".0")) {
            int x = sD.indexOf(".");
            sD = sD.substring(0, x);
        }
        binding.etInput.setText(sD);
    }

    public void clearHistory () {
        sbHistory.delete(0, sbHistory.length());
        sHistory = "";
        fragmentSendDataListener.onSendData(sbHistory);
    }

    public void clearAll () {
        binding.etInput.setText("");
        num2 = 0;
        num1 = 0;
        hasNum1 = false;
        isLastPressedOperation = false;
        isBSAvailable = false;
        sInput = "";
        operator = '0';
        clearHistory();
    }

    //метод, вызываемый при нажатии кнопки с цифрой
    public void enterDigit (int n){
        //если первая введенная цифра в числе 0, то убираем его
        if (sInput.equals("0") && !sbHistory.toString().equals("")) {
            sInput = "";
            sbHistory.deleteCharAt(sbHistory.length() - 1);
        }

        sInput = sInput + n;
        sbHistory.append(n);
        binding.etInput.setText(sInput);
        isLastPressedOperation = false;
        isBSAvailable = true;
    }

    public void showToastFirstDigit() {
        Toast.makeText(getActivity(), "Введите хотя бы одно число", Toast.LENGTH_SHORT).show();
    }

    public void showToastNextDigit() {
        Toast.makeText(getActivity(), "Введите следующее число", Toast.LENGTH_SHORT).show();
    }

    // сохранение состояния
    @Override
    public void onSaveInstanceState(Bundle outState) {
        sHistory = sbHistory.toString();
        outState.putString(sHistoryKey, sHistory);
        outState.putChar(operatorKey, operator);
        outState.putString(sInputKey, sInput);
        outState.putDouble(num1Key, num1);
        outState.putDouble(num2Key, num2);
        outState.putBoolean(hasNum1Key, hasNum1);
        outState.putBoolean(isLastPressedOperationKey, isLastPressedOperation);
        outState.putBoolean(isBSAvailableKey, isBSAvailable);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
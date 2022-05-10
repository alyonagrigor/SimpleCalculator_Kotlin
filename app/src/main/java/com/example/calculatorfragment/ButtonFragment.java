package com.example.calculatorfragment;

import static java.lang.Character.isDigit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
/* Что сделать:
1. Настроить передачу данных между фрагментами
2. доделать бекспейс
3. потестировать верстку кнопок на разных размерах
4. проверить сколько разрядов числа можно ввести и вывести в строку
5. // Сохранение состояния в parcelable доделать - доб. булевыы переменные и историю
6. что сделать с лишней кнопкой в гориз. ориентации
7. если есть только num1 и оператор, то num2=num1 ????
8. если num1 сразу отрицательное число то что ?????
9. 2 ошибка в проентацх
10. поменять кнопки местами, убрать С и ВС подальше от пальцев
11. Сделать верстку для больших экранов
12. крашится на кнопке негейт
* */

public class ButtonFragment extends Fragment {

    public ButtonFragment() {
        // Required empty public constructor
    super(R.layout.fragment_button);
    }

    interface OnFragmentSendDataListener {
        void onSendData(String data);
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


    private OnFragmentSendDataListener fragmentSendDataListener;
    Button btnAdd, btnSub, btnMult, btnCalc, btnDiv, btnClear, btnDot, btnNegate, btnBack,
            btnPercent, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0;
    EditText input;
    TextView history; //поле для отображения истории
    String sHistory = ""; //строка для сохранения истории
    String sF = ""; //служебная строка для обрезания .0 при выводе целого числа
    String last = ""; //служебная строка для операции negate
    String operator = ""; //строка, которая хранит последнюю нажатую юзером операцию, только 4
    // основные операции +, -, *, /
    String sInput = ""; //строка для хранения числа, которое сейчас вводит пользователь
    float num1 = 0; //число, которое сейчас вводит пользователь
    float num2 = 0; // результат выполненных операций
    boolean hasNum1 = false; //булева для проверки num1, проверяет что это не первое введенное число в цепочке операций
    boolean isLastPressedOperation = false; //для контроля нажатия кнопок разных операций подряд,
    //true только после нажатия 4 основных операций +-*/
    boolean isLastPressedDigit = false; //для того, чтобы стереть цифру бекспейсом

    //переменные для сохранения состояния
    final static String sHistoryKey = "sHistoryKey";
    final static String operatorKey = "operatorKey";
    final static String sInputKey = "sInputKey";
    final static String num1Key = "num1Key";
    final static String num2Key = "num2Key";
    final static String hasNum1Key = "hasNum1Key";
    final static String isLastPressedOperationKey = "isLastPressedOperationKey";
    final static String isLastPressedDigitKey = "isLastPressedDigitKey";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState){
            // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_button, container, false);
        if (savedInstanceState != null) {
            sHistory = savedInstanceState.getString(sHistoryKey);
            operator = savedInstanceState.getString(operatorKey);
            sInput = savedInstanceState.getString(sInputKey);
            num1 = savedInstanceState.getFloat(num1Key);
            num2 = savedInstanceState.getFloat(num2Key);
            hasNum1 = savedInstanceState.getBoolean(hasNum1Key);
            isLastPressedOperation = savedInstanceState.getBoolean(isLastPressedOperationKey);
            isLastPressedDigit = savedInstanceState.getBoolean(isLastPressedDigitKey);
        }
        return view;
        }

        public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState){
            super.onViewCreated(view, savedInstanceState);

            input = view.findViewById(R.id.input);
            history = view.findViewById(R.id.history);
            btnAdd = view.findViewById(R.id.add);
            btnSub = view.findViewById(R.id.sub);
            btnMult = view.findViewById(R.id.mult);
            btnCalc = view.findViewById(R.id.calc);
            btnDiv = view.findViewById(R.id.div);
            btn1 = view.findViewById(R.id.btn1);
            btn2 = view.findViewById(R.id.btn2);
            btn3 = view.findViewById(R.id.btn3);
            btn4 = view.findViewById(R.id.btn4);
            btn5 = view.findViewById(R.id.btn5);
            btn6 = view.findViewById(R.id.btn6);
            btn7 = view.findViewById(R.id.btn7);
            btn8 = view.findViewById(R.id.btn8);
            btn9 = view.findViewById(R.id.btn9);
            btn0 = view.findViewById(R.id.btn10);
            btnClear = view.findViewById(R.id.btnClear);
            btnDot = view.findViewById(R.id.btnDot);
            btnNegate = view.findViewById(R.id.btnNegate);
            btnBack = view.findViewById(R.id.btnBack);
            btnPercent = view.findViewById(R.id.btnPercent);

            //выводим пустые строки
            input.setText(sInput);
            history.setText(sHistory);
            history.setVisibility(View.GONE);
            fragmentSendDataListener.onSendData(sHistory);

            //определяем слушатели для действий
            btnAdd.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MainOperation("+");
                    isLastPressedOperation = true;
                    isLastPressedDigit = false;
                }
            });

            btnMult.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MainOperation("*");
                    isLastPressedOperation = true;
                    isLastPressedDigit = false;
                }
            });

            btnSub.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MainOperation("-");
                    isLastPressedOperation = true;
                    isLastPressedDigit = false;
                }
            });

            btnDiv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MainOperation("/");
                    isLastPressedOperation = true;
                    isLastPressedDigit = false;
                }
            });

            //кнопка равно
            btnCalc.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    operationCalc();
                    history.setText(sHistory);
                    fragmentSendDataListener.onSendData(sHistory);//выводим историю, включая последнее число, но без оператора
                    operator = ""; // обнуляем
                    isLastPressedOperation = false;
                    isLastPressedDigit = false;
                }
            });

            //кнопка С
            btnClear.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    clearAll();
                }
            });

            //кнопка отрицательное/положительное число
            btnNegate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (sInput.charAt(0) != '-') {
                        sInput = "-" + sInput;
                    } else {
                        sInput = sInput.substring(1);
                    }
                    input.setText(sInput);
                    negateInHistory();
                    isLastPressedOperation = false;
                    isLastPressedDigit = false;
                }
            });

            //кнопка Backspace
            btnBack.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (isLastPressedDigit) {
                        //ломается если в input только одна цифра
                        if (sInput.length() == 1) {
                            sInput = "";
                            input.setText(sInput);
                        } else {
                            sInput = sInput.substring(0, sInput.length() - 1);
                            input.setText(sInput);
                        }
                        sHistory = sHistory.substring(0, sInput.length() - 1);
                    }
                }
            });

            //кнопка процент
            btnPercent.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    operationPercent();
                }
            });
            /* НАЧАЛО добавляем слушатели для цифр*/
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterDigit(1);
                }
            });
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterDigit(2);
                }
            });
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterDigit(3);
                }
            });
            btn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterDigit(4);
                }
            });
            btn5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterDigit(5);
                }
            });
            btn6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterDigit(6);
                }
            });
            btn7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterDigit(7);
                }
            });
            btn8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterDigit(8);
                }
            });
            btn9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterDigit(9);
                }
            });
            btn0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    enterDigit(0);
                }
            });
            btnDot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sHistory = sHistory + ".";
                    sInput = sInput + "."; //записывается в строке для инпута
                    input.setText(sInput); //выводится на экран
                    isLastPressedOperation = false;
                    isLastPressedDigit = true;
                }
            });
            /* КОНЕЦ слушатели для цифр*/

        } /* КОНЕЦ ONVIEWCREATED*/

    // методы для операций

        public void operationPercent () { // проверяем состояние программы при нажатии,
            // срабатывает только если уже введен 1 оператор и 2 операнда

            if (!hasNum1 && !sInput.equals("") && operator.equals("") ||
                    hasNum1 && !sInput.equals("") && !operator.equals("") && isLastPressedOperation) { //если введено только 1 число,
                //либо введено 1 число и оператор, но 2-е число не введено - то нажатие на % обнуляет все состояние
                //!!! 2-е условие не срабатывает
                clearAll();
            }

            if (hasNum1 && !sInput.equals("") && !operator.equals("") && !isLastPressedOperation) {
                //если уже введен 1 оператор и 1 операнда, то выдает процент от num2 и перезаписывает
                // его, но не выполняет то, что записано в операторе
                num2 = Float.parseFloat(sInput);
                sHistory = sHistory.substring(0, sHistory.length() - sInput.length()); //стираем num2
                // из строки памяти, т.к. оно будет изменено;
                num2 = num1 * num2 / 100;
                cutZero(num2);
                input.setText(String.valueOf(num2));
                sHistory = sHistory + num2; //записываем в строку памяти полученное значение
                history.setText(sHistory);
                fragmentSendDataListener.onSendData(sHistory);
                isLastPressedOperation = false;
                isLastPressedDigit = false;
            /* не нужно обнулять, чтобы можно было повторно выполнить это жейтвие, но пока это не работает
            operator = ""; //обнуляем строку оператор, т.к. действие из нее выполнено
            sInput = ""; //обнуляем строку для ввода нового числа*/
            }

        }

        public void MainOperation (String sign){

            if (!isLastPressedOperation) {

                if (!hasNum1 && !sInput.equals("") && operator.equals("")) { //если в строке записано 1 число,
                    // но оператор еще не записан
                    operator = sign; //записываем в текущий оператор
                    sHistory = sHistory + operator; //записываем в историю
                    history.setText(sHistory);//выводим в поле с историей
                    fragmentSendDataListener.onSendData(sHistory);
                    num1 = Float.parseFloat(sInput); //записываем первое введенное число в num1
                    hasNum1 = true; // num1 теперь не пустое
                    sInput = ""; // очищаем для ввода следующего операнда, но не отображаем
                }

                if (hasNum1 && !sInput.equals("") && !operator.equals("")) { //если в строке записано 1 число,
                    // есть num1 и оператор, то срабатывает как кнопка =
                    operationCalc();
                    operator = sign; //записываем в текущий оператор
                    sHistory = sHistory + operator; //записываем в историю
                    history.setText(sHistory);//выводим в поле с историей
                    fragmentSendDataListener.onSendData(sHistory);
                }

                if (hasNum1 && sInput.equals("") && operator.equals("")) { //последующие операции после первой
                    operator = sign; //записываем в текущий оператор
                    sHistory = sHistory + operator; //записываем в историю
                    history.setText(sHistory);//выводим в поле с историей
                    fragmentSendDataListener.onSendData(sHistory);

                }
            } else { //если был только что нажат другой оператор
                operator = sign; //записываем в текущий оператор
                sHistory = sHistory.substring(0, sHistory.length() - 1); // удаляем предыдущий оператор из истории
                sHistory = sHistory + operator; //записываем в историю новый оператор
                history.setText(sHistory);//выводим в поле с историей
                fragmentSendDataListener.onSendData(sHistory);
            }
        }


        //метод выполняет основную операцию - расчет
        public float operationCalc () {
            num2 = Float.parseFloat(sInput); // получаем введенное число

            if (operator.equals("+")) {
                num1 += num2;
                output(num1);
            }

            if (operator.equals("-")) {
                num1 = num1 - num2;
                output(num1);
            }

            if (operator.equals("*")) {
                num1 *= num2;
                output(num1);
            }

            if (operator.equals("/") && num2 != 0) {
                num1 = num1 / num2;
                output(num1);
            }

            if (operator.equals("/") && num2 == 0) {
                input.setText("Error");
                //очищаем все данные
                clearAll();
            }

            num2 = 0; //обнуляем
            sInput = ""; //обнуляем
            return num1;
        }


        public void output (float f){ // обрезаем .0 при выводе у num1, num2
            sF = String.valueOf(f);
            if (sF.endsWith(".0")) {
                int x = sF.indexOf(".");
                sF = sF.substring(0, x);
                input.setText(sF);
            } else {
                input.setText(sF);
            }
        }

        public float cutZero (float f){ // обрезаем .0 без вывода
            sF = String.valueOf(f);
            if (sF.endsWith(".0")) {
                int x = sF.indexOf(".");
                sF = sF.substring(0, x);
            }
            num2 = Float.parseFloat(sF);
            return num2;
        }

        public void negateInHistory () {
            int i = sHistory.length() - 1;
            while (isDigit(sHistory.charAt(i))) {
                i--;
            }
            last = sHistory.substring(i + 1);
            sHistory = sHistory.substring(0, i + 1);
            sHistory = sHistory + "(-" + last + ")"; //вставляем в строку минус и кавычки для последнего введенного числа
        }


        public void clearHistory () {
            sHistory = "";
            history.setText(sHistory);
            fragmentSendDataListener.onSendData(sHistory);
        }

        public void clearAll () {
            input.setText("");
            num2 = 0; //обнуляем
            num1 = 0;
            hasNum1 = false;
            isLastPressedOperation = false;
            isLastPressedDigit = false;
            sInput = ""; //обнуляем
            operator = ""; // обнуляем
            clearHistory();
        }

        //метод, вызываемый при нажатии кнопки с цифрой
        public void enterDigit (int n){
            sHistory = sHistory + n;
            sInput = sInput + n; //цифра записывается в строке для инпута
            input.setText(sInput); // новая цифра выводится на экран
            isLastPressedOperation = false;
            isLastPressedDigit = true;
        }

    // сохранение состояния
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString(sHistoryKey, sHistory);
        outState.putString(operatorKey, operator);
        outState.putString(sInputKey, sInput);
        outState.putFloat(num1Key, num1);
        outState.putFloat(num2Key, num2);
        outState.putBoolean(hasNum1Key, hasNum1);
        outState.putBoolean(isLastPressedOperationKey, isLastPressedOperation);
        outState.putBoolean(isLastPressedDigitKey, isLastPressedDigit);
        super.onSaveInstanceState(outState);
    }

}
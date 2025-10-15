package com.example.labworkandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView dateTxtBx;
    private EditText loanAmountTxtBx;
    private EditText initialPaymentTxtBx;
    private Spinner loanTermDrpDwn;
    private EditText discountRateTxtBx;
    private Button performBtn;
    private TextView monthlyPaymentTxtBx;
    private TextView totalOverpayment;
    private RadioGroup radioGroup;
    private EditText paymentScheduleTxt;
    private CheckBox showSheduleChckBx;
    private TextView headSheduleTxt;
    private Button clearBtn;

    private String paymentScheduleData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupDate();
        setupLoanTerm();
        clickPerformBtn();
        showPaymentSchedule();
        clickShowPasswordRdBtn();
        clickClearBtn();
    }

    private void initViews() {
        dateTxtBx = findViewById(R.id.dateTxtBx);
        loanAmountTxtBx = findViewById(R.id.loanAmountTxtBx);
        clearBtn = findViewById(R.id.clearBtn);
        initialPaymentTxtBx = findViewById(R.id.initialPaymentTxtBx);
        loanTermDrpDwn = findViewById(R.id.loanTermDrpDwn);
        discountRateTxtBx = findViewById(R.id.discountRateTxtBx);
        performBtn = findViewById(R.id.performBtn);
        monthlyPaymentTxtBx = findViewById(R.id.monthlyPaymentTxtBx);
        totalOverpayment = findViewById(R.id.totalOverpayment);
        radioGroup = findViewById(R.id.radioGroupCon);
        paymentScheduleTxt = findViewById(R.id.paymentScheduleTxt);
        showSheduleChckBx = findViewById(R.id.showSheduleChckBx);
        headSheduleTxt = findViewById(R.id.headSheduleTxt);
    }

    private void clickPerformBtn() {
        performBtn.setOnClickListener(v -> {
            if(checkCorrectlyValue()){
                calculatePayment();
            }
        });
    }

    private boolean checkCorrectlyValue() {
        String loanAmountStr = loanAmountTxtBx.getText().toString().trim();
        String downPaymentStr = initialPaymentTxtBx.getText().toString().trim();
        String discountStr = discountRateTxtBx.getText().toString().trim();

        if (loanAmountStr.isEmpty()) {
            Toast.makeText(this, "Сумма кредита не заполнена!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (downPaymentStr.isEmpty()) {
            Toast.makeText(this, "Первоначальный взнос не заполнен!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (discountStr.isEmpty()) {
            Toast.makeText(this, "Процентная ставка не заполнена!", Toast.LENGTH_SHORT).show();
            return false;
        }

        double loanAmount = Double.parseDouble(loanAmountStr);
        double downPayment = Double.parseDouble(downPaymentStr);

        if (loanAmount < 1000) {
            Toast.makeText(this, "Сумма кредита не меньше 1000!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (loanAmount < downPayment) {
            Toast.makeText(this, "Некорректные данные! Взнос не может быть больше кредита.", Toast.LENGTH_SHORT).show();
            return false;
        }

        int years = getYears();
        if (years == 0) {
            Toast.makeText(this, "Выберите срок кредита!", Toast.LENGTH_SHORT).show();
            return false;
        }

        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Выберите тип кредита!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void clickClearBtn(){
        clearBtn.setOnClickListener(v -> {
            loanAmountTxtBx.setText("");
            initialPaymentTxtBx.setText("");
            loanTermDrpDwn.setSelection(0);
            discountRateTxtBx.setText("");
            monthlyPaymentTxtBx.setText("Ежемесячный платеж:");
            totalOverpayment.setText("Общая переплата:");
            paymentScheduleTxt.setText("");
            paymentScheduleTxt.setVisibility(View.INVISIBLE);
            headSheduleTxt.setVisibility(View.INVISIBLE);
            showSheduleChckBx.setChecked(false);
            radioGroup.clearCheck();
        });
    }

    private void calculatePayment() {
        double loanAmount = Double.parseDouble(loanAmountTxtBx.getText().toString());
        double downPayment = Double.parseDouble(initialPaymentTxtBx.getText().toString());
        double annualRate = Double.parseDouble(discountRateTxtBx.getText().toString());
        int years = getYears();

        double creditSum = loanAmount - downPayment;
        int months = years * 12;

        int checkedId = radioGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.annuityRdBtn) {
            calculateAnnuity(creditSum, annualRate, months);
            paymentScheduleData = generateAnnuitySchedule(creditSum, annualRate, months);
        } else if (checkedId == R.id.differentiatedRdBtn) {
            calculateDifferentiated(creditSum, annualRate, months);
            paymentScheduleData = generateDifferentiatedSchedule(creditSum, annualRate, months);
        }

        paymentScheduleTxt.setText(paymentScheduleData);
    }

    private int getYears() {
        switch (loanTermDrpDwn.getSelectedItem().toString()) {
            case "1 год":
                return 1;
            case "2 года":
                return 2;
            case "3 года":
                return 3;
            case "4 года":
                return 4;
            case "5 лет":
                return 5;
            default:
                return 0;
        }
    }

    private void calculateAnnuity(double principal, double annualRate, int months) {
        double monthlyRate = annualRate / 12 / 100;
        double annuityCoeff = (monthlyRate * Math.pow(1 + monthlyRate, months)) /
                (Math.pow(1 + monthlyRate, months) - 1);

        double monthlyPayment = principal * annuityCoeff;
        double totalPayment = monthlyPayment * months;
        double overpayment = totalPayment - principal;

        DecimalFormat df = new DecimalFormat("#,##0.00");
        String monthlyText = "Ежемесячный платёж: " + df.format(monthlyPayment) + " ₸";
        String total = "Общая переплата: " + df.format(overpayment) + " ₸";
        monthlyPaymentTxtBx.setText(monthlyText);
        totalOverpayment.setText(total);
    }

    private void showPaymentSchedule() {
        showSheduleChckBx.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                paymentScheduleTxt.setText(paymentScheduleData);
            } else {
                paymentScheduleTxt.setText("");
            }
        });
    }

    private void calculateDifferentiated(double principal, double annualRate, int months) {
        double monthlyRate = annualRate / 12 / 100;
        double monthlyPrincipal = principal / months;
        double total = 0;

        for (int i = 1; i <= months; i++) {
            double remaining = principal - monthlyPrincipal * (i - 1);
            double interest = remaining * monthlyRate;
            double payment = monthlyPrincipal + interest;
            total += payment;
        }

        double overpayment = total - principal;
        double firstPayment = monthlyPrincipal + principal * monthlyRate;

        DecimalFormat df = new DecimalFormat("#,##0.00");
        String monthlyText =
                "Ежемесячный платёж: " + df.format(firstPayment) + " до " + df.format(monthlyPrincipal + (principal / months)) + " ₸";
        String totalText = "Общая переплата: " + df.format(overpayment) + " ₸";
        monthlyPaymentTxtBx.setText(monthlyText);
        totalOverpayment.setText(totalText);
    }

    private void setupDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        String dateText = "Дата: " + currentDate;
        dateTxtBx.setText(dateText);
    }

    private void setupLoanTerm() {
        String[] loanTerms = {"Ничего не выбрано", "1 год", "2 года", "3 года", "4 года", "5 лет"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, loanTerms
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loanTermDrpDwn.setAdapter(adapter);
        loanTermDrpDwn.setSelection(0);
    }

    private String generateAnnuitySchedule(double principal, double annualRate, int months) {
        double monthlyRate = annualRate / 12 / 100;
        double annuityCoeff = (monthlyRate * Math.pow(1 + monthlyRate, months)) /
                (Math.pow(1 + monthlyRate, months) - 1);
        double monthlyPayment = principal * annuityCoeff;
        double remainingDebt = principal;

        StringBuilder schedule = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Calendar paymentDate = Calendar.getInstance();

        for (int i = 1; i <= months; i++) {
            double interest = remainingDebt * monthlyRate;
            double principalPayment = monthlyPayment - interest;
            remainingDebt -= principalPayment;

            paymentDate.add(Calendar.MONTH, 1);
            String formattedDate = formatter.format(paymentDate.getTime());

            String sheduleText = String.format(
                    "%s: Осн. долг: %.2f₸, Проценты: %.2f₸, Сумма платежа: %.2f₸\n",
                    formattedDate, principalPayment, interest, monthlyPayment);

            schedule.append(sheduleText);
        }

        paymentScheduleTxt.setText(schedule.toString());
        return schedule.toString();
    }

    private String generateDifferentiatedSchedule(double principal, double annualRate, int months) {
        double monthlyRate = annualRate / 12 / 100;
        double monthlyPrincipal = principal / months;
        double remainingDebt = principal;

        StringBuilder schedule = new StringBuilder();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        Calendar paymentDate = Calendar.getInstance();

        for (int i = 1; i <= months; i++) {
            double interest = remainingDebt * monthlyRate;
            double totalPayment = monthlyPrincipal + interest;
            remainingDebt -= monthlyPrincipal;

            paymentDate.add(Calendar.MONTH, 1);
            String formattedDate = formatter.format(paymentDate.getTime());

            String sheduleText = String.format(
                    "%s: Осн. долг: %.2f₸, Проценты: %.2f₸, Сумма платежа: %.2f₸\n",
                    formattedDate, monthlyPrincipal, interest, totalPayment);

            schedule.append(sheduleText);
        }

        paymentScheduleTxt.setText(schedule.toString());
        return schedule.toString();
    }

    private void clickShowPasswordRdBtn(){
        showSheduleChckBx.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                paymentScheduleTxt.setVisibility(View.VISIBLE);
                headSheduleTxt.setVisibility(View.VISIBLE);
            } else {
                paymentScheduleTxt.setVisibility(View.INVISIBLE);
                headSheduleTxt.setVisibility(View.INVISIBLE);
            }
        });
    }
}
package com.example.labworkandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Spinner brand_spinner;
    private Spinner product_spinner;
    private ImageView productImgBx;
    private TextView specificationsTxtBx;
    private Button clearBtn;
    private List<String> brands;
    private List<ProductModel> products;
    private TextView messageTxtBx;
    private final String SPECIFICATIONS_TEMPLATE = "Бренд: \nНазвание: %s \n\nХаракетристики: %s";
    private final String CHOOSE_BRAND = "Выберите бренд!";
    private final String CHOOSE_PRODUCT = "Выберите товар!";

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
        initData();
        setupBrandSpinner();
        setupListeners();
        setupClearButton();
    }

    private void initViews() {
        brand_spinner = findViewById(R.id.brand_spinner);
        product_spinner = findViewById(R.id.product_spinner);
        productImgBx = findViewById(R.id.productImgBx);
        specificationsTxtBx = findViewById(R.id.specificationsTxtBx);
        messageTxtBx = findViewById(R.id.messageTxtBx);
        clearBtn = findViewById(R.id.clearBtn);
    }

    private void setupBrandSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                brands
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brand_spinner.setAdapter(adapter);
    }

    private void setupProductSpinner(List<String> products) {
        ArrayAdapter<String> productsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                products
        );
        productsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_spinner.setAdapter(productsAdapter);
    }

    private void setupClearButton() {
        clearBtn.setOnClickListener(v -> {
            brand_spinner.setSelection(0);

            setupProductSpinner(new ArrayList<>());

            messageTxtBx.setText(CHOOSE_BRAND);
            specificationsTxtBx.setText("");
            productImgBx.setImageResource(R.drawable.basket);
        });
    }

    private void setupListeners() {
        brand_spinner.setOnItemSelectedListener(getBrandListener());
        product_spinner.setOnItemSelectedListener(getProductListener());

        messageTxtBx.setText(CHOOSE_BRAND);
    }

    private AdapterView.OnItemSelectedListener getBrandListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBrand = brands.get(position);

                List<String> productNames = new ArrayList<>();
                for (ProductModel product : products) {
                    if (product.brand.equals(selectedBrand)) {
                        productNames.add(product.name);
                    }
                }

                setupProductSpinner(productNames);

                if (!productNames.isEmpty()) {
                    messageTxtBx.setText(CHOOSE_PRODUCT);
                } else {
                    messageTxtBx.setText(CHOOSE_BRAND);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                messageTxtBx.setText(CHOOSE_BRAND);
            }
        };
    }

    private AdapterView.OnItemSelectedListener getProductListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProductName = (String) parent.getItemAtPosition(position);

                for (ProductModel product : products) {
                    if (product.name.equals(selectedProductName)) {
                        specificationsTxtBx.setText(
                                String.format(SPECIFICATIONS_TEMPLATE,
                                        product.brand, product.name, product.specifications));
                        productImgBx.setImageResource(product.imageRes);
                        messageTxtBx.setText("");
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                messageTxtBx.setText(CHOOSE_PRODUCT);
            }
        };
    }

    private void initData() {
        brands = Arrays.asList("Выберите бренд", "Hikvision", "Samsung", "Apple", "Xiaomi", "ASUS");

        products = new ArrayList<>();

        //Hikvision
        products.add(new ProductModel(
                "Hikvision",
                "Веб-камера Hikvision DS-U02",
                "матрица 2 Мп CMOS; встроенный микрофон с четким звучанием;",
                R.drawable.hikvision_ds_u02
        ));
        products.add(new ProductModel(
                "Hikvision",
                "Источник бесперебойного питания Hikvision DS-UPS2000",
                "защита от короткого замыкания",
                R.drawable.hikvision_power_supply_ds_2000
        ));
        products.add(new ProductModel(
                "Hikvision",
                "Монитор 23.8 Hikvision DS-D5024F2-2V2",
                "Hikvision",
                R.drawable.hikvision_monitor_ds502
        ));
        products.add(new ProductModel("Hikvision",
                "USB Flash карта Hikvision HS-USB-M200/128G",
                "Скорость чтения данных 80.0 Мбит/с",
                R.drawable.hikvision_usb_flash_m200
        ));
        products.add(new ProductModel(
                "Hikvision",
                "USB Flash карта Hikvision HS-USB-M200/128G",
                "Скорость чтения данных 80.0 Мбит/с",
                R.drawable.hikvision_usb_flash_m200
        ));

        //Samsung
        products.add(new ProductModel(
                "Samsung",
                "Карта памяти Samsung MB-MC128KA/RU 128 Гб",
                "Скорость чтения данных 130.0 МБ/с",
                R.drawable.samsung_memory_card_mc128
        ));
        products.add(new ProductModel(
                "Samsung",
                "Монитор Samsung LS25BG400EIXCI ",
                "Интерфейсы: Разъем для наушников 3.5 minijack",
                R.drawable.samsung_monitor_odyssey_dsd5
        ));
        products.add(new ProductModel(
                "Samsung",
                "Внешний накопитель Samsung T7 Shield MU-PE1T0S/AM",
                "Емкость 1000.0 Гб",
                R.drawable.samsung_external_storage_t7mu
        ));
        products.add(new ProductModel(
                "Samsung",
                "USB Flash карта Samsung MUF-256DA 256 Гб",
                "Скорость чтения данных 400.0 Мбит/с",
                R.drawable.samsung_flash_card_muf256
        ));
        products.add(new ProductModel(
                "Samsung",
                "Блок питания для монитора Samsung A3514-DPN",
                "Мощность 35.0 Вт",
                R.drawable.samsung_power_unit
        ));

        //Apple
        products.add(new ProductModel(
                "Apple",
                "Мышь Apple Magic Mouse 2 белый",
                "Тип сенсора оптическая светодиодная",
                R.drawable.apple_mouse_white
        ));
        products.add(new ProductModel(
                "Apple",
                "Кабель Apple USB Type-C - MagSafe 3 2 м MLYV3",
                "Длина кабеля 2.0 м",
                R.drawable.apple_wireless
        ));
        products.add(new ProductModel(
                "Apple",
                "Клавиатура Apple Magic Keyboard 2nd generation белый",
                "Тип мембранная",
                R.drawable.apple_keyboard_white
        ));
        products.add(new ProductModel(
                "Apple",
                "Мышь Apple Magic Trackpad черный",
                "Тип подключения беспроводная",
                R.drawable.apple_trackpad
        ));
        products.add(new ProductModel(
                "Apple",
                "Монитор Apple Studio Display Nano-texture glass MMYW3 серебристый",
                "Разрешение 5120x2880",
                R.drawable.apple_monitor
        ));

        //Xiaomi
        products.add(new ProductModel(
                "Xiaomi",
                "Мышь Xiaomi MIIIW MWMM01 черный",
                "Разрешение оптического сенсора 1600.0 dpi",
                R.drawable.xiaomi_mouse
        ));
        products.add(new ProductModel(
                "Xiaomi",
                "Монитор 27 Xiaomi Redmi G27Q 2025 P27QCA-RG черный",
                "Разрешение 2560x1440",
                R.drawable.xiaomi_monitor
        ));
        products.add(new ProductModel(
                "Xiaomi",
                "Веб-камера Xiaomi Xiaovv Via",
                "Совместимые ОС Windows, MacOS, Linux",
                R.drawable.xiaomi_web_camera
        ));
        products.add(new ProductModel(
                "Xiaomi",
                "Светильник USB Xiaomi Mi Computer Monitor Light Bar BHR4838GL черный",
                "Назначение для монитора",
                R.drawable.xiaomi_lamp
        ));
        products.add(new ProductModel(
                "Xiaomi",
                "Коврик для мыши Xiaomi MIIIW M24 MWMLV01 900x400x1.8 мм черный",
                "Материал покрытия искусственная кожа",
                R.drawable.apple_monitor
        ));

        //Asus
        products.add(new ProductModel(
                "ASUS",
                "Монитор ASUS TUF Gaming VG259QM черный",
                "Технология динамического обновления экрана NVIDIA G-SYNC Compatible",
                R.drawable.asus_monitor
        ));
        products.add(new ProductModel(
                "ASUS",
                "Мышь ASUS TUF Gaming M3 Gen II черный",
                "Тип сенсора оптическая светодиодная",
                R.drawable.asus_mouse
        ));
        products.add(new ProductModel(
                "ASUS",
                "Клавиатура ASUS ROG Strix Scope II 96 Wireless черный",
                "Вес 1120.0 г",
                R.drawable.asus_keyboard
        ));
        products.add(new ProductModel(
                "ASUS",
                "Внешний бокс ASUS ROG STRIX ARION ESD-S1C черный",
                "Максимальная скорость передачи данных 10.0 Гб/с",
                R.drawable.asus_ssd
        ));
        products.add(new ProductModel(
                "ASUS",
                "Коврик для мыши ASUS ROG Hone Ace Aim Lab Edition 420x508x3 мм черный",
                "Материал основания резина",
                R.drawable.asus_carpet
        ));
    }
}
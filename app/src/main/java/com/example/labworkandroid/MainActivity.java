package com.example.labworkandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ImageView imageBox;
    private Button clearBtn;

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

        imageBox = findViewById(R.id.imageBox);
        clearBtn = findViewById(R.id.clearBtn);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.bracketRdBtn) {
                    imageBox.setImageResource(R.drawable.bracket);
                } else if (checkedId == R.id.routerRdBtn) {
                    imageBox.setImageResource(R.drawable.router);
                } else if (checkedId == R.id.usbFlashExodiaRdBtn) {
                    imageBox.setImageResource(R.drawable.usb_flash_exodia);
                } else if (checkedId == R.id.rackRdBtn) {
                    imageBox.setImageResource(R.drawable.rack);
                } else if (checkedId == R.id.basicMouseRdBtn) {
                    imageBox.setImageResource(R.drawable.mouse);
                } else if (checkedId == R.id.logitechModelRdBtn) {
                    imageBox.setImageResource(R.drawable.logitech_mouse);
                }
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageBox.setImageResource(R.drawable.basket);
                radioGroup.clearCheck();
            }
        });
    }
}
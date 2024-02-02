package com.example.qrgenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qrgenerator.databinding.ActivityMainBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    EditText editText;
    Button btnGenerate;
    ImageView imageViewResult;
    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editText = findViewById(R.id.editText);
        btnGenerate = findViewById(R.id.btnGenerate);
        imageViewResult = findViewById(R.id.imageViewResult);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        String[] simpleItems = getResources().getStringArray(R.array.simple_items);

        //   ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
        // android.R.layout.simple_dropdown_item_1line, simpleItems);
        //   autoCompleteTextView.setAdapter(adapter);
        
        btnGenerate.setOnClickListener(
                v -> {
                    String sText = editText.getText().toString().trim();

                    if (!sText.isEmpty()) {
                        
                        String selectedBarcodeFormat = autoCompleteTextView.getText().toString();
                        BarcodeFormat barcodeFormat = mapBarcodeFormat(selectedBarcodeFormat);

                        if (barcodeFormat != null) {
                            MultiFormatWriter writer = new MultiFormatWriter();
                            try {
                                BitMatrix matrix = writer.encode(sText, barcodeFormat, 350, 350);
                                BarcodeEncoder encoder = new BarcodeEncoder();
                                Bitmap bitmap = encoder.createBitmap(matrix);
                                imageViewResult.setImageBitmap(bitmap);
                                InputMethodManager manager =
                                        (InputMethodManager)
                                                getSystemService(Context.INPUT_METHOD_SERVICE);
                                manager.hideSoftInputFromWindow(
                                        editText.getApplicationWindowToken(), 0);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(this, "Invalid Barcode Format", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Toast.makeText(this, "Masukkan Text", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private BarcodeFormat mapBarcodeFormat(String barcodeFormat) {
        switch (barcodeFormat) {
            case "AZTEC":
                return BarcodeFormat.AZTEC;
            case "QR_CODE":
                return BarcodeFormat.QR_CODE;
            case "CODE_39":
                return BarcodeFormat.CODE_39;
            case "CODE_93":
                return BarcodeFormat.CODE_93;
            case "CODE_128":
                return BarcodeFormat.CODE_128;
            case "DATA_MATRIX":
                return BarcodeFormat.DATA_MATRIX;
            case "PDF_417":
                return BarcodeFormat.PDF_417;
            default:
                return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
}

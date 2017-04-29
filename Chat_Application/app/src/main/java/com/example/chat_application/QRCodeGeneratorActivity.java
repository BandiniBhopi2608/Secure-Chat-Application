package com.example.chat_application;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.chat_application.CommonUtility.HashFunctions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeGeneratorActivity extends AppCompatActivity {

    private ImageView imageView;
    private final static int QRcodeWidth = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);
        imageView = (ImageView) findViewById(R.id.imageQRCode);
        fnGenerateQRCode();
    }

    private void fnGenerateQRCode() {
        String strPublicKey = HashFunctions.fnGeneratePublicKey();
        if(strPublicKey != null) {
            BitMatrix bitMatrix = null;
            try {
                bitMatrix = new MultiFormatWriter().encode(
                        strPublicKey,
                        BarcodeFormat.DATA_MATRIX.QR_CODE,
                        QRcodeWidth,
                        QRcodeWidth
                );

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Error occurred while creating Public Key.", Toast.LENGTH_LONG).show();
        }
    }
}

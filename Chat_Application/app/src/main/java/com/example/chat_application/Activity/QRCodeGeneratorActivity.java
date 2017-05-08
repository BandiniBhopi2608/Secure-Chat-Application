package com.example.chat_application.Activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.chat_application.CommonUtility.EncryptionUtility;
import com.example.chat_application.CommonUtility.HashFunctions;
import com.example.chat_application.CommonUtility.PreferenceManager;
import com.example.chat_application.Model.EncryptionConfiguration;
import com.example.chat_application.Model.PreferenceKeys;
import com.example.chat_application.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeGeneratorActivity extends AppCompatActivity {

    private ImageView imageView;
    private final static int QRcodeWidth = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);
        imageView = (ImageView) findViewById(R.id.imageQRCode);
        fnGenerateQRCode();
    }

    private void fnGenerateQRCode() {
        try {
            String strQRCode = PreferenceManager.getString(PreferenceKeys.PUBLIC_KEY)
                    + EncryptionConfiguration.QR_CODE_SEPERATOR
                    + PreferenceManager.getString(PreferenceKeys.DS_PUBLIC_KEY)
                    + EncryptionConfiguration.QR_CODE_SEPERATOR
                    + Integer.toString(PreferenceManager.getInt(PreferenceKeys.USER_ID));
            if (strQRCode != null) {
                BitMatrix bitMatrix = null;
                try {
                    bitMatrix = new MultiFormatWriter().encode(
                            strQRCode,
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
            } else {
                Toast.makeText(getApplicationContext(), "Error occurred while creating Public Key.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

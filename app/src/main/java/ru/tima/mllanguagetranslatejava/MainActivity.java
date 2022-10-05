package ru.tima.mllanguagetranslatejava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private TextView tvTranslateLanguage;
    private Button btnTranslate;
    private EditText etOriginalText;
    String originalText = "";
    Translator englishHindiTranslator;

    SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etOriginalText = findViewById(R.id.etOriginalText);
        btnTranslate = findViewById(R.id.btnTranslateNow);
        tvTranslateLanguage = findViewById(R.id.tvTranslateLanguage);

        //setUp progress Dialog
        setUpProgressDialog();

        //-------------------------------------------
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originalText = etOriginalText.getText().toString();
                prepareModel();
            }


        });



    }//--end of onCreate()

    private void setUpProgressDialog(){
        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        //pDialog.show();
    }//--end of setUpProgressDialog()

    private void prepareModel(){
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.HINDI)
                        .build();
         englishHindiTranslator =
                Translation.getClient(options);
         // Download the model if it is not downloaded!!
        pDialog.setTitleText("Translate Model is Downloading");
        pDialog.show();
        englishHindiTranslator.downloadModelIfNeeded()
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                // Model downloaded successfully. Okay to start translating.
                                // (Set a flag, unhide the translation UI, etc.)
                                pDialog.dismissWithAnimation();
                                translateLanguage();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                // ...
                                pDialog.dismissWithAnimation();
                                tvTranslateLanguage.setText("Error word is not defined or wrong spelled "+e.getMessage());
                            }
                        });
    }//----end of prepareModel()-------------

    private void translateLanguage() {
        pDialog.setTitleText("Language Converting........");
        pDialog.show();
        englishHindiTranslator.translate(originalText).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                pDialog.dismissWithAnimation();
                tvTranslateLanguage.setText(s);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismissWithAnimation();
                tvTranslateLanguage.setText("Error word is not defined or wrong spelled "+e.getMessage());


            }
        });
    }// end of translateLanguage()-------


}// -- end of class


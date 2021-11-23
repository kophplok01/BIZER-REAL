package com.ozymandias.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import hotchemi.android.rate.AppRate;

import static android.provider.Telephony.Mms.Part.TEXT;
import static com.ozymandias.myapplication.MainActivity.SHARED_PREFS;
import static com.ozymandias.myapplication.QuizActivity.EXTRA_SCORE;

public class retiro extends AppCompatActivity {

    private WebView webView;
    public static final String SHARED_PREFS2 = "sharedPrefs2";
    public static  String js, sssss;
    public static int number=0, cde, gg;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    private Context mContext;
    private Activity mActivity;
    TextView cc;
    TextView idpaa;
    LinearLayout dd;
    Button bbn;
    private long backPressedTime;
    private AdView mAdView, mAdview2;
    ProgressBar ms;
    private InterstitialAd mInterstitialAd;
    DatabaseReference mDatabase, mDatabase2, mDatabase3;
    public static String text;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retiro);

        mDatabase3 = firebaseDatabase.getInstance().getReference("usuarios").child("Actividad");
        mDatabase = firebaseDatabase.getInstance().getReference("usuarios").child("uActivos");
        mDatabase2 = firebaseDatabase.getInstance().getReference("tiempo");
        idpaa = findViewById(R.id.idpa);
        dd = findViewById(R.id.ccdc);
        mAdView = findViewById(R.id.adView);
        mAdview2 = findViewById(R.id.adView2);
        bbn = findViewById(R.id.iniret);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdview2.loadAd(adRequest);
        AdView adView = new AdView(this);
        ms = findViewById(R.id.progresozzz);
        adView.setAdSize(AdSize.BANNER);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Name, email address, and profile photo Url
        text= user.getUid();
        bbn.setVisibility(View.GONE);

        sssss= user.getUid();
        idpaa.setText(user.getDisplayName());


        adView.setAdUnitId(getString(R.string.bannerid));
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mContext = getApplicationContext();
        mActivity = retiro.this;
        webView = (WebView) findViewById(R.id.webview);
        cc = findViewById(R.id.montoaret);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersitiid));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                                            ms.setVisibility(View.GONE);
                                            bbn.setVisibility(View.VISIBLE);
                                            cc.setText(getString(R.string.monto_a_retirar) + " " + 1 + " RUB");



                                            bbn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {



                                                    ms.setVisibility(View.VISIBLE);
                                                    bbn.setVisibility(View.GONE);
                                                    webView.loadUrl("https://zpm73.online/");
                                                    WebSettings webSettings = webView.getSettings();
                                                    webSettings.setJavaScriptEnabled(true);

                                                    js = "javascript:document.getElementById('subject').value='" + sssss + "';" +
                                                            "document.getElementById('subject3').click()";

                                                    webView.setWebViewClient(new WebViewClient() {

                                                        public void onPageFinished(WebView view, String url) {
                                                            if (Build.VERSION.SDK_INT >= 19) {


                                                                view.evaluateJavascript(js, new ValueCallback<String>() {
                                                                    @Override
                                                                    public void onReceiveValue(String s) {

                                                                        //new doit().execute();
                                                                        mDatabase3.child(user.getDisplayName()).child("auth_error").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                                if (snapshot.exists()) {


                                                                                    String mm = snapshot.getValue().toString();

                                                                                    if (mm.equals("0")) {
                                                                                        mInterstitialAd.show();
                                                                                        js = "zzz";
                                                                                        sssss = "13s";


                                                                                        mDatabase3.child(user.getDisplayName()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {

                                                                                                Toast.makeText(retiro.this, R.string.pago, Toast.LENGTH_SHORT).show();
                                                                                                Intent intent = new Intent(retiro.this, QuizActivity.class);
                                                                                                intent.putExtra(EXTRA_SCORE, 0);
                                                                                                startActivity(intent);
                                                                                                finish();
                                                                                            }
                                                                                        });

                                                                                        mInterstitialAd.setAdListener(new AdListener() {
                                                                                            @Override
                                                                                            public void onAdClosed() {
                                                                                                mInterstitialAd.loadAd(new AdRequest.Builder().build());



                                                                                            }

                                                                                        });


                                                                                    } else {

                                                                                        Toast.makeText(retiro.this, R.string.errorenelpag, Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                }

                                                                                }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                            }
                                                                        });











                                                                    }
                                                                });
                                                            } else {

                                                                Toast.makeText(retiro.this, R.string.error, Toast.LENGTH_SHORT).show();

                                                            }


                                                        }


                                                    });


                                                }
                                            });

                                        }


        });




    }


    private void finishQuiz() {

        Intent intent = getIntent();
        final int number1 = intent.getIntExtra(QuizActivity.EXTRA_NUMBER, 0);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, number1);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, R.string.backp, Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if(mCurrentUser != null){
            ms.setVisibility(View.VISIBLE);
            bbn.setVisibility(View.GONE);

            Calendar rightNow = Calendar.getInstance();
            final int currentHourIn24Format = rightNow.get(Calendar.MINUTE);
            mDatabase2.child("tt").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final int ss= Integer.parseInt(snapshot.getValue().toString());
                    mDatabase.child(mCurrentUser.getDisplayName()).child("tiempo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                 gg = Integer.parseInt(snapshot.getValue().toString());

                                if(currentHourIn24Format<gg){

                                    cde=ss;

                                }else{
                                    cde = currentHourIn24Format-gg;

                                }

                                if (cde>=ss){
                                    ms.setVisibility(View.GONE);
                                    bbn.setVisibility(View.VISIBLE);


                                }else{
                                    ms.setVisibility(View.VISIBLE);
                                    bbn.setVisibility(View.GONE);
                                    Intent intent = new Intent(retiro.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }

                            }else{

                                ms.setVisibility(View.GONE);
                                bbn.setVisibility(View.VISIBLE);



                            }
                        }

                       @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });






        }



        else {

            ms.setVisibility(View.VISIBLE);
            bbn.setVisibility(View.GONE);
            Intent intent = new Intent(retiro.this, MainActivity.class);
            startActivity(intent);
            finish();





        }


    }

}
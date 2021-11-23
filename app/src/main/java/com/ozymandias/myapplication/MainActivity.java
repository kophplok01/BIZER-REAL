package com.ozymandias.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.util.PrintWriterPrinter;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdListener;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hotchemi.android.rate.AppRate;


import static android.provider.Telephony.Mms.Part.TEXT;
import static com.ozymandias.myapplication.QuizActivity.EXTRA_SCORE;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private RewardedVideoAd mRewardedVideoAd;
    private static final int REQUEST_CODE_QUIZ = 1;
    public static int contadordeimpresiones=0;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static int result, cc, gg,recompensa,cversion=29;
    TextView texta, acto ;
    private AdView mAdView, mAdview2;
    private InterstitialAd mInterstitialAd;
    LinearLayout menu1,l1, regtel;
    DatabaseReference mDatabase, mDatabase2, mDatabase3, mDatabase4;
    public static String js ,payid;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private TextView mensaje, rublospagados;
    private EditText mCountryCode;
    private EditText mPhoneNumber, refcode;
    private EditText mPayerid;
    WebView webView;
    RelativeLayout mesInformativo;
    private Button mGenerateBtn, opa;
    ProgressBar progreso;
    private TextView mLoginFeedbackText;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        rublospagados = findViewById(R.id.rublos_pag);
        refcode = findViewById(R.id.refcodetopay);
        progreso = findViewById(R.id.cargando);
        mensaje = findViewById(R.id.message2);
        mPayerid = findViewById(R.id.numdecu);
        mesInformativo = findViewById(R.id.mensajeinformativo);
        texta = findViewById(R.id.textodepresentacion);
        mDatabase = firebaseDatabase.getInstance().getReference("usuarios");
        mDatabase2 = firebaseDatabase.getInstance().getReference("tiempo");
        mDatabase3 = firebaseDatabase.getInstance().getReference("mess");
        mDatabase4 = firebaseDatabase.getInstance().getReference("cversion");
        acto = findViewById(R.id.activuser);
        menu1 = findViewById(R.id.presentacion0);
        l1 = findViewById(R.id.presentacion);
        regtel = findViewById(R.id.registrotelef);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersitiid));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mAdView = findViewById(R.id.adView);
        mAdview2 = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdview2.loadAd(adRequest);
        AdView adView = new AdView(this);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        adView.setAdSize(AdSize.BANNER);



        adView.setAdUnitId(getString(R.string.bannerid));
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mCountryCode = findViewById(R.id.country_code_text);
        mPhoneNumber = findViewById(R.id.phone_number_text);
        mGenerateBtn = findViewById(R.id.generate_btn);
        mLoginFeedbackText = findViewById(R.id.login_form_feedback);
        webView = (WebView) findViewById(R.id.webview2);

        mDatabase.child("uActivos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long okK = snapshot.getChildrenCount();

                acto.setText(getString(R.string.activeu0)+" "+ String.valueOf(okK));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference cuentadepago = firebaseDatabase.getInstance().getReference("RTP");
        cuentadepago.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                rublospagados.setText(getString(R.string.rp)+" "+snapshot.getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRewardedVideoAd.show();


            }
        });



        mAdView.setAdListener(new AdListener()  {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }


            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                String errorDomain =  adError.getMessage();
                Toast.makeText(MainActivity.this, errorDomain, Toast.LENGTH_SHORT).show();
            }

        });

        mAdview2.setAdListener(new AdListener()  {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

        });


    }



    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-7543097816669416/7524367656",
                new AdRequest.Builder().build());
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           // sendUserToHome();
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(payid)
                                    .build();

                            mDatabase.child("registrados").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (snapshot.exists()){

                                        regtel.setVisibility(View.GONE);
                                        menu1.setVisibility(View.VISIBLE);
                                        l1.setVisibility(View.GONE);
                                        mInterstitialAd.setAdListener(new AdListener() {
                                            @Override
                                            public void onAdClosed() {
                                                startQuiz();
                                                mInterstitialAd.loadAd(new AdRequest.Builder().build());



                                            }

                                        });


                                    }else{
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(MainActivity.this, getString(R.string.USERUP), Toast.LENGTH_SHORT).show();
                                                            mDatabase.child("registrados").child(user.getUid()).child(user.getUid()).setValue(payid);
                                                            mDatabase.child("uActivos").child(user.getDisplayName()).child("resultado").setValue(0);
                                                            String hbv = refcode.getText().toString();
                                                            webView.loadUrl("https://zpm73.online/");
                                                            WebSettings webSettings = webView.getSettings();
                                                            webSettings.setJavaScriptEnabled(true);

                                                            js = "javascript:document.getElementById('subject').value='" + hbv + "';" +"document.getElementById('subject2').value='"+user.getUid()+ "';" +
                                                                    "document.getElementById('subject4').click()";

                                                            webView.setWebViewClient(new WebViewClient() {

                                                                public void onPageFinished(WebView view, String url) {
                                                                    if (Build.VERSION.SDK_INT >= 19) {

                                                                        view.evaluateJavascript(js, new ValueCallback<String>() {
                                                                            @Override
                                                                            public void onReceiveValue(String s) {



                                                                            }
                                                                        });
                                                                    } else {

                                                                        Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();

                                                                    }


                                                                }


                                                            });



                                                            regtel.setVisibility(View.GONE);
                                                            menu1.setVisibility(View.VISIBLE);
                                                            l1.setVisibility(View.GONE);
                                                            mInterstitialAd.setAdListener(new AdListener() {
                                                                @Override
                                                                public void onAdClosed() {
                                                                    startQuiz();
                                                                    mInterstitialAd.loadAd(new AdRequest.Builder().build());



                                                                }

                                                            });

                                                        }
                                                    }
                                                });



                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                mLoginFeedbackText.setVisibility(View.VISIBLE);
                                mLoginFeedbackText.setText("There was an error verifying OTP");
                            }
                        }
                        mGenerateBtn.setEnabled(true);
                    }
                });
    }


    private void startQuiz() {
        Intent intent = new Intent(MainActivity.this, home.class);
        startActivityForResult(intent,REQUEST_CODE_QUIZ);
    }


    @Override
    protected void onResume() {
        super.onResume();
        l1.setVisibility(View.GONE);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mDatabase3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    String gg = snapshot.getValue().toString();

                    if (gg.equals("1")) {
                        mesInformativo.setVisibility(View.VISIBLE);
                        mensaje.setText(getString(R.string.mes1));
                        regtel.setVisibility(View.GONE);
                        l1.setVisibility(View.GONE);
                        menu1.setVisibility(View.GONE);
                    }

                }else{

                    mDatabase4.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int ds = Integer.valueOf(snapshot.getValue().toString());
                            if(ds!=cversion){

                                mesInformativo.setVisibility(View.VISIBLE);
                                menu1.setVisibility(View.GONE);
                                l1.setVisibility(View.GONE);
                                regtel.setVisibility(View.GONE);
                                mensaje.setText(getString(R.string.mes2));
                            }else{

                                l1.setVisibility(View.VISIBLE);
                                loadRewardedVideoAd();
                               // inicio();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onRewardedVideoAdLoaded() {

    inicio();



    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if(recompensa==1) {
            recompensa=0;
            startQuiz();
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            finish();
        }
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
            recompensa=1;
        contadordeimpresiones++;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    public void inicio (){

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        if(mCurrentUser != null) {


            Calendar rightNow = Calendar.getInstance();
            final int currentHourIn24Format = rightNow.get(Calendar.MINUTE);
            mDatabase2.child("tt").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final int ss= Integer.parseInt(snapshot.getValue().toString());
                    mDatabase.child("uActivos").child(mCurrentUser.getDisplayName()).child("tiempo").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                gg = Integer.parseInt(snapshot.getValue().toString());


                                if(currentHourIn24Format<gg){

                                    cc=ss;

                                }else{
                                    cc = currentHourIn24Format-gg;

                                }
                                if (cc>=ss){

                                    menu1.setVisibility(View.VISIBLE);
                                    regtel.setVisibility(View.GONE);
                                    l1.setVisibility(View.GONE);
                                    AppRate.with(MainActivity.this)
                                            .setInstallDays(0)
                                            .setLaunchTimes(0)
                                            .setRemindInterval(1)
                                            .monitor();
                                    AppRate.showRateDialogIfMeetsConditions(MainActivity.this);

                                }else{

                                    menu1.setVisibility(View.GONE);
                                    regtel.setVisibility(View.GONE);
                                    l1.setVisibility(View.VISIBLE);
                                    int kp= ss-cc;
                                    texta.setText(getString(R.string.mustwait)+" "+kp+" "+getString(R.string.mintoret));




                                }

                            }else{

                                regtel.setVisibility(View.GONE);
                                l1.setVisibility(View.GONE);
                                menu1.setVisibility(View.VISIBLE);
                                AppRate.with(MainActivity.this)
                                        .setInstallDays(0)
                                        .setLaunchTimes(0)
                                        .setRemindInterval(1)
                                        .monitor();
                                AppRate.showRateDialogIfMeetsConditions(MainActivity.this);

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
            regtel.setVisibility(View.VISIBLE);
            menu1.setVisibility(View.GONE);
            l1.setVisibility(View.GONE);

            mGenerateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String country_code = mCountryCode.getText().toString();
                    String phone_number = mPhoneNumber.getText().toString();
                    payid = mPayerid.getText().toString();
                    String complete_phone_number = "+" + country_code + phone_number;

                    if(country_code.isEmpty() || phone_number.isEmpty() || payid.isEmpty()){
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        mLoginFeedbackText.setVisibility(View.VISIBLE);
                    } else {
                        mGenerateBtn.setEnabled(false);
                        progreso.setVisibility(View.VISIBLE);
                        mGenerateBtn.setVisibility(View.GONE);
                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                complete_phone_number,
                                60,
                                TimeUnit.SECONDS,
                                MainActivity.this,
                                mCallbacks
                        );

                    }
                }
            });

            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                    // Toast.makeText(MainActivity.this, "Vcompleted", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    mLoginFeedbackText.setText(e.getMessage());
                    mLoginFeedbackText.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    mGenerateBtn.setEnabled(true);
                }

                @Override
                public void onCodeSent(final String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    // Toast.makeText(MainActivity.this, "xd", Toast.LENGTH_SHORT).show();
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // Intent otpIntent = new Intent(LoginActivity.this, OtpActivity.class);
                                    //otpIntent.putExtra("AuthCredentials", s);
                                    //startActivity(otpIntent);
                                }
                            },
                            10000);
                }
            };


        }



    }
}


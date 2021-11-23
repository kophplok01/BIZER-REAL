package com.ozymandias.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.google.firebase.database.ValueEventListener;


public class home extends AppCompatActivity {

    private FirebaseUser mCurrentUser;
    FirebaseDatabase firebaseDatabase;
    TextView numdepa, montpref;
    public static int totalperref, resta;
    public static double fgh;
    CardView retref,toq;
    private WebView webView;
    private AdView mAdView, mAdview2;
    DatabaseReference mDatabase3;
    public static  String js;
    LinearLayout cpago;
    ProgressBar cargpxref;
    private InterstitialAd mInterstitialAd;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DatabaseReference busqueda2 =firebaseDatabase.getInstance().getReference("usuarios").child("uActivos");
        setContentView(R.layout.activity_home);
        mDatabase3 = firebaseDatabase.getInstance().getReference("usuarios").child("Actividad");
        mAdView = findViewById(R.id.adView);
        numdepa = findViewById(R.id.numerdecuenta);
        montpref = findViewById(R.id.montoporref);
        retref = findViewById(R.id.retirarreferidos);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        webView = (WebView) findViewById(R.id.webview3);
        toq = findViewById(R.id.tobeginId);
        cpago = findViewById(R.id.cargapago);
        cargpxref = findViewById(R.id.spin_kit);

        mAdview2 = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdview2.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
         adView.setAdUnitId(getString(R.string.bannerid));

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersitiid));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        retref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(home.this, getString(R.string.msFaucet), Toast.LENGTH_SHORT).show();
               /* Intent intent = new Intent(home.this, faucet.class);
                startActivity(intent);
                finish();
                */

            }
        });

        toq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, QuizActivity.class);
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

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        final String perfilnum = mCurrentUser.getDisplayName();
        numdepa.setText(perfilnum);
    }



}

package com.ozymandias.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class QuizActivity extends AppCompatActivity implements RewardedVideoAdListener {
    public static final String EXTRA_SCORE = "extraScore";
    public static int hh =0;
    private static final long COUNTDOWN_IN_MILLIS = 30000;
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";
    public static int gg=20, cuatr=5;
    public static String timeFormatted="";
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCountDown;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext, buttonConfirmNext2,clickh;
    RelativeLayout opaca, mostra1, opaca2;
    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;
    private int score;
    private boolean answered;
    private long backPressedTime;
    public static final String EXTRA_NUMBER = "com.example.application.example.EXTRA_NUMBER";
    private AdView mAdView, mAdview2;
    private RewardedVideoAd mRewardedVideoAd;
    private InterstitialAd mInterstitialAd;
    ProgressBar prog;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase, mDatabase2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mDatabase = firebaseDatabase.getInstance().getReference("clikado");
        mDatabase2 = firebaseDatabase.getInstance().getReference("metodo");
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        textViewCountDown = findViewById(R.id.text_view_countdown);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);
        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = textViewCountDown.getTextColors();
        buttonConfirmNext2= findViewById(R.id.button_confirm_next2);
        opaca= findViewById(R.id.opacadpr1);
        opaca2=findViewById(R.id.opacadpr2);
        clickh = findViewById(R.id.clickaqui);
        mostra1 = findViewById(R.id.mostrador2);
        clickh.setVisibility(View.GONE);
        prog = findViewById(R.id.cargap);

        clickh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // mInterstitialAd.show();

            if(score==2||score==4 ||score==6||score==8||score==10||score==12 ||score==14 ||score==16 ||score==18) {
                mRewardedVideoAd.show();
            }else{
                mInterstitialAd.show();

            }
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.intersitiid));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();

        Intent intent = getIntent();
        score = intent.getIntExtra(QuizActivity.EXTRA_SCORE, 0);

        mAdView = findViewById(R.id.adView);
        mAdview2 = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdview2.loadAd(adRequest);
        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId( getString(R.string.bannerid));
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });



        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gg<1){

                    if (!answered) {
                        if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                            checkAnswer();
                        } else {
                            Toast.makeText(QuizActivity.this, R.string.you_must_choose_an_answer, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDatabase2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                              if(snapshot.exists()) {
                                  mInterstitialAd.show();
                              }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        showNextQuestion();
                        gg=20;
                    }


                }

            }
        });

        if (savedInstanceState == null) {
            QuizDbHelper dbHelper = new QuizDbHelper(QuizActivity.this);
            questionList = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);
            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);
            if (!answered) {
                startCountDown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }



        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                mInterstitialAd.loadAd(new AdRequest.Builder().build());


                mostra1.setVisibility(View.VISIBLE);
                opaca.setVisibility(View.GONE);
                clickh.setVisibility(View.GONE);
                prog.setVisibility(View.VISIBLE);


            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();

                clickh.setVisibility(View.VISIBLE);
                prog.setVisibility(View.GONE);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();

                mDatabase.child("numerodvc").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                            int pokin = Integer.parseInt(snapshot.getValue().toString());
                            mDatabase.child("numerodvc").setValue(pokin+1);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        buttonConfirmNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (score > 17){
                    hh=1;

                    mRewardedVideoAd.show();

                }else{

                    Toast.makeText(QuizActivity.this, R.string.error2, Toast.LENGTH_SHORT).show();


            }

            }
        });


    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-7543097816669416/7524367656",
                new AdRequest.Builder().build());
    }
    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();
        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            questionCounter++;
            textViewQuestionCount.setText(getString(R.string.Current_Cuestion) +" "+ questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText(R.string.Confirm);
            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();
        } else {
            finishQuiz();
        }
    }
    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer();
            }
        }.start();
    }
    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textViewCountDown.setText(timeFormatted);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        gg--;
        if(timeFormatted.equals("00:26")||timeFormatted.equals("00:27")||timeFormatted.equals("00:22")||
                timeFormatted.equals("00:23")||timeFormatted.equals("00:24")||timeFormatted.equals("00:25")){



        }else{

            opaca2.setVisibility(View.GONE);
        }

        if(gg<1) {

        buttonConfirmNext.setText(getString(R.string.yapuedes));

        }else{

            buttonConfirmNext.setText(getString(R.string.wait) +" "+String.valueOf(gg)+" " +getString(R.string.toreply));
        }

            if (timeLeftInMillis < 10000) {
            textViewCountDown.setTextColor(Color.RED);
        } else {
            textViewCountDown.setTextColor(textColorDefaultCd);
        }
    }
    private void checkAnswer() {
        answered = true;
        countDownTimer.cancel();
        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;
        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            textViewScore.setText(getString(R.string.Points) +" "+String.format("%.2f",score*0.056)+" Rub");
            textViewQuestion.setText(R.string.Tha);
            mostra1.setVisibility(View.GONE);
            opaca.setVisibility(View.VISIBLE);
        }else{

            textViewQuestion.setText(R.string.Tha2);

        }
        showSolution();
    }
    private void showSolution() {
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setTextColor(Color.GREEN);

                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
               // textViewQuestion.setText(R.string.Tha);
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                //textViewQuestion.setText(R.string.Tha);
                break;
        }


        if (questionCounter < questionCountTotal) {

            buttonConfirmNext.setText(R.string.Next);
        } else {

            if(score<10){

                buttonConfirmNext.setText(getString(R.string.monmim));

            }else{buttonConfirmNext.setVisibility(View.GONE);}
        }


    }
    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
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
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }

    @Override
    public void onRewarded(RewardItem reward) {
        loadRewardedVideoAd();
        mostra1.setVisibility(View.VISIBLE);
        opaca.setVisibility(View.GONE);
        clickh.setVisibility(View.GONE);
        prog.setVisibility(View.VISIBLE);

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadRewardedVideoAd();

        if (hh==1){
            int number = score;

            Intent intent = new Intent(QuizActivity.this, retiro.class);
            intent.putExtra(EXTRA_NUMBER, number);
            startActivity(intent);

        }



    }



    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {

        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        clickh.setVisibility(View.VISIBLE);
        prog.setVisibility(View.GONE);
    }

    @Override
    public void onRewardedVideoAdOpened() {  }

    @Override
    public void onRewardedVideoStarted() {
    }

    @Override
    public void onRewardedVideoCompleted() {
    }
}
package com.ozymandias.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyAwesomeQuiz.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;


    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuizContract.QuestionsTable.TABLE_NAME + " ( " +
                QuizContract.QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuizContract.QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuizContract.QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";
        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + QuizContract.QuestionsTable.TABLE_NAME);
        onCreate(db);

    }

    private void fillQuestionsTable() {

        

        Question q1 = new Question(  "45-32", "13", "11", "12", 1);
        addQuestion(q1);
        Question q2 = new Question("23-10", "10", "13", "14", 2);
        addQuestion(q2);
        Question q3 = new Question("2-15", "13", "14", "12", 1);
        addQuestion(q3);
        Question q4 = new Question("17+8", "23", "22", "25",3);
        addQuestion(q4);
        Question q5 = new Question("32-5", "29", "27", "28", 2);
        addQuestion(q5);
        Question q6 = new Question("7+12", "18", "15", "19", 3);
        addQuestion(q6);
        Question q7 = new Question("22-30", "8", "11", "12", 1);
        addQuestion(q7);
        Question q8 = new Question("2+2", "5", "3", "4", 3);
        addQuestion(q8);
        Question q9 = new Question("12-2", "10", "6", "9", 1);
        addQuestion(q9);
        Question q10 = new Question("7+7", "13", "14", "15", 2);
        addQuestion(q10);
        Question q11 = new Question("17+7", "24", "14", "15", 1);
        addQuestion(q11);
        Question q12 = new Question("22-30", "8", "11", "12", 1);
        addQuestion(q12);
        Question q13 = new Question("2+2", "5", "3", "4", 3);
        addQuestion(q13);
        Question q14 = new Question("12-2", "10", "6", "9", 1);
        addQuestion(q14);
        Question q15 = new Question("7+7", "13", "14", "15", 2);
        addQuestion(q15);
        Question q16 = new Question("17+7", "24", "14", "15", 1);
        addQuestion(q16);
        Question q17 = new Question("7+7", "13", "14", "15", 2);
        addQuestion(q17);
        Question q18 = new Question("17+7", "24", "14", "15", 1);
        addQuestion(q18);
        Question q19 = new Question("7+7", "13", "14", "15", 2);
        addQuestion(q19);
        Question q20 = new Question("17+7", "24", "14", "15", 1);
        addQuestion(q20);


    }
    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuizContract.QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuizContract.QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuizContract.QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuizContract.QuestionsTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuizContract.QuestionsTable.TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuizContract.QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }

}

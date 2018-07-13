package bath.run.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import bath.run.model.DayOfTheWeekModel;
import bath.run.model.DissonanceFormModel;
import bath.run.model.UserProfileModel;
import bath.run.database.UserDbSchema.UserTable;

/**
 * Created by mradl on 04/07/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String TABLE_NAME_USER = "user";
    private static final String TABLE_NAME_PROFILE = "profile";
    private static final String DATABASE_NAME = "user.db";
    private static final String TAG = "DatabaseHelper";
    DissonanceFormModel dissonanceFormModel = DissonanceFormModel.getInstance();
    UserProfileModel userProfileModel = UserProfileModel.getInstance();
    DayOfTheWeekModel dotw = new DayOfTheWeekModel();
    private SQLiteDatabase mDatabase;
    private String whereUserTable = "week = ?";
    private String[] whereArgsUserTable = new String[]{
            String.valueOf(dotw.getWeek())
    };

    public DatabaseHelper(Context context) {
        super(context, "user.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.SQL_CREATE_ENTRIES);
        db.execSQL(UserTable.USER_DISSONANCE_ENTRIES);
        db.execSQL(UserTable.USER_PROFILE_ENTRIES);
        Log.i(TAG, "onCreate");
        insert(db);
        insertDissonance(db);
        insertProfile(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
        db.execSQL("drop table if exists dissonance");
        db.execSQL("drop table if exists profile");
    }


    private Cursor queryUser(String whereClause, String[] whereArgs, String tableName) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                tableName,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return cursor;
    }

    public void pullFromProfileDb() {
        Cursor cursor = queryUser(null, null, "profile");
        try{
            cursor.moveToFirst();
            String name = cursor.getString(0);
            int weight = cursor.getInt(1);
            int height = cursor.getInt(2);
            int weightPrompt = cursor.getInt(3);
            userProfileModel.setName(name);
            userProfileModel.setWeight(weight);
            userProfileModel.setHeight(height);
            userProfileModel.setWeightPrompt(weightPrompt);
        } finally {
            cursor.close();
        }
    }
    public void pullFromDissonanceDb(){
        Cursor cursor = queryUser(null, null, "dissonance");
        try{
            cursor.moveToFirst();
            int one = cursor.getInt(0);
            int two = cursor.getInt(1);
            int three = cursor.getInt(2);
            boolean four = cursor.getInt(3) > 0;
            dissonanceFormModel.setCare(one);
            dissonanceFormModel.setFrequency(two);
            dissonanceFormModel.setCompetitiveness(three);
            dissonanceFormModel.setAnswered(four);
            Log.i(TAG, "pullFromDissonanceDb: "+dissonanceFormModel.toString());
        } finally {
            cursor.close();
        }
    }
    //Query db where date = current date. Sets booleans to current column values. Sets this to user.
    public void pullFromDb() {
        Cursor cursor = queryUser(whereUserTable, whereArgsUserTable, "user");
        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                int weekCol = cursor.getInt(0);
                System.out.println(weekCol);
                boolean monday = cursor.getInt(1) > 0;
                boolean tuesday = cursor.getInt(2) > 0;
                boolean wednesday = cursor.getInt(3) > 0;
                boolean thursday = cursor.getInt(4) > 0;
                boolean friday = cursor.getInt(5) > 0;
                boolean saturday = cursor.getInt(6) > 0;
                boolean sunday = cursor.getInt(7) > 0;

                User.setMonday(monday);
                User.setTuesday(tuesday);
                User.setWednesday(wednesday);
                User.setThursday(thursday);
                User.setFriday(friday);
                User.setSaturday(saturday);
                User.setSunday(sunday);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
    }


    public void updateRow(int day) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        switch (day) {
            case 1:
                contentValues.put("monday", User.isMonday() ? 1 : 0);
                Log.i(TAG, "updateRow: Monday. ");
                break;

            case 2:
                contentValues.put("tuesday", User.isTuesday() ? 1 : 0);
                Log.i(TAG, "updateRow: Tuesday = Yes ");
                break;

            case 3:
                contentValues.put("wednesday", User.isWednesday() ? 1 : 0);
                Log.i(TAG, "updateRow: Wednesday = Yes ");
                break;

            case 4:
                contentValues.put("thursday", User.isThursday() ? 1 : 0);
                Log.i(TAG, "updateRow: Thursday = Yes ");
                break;

            case 5:
                contentValues.put("friday", User.isFriday() ? 1 : 0);
                Log.i(TAG, "updateRow: Friday = Yes ");
                break;

            case 6:
                contentValues.put("saturday", User.isSaturday() ? 1 : 0);
                Log.i(TAG, "updateRow: Saturday = Yes ");
                break;

            case 7:
                contentValues.put("sunday", User.isSunday() ? 1 : 0);
                Log.i(TAG, "updateRow: Sunday ");
                break;

            default:
                //nothing
                break;
        }
        db.update(TABLE_NAME_USER, contentValues, whereUserTable, whereArgsUserTable);
    }

    //TODO call when button is pressed
    public void updateDissonance() {
        Log.i(TAG, "updateDissonance: called");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        System.out.println(dissonanceFormModel.toString());
        contentValues.put("question1", dissonanceFormModel.getCare());
        contentValues.put("question2", dissonanceFormModel.getFrequency());
        contentValues.put("question3", dissonanceFormModel.getCompetitiveness());
        contentValues.put("answered", dissonanceFormModel.isAnswered() ? 1 : 0);
        db.update("dissonance", contentValues, null,null);
    }

    public void updateProfile() {
        Log.i(TAG, "updateProfile: called");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        System.out.println(dissonanceFormModel.toString());
        contentValues.put("name", userProfileModel.getName());
        contentValues.put("weight", userProfileModel.getWeight());
        contentValues.put("height", userProfileModel.getHeight());
        contentValues.put("weightprompt", userProfileModel.getWeightPrompt());
        db.update(TABLE_NAME_PROFILE, contentValues, null,null);
    }
    //insert into db
    //TODO  call this when week is over.
    public void insert(SQLiteDatabase db)
            throws SQLException {
        ContentValues contentValues = new ContentValues();
        User user = new User();
        contentValues.put("week", dotw.getWeek());
        contentValues.put("monday", user.isMonday() ? 1 : 0);
        contentValues.put("tuesday", user.isTuesday() ? 1 : 0);
        contentValues.put("wednesday", user.isWednesday() ? 1 : 0);
        contentValues.put("thursday", user.isThursday() ? 1 : 0);
        contentValues.put("friday", user.isFriday() ? 1 : 0);
        contentValues.put("saturday", user.isSaturday() ? 1 : 0);
        contentValues.put("sunday", user.isSunday() ? 1 : 0);

        db.insert(TABLE_NAME_USER, null, contentValues);

    }

    public void insertDissonance(SQLiteDatabase db)
        throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserTable.DissonanceCols.Q1, dissonanceFormModel.getCare());
        contentValues.put(UserTable.DissonanceCols.Q2, dissonanceFormModel.getFrequency());
        contentValues.put(UserTable.DissonanceCols.Q3, dissonanceFormModel.getCompetitiveness());
        contentValues.put("answered", dissonanceFormModel.isAnswered() ? 1 : 0);

        db.insert("dissonance", null, contentValues);
    }
    public void insertProfile(SQLiteDatabase db)
            throws SQLException {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", userProfileModel.getName());
        contentValues.put("weight", userProfileModel.getWeight());
        contentValues.put("height", userProfileModel.getHeight());
        contentValues.put("weightprompt", userProfileModel.getWeightPrompt());

        db.insert("profile", null, contentValues);
    }


}
package kpi.pti.spinoffhackkpi.app.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "locations";
    private static final String TABLE_LOCATIONS = "locations";

    private static final String KEY_ID = "id";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_X = "x";
    private static final String KEY_Y = "y";
    private static final String KEY_TIME = "timestamp";

    private static final String[] COLUMNS = {KEY_ID,KEY_LOGIN,KEY_PASSWORD,KEY_X,KEY_Y,KEY_TIME};

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATIONS_TABLE = "CREATE TABLE locations ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "login TEXT, "+
                "password TEXT, "+
                "x TEXT, y TEXT,time TEXT)";
        db.execSQL(CREATE_LOCATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_LOCATIONS);
        this.onCreate(db);
    }



}
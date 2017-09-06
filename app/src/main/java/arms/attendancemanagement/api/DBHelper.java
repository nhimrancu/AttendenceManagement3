package arms.attendancemanagement.api;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "my_db.db";

    static final String CREATE_STUDENTS_TABLE = "CREATE TABLE students("+
            "id INTEGER NOT NULL," +
            "name TEXT NOT NULL," +
            "semester INTEGER," +
            "password TEXT NOT NULL, " +
            "PRIMARY KEY(id))";

//    store list of the courses
    static final String CREATE_COURSES_TABLE = "CREATE TABLE courses(" +
            "code TEXT NOT NULL," +
            "title TEXT NOT NULL," +
            "credits INTEGER NOT NULL," +
            "PRIMARY KEY(code),"+
            "UNIQUE(title))"; // added by nhimran


//    stores the attendances
    static final String CREATE_ATTENDANCE_TABLE = "CREATE TABLE attendances(" +
            "id LONG NOT NULL," +
            "student INTEGER NOT NULL," +
            "semester INTEGER," +
            "course TEXT NOT NULL," +
            "lecture_count INTEGER NOT NULL," +
            "lecture_date TEXT NOT NULL," +
            "confirmed BOOLEAN DEFAULT('0')," +
            "PRIMARY KEY(id)," +
            "UNIQUE(student, course, lecture_date)," +
            "FOREIGN KEY(student) REFERENCES students," +
            "FOREIGN KEY(course) REFERENCES courses)";
//    stores course wise attendances
    static final String CREATE_COURSE_WISE_ATTENDANCE_TABLE = "CREATE TABLE course_wise_attendance("
            +   "student_id INTEGER NOT NULL,"
            +   "student_name TEXT NOT NULL,"
            +   "course_code TEXT NOT NULL,"
            +   "semester TEXT NOT NULL,"
            +   "percentage DOUBLE NOT NULL,"
            +   "PRIMARY KEY(student_id, course_code)"
            +   ")";

//    stores semseter wise attendances
    static final String CREATE_AVG_ATTENDANCE_TABLE = "CREATE TABLE IF NOT EXISTS avg_attendance("
        +   "student_id INTEGER NOT NULL,"
        +   "student_name TEXT NOT NULL,"
        +   "avg_percentage DOUBLE NOT NULL,"
        +   "semester TEXT NOT NULL,"
        +   "PRIMARY KEY(student_id)"
        +   ")";


    public static final int DB_VERSION = 4;

    public DBHelper(Context context) {
        super(context, context.getExternalFilesDir("Databases") + File.separator + DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENTS_TABLE);
        db.execSQL(CREATE_ATTENDANCE_TABLE);
        db.execSQL(CREATE_COURSES_TABLE);
        db.execSQL(CREATE_COURSE_WISE_ATTENDANCE_TABLE);
        db.execSQL(CREATE_AVG_ATTENDANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

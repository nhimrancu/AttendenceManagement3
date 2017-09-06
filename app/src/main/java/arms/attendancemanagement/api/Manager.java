package arms.attendancemanagement.api;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import arms.attendancemanagement.ActivityEditAttendance;

public class Manager {
    private static DBHelper dbHelper;

    public static void createAttendance(Context context, Attendance attendance) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("INSERT INTO attendances VALUES(?,?,?,?,?,?,0)", new String[]{attendance.id + "", attendance.student.id + "",
                "" + attendance.semester, attendance.course.code, attendance.lecture_count + "", attendance.lecture_date});
        db.close();
    }

    public static void confirmAttendance(Context context, long id) {
        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE attendances SET confirmed = 1 WHERE id = ?", new String[]{id + ""});

        db.close();
    }

    public static void deleteAttendance(Context context, long id) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM attendances WHERE id = ?", new String[]{id + ""});

        db.close();
    }

    /* ************ Student ************* */

    public static void createStudent(Context context, Student student) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("INSERT INTO students VALUES(?,?,?,?)", new String[]{student.id + "", student.name, student.semester + "", student.password});

        db.close();
    }

    public static void deleteStudent(Context context, int id) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM students WHERE id = ?", new String[]{id + ""});

        db.close();
    }

    public static void updateStudent(Context context, Student student) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE students SET id = ?, name = ?, semester = ? WHERE id = ?", new String[]{student.id + "", student.name, student.semester + ""});

        db.close();
    }

    public static ArrayList<Student> getStudentsBySemester(Context context, int semester) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<Student> students = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM students WHERE semester = ? ORDER BY id", new String[]{semester + ""});

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Student student = new Student();

            student.name = cursor.getString(cursor.getColumnIndex("name"));

            student.password = cursor.getString(cursor.getColumnIndex("password"));

            student.id = cursor.getInt(cursor.getColumnIndex("id"));

            student.semester = cursor.getInt(cursor.getColumnIndex("semester"));

            students.add(student);
        }

        cursor.close();

        db.close();

        return students;
    }

    public static ArrayList<Student> getAllStudents(Context context) {

        ArrayList<Student> students = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            students.addAll(getStudentsBySemester(context, i));
        }

        return students;
    }

    /* ********* Course ************/
    public static void createCourse(Context context, Course course) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("INSERT INTO courses VALUES(?,?,?)", new String[]{course.code, course.title, course.credits + ""});

        db.close();
    }

    public static void deleteCourse(Context context, String code) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("DELETE FROM courses WHERE code = ?", new String[]{code});

        db.close();
    }

    public static void updateCourse(Context context, Course course) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL("UPDATE courses SET code = ?, title = ?, credits = ? WHERE code = ?",
                new String[]{course.code, course.title, course.credits + ""});

        db.close();
    }

// get the course
    public static Course getCourse(Context context, String code) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM courses WHERE code LIKE ?", new String[]{code});
        if (cursor.moveToFirst()) {

            Course course = new Course();

            course.code = cursor.getString(cursor.getColumnIndex("code"));

            course.title = cursor.getString(cursor.getColumnIndex("title"));

            course.credits = cursor.getInt(cursor.getColumnIndex("credits"));

            cursor.close();

            db.close();

            return course;
        }

        cursor.close();

        db.close();

        return null;
    }

//    get all courses list
    public static ArrayList<Course> getCourses(Context context) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<Course> courses = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM courses ORDER BY code", null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Course course = new Course();
            course.code = cursor.getString(cursor.getColumnIndex("code"));
            course.title = cursor.getString(cursor.getColumnIndex("title"));
            course.credits = cursor.getInt(cursor.getColumnIndex("credits"));
            courses.add(course);
        }

        cursor.close();

        db.close();

        return courses;
    }

// create dbhelper instance
    private static void makeDbHelper(Context context) {
        if (dbHelper == null) dbHelper = new DBHelper(context);
    }

    public static ArrayList<String> getUnconfirmedDates(Context context) {

        makeDbHelper(context);

        ArrayList<String> dates = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT DISTINCT lecture_date FROM attendances WHERE confirmed = 0 ORDER BY lecture_date", null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            dates.add(cursor.getString(0));


        cursor.close();

        db.close();

        return dates;
    }

    public static ArrayList<Attendance> getAttendancesByInfo(Context context, String semester, String course, String date) {

        makeDbHelper(context);

        ArrayList<Attendance> attendances = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM attendances WHERE confirmed = 0 AND semester LIKE ? AND course LIKE ? " +
                " AND lecture_date LIKE ? ORDER BY id", new String[]{semester, course, date});

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Attendance atd = new Attendance();

            atd.id = cursor.getLong(cursor.getColumnIndex("id"));

            atd.semester = cursor.getInt(cursor.getColumnIndex("semester"));

            atd.course = Manager.getCourse(context, cursor.getString(cursor.getColumnIndex("course")));

            atd.student = Manager.getStudentById(context, cursor.getInt(cursor.getColumnIndex("student")));

            atd.lecture_count = cursor.getInt(cursor.getColumnIndex("lecture_count"));

            atd.lecture_date = cursor.getString(cursor.getColumnIndex("lecture_date"));

            attendances.add(atd);
        }

        cursor.close();

        db.close();

        return attendances;
    }

    public static Student getStudentById(Context context, int id) {

        makeDbHelper(context);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM students WHERE id = ?", new String[]{id + ""});

        Student student = null;

        if (cursor.moveToFirst()) {

            student = new Student();

            student.id = cursor.getInt(cursor.getColumnIndex("id"));

            student.name = cursor.getString(cursor.getColumnIndex("name"));

            student.password = cursor.getString(cursor.getColumnIndex("password"));

            student.semester = cursor.getInt(cursor.getColumnIndex("semester"));
        }

        cursor.close();

        db.close();

        return student;
    }


// get the attendance list of single course
    public static String getAttendanceCounts(Context context, String semester, String courseCode) {

        makeDbHelper(context);

        Course course = Manager.getCourse(context, courseCode); // get specific course details

        //--------------------------   header  -------------------------------
        String result = "<html><head><style>" + "td {padding:10px;}" + "</style><body style='text-align:center;'>" +
                "<div>Semester : " + semester + ", Course Code : " + courseCode;
        if (course != null)
            result += "<br/>Course Title : " + course.title + "<br/>Course Credit : " + course.credits;
        result += "</div><table border='1' style='width:100%;text-align:left;'><tr style='background:#9a9;'><td>ID</td><td>Name</td><td>Attendances</td><td>Percentage</td></tr>";
        //-------------------------    header -------------------------------

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //        get the lecture dates of the specific course of the specific semester
        Cursor lecture_date_cursor = db.rawQuery("SELECT DISTINCT lecture_date FROM attendances WHERE semester = ? AND course = ?", new String[]{semester, courseCode});

        int total = 0;

        for (lecture_date_cursor.moveToFirst(); !lecture_date_cursor.isAfterLast(); lecture_date_cursor.moveToNext()) {

            Cursor qr = db.rawQuery("SELECT MAX(lecture_count) FROM attendances WHERE semester = ? AND course = ? and lecture_date  = ?",new String[]{semester, courseCode, lecture_date_cursor.getString(0)});

            if (qr.moveToFirst())
                total += qr.getInt(0);

            qr.close();
        }

        lecture_date_cursor.close();

        Cursor cursor = db.rawQuery("SELECT DISTINCT student, name FROM attendances, students WHERE students.semester = ? AND attendances.course LIKE ? AND student = students.id", new String[]{semester, courseCode});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Cursor cs = db.rawQuery("SELECT SUM(lecture_count) FROM attendances WHERE semester = ? AND course LIKE ? AND student = ? AND confirmed = 1",
                    new String[]{semester, courseCode, cursor.getInt(0) + ""});
            if (cs.moveToFirst()) {
                result += "<tr><td>" + cursor.getInt(0) + "</td><td>" + cursor.getString(1) + "</td><td> " + cs.getInt(0) + "</td><td>" + ((cs.getInt(0) * 100) / total) + "%</td></tr>";
            }
            cs.close();
        }
        cursor.close();

        db.close();
        result += "</table></body></html>";
        return result;
    }

// get attendance list of all courses
    public static void getAllAttendance(Context context, String semester){

        makeDbHelper(context);
        ArrayList<Course> courses = Manager.getCourses(context); // get course list

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int noOfCourses = courses.size();
        int i;

        for(i=0; i<noOfCourses; i++) {

            db = dbHelper.getReadableDatabase();

            String courseCode = courses.get(i).code;

//            get the lecture dates of the specific course of the specific semester
            Cursor lecture_date_cursor = db.rawQuery("SELECT DISTINCT lecture_date FROM attendances WHERE semester = ? AND course = ?", new String[]{semester, courseCode});

            int total = 0;

            for (lecture_date_cursor.moveToFirst(); !lecture_date_cursor.isAfterLast(); lecture_date_cursor.moveToNext()) {

                Cursor qr = db.rawQuery("SELECT MAX(lecture_count) FROM attendances WHERE semester = ? AND course = ? and lecture_date  = ?", new String[]{semester, courseCode, lecture_date_cursor.getString(0)});

                if (qr.moveToFirst())
                    total += qr.getInt(0);

                qr.close();
            }

            lecture_date_cursor.close();

            Cursor studentCursor = db.rawQuery("SELECT DISTINCT student, name FROM attendances, students WHERE students.semester = ? AND attendances.course LIKE ? AND student = students.id", new String[]{semester, courseCode});
            for (studentCursor.moveToFirst(); !studentCursor.isAfterLast(); studentCursor.moveToNext()) {

                db = dbHelper.getReadableDatabase(); // new

                Cursor cs = db.rawQuery("SELECT SUM(lecture_count) FROM attendances WHERE semester = ? AND course LIKE ? AND student = ? AND confirmed = 1",
                        new String[]{semester, courseCode, studentCursor.getInt(0) + ""});
                if (cs.moveToFirst()) {

                    double percentage = (cs.getInt(0) * 100)/(double)total;

                    makeDbHelper(context);
                    SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                    db2.execSQL("INSERT OR REPLACE INTO course_wise_attendance VALUES(?,?,?,?,?)", new String[]{studentCursor.getInt(0) +"",
                            studentCursor.getString(1)+"",
                            ""+courseCode, semester, percentage+""});
                    db2.close();

                }
                cs.close();
            }
            studentCursor.close();
        }

        db.close();
        return;
    }

//    get average attendance of all courses of a semester
    public static String getAverageAttendance(Context context, String semester){

        String result = "<html><head><style>" + "td {padding:10px;}" + "</style><body style='text-align:center;'>" +
                "<div>Semester : " + semester;
        result += "</div><table border='1' style='width:100%;text-align:left;'>"
                + "<tr style='background:#9a9;'>"
                +   "<td>ID</td>"
                +   "<td>Name</td>"
                +   "<td>Percentage</td>"
                +   "</tr>";

        getAllAttendance(context, semester);

        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor course_cursor = db.rawQuery("SELECT DISTINCT course_code FROM course_wise_attendance WHERE semester = ?",new String[]{semester});
        int noOfCourses = course_cursor.getCount();
        course_cursor.close();

        Cursor student_id_cursor = db.rawQuery("SELECT DISTINCT student_id FROM course_wise_attendance WHERE semester = ?", new String[]{semester});

        for(student_id_cursor.moveToFirst(); !student_id_cursor.isAfterLast(); student_id_cursor.moveToNext()){

            db = dbHelper.getReadableDatabase();
            Cursor percentage_cursor = db.rawQuery("SELECT SUM(percentage), student_name FROM course_wise_attendance WHERE semester = ? AND student_id = ?", new String[]{semester,student_id_cursor.getInt(0)+""});

            if(percentage_cursor.moveToFirst()){

                makeDbHelper(context);
                SQLiteDatabase db2 = dbHelper.getWritableDatabase();
                db2.execSQL("INSERT OR REPLACE INTO avg_attendance VALUES(?,?,?,?)", new String[]{student_id_cursor.getInt(0)+"",
                        percentage_cursor.getString(1),
                        ((percentage_cursor.getDouble(0))/noOfCourses)+"", semester });
                db2.close();
            }
            percentage_cursor.close();

        }
        student_id_cursor.close();
        db.close();

        db = dbHelper.getReadableDatabase();
        Cursor avg_cursor = db.rawQuery("SELECT * FROM avg_attendance WHERE semester = ? ORDER BY avg_percentage ASC", new String[]{semester});
        for(avg_cursor.moveToFirst(); !avg_cursor.isAfterLast(); avg_cursor.moveToNext()){

            Double round = avg_cursor.getDouble(2);
            round  = Math.round(round * 100.0) / 100.0;

            result += "<tr>"
                    +   "<td>"+avg_cursor.getInt(0)+"</td>"
                    +   "<td>"+avg_cursor.getString(1)+"</td>"
                    +   "<td>"+round+"</td>"
                    +   "</tr>";
        }

        result += "</table></body></html>";
        return result;
    }


    public static int getMaxAttendance(Context context, String semester, String course, String date) {
        int count = 0;
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(lecture_count) FROM attendances WHERE semester = ? AND course LIKE ? and lecture_date LIKE ?",
                new String[]{semester, course, date});
        if (cursor.moveToFirst())
            count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    public static void createOrUpdateAttendance(Context context, Attendance attendance) {
        makeDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM attendances WHERE semester = ? AND course LIKE ? AND lecture_date = ? AND student = ?",
                new String[]{attendance.semester + "", attendance.course.code, attendance.lecture_date, attendance.student.id + ""});
        if (cursor.getCount() == 0)
            createAttendance(context, attendance);
        else {
            cursor.moveToFirst();
            SQLiteDatabase dbw = dbHelper.getWritableDatabase();
            dbw.execSQL("UPDATE attendances SET lecture_count = ? WHERE id = ?", new String[]{cursor.getLong(0) + ""});
            dbw.close();
        }
        cursor.close();
        db.close();
    }
}

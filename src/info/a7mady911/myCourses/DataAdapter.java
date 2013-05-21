package info.a7mady911.myCourses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * User: A7madY911
 */
public class DataAdapter {

    private static final String TAG = DataAdapter.class.getSimpleName();
    private DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "myCoursesDatabase";
    private static final String TABLE_NAME_STUDENT = "Student";
    private static final String TABLE_NAME_COURSE = "Course";
    private static final String TABLE_NAME_CLASS = "Class";
    private static final String TABLE_NAME_SESSION = "Session";
    private static final String TABLE_NAME_MAJOR = "Major";
    private static final String TABLE_NAME_PROGRAMME = "Programme";
    private static final String TABLE_NAME_ASSESSMENTS = "Assessments";
    private static final String TABLE_NAME_REGSTUDENT = "RegStudent";
    private static final String TABLE_NAME_STAFF = "Staff";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;

    //All Database tables keys
    public static final String KEY_ROW_ID = "_id";
    public static final String KEY_STUDENT_ID = "Student_ID";
    public static final String KEY_STUDENT_NAME = "Student_Name";
    public static final String KEY_STUDENT_EMAIL = "Student_Email";
    public static final String KEY_PROG_CODE = "Prog_Code";
    public static final String KEY_PROG_NAME = "Prog_Name";
    public static final String KEY_COURSE_CODE = "Course_Code";
    public static final String KEY_COURSE_NAME = "Course_Name";
    public static final String KEY_COURSE_CREDIT = "Course_Credit";
    public static final String KEY_COURSE_Level = "Course_Level";
    public static final String KEY_CLASS_CRN = "Class_CRN";
    public static final String KEY_SESSION_ID = "Session_ID";
    public static final String KEY_SESSION_START = "Session_Start";
    public static final String KEY_SESSION_END = "Session_End";
    public static final String KEY_SESSION_LOCATION = "Session_Location";
    public static final String KEY_ASSESSMENT_ID = "Assessment_ID";
    public static final String KEY_ASSESSMENT_NAME = "Assessment_Name";
    public static final String KEY_ASSESSMENT_WEIGHT = "Assessment_Weight";
    public static final String KEY_ASSESSMENT_DUE = "Assessment_Due";
    public static final String KEY_ASSESSMENT_GRADE = "Assessment_Grade";
    public static final String KEY_STAFF_ID = "Staff_ID";
    public static final String KEY_STAFF_NAME = "Staff_Name";
    public static final String KEY_STAFF_EMAIL = "Staff_Email";
    public static final String KEY_STAFF_OFFICE = "Staff_Office";
    public static final String KEY_STAFF_PHONE = "Staff_Phone";
    public static final String KEY_MAJOR_NAME = "Major_Name";
    public static final String KEY_MAJOR_CODE = "Major_Code";


    /* Students Table Create */
    private static final String DATABASE_STUDENT_CREATE = "CREATE TABLE if not exists "
            + TABLE_NAME_STUDENT + " (" + KEY_ROW_ID + " PRIMARY KEY," + KEY_STUDENT_ID
            + "," + KEY_STUDENT_NAME + ", " + KEY_STUDENT_EMAIL + "," + " UNIQUE (" + KEY_ROW_ID + "));";

    /* Registered Student Table Create */
    private static final String DATABASE_REGSTUDENT_CREATE = "CREATE TABLE if not exists "
            + TABLE_NAME_REGSTUDENT + " (" + KEY_ROW_ID + " PRIMARY KEY," + " UNIQUE (" + KEY_ROW_ID + "));";

    /* Staff Table Create */
    private static final String DATABASE_STAFF_CREATE = "CREATE TABLE if not exists "
            + TABLE_NAME_STAFF + " (" + KEY_ROW_ID + " PRIMARY KEY," + KEY_STAFF_ID
            + "," + KEY_STAFF_NAME + "," + KEY_STAFF_EMAIL + "," + KEY_STAFF_OFFICE + "," + KEY_STAFF_PHONE
            + "," + " UNIQUE (" + KEY_ROW_ID + "));";

    /* Session Table Create */
    private static final String DATABASE_SESSION_CREATE = "CREATE TABLE if not exists "
            + TABLE_NAME_SESSION + " (" + KEY_ROW_ID + " PRIMARY KEY," + KEY_SESSION_ID
            + "," + KEY_SESSION_START + "," + KEY_SESSION_END + "," + KEY_SESSION_LOCATION
            + "," + " UNIQUE (" + KEY_ROW_ID + "));";

    /* Class Table Create */
    private static final String DATABASE_CLASS_CREATE = "CREATE TABLE if not exists "
            + TABLE_NAME_CLASS + " (" + KEY_ROW_ID + " PRIMARY KEY," + KEY_CLASS_CRN + "," + " UNIQUE (" + KEY_ROW_ID + "));";

    /* Major Table Create */
    private static final String DATABASE_MAJOR_CREATE = "CREATE TABLE if not exists "
            + TABLE_NAME_MAJOR + " (" + KEY_ROW_ID + " PRIMARY KEY," + KEY_MAJOR_CODE
            + "," + KEY_MAJOR_NAME + "," + " UNIQUE (" + KEY_ROW_ID + "));";


    /* Courses Table Create */
    private static final String DATABASE_COURSES_CREATE = "CREATE TABLE if not exists "
            + TABLE_NAME_COURSE + " (" + KEY_ROW_ID + " PRIMARY KEY," + KEY_COURSE_CODE
            + "," + KEY_COURSE_NAME + "," + KEY_COURSE_Level + "," + KEY_COURSE_CREDIT + "," + " UNIQUE (" + KEY_ROW_ID + "));";

    /* Programme Table Create */
    private static final String DATABASE_PROGRAMME_CREATE = "CREATE TABLE if not exists "
            + TABLE_NAME_PROGRAMME + " (" + KEY_ROW_ID + " PRIMARY KEY," + KEY_PROG_CODE
            + "," + KEY_PROG_NAME + "," + " UNIQUE (" + KEY_ROW_ID + "));";

    /* Assessments Table Create */
    private static final String DATABASE_ASSESSMENT_CREATE = "CREATE TABLE if not exists "
            + TABLE_NAME_ASSESSMENTS + " (" + KEY_ROW_ID + " PRIMARY KEY," + KEY_ASSESSMENT_ID
            + "," + KEY_ASSESSMENT_NAME + "," + KEY_ASSESSMENT_WEIGHT + "," + KEY_ASSESSMENT_DUE
            + "," + KEY_ASSESSMENT_GRADE + "," + " UNIQUE (" + KEY_ROW_ID + "));";

    /* Add Staff ID References to Student Table */
    private static final String DATABASE_STUDENT_REFERENCES_ADD_STAFFID = "ALTER TABLE "
            + TABLE_NAME_STUDENT + " ADD COLUMN " + KEY_STAFF_ID + " REFERENCES " + TABLE_NAME_STAFF +
            "(" + KEY_STAFF_ID + ");";

    /* Add Programme Code References to Student Table */
    private static final String DATABASE_STUDENT_REFERENCES_ADD_PROGCODE = "ALTER TABLE "
            + TABLE_NAME_STUDENT + " ADD COLUMN " + KEY_PROG_CODE + " REFERENCES " + TABLE_NAME_PROGRAMME +
            "(" + KEY_PROG_CODE + ");";

    /* Add CRN References to RegStudent Table */
    private static final String DATABASE_REGSTUDENT_REFERENCES_ADD_STUDENTID = "ALTER TABLE "
            + TABLE_NAME_REGSTUDENT + " ADD COLUMN " + KEY_STUDENT_ID + " REFERENCES " + TABLE_NAME_STUDENT +
            "(" + KEY_STUDENT_ID + ");";

    /* Add Student ID References to RegStudent Table */
    private static final String DATABASE_REGSTUDENT_REFERENCES_ADD_CLASSCRN = "ALTER TABLE "
            + TABLE_NAME_REGSTUDENT + " ADD COLUMN " + KEY_CLASS_CRN + " REFERENCES " + TABLE_NAME_CLASS +
            "(" + KEY_CLASS_CRN + ");";

    /* Add Programme ID References to Staff Table */
    private static final String DATABASE_STAFF_REFERENCES_ADD_PROGCODE = "ALTER TABLE "
            + TABLE_NAME_STAFF + " ADD COLUMN " + KEY_PROG_CODE + " REFERENCES " + TABLE_NAME_PROGRAMME +
            "(" + KEY_PROG_CODE + ");";

    /* Add Class CRN References to Session Table */
    private static final String DATABASE_SESSION_REFERENCES_ADD_CLASSCRN = "ALTER TABLE "
            + TABLE_NAME_SESSION + " ADD COLUMN " + KEY_CLASS_CRN + " REFERENCES " + TABLE_NAME_CLASS +
            "(" + KEY_CLASS_CRN + ");";

    /* Add Course Code References to Class Table */
    private static final String DATABASE_CLASS_REFERENCES_ADD_COURSECODE = "ALTER TABLE "
            + TABLE_NAME_CLASS + " ADD COLUMN " + KEY_COURSE_CODE + " REFERENCES " + TABLE_NAME_COURSE +
            "(" + KEY_COURSE_CODE + ");";

    /* Add Staff ID References to Class Table */
    private static final String DATABASE_CLASS_REFERENCES_ADD_STAFFID = "ALTER TABLE "
            + TABLE_NAME_CLASS + " ADD COLUMN " + KEY_STAFF_ID + " REFERENCES " + TABLE_NAME_STAFF +
            "(" + KEY_STAFF_ID + ");";

    /* Add Programme Code References to Major Table */
    private static final String DATABASE_MAJOR_REFERENCES_ADD_PROGCODE = "ALTER TABLE "
            + TABLE_NAME_MAJOR + " ADD COLUMN " + KEY_PROG_CODE + " REFERENCES " + TABLE_NAME_PROGRAMME +
            "(" + KEY_PROG_CODE + ");";

    /* Add Programme Code References to Major Table */
    private static final String DATABASE_COURSE_REFERENCES_ADD_PROGCODE = "ALTER TABLE "
            + TABLE_NAME_COURSE + " ADD COLUMN " + KEY_PROG_CODE + " REFERENCES " + TABLE_NAME_PROGRAMME +
            "(" + KEY_PROG_CODE + ");";

    /* Add Student ID References to Assessments Table */
    private static final String DATABASE_ASSESSMENTS_REFERENCES_ADD_STUDENTID = "ALTER TABLE "
            + TABLE_NAME_ASSESSMENTS + " ADD COLUMN " + KEY_STUDENT_ID + " REFERENCES " + TABLE_NAME_STUDENT +
            "(" + KEY_STUDENT_ID + ");";

    /* Add Course Code References to Assessments Table */
    private static final String DATABASE_ASSESSMENTS_REFERENCES_ADD_COURSECODE = "ALTER TABLE "
            + TABLE_NAME_ASSESSMENTS + " ADD COLUMN " + KEY_COURSE_CODE + " REFERENCES " + TABLE_NAME_COURSE +
            "(" + KEY_COURSE_CODE + ");";


    /* Database Helper Class */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        /* Database Helper constructor */
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /* On Create Database */
        @Override
        public void onCreate(SQLiteDatabase db) {

            //Create tables
            db.execSQL(DATABASE_STUDENT_CREATE);
            db.execSQL(DATABASE_REGSTUDENT_CREATE);
            db.execSQL(DATABASE_STAFF_CREATE);
            db.execSQL(DATABASE_SESSION_CREATE);
            db.execSQL(DATABASE_CLASS_CREATE);
            db.execSQL(DATABASE_MAJOR_CREATE);
            db.execSQL(DATABASE_PROGRAMME_CREATE);
            db.execSQL(DATABASE_COURSES_CREATE);
            db.execSQL(DATABASE_ASSESSMENT_CREATE);

            //Alters and adding references
            db.execSQL(DATABASE_STUDENT_REFERENCES_ADD_STAFFID);
            db.execSQL(DATABASE_STUDENT_REFERENCES_ADD_PROGCODE);
            db.execSQL(DATABASE_REGSTUDENT_REFERENCES_ADD_STUDENTID);
            db.execSQL(DATABASE_REGSTUDENT_REFERENCES_ADD_CLASSCRN);
            db.execSQL(DATABASE_STAFF_REFERENCES_ADD_PROGCODE);
            db.execSQL(DATABASE_SESSION_REFERENCES_ADD_CLASSCRN);
            db.execSQL(DATABASE_CLASS_REFERENCES_ADD_COURSECODE);
            db.execSQL(DATABASE_CLASS_REFERENCES_ADD_STAFFID);
            db.execSQL(DATABASE_MAJOR_REFERENCES_ADD_PROGCODE);
            db.execSQL(DATABASE_COURSE_REFERENCES_ADD_PROGCODE);
            db.execSQL(DATABASE_ASSESSMENTS_REFERENCES_ADD_STUDENTID);
            db.execSQL(DATABASE_ASSESSMENTS_REFERENCES_ADD_COURSECODE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STUDENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_REGSTUDENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_STAFF);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SESSION);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CLASS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ASSESSMENTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_COURSE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PROGRAMME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_MAJOR);
            onCreate(db);
        }
    }

    /* Data Adapter constructor */
    public DataAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /* Open method for Data Adapter  */
    public DataAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        mDb.isOpen();
        return this;
    }

    /* Check if open  */
    public Boolean isDBHelperOpen() {
        return mDb.isOpen();
    }

    /* Check if writable  */
    public Boolean isDBHelperWritable() {
        return !mDb.isReadOnly();
    }

    /* Close method for Data Adapter  */
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    /* Create Data */
    public long createData(String tableName, String fieldID, String field1, String field2,
                           String field3, String field4, String field5, String field6, String field7) {

        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_ROW_ID, fieldID);
        if (tableName.equalsIgnoreCase(TABLE_NAME_STUDENT)) {
            initialValues.put(KEY_STUDENT_ID, field1);
            initialValues.put(KEY_STUDENT_NAME, field2);
            initialValues.put(KEY_STUDENT_EMAIL, field3);
            initialValues.put(KEY_STAFF_ID, field4);
            initialValues.put(KEY_PROG_CODE, field5);
        } else if (tableName.equalsIgnoreCase(TABLE_NAME_REGSTUDENT)) {
            initialValues.put(KEY_STUDENT_ID, field1);
            initialValues.put(KEY_CLASS_CRN, field2);
        } else if (tableName.equalsIgnoreCase(TABLE_NAME_STAFF)) {
            initialValues.put(KEY_STAFF_ID, field1);
            initialValues.put(KEY_STAFF_NAME, field2);
            initialValues.put(KEY_STAFF_EMAIL, field3);
            initialValues.put(KEY_STAFF_OFFICE, field4);
            initialValues.put(KEY_STAFF_PHONE, field5);
            initialValues.put(KEY_PROG_CODE, field6);
        } else if (tableName.equalsIgnoreCase(TABLE_NAME_CLASS)) {
            initialValues.put(KEY_CLASS_CRN, field1);
            initialValues.put(KEY_COURSE_CODE, field2);
            initialValues.put(KEY_STAFF_ID, field3);
        } else if (tableName.equalsIgnoreCase(TABLE_NAME_SESSION)) {
            initialValues.put(KEY_SESSION_ID, field1);
            initialValues.put(KEY_CLASS_CRN, field2);
            initialValues.put(KEY_SESSION_START, field3);
            initialValues.put(KEY_SESSION_END, field4);
            initialValues.put(KEY_SESSION_LOCATION, field5);
        } else if (tableName.equalsIgnoreCase(TABLE_NAME_MAJOR)) {
            initialValues.put(KEY_MAJOR_CODE, field1);
            initialValues.put(KEY_PROG_CODE, field2);
            initialValues.put(KEY_MAJOR_NAME, field3);
        } else if (tableName.equalsIgnoreCase(TABLE_NAME_PROGRAMME)) {
            initialValues.put(KEY_PROG_CODE, field1);
            initialValues.put(KEY_PROG_NAME, field2);
        } else if (tableName.equalsIgnoreCase(TABLE_NAME_COURSE)) {
            initialValues.put(KEY_COURSE_CODE, field1);
            initialValues.put(KEY_COURSE_NAME, field2);
            initialValues.put(KEY_COURSE_CREDIT, field3);
            initialValues.put(KEY_COURSE_Level, field4);
            initialValues.put(KEY_PROG_CODE, field5);
        } else if (tableName.equalsIgnoreCase(TABLE_NAME_ASSESSMENTS)) {
            initialValues.put(KEY_ASSESSMENT_ID, field1);
            initialValues.put(KEY_ASSESSMENT_NAME, field2);
            initialValues.put(KEY_COURSE_CODE, field3);
            initialValues.put(KEY_ASSESSMENT_WEIGHT, field4);
            initialValues.put(KEY_ASSESSMENT_DUE, field5);
            initialValues.put(KEY_ASSESSMENT_GRADE, field6);
            initialValues.put(KEY_STUDENT_ID, field7);
        }

        long insertLong = 0;
        try {
            insertLong = mDb.insertWithOnConflict(tableName, null,
                    initialValues, SQLiteDatabase.CONFLICT_IGNORE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return insertLong;
    }

    /* Delete all student data */
    public static boolean deleteAllData(String tableName) {
        int doneDelete = 0;
        doneDelete = mDb.delete(tableName, null, null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    /* Get student data */
    public Cursor fetchStudentData() {
        Cursor mCursor = mDb.rawQuery("SELECT * FROM " + TABLE_NAME_STUDENT + ", " + TABLE_NAME_STAFF +
                " WHERE " + TABLE_NAME_STUDENT + "." + KEY_STAFF_ID + " = " + TABLE_NAME_STAFF + "." + KEY_STAFF_ID
                + " GROUP BY " + TABLE_NAME_STUDENT + "." + KEY_STAFF_ID, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        } else {
            Log.d(TAG, "fetchStudentData cursor is null");
        }
        return mCursor;
    }

    /* Get all courses data */
    public Cursor fetchAllCoursesData() {
        Cursor mCursor = mDb.query(TABLE_NAME_COURSE, new String[]{KEY_ROW_ID, KEY_COURSE_CODE,
                KEY_COURSE_NAME, KEY_COURSE_Level}, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        } else {
            Log.d(TAG, "fetchAllCoursesData cursor is null");
        }
        return mCursor;
    }

    /* Get Course details data by Course ID */
    public Cursor fetchCourseDetailsDataByCode(String courseCode) {
        Cursor mCursor = mDb.rawQuery("SELECT Course.Course_Code, Course.Course_Name, Course.Course_Credit," +
                "Course.Course_Level, Programme.Prog_Name, Programme.Prog_Code, Class.Class_CRN, Staff.Staff_ID, Staff.Staff_Name " +
                "FROM Course, Programme, Staff, Class WHERE Course.Course_Code = '" + courseCode + "' AND " +
                "Course.Course_Code = Class.Course_Code AND Course.Prog_Code = Programme.Prog_Code " +
                "AND Class.Staff_ID = Staff.Staff_ID", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        } else {
            Log.d(TAG, "fetchCourseDetailsDataByCode cursor is null");
        }
        return mCursor;
    }

    /* Get Assessments data by Course ID */
    public Cursor fetchAssessmentsDataByCourseCode(String courseCode) {
        Cursor mCursor = mDb.rawQuery("SELECT Assessments._id, Assessments.Assessment_ID, Assessments.Assessment_Name, " +
                "Assessments.Assessment_Due, Assessments.Course_Code FROM Assessments, Course WHERE assessments.Course_Code = '"
                + courseCode + "' GROUP BY Assessments.Assessment_ID", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        } else {
            Log.d(TAG, "fetchAssessmentsDataByCourseCode cursor is null");
        }
        return mCursor;
    }

    /* Get Assessment details data by Course ID */
    public Cursor fetchAssessmentDetailsDataByID(String assessmentID, String courseCode) {
        Cursor mCursor = mDb.rawQuery("SELECT assessments._id, assessments.Assessment_ID, assessments.Assessment_Name," +
                "assessments.Assessment_Weight, assessments.Assessment_Due, assessments.Course_Code, assessments.Assessment_Grade"
                + " FROM assessments, course WHERE assessments.Course_Code = '" + courseCode + "'"
                + " AND assessments.Assessment_ID = '" + assessmentID + "' GROUP BY Assessments.Assessment_ID", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        } else {
            Log.d(TAG, "fetchAssessmentDetailsDataByID cursor is null");
        }
        return mCursor;
    }

    /* Get Staff list data by Programme Code */
    public Cursor fetchStaffListDataByProgCode(String progCode) {
        Cursor mCursor = mDb.rawQuery("SELECT Programme._id, Programme.Prog_Code, Programme.Prog_Name, Staff.Staff_ID," +
                " Staff.Staff_Name FROM Programme, Staff WHERE Programme.Prog_Code = '" + progCode + "'"
                + " AND Programme.Prog_Code = Staff.Prog_Code", null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        } else {
            Log.d(TAG, "fetchStaffListDataByProgCode cursor is null");
        }
        return mCursor;
    }

    /* Update the assessment grade by ID */
    public int updateAssessmentGrade(String assessmentGrade, String assessmentID, String rowID) {
        Log.d(TAG, "param: " + assessmentGrade + ", " + assessmentID + ", " + rowID);
        ContentValues values = new ContentValues();
        values.put(KEY_ASSESSMENT_GRADE, assessmentGrade);
        return mDb.updateWithOnConflict(TABLE_NAME_ASSESSMENTS, values, KEY_ROW_ID + "=? AND " +
                KEY_ASSESSMENT_ID + "=?", new String[]{rowID, assessmentID}, SQLiteDatabase.CONFLICT_FAIL);
    }


    /* Get Staff details data by Staff ID */
    public Cursor fetchStaffDetailsDataByID(String staffID) {
        Cursor mCursor = mDb.rawQuery("SELECT * FROM " + TABLE_NAME_STAFF + ", " + TABLE_NAME_PROGRAMME +
                " WHERE " + TABLE_NAME_STAFF + "." + KEY_PROG_CODE + " = " + TABLE_NAME_PROGRAMME + "." + KEY_PROG_CODE +
                " AND " + TABLE_NAME_STAFF + "." + KEY_STAFF_ID + " like '%" + staffID + "%'"
                + " GROUP BY " + TABLE_NAME_STAFF + "." + KEY_STAFF_ID, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        } else {
            Log.d(TAG, "fetchStaffDetailsDataByID cursor is null");
        }
        return mCursor;
    }

    /* Insert all student data */
    //tableName, fieldID, field1, field2, field3, field4, field5, field6
    public void insertAllData() {
        createData(TABLE_NAME_PROGRAMME, "1", "ICT", "Information and Communication Technology", null, null, null, null, null);
        createData(TABLE_NAME_STAFF, "1", "1", "Philippe Pringuet", "Philippe.Pringuet@polytechnic.bh", "26-011", "17897346", "ICT", null);
        createData(TABLE_NAME_STAFF, "2", "2", "Trevor Prendergast", "Trevor.Prendergast@polytechnic.bh", "26-011", "17891022", "ICT", null);
        createData(TABLE_NAME_STUDENT, "1", "20900224", "Ahmed Yusuf", "20900224@student.polytechnic.bh", "1",
                "ICT", null, null);
        createData(TABLE_NAME_COURSE, "1", "GSZ5003", "Special Project", "15", "5", "ICT", null, null);
        createData(TABLE_NAME_COURSE, "2", "ITB6099", "IT Project", "60", "6", "ICT", null, null);
        createData(TABLE_NAME_CLASS, "1", "20632", "ITB6099", "1", null, null, null, null);
        createData(TABLE_NAME_CLASS, "2", "20851", "GSZ5003", "2", null, null, null, null);
        createData(TABLE_NAME_REGSTUDENT, "1", "20900224", "GSZ5003", null, null, null, null, null);
        createData(TABLE_NAME_REGSTUDENT, "2", "20900224", "ITB6099", null, null, null, null, null);
        createData(TABLE_NAME_SESSION, "1", "1", "20632", "Sun, 1:00pm", "Sunday, 2:50pm", "36A.109", null, null);
        createData(TABLE_NAME_SESSION, "2", "2", "20632", "Tue, 1:00pm", "Tue, 2:50pm", "36.201", null, null);
        createData(TABLE_NAME_SESSION, "3", "3", "20632", "Tue, 10:00am", "Tue, 11:50am", "36.201", null, null);
        createData(TABLE_NAME_SESSION, "4", "4", "20632", "Wed, 10:00am", "Wed, 11:50am", "36.201", null, null);
        createData(TABLE_NAME_SESSION, "5", "5", "20632", "Thu, 10:00am", "Thu, 11:50am", "36A.109", null, null);
        createData(TABLE_NAME_SESSION, "6", "6", "20851", "TBA", "TBA", "TBA", null, null);
        createData(TABLE_NAME_MAJOR, "1", "Prog", "ICT", "Mobile Programming", null, null, null, null);
        createData(TABLE_NAME_ASSESSMENTS, "1", "01", "Project Document", "ITB6099", "30", "7 April 2013", "%0", "20900224");
        createData(TABLE_NAME_ASSESSMENTS, "2", "02", "Thesis Document", "ITB6099", "30", "16 June 2013", "%0", "20900224");
        createData(TABLE_NAME_ASSESSMENTS, "3", "03", "Implementation - Weekly Meetings", "ITB6099", "15", "Weekly", "%0", "20900224");
        createData(TABLE_NAME_ASSESSMENTS, "4", "04", "Implementation - Final Demonstration", "ITB6099", "25", "25 June 2013", "%0", "20900224");

    }

    /* Count table records */
    public long countTableRecords(String tableName) {
        return DatabaseUtils.queryNumEntries(mDb, tableName);
    }

}

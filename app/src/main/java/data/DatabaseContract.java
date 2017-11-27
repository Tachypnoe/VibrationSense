package data;

import android.provider.BaseColumns;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;

import com.tachypnoe.vibrationsense.R;

/**
 * Framework for database
 */

public final class DatabaseContract {

    private DatabaseContract () {
    };

    public final class Measures implements BaseColumns {

        public final static String TABLE_NAME = "History";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "Name";
        public final static String COLUMN_DISEASE = "Condition";
        public final static String COLUMN_GENDER = "Gender";
        public final static String COLUMN_AGE = "Age";
        public final static String COLUMN_MISC = "Misc";
        public final static String COLUMN_MEASURE = "Measure";
        public final static String COLUMN_AREA = "Area";
        public final static String COLUMN_COMPARE = "TuningFork";

        public static final int GENDER_FEMALE = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_UNKNOWN = 2;



    }
}

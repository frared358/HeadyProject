package francis.headyproject.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Heady.db";
    private final String TABLE_NAME = "Product_table";
    private final String COL_1 = "categories_id";
    private final String COL_2 = "categories_name";
    private final String COL_3 = "products_id";
    private final String COL_4 = "products_name";
    private final String COL_5 = "variants_id";
    private final String COL_6 = "color";
    private final String COL_7 = "size";
    private final String COL_8 = "price";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ("
                +COL_1+" INTEGER,"
                +COL_2+" TEXT,"
                +COL_3+" INTEGER, "
                +COL_4+" TEXT,"
                +COL_5+" INTEGER,"
                +COL_6+" TEXT, "
                +COL_7+" TEXT, "
                +COL_8+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(int categories_id,String categories_name,int products_id,String products_name,int variants_id, String color,String size,String price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,categories_id);
        contentValues.put(COL_2,categories_name);
        contentValues.put(COL_3,products_id);
        contentValues.put(COL_4,products_name);
        contentValues.put(COL_5,variants_id);
        contentValues.put(COL_6,color);
        contentValues.put(COL_7,size);
        contentValues.put(COL_8,price);

        long res = db.insert(TABLE_NAME,null,contentValues);

        if (res == -1){
            return false;
        }else {
            return true;
        }

    }
}

package francis.headyproject.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Heady.db";
    private final String TABLE_Product = "Product_table";
    private final String TABLE_Categories = "categories_table";
    private final String TABLE_Data = "Data_table";

    private final String COLC_1 = "categories_id";
    private final String COLC_2 = "categories_name";

    private final String COLP_1 = "products_id";
    private final String COLP_2 = "products_name";

    private final String COLV_1 = "variants_id";
    private final String COLV_2 = "variants_color";
    private final String COLV_3 = "variants_size";
    private final String COLV_4 = "variants_price";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_Categories + " ("
                +COLC_1+" INTEGER,"
                +COLC_2+" TEXT)");

        db.execSQL("create table " + TABLE_Product + " ("
                +COLP_1+" INTEGER,"
                +COLC_1+" INTEGER,"
                +COLP_2+" TEXT)");

        db.execSQL("create table " + TABLE_Data + " ("
                +COLV_1+" INTEGER,"
                +COLP_1+" INTEGER,"
                +COLV_2+" TEXT,"
                +COLV_3+" TEXT, "
                +COLV_4+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Data);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Product);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Categories);
    }

//    public boolean insertData1(int categories_id,String categories_name,int products_id,String products_name,int variants_id, String color,String size,String price){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_1,categories_id);
//        contentValues.put(COL_2,categories_name);
//        contentValues.put(COL_3,products_id);
//        contentValues.put(COL_4,products_name);
//        contentValues.put(COL_5,variants_id);
//        contentValues.put(COL_6,color);
//        contentValues.put(COL_7,size);
//        contentValues.put(COL_8,price);
//
//        long res = db.insert(TABLE_NAME,null,contentValues);
//
//        if (res == -1){
//            return false;
//        }else {
//            return true;
//        }
//
//    }

    public boolean insertCategories(int categories_id,String categories_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLC_1,categories_id);
        contentValues.put(COLC_2,categories_name);

        long res = db.insert(TABLE_Categories,null,contentValues);

        return res != -1;
    }

    public boolean insertProducts(int categories_id, int products_id,String products_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLC_1,categories_id);
        contentValues.put(COLP_1,products_id);
        contentValues.put(COLP_2,products_name);

        long res = db.insert(TABLE_Product,null,contentValues);

        return res != -1;
    }

    public boolean insertData(int products_id,int variants_id, String color,String size,String price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLP_1,products_id);
        contentValues.put(COLV_1,variants_id);
        contentValues.put(COLV_2,color);
        contentValues.put(COLV_3,size);
        contentValues.put(COLV_4,price);

        long res = db.insert(TABLE_Data,null,contentValues);

        return res != -1;
    }

    public Cursor getCategoriesData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_Categories,null);
        return res;
    }

    public Cursor getProductData(int Categories_id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_Product+ " WHERE categories_id = "+Categories_id+";",null);
        return res;
    }

    public Cursor getAllData(int products_id){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_Data+ " WHERE products_id = "+products_id+";",null);
        return res;
    }
}

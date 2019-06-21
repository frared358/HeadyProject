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

    private final String TABLE_VIEW = "rank_view_table";
    private final String COLRV_1 = "view_id";
    private final String COLRV_2 = "view_count";

    private final String TABLE_SHARE = "rank_share_table";
    private final String COLRS_1 = "share_id";
    private final String COLRS_2 = "share_count";

    private final String TABLE_ORDER = "rank_order_table";
    private final String COLRO_1 = "order_id";
    private final String COLRO_2 = "order_count";


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

        db.execSQL("create table " + TABLE_VIEW + " ("
                +COLV_1+" INTEGER,"
                +COLRV_2+" TEXT,"
                +COLV_2+" TEXT,"
                +COLV_3+" TEXT, "
                +COLV_4+" TEXT)");

        db.execSQL("create table " + TABLE_SHARE + " ("
                +COLV_1+" INTEGER,"
                +COLRS_2+" TEXT,"
                +COLV_2+" TEXT,"
                +COLV_3+" TEXT, "
                +COLV_4+" TEXT)");

        db.execSQL("create table " + TABLE_ORDER + " ("
                +COLV_1+" INTEGER,"
                +COLRO_2+" TEXT,"
                +COLV_2+" TEXT,"
                +COLV_3+" TEXT, "
                +COLV_4+" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Data);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Product);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Categories);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEW);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHARE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
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

    public void insertCategories(int categories_id,String categories_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLC_1,categories_id);
        contentValues.put(COLC_2,categories_name);

        db.insert(TABLE_Categories,null,contentValues);


    }

    public void insertProducts(int categories_id, int products_id,String products_name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLC_1,categories_id);
        contentValues.put(COLP_1,products_id);
        contentValues.put(COLP_2,products_name);

        db.insert(TABLE_Product,null,contentValues);
    }

    public void insertData(int products_id,int variants_id, String color,String size,String price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLP_1,products_id);
        contentValues.put(COLV_1,variants_id);
        contentValues.put(COLV_2,color);
        contentValues.put(COLV_3,size);
        contentValues.put(COLV_4,price);

        db.insert(TABLE_Data,null,contentValues);
    }

    public Cursor getCategoriesData(){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_Categories,null);
    }

    public Cursor getProductData(int Categories_id){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_Product+ " WHERE categories_id = "+Categories_id+";",null);
    }

    public Cursor getAllData(int products_id){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_Data+ " WHERE products_id = "+products_id+";",null);
    }

    public Cursor getRankingData(int variants_id){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_Data+ " WHERE variants_id = "+variants_id+";",null);
    }

    public void insertMostViewed(int varient_id, String view_count,String view_color,String view_size,String view_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLV_1,(varient_id));
        contentValues.put(COLRV_2,view_count);
        contentValues.put(COLV_2,(view_color));
        contentValues.put(COLV_3,(view_size));
        contentValues.put(COLV_4,(view_price));

        db.insert(TABLE_VIEW,null,contentValues);
    }

    public void insertMostShared(int varient_id, String share_count,String share_color,String share_size,String share_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLV_1,(varient_id));
        contentValues.put(COLRS_2,share_count);
        contentValues.put(COLV_2,(share_color));
        contentValues.put(COLV_3,(share_size));
        contentValues.put(COLV_4,(share_price));

        db.insert(TABLE_SHARE,null,contentValues);
    }

    public void insertMostOrder(int varient_id, String order_count, String order_color,String order_size,String order_price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLV_1,(varient_id));
        contentValues.put(COLRO_2,order_count);
        contentValues.put(COLV_2,(order_color));
        contentValues.put(COLV_3,(order_size));
        contentValues.put(COLV_4,(order_price));

        db.insert(TABLE_ORDER,null,contentValues);
    }

    public Cursor getTableData(String table_name){

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+table_name,null);
    }
}

package francis.headyproject;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import francis.headyproject.adapter.ExpandableListAdapter;
import francis.headyproject.pojo.MenuData;
import francis.headyproject.pojo.ResponseData;
import francis.headyproject.util.ApiClient;
import francis.headyproject.util.ApiInterface;
import francis.headyproject.util.DatabaseHelper;
import francis.headyproject.util.SharePref;
import francis.headyproject.variants.DataAdapter;
import francis.headyproject.variants.AllData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<MenuData> listCategory = new ArrayList<>();
    HashMap<MenuData, ArrayList<MenuData>> listProduct = new HashMap<>();
    ArrayList<AllData> listData = new ArrayList<>();
    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    DataAdapter adapter;
    RecyclerView recyle_Data;
    DatabaseHelper db = new DatabaseHelper(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyle_Data = findViewById(R.id.recyle_Data);
        recyle_Data.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DataAdapter(this,listData);
        recyle_Data.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = findViewById(R.id.expandableListView);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        SharePref sharePref = new SharePref();

        if (!sharePref.getPrefData(this).contains("key_check")) {
            setData();
            sharePref.setData(this,"key_check",false);
        }

        drawerMenu();
        populateExpandableList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setData() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseData> call = apiInterface.getData();

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                List<ResponseData.Category> list =  response.body().getCategories();


                for (int i=0;i<list.size();i++){
                    //insert in Table Categories
                    db.insertCategories(list.get(i).getId(),list.get(i).getName());
                    List<ResponseData.Product> productList =  response.body().getCategories().get(i).getProducts();

                    for (int j=0;j<productList.size();j++){
                        //insert in Table Product
                        db.insertProducts(list.get(i).getId(),productList.get(j).getId(),productList.get(j).getName());
                        List<ResponseData.Variant> variantList =  productList.get(j).getVariants();

                        for (int k=0;k<variantList.size();k++){
                            Log.i("DATATA",list.get(i).getId()+" "+list.get(i).getName()+" "+productList.get(j).getId()+" "+productList.get(j).getName()+" "+variantList.get(k).getId()+" "+variantList.get(k).getColor()+" "+variantList.get(k).getSize()+" "+variantList.get(k).getPrice());
                            //insert in Table Data
                            db.insertData(productList.get(j).getId(),variantList.get(k).getId(),variantList.get(k).getColor(),String.valueOf(variantList.get(k).getSize()),String.valueOf(variantList.get(k).getPrice()));

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.i("Error->",""+t.getMessage());
            }
        });
    }

    //drawer menu
    private void drawerMenu(){
        Cursor cursor = db.getCategoriesData();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int cID = cursor.getInt(0);
                String cName = cursor.getString(1);

                Cursor pCursor = db.getProductData(cID);
                ArrayList<MenuData> childDataList = new ArrayList<>();
                while (pCursor.moveToNext()) {
                    childDataList.add(new MenuData(pCursor.getInt(0),pCursor.getString(2)));
                }

                if (childDataList.size()>0) {
                    MenuData menuData = new MenuData(cID,cName);
                    listCategory.add(menuData);
                    listProduct.put(menuData, childDataList);
                }
            }
        }
    }


    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, listCategory, listProduct);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (listProduct.get(listCategory.get(groupPosition)) != null) {
                    MenuData obj = listProduct.get(listCategory.get(groupPosition)).get(childPosition);
                    Cursor cur = db.getAllData(obj.id);
                    while (cur.moveToNext()){
                        listData.add(new AllData(cur.getString(2),cur.getString(3),cur.getString(4)));
                        adapter.notifyDataSetChanged();
                    }
                    onBackPressed();
                }

                return false;
            }
        });
    }

}

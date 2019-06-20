package francis.headyproject;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<MenuData> listCategory = new ArrayList<>();
    HashMap<MenuData, ArrayList<MenuData>> listProduct = new HashMap<>();

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyle_Data = findViewById(R.id.recyle_Data);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        expandableListView = findViewById(R.id.expandableListView);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    DatabaseHelper db = new DatabaseHelper(MainActivity.this);
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
                Log.i("data", cursor.getInt(0) + " " + cursor.getInt(1));
                int cID = cursor.getInt(0);
                String cName = cursor.getString(1);
                MenuData menuData = new MenuData(cID,cName);
                listCategory.add(menuData);

                Cursor pCursor = db.getProductData(cID);
                ArrayList<MenuData> childDataList = new ArrayList<>();
                while (pCursor.moveToNext()) {
                    Log.i("data1", pCursor.getInt(0) + " " + pCursor.getInt(2));
                    childDataList.add(new MenuData(pCursor.getInt(0),pCursor.getString(2)));
                }

                if (childDataList.size()>0) {
                    listProduct.put(menuData, childDataList);
                }
            }
        }
    }


    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, listCategory, listProduct);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (listCategory.get(groupPosition).isGroup) {
                    if (!listCategory.get(groupPosition).hasChildren) {
                        WebView webView = findViewById(R.id.webView);
                        webView.loadUrl(listCategory.get(groupPosition).url);
                        onBackPressed();
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (listProduct.get(listCategory.get(groupPosition)) != null) {
                    MenuData obj = listProduct.get(listCategory.get(groupPosition)).get(childPosition);
//                    if (obj.url.length() > 0) {
//                        WebView webView = findViewById(R.id.webView);
//                        webView.loadUrl(obj.url);
//                        onBackPressed();
//                    }
                }

                return false;
            }
        });
    }

}

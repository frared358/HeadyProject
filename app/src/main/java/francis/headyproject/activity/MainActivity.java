package francis.headyproject.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import francis.headyproject.R;
import francis.headyproject.adapter.ExpandableListAdapter;
import francis.headyproject.pojo.MenuData;
import francis.headyproject.pojo.ResponseData;
import francis.headyproject.remote.ApiClient;
import francis.headyproject.remote.ApiInterface;
import francis.headyproject.util.DatabaseHelper;
import francis.headyproject.util.SharePref;
import francis.headyproject.adapter.DataAdapter;
import francis.headyproject.pojo.AllData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            dialgRanking();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseData> call = apiInterface.getData();

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(@NonNull Call<ResponseData> call,@NonNull Response<ResponseData> response) {

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
                            //insert in Table Data
                            db.insertData(productList.get(j).getId(),variantList.get(k).getId(),variantList.get(k).getColor(),String.valueOf(variantList.get(k).getSize()),String.valueOf(variantList.get(k).getPrice()));

                        }
                    }
                }

                List<ResponseData.Ranking> listRank =  response.body().getRankings();

                for (int i=0;i<listRank.size();i++){
                    List<ResponseData.Product_> listRankProduct = listRank.get(i).getProducts();


                    for (int j=0;j<listRankProduct.size();j++){
                        Cursor cursor = db.getRankingData(listRankProduct.get(j).getId());

                        while (cursor.moveToNext()) {
                            int v_id = cursor.getInt(0);
                            String v_color = cursor.getString(2);
                            String v_size = cursor.getString(3);
                            String v_price = cursor.getString(4);
                            if (listRank.get(i).getRanking().equalsIgnoreCase("Most Viewed Products")){
                                String count = String.valueOf(listRankProduct.get(j).getViewCount());
                                db.insertMostViewed(v_id,count,v_color,v_size,v_price);
                            }else if (listRank.get(i).getRanking().equalsIgnoreCase("Most OrdeRed Products")){
                                String count = String.valueOf(listRankProduct.get(j).getOrderCount());
                                db.insertMostOrder(v_id,count,v_color,v_size,v_price);
                            }else if (listRank.get(i).getRanking().equalsIgnoreCase("Most ShaRed Products")){
                                String count = String.valueOf(listRankProduct.get(j).getShares());
                                db.insertMostShared(v_id,count,v_color,v_size,v_price);
                            }
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
                listData.clear();
                if (listProduct.get(listCategory.get(groupPosition)) != null) {

                    MenuData obj = listProduct.get(listCategory.get(groupPosition)).get(childPosition);
                    setTitle(obj.name);
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

    AlertDialog alertDialog;
    RadioButton rb_view,rb_share,rb_order;
    private void dialgRanking(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filters");
        builder.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_ranking, null);
        builder.setView(v);

        alertDialog = builder.create();

        rb_view =  v.findViewById(R.id.rb_view);
        rb_share =  v.findViewById(R.id.rb_share);
        rb_order =  v.findViewById(R.id.rb_order);
        TextView tv_dialog_Filter =  v.findViewById(R.id.tv_dialog_Filter);
        TextView tv_dialog_Cancel =  v.findViewById(R.id.tv_dialog_Cancel);

        tv_dialog_Cancel.setOnClickListener(this);
        tv_dialog_Filter.setOnClickListener(this);

        alertDialog.show();

    }

    Cursor cur;
    String rankTitle;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dialog_Cancel:
                alertDialog.cancel();
                break;

            case R.id.tv_dialog_Filter:
                listData.clear();

                if (rb_view.isChecked()){
                    cur = db.getTableData("rank_view_table");
                    rankTitle = "View Count";
                    setTitle(getResources().getString(R.string.most_viewed_products));
                }else if (rb_order.isChecked()){
                    cur = db.getTableData("rank_order_table");
                    rankTitle = "Order Count";
                    setTitle(getResources().getString(R.string.most_ordered_products));
                }else if (rb_share.isChecked()){
                    cur = db.getTableData("rank_share_table");
                    setTitle(getResources().getString(R.string.most_shared_products));
                    rankTitle = "Share Count";
                }

                while (cur.moveToNext()){
                    listData.add(new AllData(cur.getString(2),cur.getString(3),cur.getString(4)));
                    adapter.notifyDataSetChanged();
                }
                alertDialog.cancel();
                break;
        }
    }
}

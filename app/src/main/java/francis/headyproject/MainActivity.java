package francis.headyproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import francis.headyproject.pojo.ResponseData;
import francis.headyproject.util.ApiClient;
import francis.headyproject.util.ApiInterface;
import francis.headyproject.util.DatabaseHelper;
import francis.headyproject.util.SharePref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        SharePref sharePref = new SharePref();

        if (!sharePref.getPrefData(this).contains("key_check")) {
            getData();
            sharePref.setData(this,"key_check",false);
        }



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
    private void getData() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseData> call = apiInterface.getData();

        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                List<ResponseData.Category> list =  response.body().getCategories();


                for (int i=0;i<list.size();i++){
                    List<ResponseData.Product> productList =  response.body().getCategories().get(i).getProducts();
                    for (int j=0;j<productList.size();j++){
                        List<ResponseData.Variant> variantList =  productList.get(j).getVariants();
                        for (int k=0;k<variantList.size();k++){
                            Log.i("DATATA",list.get(i).getId()+" "+list.get(i).getName()+" "+productList.get(j).getId()+" "+productList.get(j).getName()+" "+variantList.get(k).getId()+" "+variantList.get(k).getColor()+" "+variantList.get(k).getSize()+" "+variantList.get(k).getPrice());
                            db.insertData(list.get(i).getId(),list.get(i).getName(),productList.get(j).getId(),productList.get(j).getName(),variantList.get(k).getId(),variantList.get(k).getColor(),String.valueOf(variantList.get(k).getSize()),String.valueOf(variantList.get(k).getPrice()));
                        }
                    }
                }

                Log.i("Message->",""+response.body().getCategories().get(1).getName());
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.i("Error->",""+t.getMessage());
            }
        });
    }
}

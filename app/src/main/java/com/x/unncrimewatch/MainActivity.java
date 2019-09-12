package com.x.unncrimewatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.x.unncrimewatch.adapter.ImagesAdapter;
import com.x.unncrimewatch.model.CWViewModel;
import com.x.unncrimewatch.model.Image;
import com.x.unncrimewatch.roomDB.CW;
import com.x.unncrimewatch.roomDB.CWDatabaseAccessor;
import com.x.unncrimewatch.util.FileCompressor;
import com.x.unncrimewatch.views.CWListFragment;
import com.x.unncrimewatch.views.HomeFragment;
import com.x.unncrimewatch.views.UploadNewsFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CWListFragment.OnListFragmentInteractionListener {

    CWViewModel cwViewModel;
    FileCompressor mCompressor;
    RecyclerView mrv, mRecyclerview;

    UploadNewsFragment UNfragment = new UploadNewsFragment();

    private ArrayList<Uri> newsImagesuri = new ArrayList<>();
    private ArrayList<String> uris = new ArrayList<>();
    String uriTostring;
    CW update;

    private ArrayList<Image> newsImages = new ArrayList<>();
    private ImagesAdapter mImagesAdapter = new ImagesAdapter(newsImages);


    @Override
    public void onListFragmentRefreshRequested() {
        reloadUpdates();
    }

    private void reloadUpdates() {

        cwViewModel.getUpdates();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCompressor = new FileCompressor(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_crime(view);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_home);


        cwViewModel = ViewModelProviders.of(this)
                .get(CWViewModel.class);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else {
            finish();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_home:
                fragment = new CWListFragment();
                FloatingActionButton fabh = findViewById(R.id.fab);
                fabh.hide();
                break;
            case R.id.nav_upload:
                fragment = new UploadNewsFragment();
                FloatingActionButton fabs = findViewById(R.id.fab);
                fabs.show();
                break;
            case R.id.nav_call:
                fragment = new HomeFragment();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            replaceFragment(fragment);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    public void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        final FragmentManager fManager = getSupportFragmentManager();
        Fragment fragm = fManager.findFragmentByTag(fragmentTag);

        if (fragm == null) {  //here fragment is not available in the stack
            transaction.replace(R.id.main_activity_frame, fragment, fragmentTag);
            transaction.addToBackStack(backStateName);
        } else {
            //fragment was found in the stack , now we can reuse the fragment
            // please do not add in back stack else it will add transaction in back stack
            transaction.replace(R.id.main_activity_frame, fragm, fragmentTag);
        }
        transaction.commit();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /**
         // Handle navigation view item clicks here.
         int id = item.getItemId();

         if (id == R.id.nav_home) {
         // Handle the camera action
         } else if (id == R.id.nav_upload) {

         } else if (id == R.id.nav_call) {

         }
         DrawerLayout drawer = findViewById(R.id.drawer_layout);
         drawer.closeDrawer(GravityCompat.START);
         **/
        displaySelectedScreen(item.getItemId());

        return true;
    }


    public void upload_crime(View view) {
        EditText update_news = findViewById(R.id.update_news);
        String contents = update_news.getText().toString();
        Date now = Calendar.getInstance().getTime();

        newsImagesuri = mImagesAdapter.getImageUris();
        if (newsImagesuri != null) {
            for (Uri uri : newsImagesuri) {
                uriTostring = uri.toString();
                uris.add(uriTostring);
            }

            Snackbar.make(view, "yes images", Snackbar.LENGTH_LONG).show();

            update = new CW(now, contents, uris);
        } else {

            Snackbar.make(view, "No images", Snackbar.LENGTH_LONG).show();

            update = new CW(now, contents, null);
        }

        if (contents.isEmpty()) {
            Snackbar.make(view, "please, type in your emergency", Snackbar.LENGTH_LONG).show();
        } else {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    CWDatabaseAccessor
                            .getInstance(getApplication())
                            .cwDAO()
                            .insertUpdate(update);
                }
            });

            //Snackbar.make(view, "Crime reported successfully...", Snackbar.LENGTH_LONG).show();
            displaySelectedScreen(R.id.nav_home);
        }

    }

}

package info.androidhive.recyclerviewsearch;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class mywork extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , ContactsAdapter.ContactsAdapterListener {

    private static final String TAG = mywork.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Contact> contactList;
    private ContactsAdapter mAdapter;
    private SearchView searchView;
    private String test;
    static final int READ_BLOCK_SIZE=6666;
    ProgressDialog progressDialog;

    private static final String URL = "https://offshoreinventory.herokuapp.com/constructionapi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywork);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        recyclerView = findViewById(R.id.recycler_view);
        contactList = new ArrayList<>();
        mAdapter = new ContactsAdapter(this, contactList, this);

        // white background notification bar


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        boolean showMinMax = true;

        progressDialog = new ProgressDialog(mywork.this);
        progressDialog.setMessage("Loading..");
        progressDialog.setTitle("projects");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);


        fetchContacts();
        progressDialog.dismiss();
    }


    private void fetchContacts() {
        String URL = "https://offshoreinventory.herokuapp.com/constructionapi";

        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            String ret = "";

                            try {
                                FileInputStream fileIn=openFileInput("myText.txt");

                                InputStreamReader InputRead=new InputStreamReader(fileIn);
                                BufferedReader bufferedReader = new BufferedReader(InputRead);
                                String receiveString = "";
                                StringBuilder stringBuilder = new StringBuilder();
                                while ((receiveString = bufferedReader.readLine()) != null){
                                    stringBuilder.append(receiveString);

                                }
                                InputRead.close();
                                test = stringBuilder.toString();
                                Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
//                        if (response != null) {
//
//                            test = response.toString();
//                            try {
//                                FileOutputStream fileOut= openFileOutput("myText.txt",MODE_PRIVATE);
//                                OutputStreamWriter outputWriter=new OutputStreamWriter(fileOut);
//                                outputWriter.write(response.toString());
//                                outputWriter.close();
//
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            Toast.makeText(getApplicationContext(), "Downloaded for offline.", Toast.LENGTH_LONG).show();
//                            return;
//                        }

                        List<Contact> items = new Gson().fromJson(response.toString(), new TypeToken<List<Contact>>() {
                        }.getType());

                        // adding contacts to contacts list
                        contactList.clear();
                        contactList.addAll(items);



                        try {
                            FileOutputStream fileOut= openFileOutput("myText.txt",MODE_PRIVATE);
                            OutputStreamWriter outputWriter=new OutputStreamWriter(fileOut);
                            outputWriter.write(response.toString());
                            outputWriter.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());

                String ret = "";

                try {
                    FileInputStream fileIn=openFileInput("myText.txt");

                    InputStreamReader InputRead=new InputStreamReader(fileIn);
                    BufferedReader bufferedReader = new BufferedReader(InputRead);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((receiveString = bufferedReader.readLine()) != null){
                        stringBuilder.append(receiveString);

                    }
                    InputRead.close();
                    test = stringBuilder.toString();

                    List<Contact> items = new Gson().fromJson(test, new TypeToken<List<Contact>>() {
                    }.getType());

                    // adding contacts to contacts list
                    contactList.clear();
                    contactList.addAll(items);
                    mAdapter.notifyDataSetChanged();
//                    Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "You are now offline to go online reload again ", Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }


    private void fetchProjects() {

        String URL = "https://offshoreinventory.herokuapp.com/projectapi";


        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            String ret = "";

                            try {
                                FileInputStream fileIn=openFileInput("myProject.txt");

                                InputStreamReader InputRead=new InputStreamReader(fileIn);
                                BufferedReader bufferedReader = new BufferedReader(InputRead);
                                String receiveString = "";
                                StringBuilder stringBuilder = new StringBuilder();
                                while ((receiveString = bufferedReader.readLine()) != null){
                                    stringBuilder.append(receiveString);

                                }
                                InputRead.close();
                                test = stringBuilder.toString();
                                Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
//                        if (response != null) {
//
//                            test = response.toString();
//                            try {
//                                FileOutputStream fileOut= openFileOutput("myText.txt",MODE_PRIVATE);
//                                OutputStreamWriter outputWriter=new OutputStreamWriter(fileOut);
//                                outputWriter.write(response.toString());
//                                outputWriter.close();
//
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            Toast.makeText(getApplicationContext(), "Downloaded for offline.", Toast.LENGTH_LONG).show();
//                            return;
//                        }

                        List<Contact> items = new Gson().fromJson(response.toString(), new TypeToken<List<Contact>>() {
                        }.getType());

                        // adding contacts to contacts list
                        contactList.clear();
                        contactList.addAll(items);




                        try {
                            FileOutputStream fileOut= openFileOutput("myProject.txt",MODE_PRIVATE);
                            OutputStreamWriter outputWriter=new OutputStreamWriter(fileOut);
                            outputWriter.write(response.toString());
                            outputWriter.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());

                String ret = "";

                try {
                    FileInputStream fileIn=openFileInput("myProject.txt");

                    InputStreamReader InputRead=new InputStreamReader(fileIn);
                    BufferedReader bufferedReader = new BufferedReader(InputRead);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((receiveString = bufferedReader.readLine()) != null){
                        stringBuilder.append(receiveString);

                    }
                    InputRead.close();
                    test = stringBuilder.toString();

                    List<Contact> items = new Gson().fromJson(test, new TypeToken<List<Contact>>() {
                    }.getType());

                    // adding contacts to contacts list
                    contactList.clear();
                    contactList.addAll(items);
                    mAdapter.notifyDataSetChanged();
//                    Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "You are offline " , Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }


    private void fetchClients() {

        String URL = "https://offshoreinventory.herokuapp.com/portfolioapi";


        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            String ret = "";

                            try {
                                FileInputStream fileIn=openFileInput("myClient.txt");

                                InputStreamReader InputRead=new InputStreamReader(fileIn);
                                BufferedReader bufferedReader = new BufferedReader(InputRead);
                                String receiveString = "";
                                StringBuilder stringBuilder = new StringBuilder();
                                while ((receiveString = bufferedReader.readLine()) != null){
                                    stringBuilder.append(receiveString);

                                }
                                InputRead.close();
                                test = stringBuilder.toString();
                                Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return;
                        }
//                        if (response != null) {
//
//                            test = response.toString();
//                            try {
//                                FileOutputStream fileOut= openFileOutput("myText.txt",MODE_PRIVATE);
//                                OutputStreamWriter outputWriter=new OutputStreamWriter(fileOut);
//                                outputWriter.write(response.toString());
//                                outputWriter.close();
//
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            Toast.makeText(getApplicationContext(), "Downloaded for offline.", Toast.LENGTH_LONG).show();
//                            return;
//                        }

                        List<Contact> items = new Gson().fromJson(response.toString(), new TypeToken<List<Contact>>() {
                        }.getType());

                        // adding contacts to contacts list
                        contactList.clear();
                        contactList.addAll(items);



                        try {
                            FileOutputStream fileOut= openFileOutput("myClient.txt",MODE_PRIVATE);
                            OutputStreamWriter outputWriter=new OutputStreamWriter(fileOut);
                            outputWriter.write(response.toString());
                            outputWriter.close();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());

                String ret = "";

                try {
                    FileInputStream fileIn=openFileInput("myClient.txt");

                    InputStreamReader InputRead=new InputStreamReader(fileIn);
                    BufferedReader bufferedReader = new BufferedReader(InputRead);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((receiveString = bufferedReader.readLine()) != null){
                        stringBuilder.append(receiveString);

                    }
                    InputRead.close();
                    test = stringBuilder.toString();

                    List<Contact> items = new Gson().fromJson(test, new TypeToken<List<Contact>>() {
                    }.getType());

                    // adding contacts to contacts list
                    contactList.clear();
                    contactList.addAll(items);
                    mAdapter.notifyDataSetChanged();
//                    Toast.makeText(getApplicationContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "You are now offline ", Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mywork, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            progressDialog = new ProgressDialog(mywork.this);
            progressDialog.setMessage("Loading..");
            progressDialog.setTitle("projects");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);



            fetchProjects();

            progressDialog.dismiss();
        } else if (id == R.id.nav_gallery) {

            progressDialog = new ProgressDialog(mywork.this);
            progressDialog.setMessage("Loading..");
            progressDialog.setTitle("projects");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);


            fetchClients();

            progressDialog.dismiss();

        } else if (id == R.id.nav_slideshow) {

            progressDialog = new ProgressDialog(mywork.this);
            progressDialog.setMessage("Loading..");
            progressDialog.setTitle("projects");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            progressDialog.setCancelable(false);

            fetchClients();

            progressDialog.dismiss();

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onContactSelected(Contact contact) {


        MaterialDialog dialog = new MaterialDialog.Builder(mywork.this)
                .title(contact.getName())
                .content(contact.getTask())
                .positiveText("ok")
                .show();


    }
}

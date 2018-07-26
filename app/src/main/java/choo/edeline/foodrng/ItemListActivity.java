package choo.edeline.foodrng;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ItemListActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;

    private ListView mListView;
    private FloatingActionButton mAddItem;
    private String username;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ItemListActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        getSupportActionBar().setTitle("Item List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = getIntent().getExtras().getString("username");

        mListView = (ListView) findViewById(R.id.lvItemList);
        mDatabaseHelper = new DatabaseHelper(this);

        mAddItem = (FloatingActionButton)findViewById(R.id.fabAddItem);
        mAddItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(ItemListActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_add_item, null);
            final EditText itemName = (EditText) mView.findViewById(R.id.etItemName);
            mDatabaseHelper = new DatabaseHelper(ItemListActivity.this);

            Button addButton = (Button) mView.findViewById(R.id.btnAddNewItem);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newEntry = itemName.getText().toString();
                    if (itemName.length() != 0) {
                        if (mDatabaseHelper.checkIfDataExist(newEntry, username)) {
                            toastMessage("Item entered already exists.");
                        } else {
                            AddData(newEntry, username);
                            Intent intent = new Intent(ItemListActivity.this,
                                    ItemListActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                    }
                    else {
                        toastMessage("You must put something in the text field!");
                    }
                }
            });
            mBuilder.setView(mView);
            AlertDialog dialog = mBuilder.create();
            dialog.show();
        }
    });

        populateListView();

        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String item = adapterView.getItemAtPosition(i).toString();
                final Cursor data = mDatabaseHelper.getItemID(item, username); //get the id associated with that item
                int itemID = -1;

                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    final int id = itemID;

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(ItemListActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog_edit_item, null);
                    final EditText newItemName = (EditText)mView.findViewById(R.id.etNewItemName);
                    newItemName.setText(item);

                    Button saveButton = (Button)mView.findViewById(R.id.btnSave);

                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String newEntry = newItemName.getText().toString();
                            if (newItemName.length() != 0) {
                                if (mDatabaseHelper.checkIfDataExist(newEntry, username) == true) {
                                    toastMessage("Item entered already exists.");
                                }
                                else {
                                    mDatabaseHelper.updateItem(newEntry, id, item, username);
                                    Intent intent = new Intent(ItemListActivity.this, ItemListActivity.class);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                }
                            } else {
                                toastMessage("You must put something in the text field!");
                            }
                        }
                    });

                    Button delButton = (Button)mView.findViewById(R.id.btnDelete);

                    delButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabaseHelper.deleteItem(id, item, username);
                            toastMessage("Successfully deleted.");
                            Intent intent = new Intent(ItemListActivity.this, ItemListActivity.class);
                            intent.putExtra("username", username);
                            startActivity(intent);
                        }
                    });

                    mBuilder.setView(mView);
                    AlertDialog dialog = mBuilder.create();
                    dialog.show();
                }
                else{
                    toastMessage("No ID associated with that item");
                }
            }
        });
    }

    private void populateListView() {

        //get data and append to a list
        Cursor data = mDatabaseHelper.getData(username);
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //get the value from the database in column 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);
    }

    public void AddData(String newEntry, String username) {
        boolean insertData = mDatabaseHelper.addData(newEntry, username);

        if (insertData) {
            toastMessage("Successfully added.");
        } else {
            toastMessage("Something went wrong.");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(ItemListActivity.this,message, Toast.LENGTH_SHORT).show();
    }
}
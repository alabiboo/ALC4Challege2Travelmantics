package com.pluralsight.travelmcorrect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    EditText txtTitle;
    EditText txtDescription;
    EditText txtPrice;

    private TravelDeal deal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseUtil.openFbReference("alctraveldeal");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;


        txtTitle = (EditText)findViewById(R.id.txtTitle);
        txtDescription = (EditText)findViewById(R.id.txtDescription);
        txtPrice = (EditText)findViewById(R.id.txtPrice);


        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if(deal == null) {
            deal = new TravelDeal();
        }
        this.deal = deal;
        txtTitle.setText(deal.getTitle());
        txtDescription.setText(deal.getDescription());
        txtPrice.setText(deal.getPrice());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "Deal saved", Toast.LENGTH_LONG).show();
                clean();
                backToList();
                return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal delected", Toast.LENGTH_LONG).show();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void clean() {
        txtPrice.setText("");
        txtDescription.setText("");
        txtPrice.setText("");
        txtTitle.requestFocus(); //??
    }
    private void deleteDeal() {
        if (deal == null){
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_SHORT).show();
        }
        mDatabaseReference.child(deal.getId()).removeValue();
    }


    private void backToList() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }

    private void saveDeal() {
        deal.setTitle(txtTitle.getText().toString());
        deal.setDescription(txtDescription.getText().toString());
        deal.setPrice(txtPrice.getText().toString());
        if (deal.getId() == null) {
            mDatabaseReference.push().setValue(deal);
        }else{
            mDatabaseReference.child(deal.getId()).setValue(deal);
        }
    }


}

package com.example.database;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.afinal.R;

public class MainActivity extends AppCompatActivity {
    TextView idView;
    EditText productBox;
    EditText quantityBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Get references to view objects */
        idView = findViewById(R.id.productID);
        productBox = findViewById(R.id.productName);
        quantityBox = findViewById(R.id.productQuantity);
    }

    public void newProduct (View view) {
        com.example.database.MyDBHandler dbHandler = new com.example.database.MyDBHandler(this, null, null, 1);
        String productName = productBox.getText().toString();
        String quantity = quantityBox.getText().toString();
        if (!productName.equals("") && !quantity.equals("")){
            com.example.database.Product found = dbHandler.findProduct(productName);
            if (found == null){
                com.example.database.Product product = new com.example.database.Product(productName, Integer.parseInt(quantity));
                dbHandler.addProduct(product);
                productBox.setText("");
                quantityBox.setText("");
            }
        }
    }

    public void lookupProduct (View view) {
        com.example.database.MyDBHandler dbHandler = new com.example.database.MyDBHandler(this, null, null, 1);
        com.example.database.Product product = dbHandler.findProduct(productBox.getText().toString());
        if (product != null) {
            idView.setText(String.valueOf(product.getID()));
            quantityBox.setText(String.valueOf(product.getQuantity()));
        } else {
            idView.setText(getString(R.string.no_match_found));
            quantityBox.setText("");
        }
    }
    public void removeProduct (View view) {
        com.example.database.MyDBHandler dbHandler = new com.example.database.MyDBHandler(this, null, null, 1);
        boolean result = dbHandler.deleteProduct(productBox.getText().toString());
        if (result)
        {
            idView.setText(getString(R.string.record_deleted));
            productBox.setText("");
            quantityBox.setText("");
        }
        else{
            idView.setText(getString(R.string.no_match_found));
            quantityBox.setText("");
        }
    }
}
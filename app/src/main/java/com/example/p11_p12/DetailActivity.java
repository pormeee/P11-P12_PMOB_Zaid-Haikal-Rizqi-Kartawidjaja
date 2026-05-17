package com.example.p11_p12;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.p11_p12.databinding.ActivityDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        String idMeal = getIntent().getStringExtra("i_idMeal");

        getDetailMeal(idMeal);
    }

    private void getDetailMeal(String idMeal) {

        String url =
                "https://www.themealdb.com/api/json/v1/1/lookup.php?i="
                        + idMeal;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                response -> {

                    try {

                        JSONArray mealsArray =
                                response.getJSONArray("meals");

                        JSONObject mealObj =
                                mealsArray.getJSONObject(0);

                        binding.tvName.setText(
                                mealObj.getString("strMeal")
                        );

                        binding.tvInstruction.setText(
                                mealObj.getString("strInstructions")
                        );

                        Glide.with(this)
                                .load(mealObj.getString("strMealThumb"))
                                .centerCrop()
                                .into(binding.ivImage);

                    } catch (JSONException e) {
    
                        Toast.makeText(
                                this,
                                e.getMessage(),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                },

                error -> Toast.makeText(
                        this,
                        error.getMessage(),
                        Toast.LENGTH_SHORT
                ).show()
        );

        queue.add(request);
    }
}
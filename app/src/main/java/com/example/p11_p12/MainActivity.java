package com.example.p11_p12;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.p11_p12.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final ArrayList<Meal> meals = new ArrayList<>();

    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setupRecyclerView();

        getMeals();
    }

    private void setupRecyclerView() {

        int spanCount = getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE ? 2 : 1;

        binding.recyclerView.setLayoutManager(
                new GridLayoutManager(this, spanCount)
        );

        binding.recyclerView.setItemAnimator(
                new DefaultItemAnimator()
        );

        binding.recyclerView.setHasFixedSize(true);

        adapter = new RecyclerViewAdapter(this, meals);

        binding.recyclerView.setAdapter(adapter);
    }

    private void getMeals() {

        binding.recyclerView.setVisibility(View.GONE);

        String url = "https://www.themealdb.com/api/json/v1/1/search.php?s=";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,

                response -> {

                    binding.recyclerView.setVisibility(View.VISIBLE);

                    try {

                        JSONArray mealsArray =
                                response.getJSONArray("meals");

                        meals.clear();

                        for (int i = 0; i < mealsArray.length(); i++) {

                            JSONObject mealObj =
                                    mealsArray.getJSONObject(i);

                            meals.add(
                                    new Meal(
                                            mealObj.getString("idMeal"),
                                            mealObj.getString("strMeal"),
                                            mealObj.getString("strMealThumb")
                                    )
                            );
                        }

                        adapter.notifyDataSetChanged();

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
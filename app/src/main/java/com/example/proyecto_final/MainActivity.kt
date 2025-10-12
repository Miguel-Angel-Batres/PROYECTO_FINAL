package com.example.proyecto_final

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        var bottomnav : BottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val menu = bottomnav.menu
        menu.findItem(R.id.bottom_home).icon = ContextCompat.getDrawable(this,R.drawable.baseline_home_filled_24)
        bottomnav.itemIconTintList = null
        val orange = ColorStateList.valueOf(Color.argb(255, 255, 204, 128))
        bottomnav.itemActiveIndicatorColor = orange
    }
}
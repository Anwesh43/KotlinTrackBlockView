package com.example.anweshmishra.kotlintrackblockview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.trackblockview.TrackBlockView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TrackBlockView.create(this)

    }
}

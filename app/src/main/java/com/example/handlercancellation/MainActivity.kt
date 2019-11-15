package com.example.handlercancellation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //the handler now has reference to Main Thread
        val handler= HandlerSignalNetwork(progressBar, tv_data)

        btn_init_network.setOnClickListener {
            var task= Thread(NetworkTask(handler))
            progressBar.visibility = View.VISIBLE
            task.start()
        }
        btn_cancel_network.setOnClickListener {
            var task = Thread(CancelTask(handler))
            progressBar.visibility = View.GONE
            tv_data.visibility = View.GONE
            task.start()
        }
    }
}

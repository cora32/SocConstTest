package io.humanteq.soctest

import android.databinding.BindingAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
                .replace(R.id.container, SocConstPermFragment())
                .commit()
    }
}

@BindingAdapter("img_url_crop_circle")
fun onBindCropCircle(iv: ImageView, url: String?) {
    url?.let {
        Glide.with(iv.context)
                .load(it)
                .apply(RequestOptions().dontAnimate().dontTransform().circleCrop().placeholder(iv.context.placeholder()))
                .into(iv)
    }
}
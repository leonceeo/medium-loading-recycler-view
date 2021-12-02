package lc.st.free.lrv

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter


@BindingAdapter("text")
fun setText(view: TextView, text: CharSequence) {
    view.text = text
}

@BindingAdapter("gone")
fun setGone(view: View, gone: Boolean) {
    view.visibility = if (gone) View.GONE else View.VISIBLE
}

@BindingAdapter("android:src")
fun setImageViewResource(imageView: ImageView, @DrawableRes resource: Int) {
    imageView.setImageResource(resource)
}
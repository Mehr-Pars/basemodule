package mehrpars.mobile.sample.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import mehrpars.mobile.sample.AppConfig

/**
 * Created by Ali Arasteh
 */

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(view.context.applicationContext)
            .load(AppConfig.getCompleteUrl(it))
            .apply(
                RequestOptions.centerInsideTransform()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(view)
    }
}

fun ImageView.setImageUrl(url: String?) {
//    placeholder: Int = R.drawable.placeholder,
//    error: Int = R.drawable.placeholder

    url?.let {
        Glide.with(this.context.applicationContext).load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
//            .placeholder(placeholder)
//            .error(error)
            .into(this)
    }
}
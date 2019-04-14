package epeyk.mobile.module.basemoduleholder

import android.content.Context
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

//for using in code
fun ImageView.setImageUrl(
    url: String?,
    placeholder: Int = R.drawable.ic_placeholder,
    error: Int = R.drawable.ic_error
) {
    Glide.with(context)
        .load(url)
        .apply(
            RequestOptions.centerInsideTransform()
                .placeholder(placeholder)
                .error(error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
        )
        .into(this)
}

//for using in binding
@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let {
        Glide.with(view.context)
            .load(imageUrl)
            .apply(
                RequestOptions.centerInsideTransform()
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            )
            .into(view)
    }
}

// generate RATE degree to persian description
fun Context.getRateInPersian(rate: String): String {
    when (rate) {
        "G" -> {
            return getString(R.string.rateG)
        }
        "PG" -> {
            return getString(R.string.ratePG)
        }
        "PG-13" -> {
            return getString(R.string.ratePG13)
        }
        "R" -> {
            return getString(R.string.rateR)
        }
        "NC-17" -> {
            return getString(R.string.rateNC)
        }
        else -> {
            return rate
        }
    }
}
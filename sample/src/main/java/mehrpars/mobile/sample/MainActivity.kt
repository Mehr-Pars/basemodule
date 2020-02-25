package mehrpars.mobile.sample

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import mehrpars.mobile.librarysample.DateUtils

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val date = "2020/02/25 10:44"
        val shamsiDate = DateUtils.getPersianDate(date)

        findViewById<TextView>(R.id.date).text = "miladi date: $date"
        findViewById<TextView>(R.id.shamsiDate).text = "shamsi date: $shamsiDate"
    }
}
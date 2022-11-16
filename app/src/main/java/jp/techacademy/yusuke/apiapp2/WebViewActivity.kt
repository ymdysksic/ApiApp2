package jp.techacademy.yusuke.apiapp2

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_web_view.*
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_favorite.*

class WebViewActivity : AppCompatActivity() {

    var id: String? = null
    private var itemsFA = FavoriteShop()
    private var isFavorite = FavoriteShop.findBy(itemsFA.id) != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        itemsFA = intent.getSerializableExtra(KEY_FAVS) as FavoriteShop
        update()

        webView.loadUrl(itemsFA.url)

        favoriteImageView1.setOnClickListener {
            if (isFavorite) {
                onDeleteFavorite(itemsFA.id)
            } else {
                onAddFavorite(itemsFA)
            }
        }
    }

    // Favoriteに追加するときのメソッド(Fragment -> Activity へ通知する)
    private fun onAddFavorite(itemsF: FavoriteShop) {
        FavoriteShop.insert(itemsF)
        update()
    }

    // Favoriteから削除するときのメソッド(Fragment -> Activity へ通知する)
    private fun onDeleteFavorite(id: String) {
        showConfirmDeleteFavoriteDialog(id)
    }

    // 削除ダイアログの表示
    private fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteFavorite(id)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .create()
            .show()
    }

    // 削除処理
    private fun deleteFavorite(id: String) {
        FavoriteShop.delete(id)
        update()
    }

    // 更新処理
    private fun update() {
        isFavorite = FavoriteShop.findBy(itemsFA.id)!= null
        favoriteImageView1.setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)
    }

    // クーポンページのURL取得
    companion object {
        private const val KEY_FAVS = "key_FAVS"

        fun start(activity: Activity, shop: FavoriteShop) {
            activity.startActivity(
                Intent(activity, WebViewActivity::class.java).putExtra(KEY_FAVS, shop)
            )
        }
    }
}
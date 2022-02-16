import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import org.jetbrains.skija.Image

object ImageDownloader {
    private val imageClient = HttpClient(CIO)
    suspend fun downloadImage(url: String): ImageBitmap {
        val image = imageClient.get<ByteArray>(url)
        return Image.makeFromEncoded(image).asImageBitmap()
    }
}
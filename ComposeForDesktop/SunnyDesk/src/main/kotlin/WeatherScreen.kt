import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WeatherScreen(repository: Repository) {
    var queriedCity by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = queriedCity,
            onValueChange = { queriedCity = it },
            modifier = Modifier.padding(end = 16.dp).weight(1f),
            placeholder = { Text("Any city, really...") },
            label = { Text(text = "Search for a city") },
            leadingIcon = { Icon(Icons.Filled.LocationOn, "Location") }
        )
        Button(onClick = {/* todo */ }) {
            Icon(Icons.Outlined.Search, "Search")
        }
    }
}
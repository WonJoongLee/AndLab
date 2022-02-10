import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun WeatherScreen(repository: Repository) {
    var queriedCity by remember { mutableStateOf("") }

    // weatherState는 화면에 보여줄 현재 state를 들고 있는다.
    // LCE가 바뀔 때마다 Compose가 ui를 recompose할 것이다.
    var weatherState by remember {
        mutableStateOf<Lce<WeatherResults>?>(null)
    }

    val scope = rememberCoroutineScope()

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            Button(onClick = {
                weatherState = Lce.Loading
                scope.launch {
                    weatherState = repository.weatherForCity(queriedCity)
                }
            }) {
                Icon(Icons.Outlined.Search, "Search")
            }
        }
        //LoadingUI()
        when (val state = weatherState) {
            is Lce.Loading -> LoadingUI()
            is Lce.Error -> Unit
            is Lce.Content -> ContentUI(state.data)
        }
    }
}

@Composable
fun LoadingUI() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(alignment = Alignment.Center).defaultMinSize(minWidth = 96.dp, minHeight = 96.dp)
        )
    }
}

@Composable
fun ContentUI(data: WeatherResults) {

}
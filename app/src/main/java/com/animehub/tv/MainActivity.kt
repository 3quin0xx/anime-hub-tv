package com.animehub.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.animehub.tv.data.Media
import com.animehub.tv.data.Repository
import com.animehub.tv.ui.DetailsScreen
import com.animehub.tv.ui.HomeScreen
import com.animehub.tv.ui.MyListScreen
import com.animehub.tv.ui.PlayerScreen
import com.animehub.tv.ui.SearchScreen
import com.animehub.tv.ui.theme.AnimeHubTheme
import kotlinx.coroutines.launch

private enum class NavTab { HOME, BROWSE, SEARCH, MY_LIST, SETTINGS }

private sealed interface Screen {
    data object Home : Screen
    data class Details(val media: Media) : Screen
    data class Player(val media: Media, val episode: Int) : Screen
    data object Search : Screen
    data object MyList : Screen
    data object Settings : Screen
}

/**
 * TV entry point. Tabs are at the top, exactly as in the mockups:
 * Home, Browse, Search, My List, Settings. Browse currently reuses Home's
 * trending data; Search and My List are their own screens.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimeHubTheme {
                App()
            }
        }
    }

    @Composable
    private fun App() {
        var tab by remember { mutableStateOf(NavNavTab.HOME) }
        var screen by remember { mutableStateOf<Screen>(Screen.Home) }
        var searchResults by remember { mutableStateOf<List<Media>>(emptyList()) }
        var myList by remember { mutableStateOf<List<Media>>(emptyList()) }
        var trending by remember { mutableStateOf<List<Media>>(emptyList()) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            trending = Repository.trending()
        }

        fun toggleMyList(media: Media) {
            scope.launch { Repository.toggleMyList(this@MainActivity, media.id) }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)) {
                Text("ANIME HUB")
            }
            TabRow(selectedTabIndex = tab.ordinal) {
                NavNavTab.values().forEach { item ->
                    Tab(
                        selected = tab == item,
                        onClick = { tab = item; screen = when (item) { NavTab.HOME -> Screen.Home; NavTab.BROWSE -> Screen.Home; NavTab.SEARCH -> Screen.Search; NavTab.MY_LIST -> Screen.MyList; NavTab.SETTINGS -> Screen.Settings } },
                        onClick = { tab = item; screen = when (item) { NavNavTab.HOME -> Screen.Home; NavNavTab.BROWSE -> Screen.Home; NavNavTab.SEARCH -> Screen.Search; NavNavTab.MY_LIST -> Screen.MyList; NavNavTab.SETTINGS -> Screen.Settings } },
                        onFocus = {
                            tab = item
                            screen = when (item) {
                                NavNavTab.HOME -> Screen.Home
                                NavNavTab.BROWSE -> Screen.Home
                                NavNavTab.SEARCH -> Screen.Search
                                NavNavTab.MY_LIST -> Screen.MyList
                                NavNavTab.SETTINGS -> Screen.Settings
                            }
                        },
                    ) {
                        Text(
                            when (item) {
                                NavNavTab.HOME -> "Home"
                                NavNavTab.BROWSE -> "Browse"
                                NavNavTab.SEARCH -> "Search"
                                NavNavTab.MY_LIST -> "My List"
                                NavNavTab.SETTINGS -> "Settings"
                            },
                        )
                    }
                }
            }
            when (val current = screen) {
                is Screen.Home -> HomeScreen(
                    onOpenDetails = { screen = Screen.Details(it) },
                    onPlay = { screen = Screen.Player(it, 1) },
                    onToggleMyList = ::toggleMyList,
                )
                is Screen.Details -> DetailsScreen(
                    media = current.media,
                    related = trending,
                    onPlay = { screen = Screen.Player(it, 1) },
                    onToggleMyList = ::toggleMyList,
                    onOpenEpisode = { media, ep -> screen = Screen.Player(media, ep) },
                    onBack = { screen = Screen.Home },
                )
                is Screen.Player -> PlayerScreen(
                    media = current.media,
                    episode = current.episode,
                    onBack = { screen = Screen.Details(current.media) },
                )
                is Screen.Search -> SearchScreen(
                    results = searchResults,
                    onQueryChange = { query ->
                        scope.launch { searchResults = Repository.search(query) }
                    },
                    onOpenDetails = { screen = Screen.Details(it) },
                )
                is Screen.MyList -> MyListScreen(
                    saved = myList,
                    onOpenDetails = { screen = Screen.Details(it) },
                )
                is Screen.Settings -> {
                    Column(modifier = Modifier.padding(32.dp)) {
                        Text("Settings")
                        Text("No account or streaming source is configured in this skeleton.")
                    }
                }
            }
        }
    }
}

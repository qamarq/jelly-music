package com.qamarq.jellymusic.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.qamarq.jellymusic.R
import com.qamarq.jellymusic.core.AppContainer
import com.qamarq.jellymusic.data.jellyfin.JellyfinClient
import com.qamarq.jellymusic.ui.i18n.LocalAppLanguage
import com.qamarq.jellymusic.ui.i18n.commonUiCopy
import com.qamarq.jellymusic.ui.theme.ElovaireRadii
import kotlinx.coroutines.launch

@Composable
fun JellyfinSetupScreen(
    container: AppContainer,
    onBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val language = LocalAppLanguage.current
    val common = commonUiCopy(language)

    val savedHost by container.preferenceStore.jellyfinHost.collectAsState()
    val savedUsername by container.preferenceStore.jellyfinUsername.collectAsState()
    val savedPassword by container.preferenceStore.jellyfinPassword.collectAsState()

    var host by rememberSaveable { mutableStateOf(savedHost) }
    var username by rememberSaveable { mutableStateOf(savedUsername) }
    var password by rememberSaveable { mutableStateOf(savedPassword) }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 104.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                ModuleCard {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedTextField(
                            value = host,
                            onValueChange = { host = it },
                            label = { Text("Server URL") },
                            placeholder = { Text("https://jellyfin.example.com") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(ElovaireRadii.input),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                        )

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(ElovaireRadii.input),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(ElovaireRadii.input),
                            singleLine = true,
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        painter = if (isPasswordVisible) painterResource(R.drawable.ic_lucide_eye_off) else painterResource(R.drawable.ic_lucide_eye),
                                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        )

                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }

                        Button(
                            onClick = {
                                scope.launch {
                                    isLoading = true
                                    errorMessage = null
                                    val client = JellyfinClient(host)
                                    val response = client.authenticate(username, password)
                                    if (response != null) {
                                        container.preferenceStore.setJellyfinCredentials(host, username, password)
                                        container.preferenceStore.setJellyfinSession(response.AccessToken, response.User.Id)
                                        onBack()
                                    } else {
                                        errorMessage = "Authentication failed. Please check your credentials and URL."
                                    }
                                    isLoading = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(ElovaireRadii.pill),
                            enabled = !isLoading && host.isNotBlank() && username.isNotBlank()
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Text("Connect")
                            }
                        }
                    }
                }
            }

            item {
                TextButton(
                    onClick = {
                        container.preferenceStore.setJellyfinCredentials("", "", "")
                        container.preferenceStore.clearJellyfinSession()
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Disconnect / Clear Credentials", color = MaterialTheme.colorScheme.error)
                }
            }
        }

        PinnedBackTopBar(
            title = "Jellyfin Setup",
            onBack = onBack,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

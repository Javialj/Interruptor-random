package com.example.interruptorrandom

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.interruptorrandom.ui.theme.InterruptorRandomTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InterruptorRandomTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    var isOn by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var segundos by remember { mutableStateOf(30) }

    val sonidos = mapOf(
        'a' to R.raw.a,
        'b' to R.raw.b,
        'c' to R.raw.c,
        'd' to R.raw.d,
        'e' to R.raw.e
    )

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    Column (
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isOn) {
            Image(
                painter = painterResource(id = R.drawable.apagado),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        isOn = !isOn
                        Log.d("TEMPORIZADOR", "Apagado manualmente. Segundos: $segundos")
                    }
            )

            LaunchedEffect(key1 = segundos) {
                if (segundos > 0) {
                    delay(1000)
                    segundos--
                } else {
                    val letra = ('a'..'e').random()
                    val recursoId = sonidos[letra] ?: R.raw.a

                    val player = MediaPlayer.create(context, recursoId)
                    mediaPlayer = player
                    player.start()

                    player.setOnCompletionListener {
                        it.release()
                        mediaPlayer = null
                    }

                    isOn = false
                }
            }

        } else {
            Image(
                painter = painterResource(id = R.drawable.encendido),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        isOn = true
                        segundos = 30
                        Log.d("TEMPORIZADOR", "Encendido. Temporizador iniciado: 30s")

                        mediaPlayer?.let { player ->
                            if (player.isPlaying) {
                                player.stop()
                            }
                            player.release()
                            mediaPlayer = null
                        }
                    }
            )
        }

        DisposableEffect(Unit) {
            onDispose {
                mediaPlayer?.let { it.release() }
            }
        }
    }
}



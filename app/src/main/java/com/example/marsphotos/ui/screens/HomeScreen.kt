/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import PicsumUiState
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.marsphotos.camera.CameraActivity
import com.example.marsphotos.camera.CapturedImageActivity
import com.example.marsphotos.camera.ImageUriHolder
import com.example.marsphotos.firebase.incrementRollCount
import com.example.marsphotos.firebase.loadLastSavedPhotos
import com.example.marsphotos.firebase.resetRollValue
import com.example.marsphotos.firebase.saveCurrentPhotos
import com.example.marsphotos.network.MarsPhoto
import com.example.marsphotos.network.PicsumPhoto
import com.google.marsphotos.R



@Composable
fun HomeScreen(
    marsUiState: MarsUiState, modifier: Modifier = Modifier,
    picsumUiState: PicsumUiState

) {
    if(marsUiState is MarsUiState.Error || picsumUiState is PicsumUiState.Error){
        ErrorScreen(modifier=modifier.fillMaxSize())
    }else if(marsUiState is MarsUiState.Success && picsumUiState is PicsumUiState.Success){
        resetRollValue()
        ResultScreen3(
            marsphotos = marsUiState.photos,
            randomMarsPhoto = marsUiState.randomPhoto,
            listMarsPhotos = marsUiState.listphotos,
            picsumphotos =picsumUiState.photos ,
            randomPicsumPhoto = picsumUiState.randomPhoto,
            listPicsumPhotos = picsumUiState.listphotos,

        )
    }
    else{
        LoadingScreen(modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}


@Composable
fun ResultScreen3(marsphotos: String,
                 randomMarsPhoto : MarsPhoto,
                  listMarsPhotos: List<MarsPhoto>,
                 picsumphotos:String,
                 randomPicsumPhoto: PicsumPhoto,
                  listPicsumPhotos : List <PicsumPhoto>,
                 modifier: Modifier = Modifier) {
    var currentRandomMarsPhoto by remember { mutableStateOf(randomMarsPhoto) }
    var currentRandomPicsumPhoto by remember { mutableStateOf(randomPicsumPhoto) }
    var rollCount by remember { mutableStateOf(0L) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current


    Column( modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = picsumphotos)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentRandomPicsumPhoto.imgSrc)
                .crossfade(true)
                .build(),
            contentDescription = "A photo",
            modifier = Modifier.sizeIn(maxWidth = 400.dp, maxHeight = 400.dp)
        )
        Text(text = marsphotos)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentRandomMarsPhoto.imgSrc)
                .crossfade(true)
                .build(),
            contentDescription = "A photo",
            modifier = Modifier.sizeIn(maxWidth = 350.dp, maxHeight = 350.dp)

        )

        Text("Roll Count: $rollCount")

        Row{
            Button(onClick = {
                currentRandomPicsumPhoto = listPicsumPhotos.random()
                currentRandomMarsPhoto = listMarsPhotos.random()
                incrementRollCount {count ->
                        rollCount = count
                }
            },
                modifier = Modifier.weight(1f)
            ) {
                Text("Roll")
            }


            Button(onClick = {
                saveCurrentPhotos(
                    marsPhotoName = currentRandomMarsPhoto.id,
                    marsPhotoURL = currentRandomMarsPhoto.imgSrc,
                    picsumPhotoName = currentRandomPicsumPhoto.id,
                    picsumPhotoURL = currentRandomPicsumPhoto.imgSrc
                )
            },
                modifier = Modifier.weight(1f)
            ) {
                Text("Save Photo")
            }


            Button(onClick = {
                loadLastSavedPhotos { savedPhotos ->
                    currentRandomMarsPhoto = MarsPhoto(savedPhotos.marsPhotoName,savedPhotos.marsPhotoURL)
                    currentRandomPicsumPhoto = PicsumPhoto(savedPhotos.picsumPhotoName,savedPhotos.picsumPhotoURL)

                }
            },
                modifier = Modifier.weight(1f)
            ) {
                Text("Load Photos")
            }



        }

        Row {
            Button(onClick = {
                val intent = Intent(context, CameraActivity::class.java)
                context.startActivity(intent)
            },
                modifier = Modifier.weight(1f)
            ) {
                Text("Take Photo")
            }


            Button(onClick = {
                imageUri = ImageUriHolder.imageUri
                if(imageUri != null) {
                    val intent = Intent(context, CapturedImageActivity::class.java)
                    intent.putExtra("imageUri", imageUri.toString())
                    context.startActivity(intent)
                }

            },
                modifier = Modifier.weight(1f)
            ) {
                Text("Display Photo Taken")
            }
        }

    }
}






import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marsphotos.network.PicsumApi
import com.example.marsphotos.network.PicsumPhoto
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface PicsumUiState {
    data class Success(val photos: String, val randomPhoto: PicsumPhoto, var listphotos : List<PicsumPhoto>) : PicsumUiState
    object Error : PicsumUiState
    object Loading : PicsumUiState
}

class PicsumViewModel : ViewModel() {
    var picsumUiState: PicsumUiState by mutableStateOf(PicsumUiState.Loading)
        private set

    init {
        updatePicsumPhoto()
    }

    /*fun rollPicsumPhoto() {
        updatePicsumPhoto()
    }*/

    private fun updatePicsumPhoto() {
        viewModelScope.launch {
            picsumUiState = try {
                val listResult = PicsumApi.picsumService.getPhotos()
                PicsumUiState.Success( "Success: ${listResult.size} Picsum photos retrieved", listResult.random(), listResult)
            } catch (e: IOException) {
                PicsumUiState.Error
            }
        }
    }


}



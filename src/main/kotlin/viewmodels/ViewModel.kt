package viewmodels

import com.veryphy.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

// Base ViewModel class with coroutine support
abstract class ViewModel : CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Default

    protected val apiService = ApiService()

    var isLoading: Boolean by Delegates.observable(false) { _, _, _ -> }
    var errorMessage: String? by Delegates.observable(null) { _, _, _ -> }

    protected fun launchWithLoading(block: suspend () -> Unit) {
        launch {
            try {
                isLoading = true
                errorMessage = null
                block()
            } catch (e: Exception) {
                errorMessage = e.message ?: "An unknown error occurred"
                console.error(e)
            } finally {
                isLoading = false
            }
        }
    }
}
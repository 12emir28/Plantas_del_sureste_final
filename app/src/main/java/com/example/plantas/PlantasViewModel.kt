package com.example.plantas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plantas.data.PlantaDB
import com.example.plantas.data.PlantasRepository
import com.example.plantas.ui.screens.FavoritePlant
import com.example.plantas.ui.screens.PlantDetail
import com.example.plantas.ui.screens.RemedyDetail
import com.example.plantas.ui.screens.SymptomDetail
import com.example.plantas.ui.screens.remedyDetailList
import com.example.plantas.ui.screens.symptomDetailList
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Estado de carga para el catálogo
sealed class CatalogState {
    object Loading : CatalogState()
    data class Success(val plants: List<PlantaDB>) : CatalogState()
    data class Error(val message: String) : CatalogState()
}

class PlantasViewModel(
    private val repository: PlantasRepository = PlantasRepository()
) : ViewModel() {

    // ── Catálogo desde Supabase ───────────────────────────────────
    private val _catalogState = MutableStateFlow<CatalogState>(CatalogState.Loading)
    val catalogState: StateFlow<CatalogState> = _catalogState.asStateFlow()

    private val _searchQuery  = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _activeFilter = MutableStateFlow("Todas")
    val activeFilter: StateFlow<String> = _activeFilter.asStateFlow()

    fun loadPlants() {
        viewModelScope.launch {
            _catalogState.value = CatalogState.Loading
            try {
                val plants = repository.getPlants()
                _catalogState.value = CatalogState.Success(plants)
            } catch (e: Exception) {
                _catalogState.value = CatalogState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun onSearchQueryChange(query: String) { _searchQuery.value  = query  }
    fun setFilter(filter: String)          { _activeFilter.value = filter }

    init { loadPlants() }

    // ── Planta seleccionada para detalle (formato original PlantDetail) ──
    private val _selectedPlant = MutableStateFlow(plantDetailList[0])
    val selectedPlant: StateFlow<PlantDetail> = _selectedPlant.asStateFlow()

    // Seleccionar desde PlantaDB (Supabase)
    fun selectPlantFromDB(p: PlantaDB) {
        _selectedPlant.value = PlantDetail(
            name              = p.nombreComun,
            sciName           = p.nombreCientifico,
            imageUrl          = p.imagenUrl,
            properties        = p.descripcionUso,
            steps             = listOf(
                "Consulta a un especialista en plantas medicinales de tu región.",
                "Lava bien la planta antes de usarla.",
                "Prepara según la tradición local o indicación de un experto."
            ),
            contraindications = "Consulta a un médico antes de usar esta planta como remedio. " +
                                "Evitar en embarazo, lactancia o alergias conocidas."
        )
    }

    // Seleccionar directamente (usado por SymptomDetailScreen)
    fun selectPlant(plant: PlantDetail) { _selectedPlant.value = plant }

    // ── Favoritos ─────────────────────────────────────────────────
    private val _favorites = MutableStateFlow<List<FavoritePlant>>(emptyList())
    val favorites: StateFlow<List<FavoritePlant>> = _favorites.asStateFlow()

    fun toggleFavorite(plant: PlantDetail) {
        val current = _favorites.value
        val exists  = current.any { it.name == plant.name }
        _favorites.value = if (exists) {
            current.filter { it.name != plant.name }
        } else {
            current + FavoritePlant(
                name        = plant.name,
                benefit     = plant.properties.take(40) + "...",
                description = plant.properties,
                imageUrl    = plant.imageUrl,
                category    = "Hierbas"
            )
        }
    }

    fun isFavorite(plantName: String) = _favorites.value.any { it.name == plantName }

    fun removeFavorite(plantName: String) {
        _favorites.value = _favorites.value.filter { it.name != plantName }
    }

    // ── Remedio seleccionado ──────────────────────────────────────
    private val _selectedRemedy = MutableStateFlow(remedyDetailList[0])
    val selectedRemedy: StateFlow<RemedyDetail> = _selectedRemedy.asStateFlow()
    fun selectRemedy(remedy: RemedyDetail) { _selectedRemedy.value = remedy }

    // ── Síntoma seleccionado ──────────────────────────────────────
    private val _selectedSymptom = MutableStateFlow(symptomDetailList[0])
    val selectedSymptom: StateFlow<SymptomDetail> = _selectedSymptom.asStateFlow()
    fun selectSymptom(symptom: SymptomDetail) { _selectedSymptom.value = symptom }
}

// Mantenida para que SymptomDetailScreen compile sin cambios
val plantDetailList = listOf(
    PlantDetail(
        name = "Maguey Morado", sciName = "Tradescantia spathacea",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBo7rOF3_krpUz8pbko9ZlT-CDeX0NnsqNDDo-_CtNX8oY1LU_osYS93e_lSfoTRdzoW4h_sUkXdAISN1tbZEiljcS3FOYqiCV9kttm0UcbzlhptwEzp0Tuq-L5tR0pdZelOn7U6awjDSCC9VFscXGYFDIti8IMlRhbE-3sT2d9UKiDXiF_ax-20ZlGHhIeDSvx2zLwX8lYtuUAjxeBk8q8jdVguy9eZed6L4rEajk4FsplkHgA4yC0KQCSLeleQ9swXlTV47QBAcI",
        properties = "Antiinflamatorio natural del sureste. Acelera la cicatrización de heridas y alivia dolores reumáticos.",
        steps = listOf("Lavar 2-3 hojas frescas.", "Hervir 1 litro de agua y añadir las hojas.", "Reposar 10 min, colar y servir."),
        contraindications = "El contacto con la savia puede irritar la piel sensible."
    ),
    PlantDetail(
        name = "Aloe Vera (Sábila)", sciName = "Aloe barbadensis",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDAAEfOU1DG5oEx8xZSxdbNQur5Jefzrz1IMvvOd8Slo4cXuZChWIWjTNgphRtcsIcbRvj4_yeRpGzkyCqobtOYvZ8BxWa0enP2-NZZpv5EV0uy89kZebVDj1hzgbiEGCeHBKceRvTCD6s_-AjIdapO3-Bu-wE1-1VttCW32OcKg2CFmlCLhPszTMYliRP1dW5Cf6sL8LlRuAV9rFQva9eVIxpyJuc-SRPLbUi8Cf2dwVyCad3hf3GwsvETgCRba2pYaQkRLLTKvHs",
        properties = "Cicatrizante y antiinflamatorio natural. Ideal para quemaduras leves y problemas digestivos.",
        steps = listOf("Cortar una hoja gruesa.", "Extraer el gel interior.", "Aplicar sobre la zona afectada."),
        contraindications = "No consumir en grandes cantidades."
    ),
    PlantDetail(
        name = "Menta", sciName = "Mentha piperita",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuBpxj_34t9do9UCOxuHh3M6qgrWXf5XWPS1hgO4moC559mgGFHE7StufCsbg_ElY7SRSkt_FvTmuFVRyivH8PEU7l5PdVF1b1H2FC2hGea0QrB_i8vA4pVWZh_klamMZ4V8mSqXrBhLt2T-lx45yCSWOXq2nOuaggM61BIE730HV1Ni8X20RQZCx2vdquHwhB3xDJRX92s2ICVqWgzE9GCIVJaHggfxyVWRe4Y2BrJwWI7MwxXIlAQYZb8H3fypqAuwXzB8QuoW5ic",
        properties = "Relajante y digestivo. Excelente para infusiones.",
        steps = listOf("Lavar hojas frescas.", "Hervir agua y añadir las hojas.", "Reposar 5 min y colar."),
        contraindications = "Evitar en niños menores de 2 años."
    ),
    PlantDetail(
        name = "Achiote", sciName = "Bixa orellana",
        imageUrl = "https://lh3.googleusercontent.com/aida-public/AB6AXuDxETJPSszw6vK6xnqmGREHaIn6-sHPyDqZ2NKlJHSPhwkznSkwNbfybiBvcRT9Y3L25lThn_n6EPffjwMr9xyZbByT8SvNOenm33-w9ilrNz1-WbLe7VzCkHWlvZlCyad5_J6XTC4BjtkijLYCxHqFpqfvoLBVrFTKNsJpkz6QFK2mg0WagoaHKm4XWouZlIfiNdCXFs87mrB7ZzPgM8yJ8eZajrqtjrIMSgrJTavwAd8Ro4MVdIoP_SgY1d4u15DIIRIGv76PPp8",
        properties = "Digestivo y colorante natural. Medicina tradicional maya.",
        steps = listOf("Extraer semillas de la vaina.", "Hervir en agua 10 min.", "Colar y usar."),
        contraindications = "Consultar médico si se usa durante el embarazo."
    )
)

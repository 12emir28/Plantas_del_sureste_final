package com.example.plantas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.plantas.databinding.FragmentSoyUnFragmentoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class SoyUnFragmentoFragment : Fragment() {

    private var _binding: FragmentSoyUnFragmentoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: Actividad2ViewModel by viewModels()
    private var youTubePlayerInstance: YouTubePlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSoyUnFragmentoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 5. Sincronizar el Ciclo de Vida
        lifecycle.addObserver(binding.youtubePlayerView)

        viewModel.videoTitle.observe(viewLifecycleOwner) { title ->
            binding.txtTitulo.text = title
        }

        // Inicializar el reproductor para capturar la instancia
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayerInstance = youTubePlayer
                // No cargamos automáticamente, esperamos al clic
            }

            // 4. Evitar el cambio de texto automático / Redirección en caso de error
            override fun onError(youTubePlayer: YouTubePlayer, error: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError) {
                // Dejamos vacío o logueamos para que no fuerce apertura externa
            }
        })

        // 3. Corregir lógica de reproducción: Carga interna en lugar de Intent
        binding.btnAbrirVideoYoutube.text = getString(R.string.reproducir_video_embebido)
        binding.btnAbrirVideoYoutube.setOnClickListener {
            val videoId = viewModel.videoId.value ?: ""
            youTubePlayerInstance?.loadVideo(videoId, 0f)
            binding.fabPlayVideo.visibility = View.GONE
        }

        binding.fabPlayVideo.setOnClickListener {
            binding.fabPlayVideo.visibility = View.GONE
            val videoId = viewModel.videoId.value ?: ""
            youTubePlayerInstance?.loadVideo(videoId, 0f)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
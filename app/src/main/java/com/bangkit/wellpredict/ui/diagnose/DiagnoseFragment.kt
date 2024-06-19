package com.bangkit.wellpredict.ui.diagnose

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.bangkit.wellpredict.databinding.FragmentDiagnoseBinding
import com.bangkit.wellpredict.ui.ViewModelFactory
import com.bangkit.wellpredict.ui.adapter.SelectedSymptomAdapter
import com.bangkit.wellpredict.utils.PredictionHelper
import kotlinx.coroutines.launch

class DiagnoseFragment : Fragment() {

    private val viewModel by viewModels<DiagnoseViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private var _binding: FragmentDiagnoseBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: SelectedSymptomAdapter
    private lateinit var predictionHelper: PredictionHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDiagnoseBinding.inflate(inflater, container, false)
        val root: View = binding.root

        observeSymptoms()
        addSymptomOnClickHandler()
        setupRecyclerView()
        diagnoseOnClickHandler()

        predictionHelper = PredictionHelper(
            context = requireContext(),
            onResult = { result ->
                if (result.isNotEmpty()) {
                    Log.d("hasil", "Prediction result: $result")
                } else {
                    Log.d("hasil", "Empty prediction result")
                }
            },
            onError = { errorMessage ->
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        )

        return root
    }

    private fun setupRecyclerView() {
        adapter = SelectedSymptomAdapter { symptom ->
            lifecycleScope.launch {
                viewModel.removeSelectedSymptom(symptom)
            }
        }
        val flexboxLayoutManager = FlexboxLayoutManager(requireContext())
        flexboxLayoutManager.flexDirection = FlexDirection.ROW
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START

        binding.rvSelectedSymptom.layoutManager = flexboxLayoutManager
        binding.rvSelectedSymptom.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addSymptomOnClickHandler(){
        binding.tvAddSymptom.setOnClickListener{
            val intent = Intent(activity, DiagnoseSearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeSymptoms() {
        lifecycleScope.launch {
            viewModel.selectedSymptom.collect { symptoms ->
                adapter.submitList(symptoms.toList())
            }
        }
    }

    private fun diagnoseOnClickHandler() {
        binding.btnDiagnose.setOnClickListener {
            lifecycleScope.launch {
                viewModel.selectedSymptom.collect { symptoms ->
                    // Convert symptoms to Array<String>
                    val symptomArray = symptoms.toList().toTypedArray()
                    predictionHelper.predict(symptomArray)
                }
            }
        }
    }
}
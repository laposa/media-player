package ie.laposa.common.features.common

import androidx.lifecycle.ViewModel

interface ViewModelFactory
{
    fun <T : ViewModel> create(vararg args: Any): T
}
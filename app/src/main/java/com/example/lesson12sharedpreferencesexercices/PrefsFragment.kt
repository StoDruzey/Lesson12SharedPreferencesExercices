package com.example.lesson12sharedpreferencesexercices

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.lesson12sharedpreferencesexercices.databinding.FragmentPrefsBinding
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PrefsFragment : Fragment() {
    private var _binding: FragmentPrefsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val prefs by lazy {
        requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    var editTextValue by StringPrefsDelegate(prefs, "key_edit_text")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPrefsBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            checkbox.isChecked = prefs.getBoolean("isChecked", false)
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                prefs
                    .edit()
                    .putBoolean("isChecked", isChecked)
                    .apply()
            }

            editText.setText(editTextValue)
            editText.addTextChangedListener { text ->
                editTextValue = text.toString()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val IS_CHECKED = "isChecked"
        private const val IS_TYPED = "isTyped"
    }
}

class StringPrefsDelegate( //for saving a string from editText in sharedpreferences we need use ReadWriteProperty interface
    private val prefs: SharedPreferences, //this interface requires a delegate
    private val key: String
) :ReadWriteProperty<Any, String> {
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return prefs.getString(key, "") ?: ""
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        prefs.edit {
            putString(key, value)
        }
    }
}
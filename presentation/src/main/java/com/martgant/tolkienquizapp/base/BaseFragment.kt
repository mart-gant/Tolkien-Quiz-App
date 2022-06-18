package com.martgant.tolkienquizapp.base

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected fun showToast(toastTextRes: Int) {
        context?.let{
            Toast.makeText(it, textRes, duration).show()
        }
    }
    @StringRes
    protected fun getStringByName(StringName: String): Int{
        return resources.getIdentifier(stringName, "string", activity?.packageName)
    }
}

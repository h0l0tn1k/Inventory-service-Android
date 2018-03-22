package android.inventory.siemens.cz.siemensinventory.tools

import android.content.Context
import android.inventory.siemens.cz.siemensinventory.R
import android.view.animation.AnimationUtils
import android.widget.TextView

/**
 * Created by I333206 on 22.03.2018.
 */
class TextViewHelper {

    private var context : Context? = null
    var and : TextViewHelper = this
    var isValid : Boolean = true

    fun withContext(context : Context) : TextViewHelper {
        this.context = context
        return this
    }

    fun isEmailValid(field: TextView?): TextViewHelper {
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(field?.text.toString()).matches()) {
            setErrorAndShake(field, context?.getString(R.string.email_empty).toString())
            isValid = false
        }
        return this
    }

    fun isNotEmpty(field: TextView?, errorMessage : String? = null) : TextViewHelper {
        if(field?.text.toString().isEmpty()) {
            setErrorAndShake(field, errorMessage)
            isValid = false
        }
        return this
    }

    fun setErrorAndShake(field: TextView?, errorMessage: String? = null) {
        var message = errorMessage
        if(message == null) {
            message = context?.getString(R.string.field_empty)
        }

        field?.error = message
        field?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
    }
}
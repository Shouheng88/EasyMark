package me.shouheng.sil;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Soft input layout state.
 *
 * @author <a href="mailto:shouheng2015@gmail.com">WngShhng</a>
 * @version 2020-03-18 16:50
 */
@IntDef(value = {SILState.AUTOMATIC, SILState.HIDE_SOFT_INPUT_ONLY, SILState.SHOW_SOFT_INPUT_ONLY})
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PARAMETER, ElementType.FIELD})
public @interface SILState {

    /**
     * The container will automatically be showed and hidden
     * according to the keyboard state changed.
     */
    int AUTOMATIC               = 0x0000;

    /**
     * Show the soft input layout only, the container will be displayed.
     */
    int SHOW_SOFT_INPUT_ONLY    = 0x0002;

    /**
     * Hide the soft input layout only, the container won't be hidden.
     */
    int HIDE_SOFT_INPUT_ONLY    = 0x0003;
}

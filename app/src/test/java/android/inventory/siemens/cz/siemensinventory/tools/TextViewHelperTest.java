package android.inventory.siemens.cz.siemensinventory.tools;

import android.content.Context;
import android.inventory.siemens.cz.siemensinventory.R;
import android.util.AttributeSet;
import android.util.Patterns;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by I333206 on 23.03.2018.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest( {AnimationUtils.class })
public class TextViewHelperTest {

    @Mock
    private Context mMockContext;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void textViewHelper_withContext_returnsInstance() {
        TextViewHelper helper = new TextViewHelper().withContext(mMockContext);

        assertThat(helper, instanceOf(TextViewHelper.class));
    }

    @Test
    public void textViewHelper_sets_context() {
        TextViewHelper helper = new TextViewHelper().withContext(mMockContext);

        assertThat(helper.getContext(), is(mMockContext));
    }

//    @Test
//    public void textViewHelper_isEmailValid_validEmail_returnsTrue() {
//        TextViewHelper helper = new TextViewHelper().withContext(mMockContext);
//
//        TextView tv = mock(TextView.class);
//        when(tv.getText()).thenReturn("test@test.com");
//
//        //PowerMockito.mockStatic(Pattern.class);
//        PowerMockito.mockStatic(Patterns.class);
//        PowerMockito.when(Patterns.EMAIL_ADDRESS.matcher(tv.getText().toString()).matches()).thenReturn(true);
//
//        assertThat(helper.isEmailValid(tv).isValid(), is(true));
//    }

    @Test
    public void textViewHelper_isNotEmpty_correctString() {
        TextViewHelper helper = new TextViewHelper().withContext(mMockContext);

        TextView tv = mock(TextView.class);
        when(tv.getText()).thenReturn("Test");

        assertThat(helper.isNotEmpty(tv, null).isValid(), is(true));
    }

    @Test
    public void textViewHelper_isNotEmpty_emptyString() {
        //prepare
        String empty = "empty";
        TextViewHelper helper = new TextViewHelper().withContext(mMockContext);
        TextView tv = mock(TextView.class);

        when(tv.getText()).thenReturn("");
        when(tv.getError()).thenReturn(empty);
        when(mMockContext.getString(R.string.field_empty)).thenReturn(empty);
        PowerMockito.mockStatic(AnimationUtils.class);
        when(AnimationUtils.loadAnimation(mMockContext, R.anim.shake)).thenReturn(new AnimationSet(true));

        //assert
        assertThat(helper.isNotEmpty(tv, null).isValid(), is(false));
        assertThat(tv.getError().toString(), is(empty));

        //prepare
        String customErrorMessage = "Custom Error";
        when(tv.getError()).thenReturn(customErrorMessage);

        //assert
        assertThat(helper.isNotEmpty(tv, customErrorMessage).isValid(), is(false));
        assertThat(tv.getError().toString(), is(customErrorMessage));
    }
}

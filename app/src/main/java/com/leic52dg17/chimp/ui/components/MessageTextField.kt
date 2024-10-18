import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.leic52dg17.chimp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageTextField(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = messageText,
        onValueChange = onMessageTextChange,
        label = { Text(stringResource(R.string.message_text_field_en)) },
        modifier = modifier
            .padding(6.dp),
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,

        )
    )
}
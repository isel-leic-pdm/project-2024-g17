import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageTextField(
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    inputBoxColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    minLines: Int = 1,
    maxLines: Int = 5
) {
    Box(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        TextField(
            value = messageText,
            onValueChange = onMessageTextChange,
            modifier = modifier.fillMaxWidth(),
            maxLines = maxLines,
            minLines = minLines,
            enabled = enabled,
            placeholder = {
                if (!enabled) {
                    Text("You cannot write here...")
                } else {
                    Text("Message...")
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedTextColor = textColor,
                containerColor = inputBoxColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
        )
    }
}
